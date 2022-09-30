package com.example.studentmanagementfa22.service;

import com.example.studentmanagementfa22.StudentManagementFa22Application;
import com.example.studentmanagementfa22.entity.Subject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes= StudentManagementFa22Application.class)
public class SubjectServiceTest {
    @Autowired
    private SubjectService subjectService;

    @Test
    public void getAllSubject() {
        Page<Subject> subjectPage = subjectService.getAllSubject(1);
    }

}