package com.example.studentmanagementfa22.repository;

import com.example.studentmanagementfa22.entity.Subject;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
public class SubjectRepositoryTest {
    @Autowired
    private SubjectRepository subjectRepository;


    @Test
    public void getAllSubject(){
        List<Subject> subjectList = subjectRepository.findAll();
        subjectList.forEach(s -> {
            if (s.getClassrooms().size() == 0) System.out.println(s.getSubjectName());
        });
        Assertions.assertNotNull(subjectList);
    }
}
