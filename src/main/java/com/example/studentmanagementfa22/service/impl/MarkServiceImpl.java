package com.example.studentmanagementfa22.service.impl;

import com.example.studentmanagementfa22.dto.MarkDTO;
import com.example.studentmanagementfa22.dto.MarkReportDTO;
import com.example.studentmanagementfa22.entity.*;
import com.example.studentmanagementfa22.repository.*;
import com.example.studentmanagementfa22.service.*;
import com.example.studentmanagementfa22.utility.mapper.IGenericMapper;
import com.example.studentmanagementfa22.utility.i18n.MessageCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MarkServiceImpl implements MarkService {
    @Autowired
    private MarkRepository markRepository;
    @Autowired
    private ClassroomRepository classroomRepository;

    @Autowired
    private MarkTypeRepository markTypeRepository;

    @Autowired
    private IGenericMapper<Mark, MarkDTO> markMapper;

    @Autowired
    private TeacherService teacherService;
    @Autowired
    private SubjectService subjectService;

    @Autowired
    private StudentService studentService;

    @Autowired
    private StudentReportRepository reportRepository;

    @Autowired
    private SubjectRepository subjectRepository;


    @Override
    public List<MarkDTO> getMarksBySubject(Account account, int subjectId) {
        Student student = studentService.getStudentByAccountId(account.getId());
        List<Mark> markList =  markRepository.getMarkbySubject(student.getId(), subjectId);
        List<MarkDTO> markDTOList = markList.stream().map(mark ->markMapper.mapToDTO(mark)).collect(Collectors.toList());
        return markDTOList;
    }

    @Override
    public MarkDTO editStudentMark(int markID, Mark editMark, int accountID) {
        Teacher teacher = teacherService.getTeacherByAccountId(accountID);
        Optional<Mark> optionalMark = markRepository.getMarkByIDandTeacherID(teacher.getId(), markID );
        if (optionalMark.isEmpty()) {
            throw new NoSuchElementException("Mark not found");
        }
        Mark mark = optionalMark.get();
        mark.setGrade(editMark.getGrade());
        mark.setModifyDate(new Date());
        markRepository.updateMark(mark.getGrade(), markID);
        MarkDTO markDTO = markMapper.mapToDTO(mark);
        return markDTO;
    }

//    @Override
//    public MarkDTO addStudentMark(Mark mark, Integer accountId, Integer classId, Integer studentId, Integer markTypeId) {
//        teacherService.checkTeacherClassroomStudent(accountId, classId, studentId);
//        Optional<Student> student = studentRepository.findById(studentId);
//        Optional<Classroom> classroom = classroomRepository.findById(classId);
//        mark.setCreateDate(new Date());
//        mark.setSubject(classroom.get().getSubject());
//        mark.setId(mark.getId());
//        Optional<MarkType> markType = markTypeRepository.findById(markTypeId);
//        if (markType.isEmpty()) {
//            throw new NoSuchElementException("No mark type found");
//        }
//        mark.setMarkType(markType.get());
//        mark.setGrade(mark.getGrade());
//        mark.setStudent(student.get());
//        markRepository.addMark(mark.getGrade(), mark.getMarkType().getId(), mark.getStudent().getId(), mark.getSubject().getId());
//        return markMapper.mapToDTO(mark);
//    }


    @Override
    public void deleteMark(Integer markId, Integer teacherAccountId) {
        Teacher teacher = teacherService.getTeacherByAccountId(teacherAccountId);
        Optional<Mark> optionalMark = markRepository.getMarkByIDandTeacherID(teacher.getId(), markId );
        if (optionalMark.isEmpty()) {
            throw new NoSuchElementException("Mark not found");
        }
        studentService.checkStudentTeacher(optionalMark.get().getStudent().getId(), teacher.getId());
        markRepository.deleteMark(markId);
    }

    @Override
    public List<MarkDTO> getMarksByClassroomStudent(Integer teacherAccountId, Integer classId, Integer studentId) {
        Optional<Classroom> classroom = classroomRepository.findById(classId);
        if(classroom.isEmpty()) {
            throw new NoSuchElementException("Class not found");
        }
        teacherService.checkTeacherClassroomStudent(teacherAccountId, classId, studentId);
        List<Mark> markList = markRepository.getMarkbySubject( studentId, classroom.get().getSubject().getId());
        List<MarkDTO> markDTOList = markList.stream().map(mark ->markMapper.mapToDTO(mark)).collect(Collectors.toList());
        return markDTOList;
    }

    @Override
    public MarkReportDTO getMarkReportByClassId(int classId) {
        Optional<Classroom> classroom = classroomRepository.findById(classId);
        if (classroom.isEmpty()){
            throw new NoSuchElementException(MessageCode.CLASSROOM);
        }
        MarkReportDTO report = new MarkReportDTO();
        report.setTotal(classroom.get().getCurrentNoStudent());

        List<StudentReport> studentReports = reportRepository.getReportsByClassId(classId);
        report.setHighest(StudentReportService.getTopReports(studentReports, "highest"));
        report.setLowest(StudentReportService.getTopReports(studentReports, "lowest"));

        report.setExcellent(StudentReportService.getReportsInRange(studentReports, 9, 10));
        report.setGood(StudentReportService.getReportsInRange(studentReports, 8, 8.99));
        report.setFair(StudentReportService.getReportsInRange(studentReports, 5, 7.99));
        report.setFailed(StudentReportService.getReportsInRange(studentReports, 0, 4.99));
        return report;
    }

    @Override
    public void addStudentSubjectMark(Integer studentId, Integer subjectId) {
        List<Mark> markList = markRepository.getMarkbyStudentSubject(studentId, subjectId);
        if (markList.size() > 0) {
            markRepository.restoreStudentSubjectMark(studentId, subjectId);
        }
        else {
            Subject subject = subjectService.getById(subjectId);
            List<Integer> listOfMarkTypes = markTypeRepository.listOfMarkTypesBySubject(subject.getId());
            for (int markTypeId : listOfMarkTypes) {
                int numberOfMarksTypes = markTypeRepository.numberOfSubjectMarksTypes(subject.getId(), markTypeId);
                for (int i = 0; i < numberOfMarksTypes; i++) {
                    markRepository.addStudentSubjectMark(studentId, subject.getId(), markTypeId);
                }
            }
        }
    }
}
