package com.example.studentmanagementfa22.service.impl;

import com.example.studentmanagementfa22.entity.Subject;
import com.example.studentmanagementfa22.repository.SubjectRepository;
import com.example.studentmanagementfa22.service.SubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;


@Service
public class SubjectServiceImpl implements SubjectService {
    @Autowired
    private SubjectRepository subjectRepository;
    @Override
    public Page<Subject> getAllSubject(int pageNumber){
        PageRequest pageRequest = PageRequest.of(pageNumber-1, 5);
        Page<Subject> subjectPage = subjectRepository.findAll(pageRequest);
        return subjectPage;
    }
}
