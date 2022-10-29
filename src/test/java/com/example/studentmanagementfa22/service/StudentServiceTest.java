package com.example.studentmanagementfa22.service;

import com.example.studentmanagementfa22.dto.StudentDTO;
import com.example.studentmanagementfa22.entity.Account;
import com.example.studentmanagementfa22.entity.Classroom;
import com.example.studentmanagementfa22.entity.Teacher;
import com.example.studentmanagementfa22.repository.AccountRepository;
import com.example.studentmanagementfa22.repository.TeacherRepository;
import com.example.studentmanagementfa22.service.impl.StudentServiceImpl;
import com.example.studentmanagementfa22.utility.IGenericMapper;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.Test;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class StudentServiceTest {
    @Mock
    private TeacherRepository teacherRepository;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private IGenericMapper<Account, StudentDTO> mapper;

    @InjectMocks
    private StudentServiceImpl studentService;
    @MockitoSettings(strictness = Strictness.WARN)

    @Test
    public void getListofStudentByCriteria() {
        //Createa Mock data
        Account mockAccount = Account.builder()
                .id(3)
                .roleId(2)
                .build();

        Teacher mockTeacher = Teacher.builder()
                .id(2)
                .account(mockAccount)
                .build();
        Optional<Teacher> mockOptionalTeacher = Optional.of(mockTeacher);
        Classroom mockClassroom = Classroom.builder()
                .id(2)
                .teacherId(2)
                .build();
        Account studentAcc1 = Account.builder().id(1).build();
        Account studentAcc2 = Account.builder().id(2).build();
        StudentDTO studentDTO1 = StudentDTO.builder().id(3).accountId(studentAcc1.getId()).build();
        StudentDTO studentDTO2 = StudentDTO.builder().id(4).accountId(studentAcc2.getId()).build();
        List<StudentDTO> mockStudentDTOList = new ArrayList<>();
        mockStudentDTOList.add(studentDTO1);
        mockStudentDTOList.add(studentDTO2);
        List<Account> studentAccountList = new ArrayList<>();
        studentAccountList.add(studentAcc1);
        studentAccountList.add(studentAcc2);
        Page<Account> mockPageStudentAccount = new PageImpl<>(studentAccountList);
        Sort sortObject = Sort.by(Sort.Direction.DESC,"id");
        PageRequest pageRequest = PageRequest.of(1, 5, sortObject);
        //define repository behavior
        when(teacherRepository.findTeacherByAccountId(mockAccount.getId())).thenReturn(mockOptionalTeacher);
        when(accountRepository.findStudentAccountByClassroomandTeacher(mockOptionalTeacher.get().getId(), mockClassroom.getId(), pageRequest)).thenReturn(mockPageStudentAccount);
        when(mapper.mapToDTO(studentAcc1)).thenReturn(studentDTO1);
        when(mapper.mapToDTO(studentAcc2)).thenReturn(studentDTO2);
        when(mockPageStudentAccount.stream().map(account -> {
            StudentDTO studentDTO = mapper.mapToDTO(account);
            return  studentDTO;
        }).collect(Collectors.toList())).thenReturn(mockStudentDTOList);
        // call Service
        List<StudentDTO> studentDTOList = studentService.getStudentsByClassroomandTeacher(2,3,1,5,"id,DESC" );
        //Assert result
        assertEquals(2, studentDTOList.size());
        assertTrue(studentDTOList.get(0).getId() > studentDTOList.get(1).getId(), "Sorted by ID descending");


    }
}
