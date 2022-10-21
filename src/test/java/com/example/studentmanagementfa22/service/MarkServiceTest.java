package com.example.studentmanagementfa22.service;
import com.example.studentmanagementfa22.entity.*;
import com.example.studentmanagementfa22.repository.MarkRepository;
import com.example.studentmanagementfa22.repository.StudentRepository;
import com.example.studentmanagementfa22.service.impl.MarkServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class MarkServiceTest {
    @Mock
    private MarkRepository markRepository;

    @Mock
    private StudentRepository studentRepository;
    @InjectMocks
    private MarkServiceImpl markService;

    @Test
    public void getStudentMarkBySubject() {


        Account mockAccount = Account.builder()
                .id(5)
                .username("HE16356")
                .dob(new Date(15))
                .firstName("Mock")
                .lastName("Account")
                .build();

        Student mockStudent = Student.builder()
                .id(1)
                .accountId(5)
                .build();
        Optional<Student> mockOptionalStudent= Optional.of(mockStudent);
        Subject mockSubject = Subject.builder()
                .id(1)
                .build();

        Mark mockMark = Mark.builder()
                .id(1)
                .studentId(1)
                .subjectId(1)
                .grade(7.0)
                .weight(0.1)
                .build();
        List<Mark> mockMarkList  = new ArrayList<>();
        mockMarkList.add(mockMark);
        // define behavior of Repository
        when(markRepository.getMarkbySubject(mockStudent.getId(), mockSubject.getId())).thenReturn(mockMarkList);
        when(studentRepository.findStudentByAccountId(mockAccount.getId())).thenReturn(mockOptionalStudent);
        // call service method
        List<Mark> markList = markService.getMarksBySubject(mockAccount, 1);
        // assert the result
        assertEquals(markList.size(), mockMarkList.size());
        assertEquals(markList.get(0).getStudentId(), mockMarkList.get(0).getStudentId());
        assertEquals(markList.get(0).getGrade(), mockMarkList.get(0).getGrade());

    }
}
