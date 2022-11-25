package com.example.studentmanagementfa22.service;

import com.example.studentmanagementfa22.dto.MarkDTO;
import com.example.studentmanagementfa22.dto.MarkReportDTO;
import com.example.studentmanagementfa22.dto.MarkTypeDTO;
import com.example.studentmanagementfa22.entity.*;
import com.example.studentmanagementfa22.repository.*;
import com.example.studentmanagementfa22.service.impl.MarkServiceImpl;
import com.example.studentmanagementfa22.utility.MarkMapper;
import com.example.studentmanagementfa22.utility.MarkTypeMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.mockito.stubbing.Answer;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class MarkServiceTest {
    @Mock
    private MarkRepository markRepository;

    @Mock
    private StudentRepository studentRepository;
    @Mock
    private ClassroomRepository classroomRepository;

    @Mock
    private MarkTypeRepository markTypeRepository;

    @Mock
    private TeacherService teacherService;

    @Mock
    private  StudentService studentService;

    @Mock
    private SubjectService subjectService;

    @Mock
    private MarkMapper markMapper;

    @Mock
    private MarkTypeMapper markTypeMapper;

    @Mock
    private StudentReportRepository reportRepository;

    @InjectMocks
    private MarkServiceImpl markService;

    @Test
    public void getStudentMarkBySubject() {

        Account mockAccount = Account.builder().id(5).username("HE16356").dob(new Date(15)).firstName("Mock").lastName("Account").build();
        Student mockStudent = Student.builder().id(1).build();
        Optional<Student> mockOptionalStudent= Optional.of(mockStudent);
        Subject mockSubject = Subject.builder().id(1).build();
        MarkType markType = MarkType.builder().id(3).name("mock mark type").build();

        Mark mockMark = Mark.builder().id(1).student(mockStudent).markType(markType).subject(mockSubject).grade(7.0).build();
        MarkDTO mockMarkDTO = MarkDTO.builder().id(3).grade(7.0).build();
        List<Mark> mockMarkList  = new ArrayList<>();
        mockMarkList.add(mockMark);
        // define behavior
        when(studentService.getStudentByAccountId(5)).thenReturn(mockStudent);
        when(markRepository.getMarkbySubject(mockStudent.getId(), mockSubject.getId())).thenReturn(mockMarkList);
        when(studentRepository.findStudentByAccountId(mockAccount.getId())).thenReturn(mockOptionalStudent);
        when(markMapper.mapToDTO(mockMark)).thenReturn(mockMarkDTO);
        // call service method
        List<MarkDTO> markList = markService.getMarksBySubject(mockAccount, 1);
        // assert the result
        assertEquals(markList.size(), mockMarkList.size());
        assertEquals(markList.get(0).getGrade(), mockMarkList.get(0).getGrade());

    }
    @Test void getMarkByClassroomStudent() {
        Student mockStudent = Student.builder().id(1).build();
        Account mockAccount = Account.builder().id(2).roleId(2).build();
        Teacher mockTeacher = Teacher.builder().id(4).account(mockAccount).build();
        Subject mockSubject = Subject.builder().id(1).build();
        Classroom classroom = Classroom.builder().id(3).classroomName("mock classroom").subject(mockSubject).build();

        MarkType markType = MarkType.builder().id(3).name("mock mark type").build();

        Mark mockMark = Mark.builder().id(1).student(mockStudent).markType(markType).grade(7.0).build();
        MarkDTO mockMarkDTO = MarkDTO.builder().id(3).grade(7.0).build();
        List<Mark> mockMarkList  = new ArrayList<>();
        mockMarkList.add(mockMark);
        when(classroomRepository.findById(3)).thenReturn(Optional.of(classroom));
        when(markRepository.getMarkbySubject(mockStudent.getId(), mockSubject.getId())).thenReturn(mockMarkList);
        when(markMapper.mapToDTO(mockMark)).thenReturn(mockMarkDTO);
        assertThrows(NoSuchElementException.class, () -> markService.getMarksByClassroomStudent(mockTeacher.getId(), 1000,mockStudent.getId()));
        List<MarkDTO> markList = markService.getMarksByClassroomStudent(mockTeacher.getId(), classroom.getId(),mockStudent.getId());
        assertEquals(markList.size(), mockMarkList.size());
        verify(markRepository, times(1)).getMarkbySubject(1,1);

    }
    @Test
    public void checkDeleteMark() {
        Student mockStudent = Student.builder().id(1).build();
        Subject mockSubject = Subject.builder().id(1).build();
        Account mockAccount = Account.builder().id(2).roleId(2).build();
        Teacher mockTeacher = Teacher.builder().id(1).account(mockAccount).build();
        Mark mockMark = Mark.builder().id(1).student(mockStudent).subject(mockSubject).grade(7.0).deleted(false).build();

        Optional<Mark> optionalMark = Optional.of(mockMark);
        when(teacherService.getTeacherByAccountId(mockAccount.getId())).thenReturn(mockTeacher);
        doAnswer((Answer<Void>) invocation -> {
            return null;
        }).when(studentService).checkStudentTeacher(1, 1);
        when(markRepository.getMarkByIDandTeacherID( mockTeacher.getId(),mockMark.getId())).thenReturn(optionalMark);
        doAnswer( invocation -> {
            mockMark.setDeleted(true);
            return  null;
        }).when(markRepository).deleteMark(1);
        markService.deleteMark(1, 2);
        assertThrows(NoSuchElementException.class, () -> markService.deleteMark(1000, 2));
        assertTrue(mockMark.isDeleted());
        verify(markRepository, times(1)).getMarkByIDandTeacherID(1,1);
    }

    @Test
    public void checkUpdateMark() {
        int markID = 5;
        int teacherAccountId = 2;
        Student mockStudent = Student.builder().id(9).build();
        Subject mockSubject = Subject.builder().id(8).build();
        MarkType mockMarkType = MarkType.builder().id(1).name("type").weight(0.1).build();
        MarkTypeDTO mockMarkTypeDTO = MarkTypeDTO.builder().name("type").weight(0.1).build();
        Mark unupdatedMark = Mark.builder().id(markID).grade(9).markType(mockMarkType).student(mockStudent).subject(mockSubject).createDate(null).deleteDate(null).modifyDate(new Date()).build();
        Mark updatedMark = Mark.builder().id(markID).grade(7).markType(mockMarkType).student(mockStudent).subject(mockSubject).createDate(null).deleteDate(null).modifyDate(new Date()).build();

        Optional<Mark> optionalMark = Optional.of(unupdatedMark);
        Account mockAccount = Account.builder().id(teacherAccountId).roleId(2).build();
        Teacher mockTeacher = Teacher.builder().id(1).account(mockAccount).build();

        MarkDTO markDTO = MarkDTO.builder().id(markID).grade(7).markType(mockMarkTypeDTO).build();

        when(teacherService.getTeacherByAccountId(mockAccount.getId())).thenReturn(mockTeacher);
        when(markRepository.getMarkByIDandTeacherID( mockTeacher.getId(),unupdatedMark.getId())).thenReturn(optionalMark);
        when(markRepository.getTotalWeightofStudentMark(mockStudent.getId(), mockSubject.getId())).thenReturn(0.9);

        doAnswer((Answer<MarkDTO>) invocation -> mockMapMarkToDTO(updatedMark)).when(markMapper).mapToDTO(updatedMark);
        doAnswer((Answer<MarkTypeDTO>) invocation -> mockMapTypeToDTO(mockMarkType)).when(markTypeMapper).mapToDTO(mockMarkType);
        doAnswer((Answer<MarkDTO>) invocation -> {
            mockUpdate(updatedMark, markDTO);
            return markDTO;
            }).when(markRepository).updateMark(7, 5);

        MarkDTO updateMarkDTO = markService.editStudentMark(markID, updatedMark, teacherAccountId);

        assertThrows(NoSuchElementException.class, () -> markService.editStudentMark(1000, unupdatedMark, 2));
        verify(markRepository, times(1)).getMarkByIDandTeacherID(1,5);
    }

    @Test
    public void addMarkNotExist() {
        Student mockStudent = Student.builder().id(9).build();
        Subject mockSubject = Subject.builder().id(8).subjectName("mock subject").build();
        MarkType markType1 = MarkType.builder().id(1).name("mock mark type 1").build();
        MarkType markType2 = MarkType.builder().id(2).name("mock mark type 2").build();
        List<Integer> listOfMarkTypesId = new ArrayList<>();
        listOfMarkTypesId.add(markType1.getId());
        listOfMarkTypesId.add(markType2.getId());

        List<Mark> mockMarkList  = new ArrayList<>();
        when(markRepository.getMarkbyStudentSubject(mockStudent.getId(), mockSubject.getId())).thenReturn(mockMarkList);
        when(subjectService.getById(8)).thenReturn(mockSubject);
        when(markTypeRepository.listOfMarkTypesBySubject(8)).thenReturn(listOfMarkTypesId);
        when(markTypeRepository.numberOfSubjectMarksTypes(8, 1)).thenReturn(1);
        when(markTypeRepository.numberOfSubjectMarksTypes(9, 2)).thenReturn(2);
        doAnswer((Answer<Void>) invocation -> {
            return null;
        }).when(markRepository).addStudentSubjectMark(9,8,1);
        doAnswer((Answer<Void>) invocation -> {
            return null;
        }).when(markRepository).addStudentSubjectMark(9,8,2);

        markService.addStudentSubjectMark(mockStudent.getId(), mockSubject.getId());

        verify(markTypeRepository, times(1)).numberOfSubjectMarksTypes(mockSubject.getId(), markType1.getId());
        verify(markTypeRepository, times(1)).numberOfSubjectMarksTypes(mockSubject.getId(), markType2.getId());
        verify(markTypeRepository, times(1)).listOfMarkTypesBySubject(mockSubject.getId());
        verify(markRepository, times(1)).addStudentSubjectMark(mockStudent.getId(), mockSubject.getId(), markType1.getId());
    }

    @Test
    public void addMarkExisted() {
        Student mockStudent = Student.builder().id(9).build();
        Subject mockSubject = Subject.builder().id(8).subjectName("mock subject").build();
        MarkType markType1 = MarkType.builder().id(1).name("mock mark type 1").build();
        MarkType markType2 = MarkType.builder().id(2).name("mock mark type 2").build();
        List<Integer> listOfMarkTypesId = new ArrayList<>();
        listOfMarkTypesId.add(markType1.getId());
        listOfMarkTypesId.add(markType2.getId());
        Mark mockMark = Mark.builder().id(1).student(mockStudent).grade(7.0).build();
        List<Mark> mockMarkList  = new ArrayList<>();
        mockMarkList.add(mockMark);
        when(markRepository.getMarkbyStudentSubject(mockStudent.getId(), mockSubject.getId())).thenReturn(mockMarkList);
        when(subjectService.getById(8)).thenReturn(mockSubject);
        when(markTypeRepository.listOfMarkTypesBySubject(8)).thenReturn(listOfMarkTypesId);
        when(markTypeRepository.numberOfSubjectMarksTypes(8, 1)).thenReturn(1);
        when(markTypeRepository.numberOfSubjectMarksTypes(9, 2)).thenReturn(2);
        doAnswer((Answer<Void>) invocation -> {
            return null;
        }).when(markRepository).restoreStudentSubjectMark(9,8);
        doAnswer((Answer<Void>) invocation -> {
            return null;
        }).when(markRepository).addStudentSubjectMark(9,8,1);
        doAnswer((Answer<Void>) invocation -> {
            return null;
        }).when(markRepository).addStudentSubjectMark(9,8,2);

        markService.addStudentSubjectMark(mockStudent.getId(), mockSubject.getId());

        verify(markRepository, times(1)).restoreStudentSubjectMark(mockStudent.getId(), mockSubject.getId());
        verify(markTypeRepository, times(0)).listOfMarkTypesBySubject( mockSubject.getId());


    }
    private MarkDTO mockUpdate(Mark currentMark, MarkDTO markDTO){
        markDTO.setId(currentMark.getId());
        markDTO.setGrade(currentMark.getGrade());
      //  markDTO.getMarkTypeDTO( typeMapper.mapToDTO(currentMark.getMarkType()) );
        return null;
    }
    private MarkDTO mockMapMarkToDTO(Mark currentMark){
        MarkDTO markDTO = MarkDTO.builder()
                .id(currentMark.getId())
                .grade(currentMark.getGrade())
                .build();
        return markDTO;
    }

    private MarkTypeDTO mockMapTypeToDTO(MarkType markType) {
        MarkTypeDTO markTypeDTO = MarkTypeDTO.builder().name(markType.getName()).weight(markType.getWeight()).build();
        return markTypeDTO;
    }

    @Test
    public void getMarkReportWithNonExistId(){
        int nonExistClassId = 100;
        when(classroomRepository.findById(nonExistClassId)).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> markService.getMarkReportByClassId(nonExistClassId));
        verify(classroomRepository, times(1)).findById(nonExistClassId);
    }

    @Test
    public void getMarkReportSuccessfully(){
        Classroom mockClass = Classroom.builder().id(1).currentNoStudent(3).build();
        int classId = mockClass.getId();
        StudentReport report1 = new StudentReport(1, 6.3);
        StudentReport report2 = new StudentReport(2, 6.4);
        StudentReport report3 = new StudentReport(7, 6.4);
        List<StudentReport> reports = List.of(report1, report2, report3);
        List<StudentReport> lowest = List.of(report1);


        when(classroomRepository.findById(classId)).thenReturn(Optional.of(mockClass));
        when(reportRepository.getReportsByClassId(classId)).thenReturn(reports);

        MarkReportDTO markReportDTO = markService.getMarkReportByClassId(classId);

        assertEquals(mockClass.getCurrentNoStudent(), markReportDTO.getTotal());
        assertEquals(lowest, markReportDTO.getLowest());

        verify(classroomRepository, times(1)).findById(classId);
    }
}
