package com.example.studentmanagementfa22.service;

import com.example.studentmanagementfa22.dto.AccountDTO;
import com.example.studentmanagementfa22.dto.StudentDTO;
import com.example.studentmanagementfa22.entity.Account;
import com.example.studentmanagementfa22.entity.Classroom;
import com.example.studentmanagementfa22.entity.Student;
import com.example.studentmanagementfa22.entity.Teacher;
import com.example.studentmanagementfa22.exception.customExceptions.ActionNotAllowedException;
import com.example.studentmanagementfa22.repository.AccountRepository;
import com.example.studentmanagementfa22.repository.StudentRepository;
import com.example.studentmanagementfa22.repository.TeacherRepository;
import com.example.studentmanagementfa22.service.impl.StudentServiceImpl;
import com.example.studentmanagementfa22.utility.StudentMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
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
public class StudentServiceTest {

    @Mock
    private StudentMapper mapper;
    @Mock
    private StudentRepository studentRepository;

    @Mock
    private TeacherRepository teacherRepository;

    @Mock
    private  StudentServiceImpl mockStudentService;

    @Mock
    private AccountRepository accountRepository;
     @Mock
     private AccountService accountService;

    @InjectMocks
    private StudentServiceImpl studentService;

    @Test
    public void checkStudentJoinedClass() {
        Classroom mockClassroom = Classroom.builder().id(2).build();
        Student mockStudent = Student.builder().id(9).build();
        when(studentRepository.getStudentClassroom(mockStudent.getId(),mockClassroom.getId())).thenReturn(1);
        studentService.checkStudentJoinedClass(mockStudent.getId(), mockClassroom.getId());
        verify(studentRepository, times(1)).getStudentClassroom(9,2);
    }
    @Test
    public void checkStudentNotJoinedClass() {
        Classroom mockClassroom = Classroom.builder().id(2).build();
        Student mockStudent = Student.builder().id(9).build();
        when(studentRepository.getStudentClassroom(mockStudent.getId(),mockClassroom.getId())).thenReturn(0);
        assertThrows(NoSuchElementException.class, () -> studentService.checkStudentJoinedClass(mockStudent.getId(), mockClassroom.getId()));
        verify(studentRepository, times(1)).getStudentClassroom(9,2);
    }

    @Test
    public void checkStudentTeacherTrue() {
        Student mockStudent = Student.builder().id(9).build();
        Teacher mockTeacher = Teacher.builder().id(4).build();
        Optional<Student> optionalStudent = Optional.of(mockStudent);
        when(studentRepository.getStudentbyTeacher(mockStudent.getId(), mockTeacher.getId())).thenReturn(optionalStudent);
        studentService.checkStudentTeacher(mockStudent.getId(), mockTeacher.getId());
        verify(studentRepository, times(1)).getStudentbyTeacher(9,4);
    }
    @Test
    public void checkStudentTeacherFalse() {
        Student mockStudent = Student.builder().id(9).build();
        Teacher mockTeacher = Teacher.builder().id(4).build();
        when(studentRepository.getStudentbyTeacher(mockStudent.getId(), mockTeacher.getId())).thenReturn(Optional.empty());
        assertThrows(ActionNotAllowedException.class, () -> studentService.checkStudentTeacher(mockStudent.getId(), mockTeacher.getId()));

        verify(studentRepository, times(1)).getStudentbyTeacher(9,4);
    }

    @Test
    public void getStudentbyAccountId() {
        Account mockAccount = Account.builder().id(1).username("HE163256").firstName("mock").lastName("Student account").build();
        Student mockStudent = Student.builder().id(6).account(mockAccount).build();

        when(studentRepository.findStudentByAccountId(1)).thenReturn(Optional.of(mockStudent));
        Student testStudent = studentService.getStudentByAccountId(1);
        assertThrows(NoSuchElementException.class, () -> studentService.getStudentByAccountId(1000));

        assertEquals(testStudent.getAccount().getId(), mockAccount.getId());
    }

    private Account createMockAccount(){
        return Account.builder()
                .username("HE163256")
                .firstName("Trung")
                .lastName("Kien")
                .build();
    }

    private Student createMockStudent(){
        Account account = createMockAccount();
        Date today = new Date();
        return Student.builder()
                .id(1)
                .academicSession(16)
                .account(account)
                .createDate(today)
                .modifyDate(today)
                .build();
    }

    private StudentDTO mapToStudentDTO(Student student){
        StudentDTO studentDTO = new StudentDTO();
        studentDTO.setId(student.getId());
        studentDTO.setAcademicSession(student.getAcademicSession());
        return studentDTO;
    }

