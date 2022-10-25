package com.example.studentmanagementfa22.repository;

import com.example.studentmanagementfa22.dto.TeacherDTO;
import com.example.studentmanagementfa22.entity.Teacher;
import com.example.studentmanagementfa22.service.TeacherService;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


import javax.transaction.Transactional;
import java.util.List;

@SpringBootTest
@Transactional
public class TeacherRepositoryTest {
    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private TeacherService teacherService;

    @Test
    public void showTeacherList(){
        List<Teacher> teacherList = teacherRepository.findAll();
        assertNotNull(teacherList);
    }

    @Test
    public void getTeacherPaging(){
        List<TeacherDTO> teacherDTOList = teacherService.findAllTeacherPaging(1, 2, "firstName", "asc");
        assertNotNull(teacherDTOList);
    }
    @Test
    public void getTeacherWithCriteria(){
//        List<Teacher> teacherList = teacherService.findTeacherWithCriteria(1, 2, "firstName", "asc");
//        assertNotNull(teacherList);
    }
}
