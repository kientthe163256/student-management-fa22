package com.example.studentmanagementfa22.service;

import com.example.studentmanagementfa22.dto.SubjectDTO;
import com.example.studentmanagementfa22.entity.Classroom;
import com.example.studentmanagementfa22.entity.Subject;
import com.example.studentmanagementfa22.exception.customExceptions.ActionNotAllowedException;
import com.example.studentmanagementfa22.repository.SubjectRepository;
import com.example.studentmanagementfa22.service.impl.SubjectServiceImpl;
import com.example.studentmanagementfa22.utility.SubjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;

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
        Subject subject = Subject.builder()
                .id(EXIST_ID)
                .subjectName(SUBJECT_NAME)
                .numberOfCredit(NUM_OF_CREDITS)
                .classrooms(new ArrayList<>())
                .deleted(false)
                .build();
        return subject;
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


    private Subject mockSave(Subject currentSubject, SubjectDTO editedSubject){
        currentSubject.setSubjectName(editedSubject.getSubjectName());
        currentSubject.setNumberOfCredit(editedSubject.getNumberOfCredit());
        return currentSubject;
    }

    private SubjectDTO mockMapToDTO(Subject subject){
        SubjectDTO subjectDTO = SubjectDTO.builder()
                .id(subject.getId())
                .subjectName(subject.getSubjectName())
                .numberOfCredit(subject.getNumberOfCredit())
                .build();
        return subjectDTO;
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
        doAnswer((Answer<Subject>) invocation -> mockSave(currentSubject, editedSubject)).when(subjectRepository).save(currentSubject);
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

        assertThrows(NoSuchElementException.class, () -> {
            subjectService.updateSubject(editedObject);
        });
        verify(subjectRepository).findById(NON_EXIST_ID);
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
}
