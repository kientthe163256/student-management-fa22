package com.example.studentmanagementfa22.service.impl;

import com.example.studentmanagementfa22.dto.MarkDTO;
import com.example.studentmanagementfa22.dto.MarkTypeDTO;
import com.example.studentmanagementfa22.entity.*;
import com.example.studentmanagementfa22.repository.*;
import com.example.studentmanagementfa22.service.MarkService;
import com.example.studentmanagementfa22.service.StudentService;
import com.example.studentmanagementfa22.service.SubjectService;
import com.example.studentmanagementfa22.service.TeacherService;
import com.example.studentmanagementfa22.utility.IGenericMapper;
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
    private StudentRepository studentRepository;

    @Autowired
    private SubjectRepository subjectRepository;


    @Override
    public List<Mark> getMarksBySubject(Account account, int subjectId) {
        Optional<Student> student = studentRepository.findStudentByAccountId(account.getId());
        if (student.isEmpty()) {
            return null;
        }
        return markRepository.getMarkbySubject(student.get().getId(), subjectId);
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

    @Override
    public MarkDTO addStudentMark(Mark mark, Integer accountId, Integer classId, Integer studentId, Integer markTypeId) {
        teacherService.checkTeacherClassroomStudent(accountId, classId, studentId);
        Optional<Student> student = studentRepository.findById(studentId);
        Optional<Classroom> classroom = classroomRepository.findById(classId);
        mark.setCreateDate(new Date());
        mark.setSubject(classroom.get().getSubject());
        mark.setId(mark.getId());
        Optional<MarkType> markType = markTypeRepository.findById(markTypeId);
        if (markType.isEmpty()) {
            throw new NoSuchElementException("No mark type found");
        }
        mark.setMarkType(markType.get());
        mark.setGrade(mark.getGrade());
        mark.setStudent(student.get());

        markRepository.addMark(mark.getGrade(), mark.getMarkType().getId(), mark.getStudent().getId(), mark.getSubject().getId());
        return markMapper.mapToDTO(mark);

    }


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
        teacherService.checkTeacherClassroomStudent(teacherAccountId, classId, studentId);
        Optional<Classroom> classroom = classroomRepository.findById(classId);
        List<Mark> markList = markRepository.getMarkbySubject( studentId, classroom.get().getSubject().getId());
        List<MarkDTO> markDTOList = markList.stream().map(mark ->markMapper.mapToDTO(mark)).collect(Collectors.toList());
        return markDTOList;
    }

    @Override
    public void addStudentSubjectMark(Integer studentId, Integer subjectId) {
      //  int numberOfMarks = subjectRepository.numberOfSubjectMarks(subjectId);
        Subject subject = subjectService.getById(subjectId);
        List<Integer> listOfMarkTypes = markTypeRepository.listOfMarkTypesBySubject(subject.getId());
        for(int markTypeId : listOfMarkTypes) {
            int numberOfMarksTypes = markTypeRepository.numberOfSubjectMarksTypes(subject.getId(), markTypeId);
            for (int i = 0; i < numberOfMarksTypes; i++) {
                markRepository.addStudentSubjectMark(studentId,subject.getId(),markTypeId);
            }
        }
    }
}
