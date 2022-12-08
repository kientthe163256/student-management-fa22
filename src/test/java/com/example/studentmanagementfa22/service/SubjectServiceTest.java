package com.example.studentmanagementfa22.service;

import com.example.studentmanagementfa22.dto.SubjectDTO;
import com.example.studentmanagementfa22.entity.Classroom;
import com.example.studentmanagementfa22.entity.Subject;
import com.example.studentmanagementfa22.exception.customExceptions.ActionNotAllowedException;
import com.example.studentmanagementfa22.exception.customExceptions.ElementAlreadyExistException;
import com.example.studentmanagementfa22.exception.customExceptions.InvalidInputException;
import com.example.studentmanagementfa22.repository.SubjectRepository;
import com.example.studentmanagementfa22.service.impl.SubjectServiceImpl;
import com.example.studentmanagementfa22.utility.mapper.SubjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.*;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SubjectServiceTest {
    @Mock
    private SubjectRepository subjectRepository;

    @Mock
    private SubjectMapper subjectMapper;

    @InjectMocks
    private SubjectServiceImpl subjectService;

    private static final int EXIST_ID = 1;
    private static final int NON_EXIST_ID = 100;
    private static final String SUBJECT_NAME = "Mock Subject";
    private static final String EDITED_SUBJECT_NAME = "Edited";
    private static final int NUM_OF_CREDITS = 3;
    private static final int EDITED_NUM_OF_CREDITS = 7;

    private Subject mockSubject(){
        return Subject.builder()
                .id(EXIST_ID)
                .subjectName(SUBJECT_NAME)
                .numberOfCredit(NUM_OF_CREDITS)
                .classrooms(new ArrayList<>())
                .deleted(false)
                .build();
    }

    private Subject mockSubjectWithClassrooms(){
        List<Classroom> classroomList = new ArrayList<>();
        Classroom classroom = Classroom.builder()
                .classroomName("SE1615")
                .currentNoStudent(0)
                .noStudent(30)
                .build();
        classroomList.add(classroom);
        Subject subject = mockSubject();
        subject.setClassrooms(classroomList);
        return subject;
    }


    private Subject mockUpdate(Subject currentSubject, SubjectDTO editedSubject){
        currentSubject.setSubjectName(editedSubject.getSubjectName());
        currentSubject.setNumberOfCredit(editedSubject.getNumberOfCredit());
        return currentSubject;
    }

    private SubjectDTO mockMapToDTO(Subject subject){
        return SubjectDTO.builder()
                .id(subject.getId())
                .subjectName(subject.getSubjectName())
                .numberOfCredit(subject.getNumberOfCredit())
                .build();
    }

    private Subject mockMapToEntity(SubjectDTO subjectDTO){
        return Subject.builder()
                .subjectName(subjectDTO.getSubjectName())
                .numberOfCredit(subjectDTO.getNumberOfCredit())
                .build();
    }

    @Test
    public void updateSubjectSuccessfully() {
        Subject currentSubject = mockSubject();
        SubjectDTO editedSubject = SubjectDTO.builder()
                .id(EXIST_ID)
                .subjectName(EDITED_SUBJECT_NAME)
                .numberOfCredit(EDITED_NUM_OF_CREDITS)
                .build();

        doReturn(Optional.of(currentSubject)).when(subjectRepository).findById(EXIST_ID);
        doAnswer((Answer<Subject>) invocation -> mockUpdate(currentSubject, editedSubject)).when(subjectRepository).save(currentSubject);
        doAnswer((Answer<SubjectDTO>) invocation -> mockMapToDTO(currentSubject)).when(subjectMapper).mapToDTO(currentSubject);

        SubjectDTO actualSubject = subjectService.updateSubject(editedSubject);
        assertEquals(actualSubject.getSubjectName(), editedSubject.getSubjectName());
        assertEquals(actualSubject.getNumberOfCredit(), editedSubject.getNumberOfCredit());

        verify(subjectRepository).save(currentSubject);
    }

    @Test
    public void updateSubjectWithNotExistId(){
        SubjectDTO editedObject = SubjectDTO.builder()
                .id(NON_EXIST_ID)
                .subjectName(SUBJECT_NAME)
                .numberOfCredit(NUM_OF_CREDITS)
                .build();

        when(subjectRepository.findById(NON_EXIST_ID)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> subjectService.updateSubject(editedObject));
        verify(subjectRepository).findById(NON_EXIST_ID);
    }

    @Test
    public void updateSubjectWithExistName(){
        String existedName = "SE1615";
        SubjectDTO editedObject = SubjectDTO.builder()
                .id(EXIST_ID)
                .subjectName(existedName)
                .numberOfCredit(NUM_OF_CREDITS)
                .build();

        Subject existedSubject = mockSubject();
        when(subjectRepository.findById(EXIST_ID)).thenReturn(Optional.of(existedSubject));
        when(subjectRepository.findBySubjectName(existedName)).thenReturn(existedSubject);

        assertThrows(InvalidInputException.class, () -> {
            subjectService.updateSubject(editedObject);
        });
        verify(subjectRepository).findById(EXIST_ID);
        verify(subjectRepository).findBySubjectName(existedName);
    }

    private Subject mockDeleteSubject(Subject subject){
        subject.setDeleted(true);
        subject.setDeleteDate(new Date());
        return subject;
    }

    @Test
    public void deleteSubjectSuccessfully(){
        Subject mockSubject = mockSubject();

        when(subjectRepository.findById(EXIST_ID)).thenReturn(Optional.of(mockSubject));
        doAnswer((Answer<Subject>) invocation -> mockDeleteSubject(mockSubject)).when(subjectRepository).save(mockSubject);

        subjectService.deleteSubject(EXIST_ID);
        //assert deleted is true
        assertTrue(mockSubject.isDeleted());
        //assert deleted_date
        Date today = new Date();
        assertEquals(mockSubject.getDeleteDate().toString(), today.toString());

        verify(subjectRepository, times(1)).findById(EXIST_ID);
        verify(subjectRepository, times(1)).save(mockSubject);
    }

    @Test
    public void deleteSubjectStillHaveClassroom(){
        Subject mockSubject = mockSubjectWithClassrooms();

        when(subjectRepository.findById(EXIST_ID)).thenReturn(Optional.of(mockSubject));

        assertThrows(ActionNotAllowedException.class, () -> {
            subjectService.deleteSubject(EXIST_ID);
        });
        verify(subjectRepository, times(1)).findById(EXIST_ID);
        verify(subjectRepository, times(0)).save(mockSubject);
    }

    @Test
    public void deleteSubjectWithNonExistId(){
        when(subjectRepository.findById(NON_EXIST_ID)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> {
            subjectService.deleteSubject(NON_EXIST_ID);
        });
        verify(subjectRepository, times(1)).findById(NON_EXIST_ID);
    }

    @Test
    public void getSubjectById() {
        Subject mockSubject = mockSubject();
        when(subjectRepository.findById(EXIST_ID)).thenReturn(Optional.of(mockSubject));

        Subject testSubject = subjectService.getById(EXIST_ID);
        verify(subjectRepository, times(1)).findById(EXIST_ID);
        assertEquals(testSubject, mockSubject);

    }

    @Test
    public void addMarkTypeToSubjectSuccessfully(){
        Subject subject = mockSubject();
        int markTypeId = 1;
        int noMarks = 1;
        //how to initialize javax Tuple
    }

    @Test
    public void addNewSubjectSuccessfully(){
        SubjectDTO subjectDTO = SubjectDTO.builder()
                .subjectName("SE1615")
                .numberOfCredit(3)
                .build();
        when(subjectRepository.findBySubjectName(subjectDTO.getSubjectName())).thenReturn(null);
        Subject savingSubject = mockMapToEntity(subjectDTO);
        when(subjectMapper.mapToEntity(subjectDTO)).thenReturn(savingSubject);
        when(subjectRepository.save(savingSubject)).thenReturn(savingSubject);
        when(subjectMapper.mapToDTO(savingSubject)).thenReturn(mockMapToDTO(savingSubject));

        SubjectDTO actualSubject = subjectService.addNewSubject(subjectDTO);

        assertEquals(subjectDTO.getSubjectName(), actualSubject.getSubjectName());
        assertEquals(subjectDTO.getNumberOfCredit(), actualSubject.getNumberOfCredit());

        verify(subjectRepository, times(1)).findBySubjectName(subjectDTO.getSubjectName());
    }

    @Test
    public void addNewSubjectWithExistName(){
        SubjectDTO subjectDTO = SubjectDTO.builder()
                .subjectName("SE1615")
                .numberOfCredit(3)
                .build();
        when(subjectRepository.findBySubjectName(subjectDTO.getSubjectName())).thenReturn(mockSubject());

        assertThrows(ElementAlreadyExistException.class, () -> subjectService.addNewSubject(subjectDTO));

        verify(subjectRepository, times(1)).findBySubjectName(subjectDTO.getSubjectName());
    }

    @Test
    public void getSubjectDTOById(){
        Subject subject = mockSubject();
        when(subjectRepository.findById(subject.getId())).thenReturn(Optional.of(subject));
        when(subjectMapper.mapToDTO(subject)).thenReturn(mockMapToDTO(subject));

        SubjectDTO actualSubjectDTO = subjectService.getSubjectDTOByID(subject.getId());

        assertEquals(subject.getSubjectName(), actualSubjectDTO.getSubjectName());
        assertEquals(subject.getNumberOfCredit(), actualSubjectDTO.getNumberOfCredit());
    }

    @Test
    public void getSubjectDTOList(){
        Subject subject = mockSubject();
        List<Subject> subjectList = List.of(subject);
        Page<Subject> subjectPage = new PageImpl<>(subjectList);

        PageRequest pageRequest = PageRequest.of(0, 5);
        when(subjectRepository.findAll(pageRequest)).thenReturn(subjectPage);
        when(subjectMapper.mapToDTO(subject)).thenReturn(mockMapToDTO(subject));

        List<SubjectDTO> actualList = subjectService.getSubjectDTOList(1);
        SubjectDTO actualSubject = actualList.get(0);
        assertEquals(subject.getSubjectName(), actualSubject.getSubjectName());
        assertEquals(subject.getNumberOfCredit(), actualSubject.getNumberOfCredit());

        verify(subjectRepository).findAll(pageRequest);
    }

    @Test
    public void getAllSubject() {
        PageRequest pageRequest = PageRequest.of(0, 5);
        Subject subject = mockSubject();
        List<Subject> subjectList = List.of(subject);
        Page<Subject> subjectPage = new PageImpl<>(subjectList);
        when(subjectRepository.findAll(pageRequest)).thenReturn(subjectPage);
        Page<Subject> testSubjectPage = subjectService.getAllSubject(1);
        verify(subjectRepository).findAll(pageRequest);
    }
}
