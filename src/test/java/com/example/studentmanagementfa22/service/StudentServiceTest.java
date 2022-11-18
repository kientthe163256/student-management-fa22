package com.example.studentmanagementfa22.service;

import com.example.studentmanagementfa22.dto.AccountDTO;
import com.example.studentmanagementfa22.dto.StudentDTO;
import com.example.studentmanagementfa22.entity.Account;
import com.example.studentmanagementfa22.entity.Classroom;
import com.example.studentmanagementfa22.entity.Student;
import com.example.studentmanagementfa22.entity.Teacher;
import com.example.studentmanagementfa22.repository.AccountRepository;
import com.example.studentmanagementfa22.repository.StudentRepository;
import com.example.studentmanagementfa22.repository.TeacherRepository;
import com.example.studentmanagementfa22.service.impl.StudentServiceImpl;
import com.example.studentmanagementfa22.utility.IGenericMapper;
import com.example.studentmanagementfa22.utility.StudentMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.Date;
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
    private  StudentServiceImpl mockStudentService;

    @Mock
    private AccountRepository accountRepository;
     @Mock
     private AccountService accountService;

    @InjectMocks
    private StudentServiceImpl studentService;
//    @MockitoSettings( strictness = Strictness.WARN)

//    @Test
//    public void getListofStudentByCriteria() {
//        //Createa Mock data
//        Account mockAccount = Account.builder()
//                .id(3)
//                .roleId(2)
//                .build();
//
//        Teacher mockTeacher = Teacher.builder()
//                .id(2)
//                .account(mockAccount)
//                .build();
//        Optional<Teacher> mockOptionalTeacher = Optional.of(mockTeacher);
//        Classroom mockClassroom = Classroom.builder()
//                .id(2)
//                .teacherId(2)
//                .build();
//        Account studentAcc1 = Account.builder().id(1).username("student1").build();
//        Account studentAcc2 = Account.builder().id(2).username("student2").build();
//        StudentDTO studentDTO1 = StudentDTO.builder().id(3).accountId(studentAcc1.getId()).build();
//        StudentDTO studentDTO2 = StudentDTO.builder().id(4).accountId(studentAcc2.getId()).build();
//        List<StudentDTO> mockStudentDTOList = new ArrayList<>();
//        mockStudentDTOList.add(studentDTO1);
//        mockStudentDTOList.add(studentDTO2);
//        List<Account> studentAccountList = new ArrayList<>();
//        studentAccountList.add(studentAcc1);
//        studentAccountList.add(studentAcc2);
//        Page<Account> mockPageStudentAccount = new PageImpl<>(studentAccountList);
//        Sort sortObject = Sort.by(Sort.Direction.DESC,"id");
//        PageRequest pageRequest = PageRequest.of(1, 5, sortObject);
//        //define repository behavior
//        when(teacherRepository.findTeacherByAccountId(mockAccount.getId())).thenReturn(mockOptionalTeacher);
//        when(accountRepository.findStudentAccountsByClassroomandTeacher(mockOptionalTeacher.get().getId(), mockClassroom.getId(), pageRequest)).thenReturn(mockPageStudentAccount);
//        when(mapper.mapToDTO(studentAcc1)).thenReturn(studentDTO1);
//        when(mapper.mapToDTO(studentAcc2)).thenReturn(studentDTO2);
//     //   when(mockPageStudentAccount.stream().map(account -> mapper.mapToDTO(account)).collect(Collectors.toList())).thenReturn(mockStudentDTOList);
//        // call Service
//        List<StudentDTO> studentDTOList = studentService.getStudentsByClassroomandTeacher(2,3,1,5,"id,DESC" );
//        //Assert result
//        assertEquals(2, studentDTOList.size());
//        assertTrue(studentDTOList.get(0).getId() > studentDTOList.get(1).getId(), "Sorted by ID descending");
//
//    }
    @Test
    public void checkStudentJoinedClass() {
        Classroom mockClassroom = Classroom.builder().id(2).build();
        Student mockStudent = Student.builder().id(9).build();
        when(studentRepository.getStudentClassroom(mockStudent.getId(),mockClassroom.getId())).thenReturn(1);
        boolean check = studentService.checkStudentJoinedClass(mockStudent.getId(), mockClassroom.getId());
        assertTrue(check, "Student joined class");
        verify(studentRepository, times(1)).getStudentClassroom(9,2);
    }

    @Test
    public void checkStudentTeacher() {
        Student mockStudent = Student.builder().id(9).build();
        Teacher mockTeacher = Teacher.builder().id(4).build();
        Optional<Student> optionalStudent = Optional.of(mockStudent);
        when(studentRepository.getStudentbyTeacher(mockStudent.getId(), mockTeacher.getId())).thenReturn(optionalStudent);
        studentService.checkStudentTeacher(mockStudent.getId(), mockTeacher.getId());
        verify(studentRepository, times(1)).getStudentbyTeacher(9,4);
    }

    @Test
    public void getStudentbyAccountId() {
        Account mockAccount = Account.builder().id(1).username("HE163256").firstName("mock").lastName("Student account").build();
        Student mockStudent = Student.builder().id(6).account(mockAccount).build();

        when(studentRepository.findStudentByAccountId(1)).thenReturn(Optional.of(mockStudent));
        Student testStudent = studentService.getStudentByAccountId(1);

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
}
