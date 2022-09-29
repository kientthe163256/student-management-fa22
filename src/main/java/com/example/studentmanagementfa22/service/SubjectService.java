package com.example.studentmanagementfa22.service;

import com.example.studentmanagementfa22.entity.Subject;
import org.springframework.data.domain.Page;

import java.util.List;


public interface SubjectService {
    Page<Subject> getAllSubject(int pageNumber);

    List<Subject> getSubjectList();

    Subject findById(int id);
}
