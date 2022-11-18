package com.example.studentmanagementfa22.service;

import com.example.studentmanagementfa22.dto.AccountDTO;
import com.example.studentmanagementfa22.dto.ClassroomDTO;
import com.example.studentmanagementfa22.dto.TeacherDTO;
import com.example.studentmanagementfa22.entity.Account;
import com.example.studentmanagementfa22.entity.Classroom;
import com.example.studentmanagementfa22.entity.Pagination;
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
public class TeacherServiceTest {
    @Mock
    private TeacherRepository teacherRepository;

    @Mock
    private StudentRepository studentRepository;
    @Mock
    private TeacherMapper teacherMapper;

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
    public void checkTeacherAssignedClass() {
        Teacher mockTeacher = Teacher.builder().id(2).build();
        Optional<Teacher> mockOptionalTeacher = Optional.of(mockTeacher);
        Classroom mockClassroom = Classroom.builder().id(2).teacher(mockTeacher).build();

        when(teacherRepository.findTeacherByClassroomId(mockClassroom.getId())).thenReturn(mockOptionalTeacher);

   //     boolean check = teacherService.checkTeacherAssignedClass(mockTeacher.getId(), mockClassroom.getId());

//        assertTrue(check, "Teacher is assigned to class");
        verify(teacherRepository, times(1)).findTeacherByClassroomId(2);
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
}
