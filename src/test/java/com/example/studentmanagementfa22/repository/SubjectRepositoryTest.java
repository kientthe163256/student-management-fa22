package com.example.studentmanagementfa22.repository;

import com.example.studentmanagementfa22.dto.MarkTypeDTO;
import com.example.studentmanagementfa22.entity.Subject;
import com.example.studentmanagementfa22.service.SubjectService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Map;

@SpringBootTest
@RunWith(SpringRunner.class)
public class SubjectRepositoryTest {
    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private SubjectService subjectService;

    @Test
    public void getAllSubject(){
        List<Subject> subjectList = subjectRepository.findAll();
        subjectList.forEach(s -> {
            if (s.getClassrooms().size() == 0) System.out.println(s.getSubjectName());
        });
        Assertions.assertNotNull(subjectList);
    }

    @Test
    public void getMarkTypes(){
        Subject subject = subjectRepository.findById(1).get();

    }

    @Test
    public void getAssessments(){
//        Map<MarkTypeDTO, Integer> assess = subjectService.addMarkTypeToSubject(2,1,1);
//        Assert.assertNotNull(assess);
    }
}
