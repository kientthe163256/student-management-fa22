package com.example.studentmanagementfa22.service;

import com.example.studentmanagementfa22.dto.MarkDTO;

import com.example.studentmanagementfa22.entity.*;
import com.example.studentmanagementfa22.repository.MarkRepository;
import com.example.studentmanagementfa22.repository.StudentRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

import com.example.studentmanagementfa22.repository.TeacherRepository;
import com.example.studentmanagementfa22.service.impl.MarkServiceImpl;
import com.example.studentmanagementfa22.utility.IGenericMapper;
import com.example.studentmanagementfa22.utility.MarkMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;

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
    @Mock
    private TeacherRepository teacherRepository;

    @Mock
    private MarkMapper mapper;
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

        Student mockStudent = Student.builder().id(1).build();
        Optional<Student> mockOptionalStudent= Optional.of(mockStudent);
        Subject mockSubject = Subject.builder().id(1).build();

        Mark mockMark = Mark.builder()
                .id(1)
                .student(mockStudent)
                .subject(mockSubject)
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
        assertEquals(markList.get(0).getStudent().getId(), mockMarkList.get(0).getStudent().getId());
        assertEquals(markList.get(0).getGrade(), mockMarkList.get(0).getGrade());

    }
    @Test
    public void checkDeleteMark() {
        Student mockStudent = Student.builder().id(1).build();
        Subject mockSubject = Subject.builder().id(1).build();
        Account mockAccount = Account.builder().id(2).roleId(2).build();
        Teacher mockTeacher = Teacher.builder().id(1).account(mockAccount).build();
        Mark mockMark = Mark.builder()
                .id(1)
                .student(mockStudent)
                .subject(mockSubject)
                .grade(7.0)
                .weight(0.1)
                .deleted(false)
                .build();
        Optional<Teacher> optionalTeacher = Optional.of(mockTeacher);
        Optional<Mark> optionalMark = Optional.of(mockMark);
        when(teacherRepository.findTeacherByAccountId(mockAccount.getId())).thenReturn(optionalTeacher);
        when(markRepository.getMarkByIDandTeacherID( mockTeacher.getId(),mockMark.getId())).thenReturn(optionalMark);
        doAnswer( invocation -> {
            mockMark.setDeleted(true);
            return  null;
        }).when(markRepository).deleteMark(1);
        markService.deleteMark(1, 2);
        assertTrue(mockMark.isDeleted());
        verify(teacherRepository, times(1)).findTeacherByAccountId(mockAccount.getId());
        verify(markRepository, times(1)).getMarkByIDandTeacherID(1,1);
    }

    @Test
    public void checkUpdateMark() {
        int markID = 5;
        int teacherAccountId = 2;
        Student mockStudent = Student.builder().id(9).build();
        Subject mockSubject = Subject.builder().id(8).build();
        Mark unupdatedMark = Mark.builder().id(markID).grade(9).weight(0.5).student(mockStudent).subject(mockSubject).createDate(null).deleteDate(null).modifyDate(new Date()).build();
        Mark updatedMark = Mark.builder().id(markID).grade(7).weight(0.4).student(mockStudent).subject(mockSubject).createDate(null).deleteDate(null).modifyDate(new Date()).build();

        Optional<Mark> optionalMark = Optional.of(unupdatedMark);
        Account mockAccount = Account.builder().id(teacherAccountId).roleId(2).build();
        Teacher mockTeacher = Teacher.builder().id(1).account(mockAccount).build();
        Optional<Teacher> optionalTeacher = Optional.of(mockTeacher);
        MarkDTO markDTO = MarkDTO.builder().id(markID).grade(7).weight(0.4).createDate(null).deleteDate(null).modifyDate(new Date()).build();

        when(teacherRepository.findTeacherByAccountId(mockAccount.getId())).thenReturn(optionalTeacher);
        when(markRepository.getMarkByIDandTeacherID( mockTeacher.getId(),unupdatedMark.getId())).thenReturn(optionalMark);
        when(markRepository.getTotalWeightofStudentMark(mockStudent.getId(), mockSubject.getId())).thenReturn(0.9);

        doAnswer((Answer<MarkDTO>) invocation -> mockMapToDTO(updatedMark)).when(mapper).mapToDTO(updatedMark);

        doAnswer((Answer<Void>) invocation -> {
            mockUpdate(updatedMark, markDTO);
            return null;
            }).when(markRepository).updateMark(7, 0.4 , 5);
        MarkDTO updateMarkDTO = markService.editStudentMark(markID, updatedMark, teacherAccountId);

        assertEquals(updateMarkDTO.getWeight(), updatedMark.getWeight());
        assertEquals(updateMarkDTO.getGrade(), updatedMark.getGrade());
        verify(markRepository, times(1)).getMarkByIDandTeacherID(1,5);
        verify(markRepository, times(1)).getTotalWeightofStudentMark(9,8);
    }

    private MarkDTO mockUpdate(Mark currentMark, MarkDTO markDTO){
        markDTO.setId(currentMark.getId());
        markDTO.setGrade(currentMark.getGrade());
        markDTO.setWeight(currentMark.getWeight());
        markDTO.setModifyDate(currentMark.getModifyDate());
        return null;
    }
    private MarkDTO mockMapToDTO(Mark currentMark){
        MarkDTO markDTO = MarkDTO.builder()
                .id(currentMark.getId())
                .grade(currentMark.getGrade())
                .weight(currentMark.getWeight())
                .modifyDate(currentMark.getModifyDate())
                .build();
        return markDTO;
    }

}
