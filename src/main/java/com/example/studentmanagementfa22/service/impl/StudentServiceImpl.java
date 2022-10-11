package com.example.studentmanagementfa22.service.impl;

import com.example.studentmanagementfa22.entity.Account;
import com.example.studentmanagementfa22.entity.Student;
import com.example.studentmanagementfa22.repository.StudentRepository;
import com.example.studentmanagementfa22.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;


@Service
public class StudentServiceImpl implements StudentService {
    @Autowired
    private StudentRepository studentRepository;

    @Override
    public void addStudentWithNewAccount(Account account) {
        Date today = new Date();
        //Add a new student with registered account
        Student student = Student.builder()
                .academicSession(Integer.parseInt(account.getUsername().substring(2,4)))
                .accountId(account.getId())
                .createDate(today)
                .modifyDate(today)
                .build();
        studentRepository.save(student);
    }
}
