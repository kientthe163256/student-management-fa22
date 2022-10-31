package com.example.studentmanagementfa22.service.impl;

import com.example.studentmanagementfa22.entity.*;
import com.example.studentmanagementfa22.repository.ClassroomRepository;
import com.example.studentmanagementfa22.repository.MarkRepository;
import com.example.studentmanagementfa22.repository.StudentRepository;
import com.example.studentmanagementfa22.repository.TeacherRepository;
import com.example.studentmanagementfa22.service.MarkService;
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
    private TeacherService teacherService;
    @Autowired
    private StudentRepository studentRepo;

    @Override
    public List<Mark> getMarksBySubject(Account account, int subjectId) {
        Optional<Student> student = studentRepo.findStudentByAccountId(account.getId());
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
        markRepository.save(mark);
        return mark;
    }

    @Override
    public void addStudentMark(Mark mark, Integer accountId, Integer classId) {
        Optional<Student> student = studentRepo.findById(mark.getStudentId());
        if(student.isEmpty()) {
            throw new NoSuchElementException("Student ID not exists");
        }
        Optional<Classroom> classroom = classroomRepository.findById(classId);
        if (classroom.isEmpty()){
            throw new NoSuchElementException("Classroom not exist");
        }
        Optional<Teacher> teacher = teacherRepository.findTeacherByAccountId(accountId);
        if (teacher.isEmpty()) {
            throw  new NoSuchElementException("Teacher not found");
        }
        if(!teacherService.checkStudentExistbyCriteria(student.get().getId(), accountId, classroom.get().getId())) {
            throw new IllegalArgumentException("You are not allowed to add this student subject'marks");
        }
        mark.setCreateDate(new Date());
        mark.setSubjectId(classroom.get().getSubjectId());
        mark.setId(mark.getId());
        mark.setWeight(mark.getWeight());
        mark.setGrade(mark.getGrade());
        // check weight
        int studentId = student.get().getId();
        double currentWeight = markRepository.getTotalWeightofStudentMark(studentId);
        if (mark.getWeight() + currentWeight > 1.0) {
            throw new IllegalArgumentException("Total weight can not be over 1.0. Current total weight: "+currentWeight);
        }
        markRepository.save(mark);
    }

    @Override
    public void deleteMark(Integer id) {
        Optional<Mark> optionalMark = markRepository.findMarkById(id);
        if (optionalMark.isEmpty()) {
            throw new NoSuchElementException("Mark not found");
        }
        markRepository.deleteMark(id);
    }
}
