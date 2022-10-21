package com.example.studentmanagementfa22.repository;

import com.example.studentmanagementfa22.entity.Teacher;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.Assert;

import javax.transaction.Transactional;
import java.util.List;

@SpringBootTest
@Transactional
@RunWith(SpringRunner.class)
public class TeacherRepositoryTest {
    @Autowired
    private TeacherRepository teacherRepository;

    @Test
    public void showTeacherList(){
        List<Teacher> teacherList = teacherRepository.findAll();
        Assert.notNull(teacherList);
    }
}
