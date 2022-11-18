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
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.mockito.stubbing.Answer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.util.Assert;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
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
        Classroom mockClassroom = Classroom.builder().id(5).classroomName("mock class").currentNoStudent(10).build();
        ClassroomDTO mockClassroomDTO = ClassroomDTO.builder().id(5).classroomName("mock class").currentNoStudent(10).build();
        Student mockStudent = Student.builder().id(6).build();
        PageRequest pageRequest = PageRequest.of( 0, 5);
        Page<Classroom> mockPageClassroom = new PageImpl<>(List.of(mockClassroom));
        when(classroomRepository.findAllRegisteredClass(pageRequest, mockStudent.getId())).thenReturn(mockPageClassroom);
        when(classroomMapper.mapToDTO(mockClassroom)).thenReturn(mockClassroomDTO);
        List<ClassroomDTO> classroomDTO = classroomService.getAllRegisteredClass(1, mockStudent.getId());
        Assert.notNull(classroomDTO);
        assertThrows(IllegalArgumentException.class, () -> classroomService.getAllRegisteredClass(-1, mockStudent.getId()));
        verify(classroomRepository, times(1)).findAllRegisteredClass(pageRequest, 6);
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
        assertThrows(NoSuchElementException.class, () -> classroomService.registerClassroom(-1, mockAccount.getId()));

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

        assertThrows(IllegalArgumentException.class, () -> classroomService.registerClassroom(mockClassroom.getId(), mockAccount.getId()));

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
        PageRequest pageRequest = PageRequest.of(0, 5);
        // 2. define behavior of Repository, Mapper
        when(classroomRepository.findAllAvailClassroom(pageRequest, 1)).thenReturn(mockPageClassroom);
        // 3.call service method
        Page<ClassroomDTO> classroomDTOPage = classroomService.getAllAvailClassroom(1, 1);
        //4 assert result
        assertNotNull(classroomDTOPage);
        verify(classroomRepository, times(1)).findAllAvailClassroom(pageRequest, 1);
    }

    @Test
    public void getAllTeachingClassrooms() {
        Account mockAccount = Account.builder().username("TC000000").id(8).firstName("Mock").lastName("TeacherAcc").build();
        Teacher mockTeacher = Teacher.builder().id(3).account(mockAccount).build();
        Classroom classroom = Classroom.builder().id(6).classroomName("SE1617").currentNoStudent(16).build();
        ClassroomDTO classroomDTO1 = ClassroomDTO.builder().classroomName("SE1617").currentNoStudent(16).build();
        List<ClassroomDTO> mockClassroomDTOList = new ArrayList<>();
        mockClassroomDTOList.add(classroomDTO1);

        int pageSize = 5;
        String rawSort = "classroomName,desc";
        Sort sort = Sort.by("classroom_name").descending();
        PageRequest pageRequest = PageRequest.of(0, pageSize, sort);
        Page<Classroom> classroomPage = new PageImpl<>(List.of(classroom));

        when(teacherRepository.findTeacherByAccountId(mockTeacher.getAccount().getId())).thenReturn(Optional.of(mockTeacher));
        when(classroomRepository.findClassroomsByTeacherId(mockTeacher.getId(), pageRequest)).thenReturn(classroomPage);
        when(classroomMapper.mapToDTO(classroom)).thenReturn(mapToClassroomDTO(classroom));
        Pagination<ClassroomDTO> pagination = classroomService.getAllTeachingClassrooms(mockAccount.getId(), 1, pageSize, rawSort);

        //assert the result
        assertThrows(NoSuchElementException.class, () -> classroomService.getAllTeachingClassrooms(1000, 1, pageSize, rawSort));
        assertThrows(NoSuchElementException.class, () -> classroomService.getAllTeachingClassrooms(1000, 1, pageSize, "tt,desc"));

        assertEquals(classroom.getClassroomName(), pagination.getData().get(0).getClassroomName());
        verify(classroomRepository, times(1)).findClassroomsByTeacherId(mockTeacher.getId(), pageRequest);
    }

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

    private Classroom deleteClassroom(Classroom classroom){
        classroom.setDeleted(true);
        classroom.setDeleteDate(new Date());
        return classroom;
    }

    @Test
    public void deleteClassroomSuccessfully(){
        Integer classId = 5;
        Classroom mockClassroom = Classroom.builder()
                .id(classId)
                .classroomName("mock class")
                .currentNoStudent(0)
                .build();

        when(classroomRepository.findById(classId)).thenReturn(Optional.of(mockClassroom));
        when(classroomRepository.save(mockClassroom)).thenReturn(deleteClassroom(mockClassroom));
        classroomService.deleteClassroom(classId);

        assertTrue(mockClassroom.isDeleted());

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
    public void addNewSessionClassroom(){
        Classroom classroom = createMockClassroom();
        when(classroomRepository.findByClassroomName(classroom.getClassroomName())).thenReturn(null).thenReturn(classroom);
        when(classroomMapper.mapToDTO(classroom)).thenReturn(mapToClassroomDTO(classroom));

        ClassroomDTO addedClass = classroomService.addNewClassroom(classroom);
        assertNotNull(addedClass);
        verify(classroomRepository, times(2)).findByClassroomName(classroom.getClassroomName());
    }

    @Test
    public void addNewSubjectClassroom(){
        Classroom classroom = createMockClassroom();
        classroom.setClassType(ClassType.SUBJECT);
        Subject subject = Subject.builder().id(1).build();
        classroom.setSubject(subject);

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

    @Test
    public void getClassDTOById(){
        Classroom classroom = createMockClassroom();
        when(classroomRepository.findById(classroom.getId())).thenReturn(Optional.of(classroom));
        when(classroomMapper.mapToDTO(classroom)).thenReturn(mapToClassroomDTO(classroom));

        ClassroomDTO classroomDTO = classroomService.getClassDTOById(classroom.getId());

        assertEquals(classroom.getClassroomName(), classroomDTO.getClassroomName());
    }
}