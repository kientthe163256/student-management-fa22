package com.example.studentmanagementfa22.service.impl;

import com.example.studentmanagementfa22.entity.*;
import com.example.studentmanagementfa22.repository.*;
import com.example.studentmanagementfa22.service.MarkService;
import com.example.studentmanagementfa22.service.StudentService;
import com.example.studentmanagementfa22.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class MarkServiceImpl implements MarkService {
    @Autowired
    private MarkRepository markRepository;

    @Autowired
    private TeacherRepository teacherRepository;
    @Autowired
    private ClassroomRepository classroomRepository;

    @Autowired
    private StudentService studentService;

    @Autowired
    private TeacherService teacherService;
    @Autowired
    private StudentRepository studentRepository;

    @Override
    public List<Mark> getMarksBySubject(Account account, int subjectId) {
        Optional<Student> student = studentRepository.findStudentByAccountId(account.getId());
        if (student.isEmpty()) {
            return null;
        }
        return markRepository.getMarkbySubject(student.get().getId(), subjectId);
    }

    @Override
    public Mark editStudentMark(int markID, Mark editMark, int accountID) {
        Optional<Teacher> optionalTeacher = teacherRepository.findTeacherByAccountId(accountID);
        if (optionalTeacher.isEmpty()) {
            throw  new NoSuchElementException("Teacher not found");
        }
        Optional<Mark> optionalMark = markRepository.getMarkByIDandTeacherID(optionalTeacher.get().getId(), markID );
        if (optionalMark.isEmpty()) {
            throw new NoSuchElementException("Mark not found");
        }
        Mark mark = optionalMark.get();
        mark.setGrade(editMark.getGrade());
        mark.setModifyDate(new Date());
        double currentWeight = markRepository.getTotalWeightofStudentMark(mark.getStudent().getId(), mark.getSubject().getId());
        //check total weight after edit
        if (  currentWeight - mark.getWeight() + editMark.getWeight()  > 1.0) {
            throw new IllegalArgumentException("Total weight can not be over 1.0. Current total weight: "+currentWeight);
        }
        mark.setWeight(editMark.getWeight());
        markRepository.save(mark);
        return mark;
    }

    @Override
    public Mark addStudentMark(Mark mark, Integer accountId, Integer classId,Integer studentId) {
        teacherService.checkTeacherClassroomStudent(accountId, classId, studentId);
        Optional<Student> student = studentRepository.findById(studentId);
        Optional<Classroom> classroom = classroomRepository.findById(classId);
        mark.setCreateDate(new Date());
        mark.setSubject(classroom.get().getSubject());
        mark.setId(mark.getId());
        mark.setWeight(mark.getWeight());
        mark.setGrade(mark.getGrade());
        mark.setStudent(student.get());
        double currentWeight = markRepository.getTotalWeightofStudentMark(studentId, mark.getSubject().getId());
        if (mark.getWeight() + currentWeight > 1.0) {
            throw new IllegalArgumentException("Total weight can not be over 1.0. Current total weight: "+currentWeight);
        }
        return  markRepository.save(mark);

    }


    @Override
    public void deleteMark(Integer markId, Integer teacherAccountId) {
        Optional<Teacher> optionalTeacher = teacherRepository.findTeacherByAccountId(teacherAccountId);
        if (optionalTeacher.isEmpty()) {
            throw  new NoSuchElementException("Teacher not found");
        }
        Optional<Mark> optionalMark = markRepository.getMarkByIDandTeacherID(optionalTeacher.get().getId(), markId );
        if (optionalMark.isEmpty()) {
            throw new NoSuchElementException("Mark not found");
        }
        markRepository.deleteMark(markId);
    }

    @Override
    public List<Mark> getMarksByClassroomStudent(Integer teacherAccountId, Integer classId, Integer studentId) {
        teacherService.checkTeacherClassroomStudent(teacherAccountId, classId, studentId);
        Optional<Classroom> classroom = classroomRepository.findById(classId);
        List<Mark> markList = markRepository.getMarkbySubject( studentId, classroom.get().getSubject().getId());
        return markList;
    }
}
