package com.example.studentmanagementfa22.service;

import com.example.studentmanagementfa22.entity.Subject;
import org.springframework.data.domain.Page;



public interface SubjectService {
    Page<Subject> getAllSubject(int pageNumber);
}
