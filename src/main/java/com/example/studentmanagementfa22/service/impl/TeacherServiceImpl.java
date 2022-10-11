package com.example.studentmanagementfa22.service.impl;

import com.example.studentmanagementfa22.dto.TeacherDTO;
import com.example.studentmanagementfa22.entity.Account;
import com.example.studentmanagementfa22.entity.Classroom;
import com.example.studentmanagementfa22.entity.Teacher;
import com.example.studentmanagementfa22.repository.TeacherRepository;
import com.example.studentmanagementfa22.service.TeacherService;
import com.example.studentmanagementfa22.utility.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.xml.crypto.Data;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class TeacherServiceImpl implements TeacherService {
    @Autowired
    private TeacherRepository teacherRepository;

//    @Autowired
//    private Mapper mapper;

    @Override
    public void addTeacherWithNewAccount(Account account) {
        Date today = new Date();
        Teacher teacher = Teacher.builder()
                .accountId(account.getId())
                .createDate(today)
                .modifyDate(today)
                .build();
        teacherRepository.save(teacher);
    }

    @Override
    public List<Teacher> findAllTeachers() {
        return teacherRepository.findAll();
    }

    @Override
    public Teacher findById(int id) {
        Optional<Teacher> optionalTeacher = teacherRepository.findById(id);
        if (optionalTeacher.isEmpty()){
            throw new NoSuchElementException("Teacher not found");
        }
        Teacher teacher = optionalTeacher.get();
        return teacher;
    }

//    @Override
//    public Page<TeacherDTO> findAllTeacherPaging(int pageNumber) {
//        PageRequest pageRequest = PageRequest.of(pageNumber-1, 5);
//        Page<Teacher> teacherPage = teacherRepository.findAll(pageRequest);
//        return teacherPage.map(teacher -> mapper.mapTeacher(teacher));
//    }

    @Override
    public Page<Teacher> findAllTeacherPaging(int pageNumber) {
        PageRequest pageRequest = PageRequest.of(pageNumber-1, 5);
        Page<Teacher> teacherPage = teacherRepository.findAll(pageRequest);
        return teacherPage;
    }


}
