package com.example.studentmanagementfa22.repository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class MarkRepositoryTest {
    @Autowired
    private MarkRepository markRepository;

    @Autowired
    private MarkTypeRepository markTypeRepository;

    @Test
    public void getMarkInRange(){
        double highest = markRepository.getHighestScore(1);
        double lowest = markRepository.getLowestScore(1);
        double ranged = markRepository.getNoStudentsInRange(1, 2, 10);
    }
}