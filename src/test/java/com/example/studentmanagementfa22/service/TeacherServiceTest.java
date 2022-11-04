package com.example.studentmanagementfa22.service;

import com.example.studentmanagementfa22.dto.AccountDTO;
import com.example.studentmanagementfa22.dto.TeacherDTO;
import com.example.studentmanagementfa22.entity.Account;
import com.example.studentmanagementfa22.entity.Classroom;
import com.example.studentmanagementfa22.entity.Teacher;
import com.example.studentmanagementfa22.repository.StudentRepository;
import com.example.studentmanagementfa22.repository.TeacherRepository;
import com.example.studentmanagementfa22.service.impl.TeacherServiceImpl;
import com.example.studentmanagementfa22.utility.TeacherMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TeacherServiceTest {
    @Mock
    private TeacherRepository teacherRepository;

    @Mock
    private StudentRepository studentRepository;
    @Mock
    private TeacherMapper teacherMapper;

    @InjectMocks
    private TeacherServiceImpl teacherService;

    @Test
    public void whenGetByValidIdThenTeacherIsFound() {
        // 1. create mock data
        AccountDTO mockAccountDTO = AccountDTO.builder()
                .username("HE16356")
                .dob(new Date(15))
                .firstName("Mock")
                .lastName("Account")
                .build();

        TeacherDTO mockTeacherDTO = TeacherDTO.builder()
                .id(1)
                .account(mockAccountDTO)
                .build();

        Teacher mockTeacher = Teacher.builder()
//                .accountId(1)
                .build();
        Optional<Teacher> mockOptionalTeacher = Optional.of(mockTeacher);

        // 2. define behavior of Repository, Mapper
        when(teacherRepository.findById(1)).thenReturn(mockOptionalTeacher);
        when(teacherMapper.mapToDTO(mockTeacher)).thenReturn(mockTeacherDTO);

        // 3. call service method
        TeacherDTO teacherDTO = teacherService.getTeacherDTOById(1);

        // 4. assert the result
        assertEquals(teacherDTO, mockTeacherDTO);//equal some fields
    }
    @Test
    public void checkTeacherAssignedClass() {
        Teacher mockTeacher = Teacher.builder().id(2).build();
        Optional<Teacher> mockOptionalTeacher = Optional.of(mockTeacher);
        Classroom mockClassroom = Classroom.builder().id(2).teacher(mockTeacher).build();

        when(teacherRepository.findTeacherByClassroomId(mockClassroom.getId())).thenReturn(mockOptionalTeacher);

        boolean check = teacherService.checkTeacherAssignedClass(mockTeacher.getId(), mockClassroom.getId());

        assertTrue(check, "Teacher is assigned to class");
        verify(teacherRepository, times(1)).findTeacherByClassroomId(2);
    }


    @Test
    public void whenGetByNonExistIdThenReturnNull(){
        TeacherDTO teacherDTO = teacherService.getTeacherDTOById(1000);
        assertNull(teacherDTO);
    }
}
