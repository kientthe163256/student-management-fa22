package com.example.studentmanagementfa22.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class StudentRepositoryTest {
    @Autowired
    private StudentRepository studentRepository;

    @Test
    public void getNoStudentByCriteria() {
        int noStudent = studentRepository.getNoStudentbyCriteria(1,2,2);
        assertEquals(noStudent, 0);
    }

}
