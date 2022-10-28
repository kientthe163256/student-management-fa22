package com.example.studentmanagementfa22.service.impl;

import com.example.studentmanagementfa22.entity.Account;
import com.example.studentmanagementfa22.entity.Mark;
import com.example.studentmanagementfa22.entity.Student;
import com.example.studentmanagementfa22.repository.MarkRepository;
import com.example.studentmanagementfa22.repository.StudentRepository;
import com.example.studentmanagementfa22.service.MarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class MarkServiceImpl implements MarkService {
    @Autowired
    private MarkRepository markRepository;

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
    public Mark editStudentMark(int markID, Mark editMark) {
        Optional<Mark> optionalMark = markRepository.findMarkById(markID);
        if (optionalMark.isEmpty()) {
            throw new NoSuchElementException("Mark not found");
        }
        Mark mark = optionalMark.get();
        mark.setGrade(editMark.getGrade());
        mark.setModifyDate(new Date());
        markRepository.save(mark);
        return mark;
    }
}
