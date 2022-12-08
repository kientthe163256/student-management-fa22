package com.example.studentmanagementfa22.service;

import com.example.studentmanagementfa22.dto.AccountDTO;
import com.example.studentmanagementfa22.dto.TeacherDTO;
import com.example.studentmanagementfa22.entity.*;
import com.example.studentmanagementfa22.exception.customExceptions.ActionNotAllowedException;
import com.example.studentmanagementfa22.exception.customExceptions.InvalidSortFieldException;
import com.example.studentmanagementfa22.repository.ClassroomRepository;
import com.example.studentmanagementfa22.repository.MarkRepository;
import com.example.studentmanagementfa22.repository.StudentRepository;
import com.example.studentmanagementfa22.repository.TeacherRepository;
import com.example.studentmanagementfa22.service.impl.StudentServiceImpl;
import com.example.studentmanagementfa22.service.impl.TeacherServiceImpl;
import com.example.studentmanagementfa22.utility.mapper.TeacherMapper;
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

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class TeacherServiceTest {
    @Mock
    private TeacherRepository teacherRepository;

    @Mock
    private ClassroomRepository classroomRepository;

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private MarkRepository markRepository;
    @Mock
    private TeacherMapper teacherMapper;

    @Mock
    private StudentServiceImpl studentService;

    @Mock
    private  TeacherServiceImpl mockTeacherService;

    @InjectMocks
    private TeacherServiceImpl teacherService;

    private Account mockAccount(){
        return Account.builder()
                .id(2)
                .username("HE16356")
                .dob(new Date(15))
                .firstName("Mock")
                .lastName("Account")
                .build();
    }
    private Teacher mockTeacher(){
        return Teacher.builder()
                .id(1)
                .account(mockAccount())
                .build();
    }

    private TeacherDTO mapToTeacherDTO(Teacher teacher){
        Account account = teacher.getAccount();
        AccountDTO accountDTO = AccountDTO.builder()
                .id(account.getId())
                .username(account.getUsername())
                .firstName(account.getFirstName())
                .lastName(account.getLastName())
                .build();
        return TeacherDTO.builder()
                .id(teacher.getId())
                .account(accountDTO)
                .build();
    }

    @Test
    public void getTeacherDTOById() {
        // 1. create mock data
        Teacher teacher = mockTeacher();
        int teacherId = teacher.getId();

        // 2. define behavior of Repository, Mapper
        when(teacherRepository.findById(teacherId)).thenReturn(Optional.of(teacher));
        when(teacherMapper.mapToDTO(teacher)).thenReturn(mapToTeacherDTO(teacher));

        // 3. call service method
        TeacherDTO actualTeacherDTO = teacherService.getTeacherDTOById(teacher.getId());

        // 4. assert the result
        assertEquals(teacherId, actualTeacherDTO.getId());//equal some fields
        assertEquals(teacher.getAccount().getUsername(), actualTeacherDTO.getAccount().getUsername());//equal some fields

        verify(teacherRepository, times(1)).findById(teacherId);
    }

    @Test
    public void whenGetByNonExistIdThenReturnNull(){
        int nonExistId = 1000;
        assertThrows(NoSuchElementException.class, () -> teacherService.getById(nonExistId));
        verify(teacherRepository).findById(nonExistId);
    }
    @Test
    public void checkClassNotAssignedTeacher() {
        Teacher mockTeacher = Teacher.builder().id(2).build();
        Classroom mockClassroom = Classroom.builder().id(2).teacher(mockTeacher).build();

        when(teacherRepository.findTeacherByClassroomId(mockClassroom.getId())).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> {
            teacherService.checkTeacherAssignedClass(mockTeacher.getId(), mockClassroom.getId());
        });
        verify(teacherRepository, times(1)).findTeacherByClassroomId(2);
    }
    @Test
    public void checkTeacherAssignedClass() {
        Teacher mockTeacher1 = Teacher.builder().id(2).build();
        Teacher mockTeacher2 = Teacher.builder().id(4).build();
        Classroom mockClassroom = Classroom.builder().id(2).teacher(mockTeacher1).build();
        when(teacherRepository.findTeacherByClassroomId(mockClassroom.getId())).thenReturn(Optional.of(mockTeacher1));
        assertThrows(ActionNotAllowedException.class, () -> {
            teacherService.checkTeacherAssignedClass(mockTeacher2.getId(), mockClassroom.getId());
        });
    }


    @Test
    public void addTeacherWithNewAccount(){
        Account account = mockAccount();
        Teacher savingTeacher = mockTeacher();
        when(teacherRepository.save(any(Teacher.class))).thenReturn(savingTeacher);
        when(teacherMapper.mapToDTO(savingTeacher)).thenReturn(mapToTeacherDTO(savingTeacher));

        TeacherDTO actualTeacherDTO = teacherService.addTeacherWithNewAccount(account);

        assertEquals(account.getUsername(), actualTeacherDTO.getAccount().getUsername());

        verify(teacherRepository, times(1)).save(any(Teacher.class));
    }

    @Test
    public void getAllTeacherPaging(){
        int pageNumber = 1;
        int pageSize = 5;
        String rawSort = "firstName,desc";
        Sort sort = Sort.by("account.firstName").descending();
        PageRequest pageRequest = PageRequest.of(pageNumber - 1, pageSize, sort);
        Teacher teacher = mockTeacher();
        Page<Teacher> teacherPage = new PageImpl<>(List.of(teacher));
        when(teacherRepository.findAll(pageRequest)).thenReturn(teacherPage);
        when(teacherMapper.mapToDTO(teacher)).thenReturn(mapToTeacherDTO(teacher));
        Pagination<TeacherDTO> pagination = teacherService.getAllTeacherPaging(pageNumber, pageSize, rawSort);

        assertEquals(teacher.getAccount().getFirstName(), pagination.getData().get(0).getAccount().getFirstName());
        verify(teacherRepository).findAll(pageRequest);
    }

    @Test
    public void getAllTeacherPagingInvalidSortField(){
        int pageNumber = 1;
        int pageSize = 5;
        String rawSort = "notExistField,desc";

        assertThrows(InvalidSortFieldException.class, () -> teacherService.getAllTeacherPaging(pageNumber, pageSize, rawSort));
    }

    private Teacher deleteTeacher(Teacher teacher){
        teacher.setDeleted(true);
        teacher.setDeleteDate(new Date());
        return teacher;
    }

    @Test
    public void deleteTeacherSuccessfully(){
        Teacher teacher = mockTeacher();
        when(teacherRepository.findById(teacher.getId())).thenReturn(Optional.of(teacher));
        when(teacherRepository.save(teacher)).thenReturn(deleteTeacher(teacher));

        teacherService.deleteTeacher(teacher.getId());

        assertTrue(teacher.isDeleted());

        verify(teacherRepository).save(any(Teacher.class));
    }

    @Test
    public void deleteTeacherWithNotExistId(){
        int nonExistId = 100;
        assertThrows(NoSuchElementException.class, () -> teacherService.deleteTeacher(nonExistId));
        verify(teacherRepository).findById(nonExistId);
    }

    @Test
    public void checkTeacherClassroomStudent() {
        Account account = Account.builder().id(2).roleId(2).build();
        Teacher teacher = Teacher.builder().id(1).account(account).build();
        Student student = Student.builder().id(4).build();
        when(studentRepository.findById(4)).thenReturn(Optional.of(student));
        when(teacherRepository.findTeacherByAccountId(2)).thenReturn(Optional.of(teacher));
        when(teacherRepository.findTeacherByClassroomId(1)).thenReturn(Optional.of(teacher));
        when(mockTeacherService.getTeacherByAccountId(teacher.getAccount().getId())).thenReturn(teacher);
        doNothing().when(studentService).checkStudentTeacher(4, 1);
        doNothing().when(studentService).checkStudentJoinedClass(4,1);
        doNothing().when(mockTeacherService).checkTeacherAssignedClass(1,1);
        teacherService.checkTeacherClassroomStudent(2,1,4);
        assertThrows(NoSuchElementException.class, () ->  teacherService.checkTeacherClassroomStudent(1000,1,4));
        assertThrows(NoSuchElementException.class, () ->  teacherService.checkTeacherClassroomStudent(2,1,1000));

        verify(studentService).checkStudentTeacher(4,1);
        verify(studentService).checkStudentJoinedClass(4,1);
    }

    @Test
    public void removeStudentSubjectClassroom() {
        Account account = Account.builder().id(2).roleId(2).build();
        Teacher teacher = Teacher.builder().id(1).account(account).build();
        Subject subject = Subject.builder().id(1).subjectName("mock subject").build();
        Classroom classroom = Classroom.builder().id(10).classroomName("mock class").subject(subject).teacher(teacher).currentNoStudent(20).classType(ClassType.SUBJECT).build();
        Student student = Student.builder().id(4).build();
        when(studentRepository.findById(4)).thenReturn(Optional.of(student));
        when(teacherRepository.findTeacherByAccountId(2)).thenReturn(Optional.of(teacher));
        when(teacherRepository.findTeacherByClassroomId(10)).thenReturn(Optional.of(teacher));
        when(classroomRepository.findById(10)).thenReturn(Optional.of(classroom));
        doNothing().when(mockTeacherService).checkTeacherClassroomStudent(teacher.getAccount().getId(),10, 4);
        doAnswer((Answer<Void>) invocation -> {
            return null;
        }).when(teacherRepository).deleteStudentClassroom(4,10);
        doAnswer((Answer<Void>) invocation -> {
            classroom.setCurrentNoStudent(19);
            return null;
        }).when(classroomRepository).updateNoStudentOfClass(19,10);
        doAnswer((Answer<Void>) invocation -> {
            return null;
        }).when(markRepository).deleteMarkByStudentSubject(4,1);

        teacherService.removeStudentClassroom(teacher.getAccount().getId() ,student.getId(), classroom.getId());

        assertThrows(NoSuchElementException.class, () -> teacherService.removeStudentClassroom(teacher.getAccount().getId() ,student.getId(), 1000));

        verify(classroomRepository, times(1)).findById(10);
        verify(classroomRepository, times(1)).updateNoStudentOfClass(19,10);
        verify(teacherRepository, times(1)).deleteStudentClassroom(4,10);
        verify(markRepository, times(1)).deleteMarkByStudentSubject(4,1);
    }

    @Test
    public void addStudentSessionCLass() {
        Account account = Account.builder().id(2).roleId(2).build();
        Teacher teacher = Teacher.builder().id(1).account(account).build();
        Subject subject = Subject.builder().id(1).subjectName("mock subject").build();
        Classroom classroom = Classroom.builder().id(10).classroomName("mock class").subject(subject).teacher(teacher).currentNoStudent(20).noStudent(30).classType(ClassType.SESSION).build();
        Student student = Student.builder().id(4).build();
        when(studentRepository.findById(4)).thenReturn(Optional.of(student));
        when(teacherRepository.findTeacherByAccountId(2)).thenReturn(Optional.of(teacher));
        when(teacherRepository.findTeacherByClassroomId(10)).thenReturn(Optional.of(teacher));
        when(classroomRepository.findById(10)).thenReturn(Optional.of(classroom));
        when(studentRepository.getStudentClassroom(4,10)).thenReturn(0);
        doNothing().when(classroomRepository).registerClassroom(4, 10);
        doNothing().when(classroomRepository).updateNoStudentOfClass(21, 10);

        teacherService.addStudentToSessionClass(teacher.getAccount().getId(), classroom.getId(), student.getId());
        assertThrows(NoSuchElementException.class, () -> teacherService.addStudentToSessionClass(teacher.getAccount().getId(), 1000, student.getId()));

        verify(classroomRepository, times(1)).findById(10);
        verify(studentRepository, times(1)).getStudentClassroom(4,10);
        verify(classroomRepository, times(1)).registerClassroom(4, 10);
        verify(classroomRepository, times(1)).updateNoStudentOfClass(21, 10);

    }
}
