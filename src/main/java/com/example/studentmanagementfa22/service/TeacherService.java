package com.example.studentmanagementfa22.service;

import com.example.studentmanagementfa22.entity.Account;
import com.example.studentmanagementfa22.entity.Teacher;

import java.util.List;

public interface TeacherService {
    void addTeacherWithNewAccount(Account account);

    List<Teacher> findAllTeachers();
}
