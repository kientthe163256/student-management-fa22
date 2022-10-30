package com.example.studentmanagementfa22.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

@SpringBootTest
public class MarkRepositoryTest {
    @Autowired
    private  MarkRepository markRepository;
    @Test
    public void getTotalWeightofStudentMark() {
        double currentWeight = markRepository.getTotalWeightofStudentMark(1);
        Assert.isTrue(currentWeight <= 1.0, "Total weight can not exceed 1.0");
    }
}
