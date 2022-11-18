package com.example.studentmanagementfa22.service;

import com.example.studentmanagementfa22.dto.ClassroomDTO;

import com.example.studentmanagementfa22.entity.*;
import com.example.studentmanagementfa22.exception.customExceptions.ActionNotAllowedException;
import com.example.studentmanagementfa22.exception.customExceptions.ElementAlreadyExistException;
import com.example.studentmanagementfa22.exception.customExceptions.InvalidSortFieldException;
import com.example.studentmanagementfa22.repository.ClassroomRepository;
import com.example.studentmanagementfa22.repository.TeacherRepository;
import com.example.studentmanagementfa22.service.impl.ClassroomServiceImpl;
import com.example.studentmanagementfa22.utility.ClassroomMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ClassroomServiceTest {
    @Mock
    private ClassroomRepository classroomRepository;

    @Mock
    private TeacherRepository teacherRepository;

    @Mock
    private TeacherService teacherService;

    @Mock
    private StudentService studentService;

    @Mock
    private  MarkService markService;
    @Mock
    private ClassroomMapper classroomMapper;

    @InjectMocks
    private ClassroomServiceImpl classroomService;

    @Test
    public void getAllRegisteredClass() {
        Page<ClassroomDTO> classroomDTO = classroomService.getAllRegisteredClass(2, 1);
        Assert.notNull(classroomDTO);
    }
    @Test
    public void registerClassroomSuccessful() throws Exception {
        Account mockAccount = Account.builder().id(1).username("HE163256").firstName("mock").lastName("Student account").build();
        Student mockStudent = Student.builder().id(6).account(mockAccount).build();
        Subject mockSubject = Subject.builder().id(1).build();
        Classroom mockClassroom = Classroom.builder().id(5).classroomName("mock class").currentNoStudent(10).subject(mockSubject).build();
        Student student = Student.builder().id(6).build();

        when(classroomRepository.findById(5)).thenReturn(Optional.of(mockClassroom));
        when(studentService.getStudentByAccountId(1)).thenReturn(mockStudent);
        when(classroomRepository.numOfSubjectClassByStudent(1, 6)).thenReturn(0);
        doAnswer((Answer<Void>) invocation -> {
            return null;
        }).when(classroomRepository).registerClassroom(6,5);
        doAnswer((Answer<Void>) invocation -> {
            mockClassroom.setCurrentNoStudent(11);
            return null;
        }).when(classroomRepository).updateNoStudentOfClass(5);
        doAnswer((Answer<Void>) invocation -> {
            return null;
        }).when(markService).addStudentSubjectMark(6,1);

        classroomService.registerClassroom(mockClassroom.getId(), mockAccount.getId());

        verify(classroomRepository, times(1)).updateNoStudentOfClass(5);
        verify(classroomRepository, times(1)).findById(5);
        verify(classroomRepository, times(1)).numOfSubjectClassByStudent(1, 6);
        verify(classroomRepository, times(1)).registerClassroom(6,5);
    }
    @Test
    public void registerClassroomFail() throws Exception {
        Account mockAccount = Account.builder().id(1).username("HE163256").firstName("mock").lastName("Student account").build();
        Student mockStudent = Student.builder().id(6).account(mockAccount).build();
        Subject mockSubject = Subject.builder().id(1).build();
        Classroom mockClassroom = Classroom.builder().id(5).classroomName("mock class").currentNoStudent(10).subject(mockSubject).build();
        Student student = Student.builder().id(6).build();

        when(classroomRepository.findById(5)).thenReturn(Optional.of(mockClassroom));
        when(studentService.getStudentByAccountId(1)).thenReturn(mockStudent);
        when(classroomRepository.numOfSubjectClassByStudent(1, 6)).thenReturn(1);


        Exception exception = assertThrows(Exception.class, () -> {
            classroomService.registerClassroom(mockClassroom.getId(), mockStudent.getId() );
        });
    }

    @Test
    public void getAllAvailClassroom() {
        // 1. Create mock data
        Subject mockSubject = Subject.builder().id(1).build();
        Classroom mockClassroom = Classroom.builder().id(5).classroomName("mock class").subject(mockSubject).build();
        ClassroomDTO mockClassroomDTO = ClassroomDTO.builder().id(5).classroomName("mock class").build();

        List<Classroom> classroomList = new ArrayList<>();
        classroomList.add(mockClassroom);
        Page<Classroom> mockPageClassroom = new PageImpl<>(classroomList);
        PageRequest pageRequest = PageRequest.of(1, 5);
        // 2. define behavior of Repository, Mapper
        when(classroomRepository.findAllAvailClassroom(pageRequest, 1)).thenReturn(mockPageClassroom);
        // 3.call service method
        Page<ClassroomDTO> classroomDTOPage = classroomService.getAllAvailClassroom(1, 1);
        //4 assert result
        assertNotNull(classroomDTOPage);
    }

//    @Test
//    public void getTeachingClassrooms() {
//        Account mockAccount = Account.builder()
//                .username("TC000000")
//                .id(8)
//                .firstName("Mock")
//                .lastName("TeacherAcc")
//                .build();
//        Teacher mockTeacher = Teacher.builder()
//                .id(3)
////                .accountId(8)
//                .build();
//        Optional<Teacher> mockOptionalTeacher = Optional.of(mockTeacher);
//        Classroom classroom1 = Classroom.builder().classroomName("SE1617").currentNoStudent(16).build();
//        List<Classroom> mockClassroomList = new ArrayList<>();
//        mockClassroomList.add(classroom1);
//
//        ClassroomDTO classroomDTO1 = ClassroomDTO.builder().classroomName("SE1617").currentNoStudent(16).build();
//        List<ClassroomDTO> mockClassroomDTOList = new ArrayList<>();
//        mockClassroomDTOList.add(classroomDTO1);
//
//        // define behavior of Repository
//        when(teacherRepository.findTeacherByAccountId(mockAccount.getId())).thenReturn(mockOptionalTeacher);
//        when(classroomRepository.findClassroomsByTeacherId(mockTeacher.getId())).thenReturn(mockClassroomList);
//        when(classroomMapper.mapToDTO(classroom1)).thenReturn(classroomDTO1);
//        // call service method
//        List<ClassroomDTO> classroomDTOList = classroomService.getAllTeachingClassrooms(8, pageNumber, pageSize, sort);
//
//        //assert the result
//
//        assertEquals(classroomDTOList.size(), mockClassroomDTOList.size());
//        assertEquals(classroomDTOList.get(0).getClassroomName(), mockClassroomDTOList.get(0).getClassroomName());
//        assertEquals(classroomDTOList.get(0).getCurrentNoStudent(), mockClassroomDTOList.get(0).getCurrentNoStudent());
//    }

    @Test
    public void assignClassroomWithNotExistTeacher() {
        when(teacherService.getById(100)).thenThrow(new NoSuchElementException("Teacher not found"));
        Exception exception = assertThrows(NoSuchElementException.class, () -> {
            classroomService.assignClassroom(100, 1);
        });

        String expectedMessage = "Teacher not found";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void assignClassroomWithNotExistClassId() {
        Integer classId = 100;
        Integer teacherId = 1;
        assertThrows(NoSuchElementException.class, () -> {
            classroomService.assignClassroom(teacherId, classId);
        });
    }

    @Test
    public void assignClassroomSuccessfully() {
        Integer classId = 5;
        Integer teacherId = 3;
        Classroom mockClassroom = Classroom.builder()
                .id(classId)
                .classroomName("mock class")
                .build();
        Optional<Classroom> optionalClassroom = Optional.of(mockClassroom);
        Teacher mockTeacher = Teacher.builder()
                .id(teacherId)
                .build();

        when(teacherService.getById(teacherId)).thenReturn(mockTeacher);
        doReturn(optionalClassroom).when(classroomRepository).findById(classId);
        doAnswer((Answer<Classroom>) invocation -> {
            mockClassroom.setTeacher(mockTeacher);
            return mockClassroom;
        }).when(classroomRepository).save(mockClassroom);
        when(classroomMapper.mapToDTO(mockClassroom)).thenReturn(mapToClassroomDTO(mockClassroom));

        ClassroomDTO savedClass = classroomService.assignClassroom(teacherId, classId);

        assertEquals(teacherId, mockClassroom.getTeacher().getId());

        verify(classroomRepository, times(1)).save(mockClassroom);
        verify(classroomRepository, times(1)).findById(classId);
    }

    private Classroom saveClassroom(Classroom currentClass, Classroom savingClass){
        currentClass.setClassroomName(savingClass.getClassroomName());
        return currentClass;
    }

    @Test
    public void updateClassroom() {
        Integer classId = 5;
        Classroom mockClassroom = Classroom.builder()
                .id(classId)
                .classroomName("mock class")
                .build();
        String newClassName = "Edited";
        Classroom editClass = Classroom.builder()
                .id(classId)
                .classroomName(newClassName)
                .build();

        when(classroomRepository.findById(classId)).thenReturn(Optional.of(mockClassroom));
        when(classroomRepository.save(mockClassroom)).thenReturn(saveClassroom(mockClassroom, editClass));
       when(classroomMapper.mapToDTO(mockClassroom)).thenReturn(mapToClassroomDTO(mockClassroom));

        ClassroomDTO actualClass = classroomService.updateClassroom(editClass, classId);
        assertEquals(editClass.getClassroomName(), actualClass.getClassroomName());

        verify(classroomRepository).save(mockClassroom);
    }

    private ClassroomDTO mapToClassroomDTO(Classroom classroom){
        return ClassroomDTO.builder()
                .id(classroom.getId())
                .classroomName(classroom.getClassroomName())
                .noStudent(classroom.getNoStudent())
                .createDate(classroom.getCreateDate())
                .classType(classroom.getClassType())
                .build();
    }

    @Test
    public void deleteClassroomHavingStudent(){
        Integer classId = 5;
        Classroom mockClassroom = Classroom.builder()
                .id(classId)
                .classroomName("mock class")
                .currentNoStudent(2)
                .build();

        when(classroomRepository.findById(classId)).thenReturn(Optional.of(mockClassroom));
        assertThrows(ActionNotAllowedException.class, () -> {
            classroomService.deleteClassroom(classId);
        });

        verify(classroomRepository).findById(classId);
    }

    private Classroom createMockClassroom(){
        Teacher teacher = Teacher.builder()
                .id(1)
                .build();
        return Classroom.builder()
                .id(5)
                .classroomName("SE1615")
                .noStudent(30)
                .classType(ClassType.SESSION)
                .teacher(teacher)
                .build();
    }

    @Test
    public void addNewClassroomWithExistName(){
        Classroom classroom = createMockClassroom();
        when(classroomRepository.findByClassroomName(classroom.getClassroomName())).thenReturn(classroom);
        assertThrows(ElementAlreadyExistException.class, () -> classroomService.addNewClassroom(classroom));
        verify(classroomRepository, times(1)).findByClassroomName(classroom.getClassroomName());
    }

    @Test
    public void addNewClassroomSuccessfully(){
        Classroom classroom = createMockClassroom();
        when(classroomRepository.findByClassroomName(classroom.getClassroomName())).thenReturn(null).thenReturn(classroom);
        when(classroomMapper.mapToDTO(classroom)).thenReturn(mapToClassroomDTO(classroom));

        ClassroomDTO addedClass = classroomService.addNewClassroom(classroom);
        assertNotNull(addedClass);
        verify(classroomRepository, times(2)).findByClassroomName(classroom.getClassroomName());
    }

    @Test
    public void getAllClassroomsPaging(){
        int pageNumber = 1;
        int pageSize = 5;
        String rawSort = "classroomName,desc";
        Sort sort = Sort.by("classroomName").descending();
        PageRequest pageRequest = PageRequest.of(pageNumber - 1, pageSize, sort);
        Classroom classroom = createMockClassroom();
        Page<Classroom> classroomPage = new PageImpl<>(List.of(classroom));
        when(classroomRepository.findAll(pageRequest)).thenReturn(classroomPage);
        when(classroomMapper.mapToDTO(classroom)).thenReturn(mapToClassroomDTO(classroom));
        Pagination<ClassroomDTO> pagination = classroomService.getAllClassroomsPaging(pageNumber, pageSize, rawSort);

        assertEquals(classroom.getClassroomName(), pagination.getData().get(0).getClassroomName());
        verify(classroomRepository).findAll(pageRequest);
    }

    @Test
    public void getAllClassroomsPagingWithInvalidSortField(){
        int pageNumber = 1;
        int pageSize = 5;
        String rawSort = "classroomAlias,desc";

        assertThrows(InvalidSortFieldException.class, () -> classroomService.getAllClassroomsPaging(pageNumber, pageSize, rawSort));
    }
}