    @Test
    public void addStudentWithNewAccount(){
        Account account = createMockAccount();
        Student student = createMockStudent();

        when(studentRepository.save(any(Student.class))).thenReturn(student);
        when(mapper.mapToDTO(student)).thenReturn(mapToStudentDTO(student));

        StudentDTO actualStudent = studentService.addStudentWithNewAccount(account);
        assertEquals(student.getId(), actualStudent.getId());
        assertEquals(student.getAcademicSession(), actualStudent.getAcademicSession());

        verify(studentRepository).save(any(Student.class));
    }
    @Test
    public void getStudentDTObyAccountId() {
        Account mockAccount = Account.builder().id(1).username("HE163256").password("123456").roleId(3).firstName("mock").lastName("Student account").build();
        AccountDTO accountDTO = AccountDTO.builder().id(1).username("HE163256").firstName("mock").lastName("Student account").build();
        Student mockStudent = Student.builder().id(6).academicSession(2022).account(mockAccount).build();
        StudentDTO studentDTO = StudentDTO.builder().id(6).academicSession(2022).account(accountDTO).build();
        when(studentRepository.findStudentByAccountId(1)).thenReturn(Optional.of(mockStudent));
        when(mockStudentService.getStudentByAccountId(1)).thenReturn(mockStudent);
        when(accountRepository.findById(1)).thenReturn(Optional.of(mockAccount));
        when(accountService.getById(mockAccount.getId())).thenReturn(mockAccount);
        when(mapper.mapToDTO(mockStudent)).thenReturn(studentDTO);

        StudentDTO testStudent = studentService.getStudentDTOByAccountId(1);

        assertEquals(testStudent.getAccount().getId(), mockAccount.getId());
        verify(studentRepository, times(1)).findStudentByAccountId(1);
        verify(accountService, times(1)).getById(1);

    }

    @Test
    public void getStudentsByClassroomAndTeacher() {
        Account mockAccount = Account.builder().id(3).roleId(2).build();
        Teacher mockTeacher = Teacher.builder().id(2).account(mockAccount).build();
        Account mockStudentAccount = Account.builder().id(1).username("HE163256").password("123456").roleId(3).firstName("mock").lastName("Student account").build();
        AccountDTO accountStudentDTO = AccountDTO.builder().id(1).username("HE163256").firstName("mock").lastName("Student account").build();
        Student mockStudent = Student.builder().id(6).academicSession(2022).account(mockStudentAccount).build();
        StudentDTO studentDTO = StudentDTO.builder().id(6).academicSession(2022).firstName("mock").account(accountStudentDTO).build();
        int pageSize = 5;
        String rawSort = "account_id,desc";
        Sort sort = Sort.by("id").descending();
        PageRequest pageRequest = PageRequest.of(0, pageSize, sort);
        Page<Account> accountPage = new PageImpl<>(List.of(mockStudentAccount));
        when(teacherRepository.findTeacherByAccountId(mockTeacher.getAccount().getId())).thenReturn(Optional.of(mockTeacher));
        when(accountRepository.findStudentAccountsByClassroomandTeacher(1,mockTeacher.getId(), pageRequest)).thenReturn(accountPage);
        when(studentRepository.findStudentByAccountId(mockStudent.getAccount().getId())).thenReturn(Optional.of(mockStudent));
        when(mapper.mapToDTO(mockStudent)).thenReturn(studentDTO);

        List<StudentDTO> studentDTOList = studentService.getStudentsByClassroomandTeacher(1, mockTeacher.getAccount().getId(), 1,5, rawSort);
        assertEquals(mockStudentAccount.getFirstName(), studentDTOList.get(0).getFirstName());
        assertThrows(IllegalArgumentException.class, () -> studentService.getStudentsByClassroomandTeacher(1,mockTeacher.getAccount().getId(), 1,5, "id+desc"));
        assertThrows(NoSuchElementException.class, () -> studentService.getStudentsByClassroomandTeacher(1,1000, 1,5, rawSort));
        assertThrows(IllegalArgumentException.class, () -> studentService.getStudentsByClassroomandTeacher(1,mockTeacher.getAccount().getId(), 1,5, "tt,desc"));

        verify(studentRepository, times(1)).findStudentByAccountId(mockStudent.getAccount().getId());
        verify(accountRepository, times(1)).findStudentAccountsByClassroomandTeacher(1,2, pageRequest);
    }
}
