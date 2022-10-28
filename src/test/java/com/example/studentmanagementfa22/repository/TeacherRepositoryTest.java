package com.example.studentmanagementfa22.repository;

import com.example.studentmanagementfa22.dto.TeacherDTO;
import com.example.studentmanagementfa22.entity.Teacher;
import com.example.studentmanagementfa22.service.TeacherService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.domain.Specification;

import javax.transaction.Transactional;
import java.util.List;

import static com.example.studentmanagementfa22.entity.TeacherSpecification.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class TeacherRepositoryTest {
    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private TeacherService teacherService;

    @Test
    public void showTeacherList() {
        List<Teacher> teacherList = teacherRepository.findAll();
        assertNotNull(teacherList);
    }

    @Test
    public void getTeacherWithCriteria() {
//        List<Teacher> teacherList = teacherService.findTeacherWithCriteria(1, 10, "id", "desc");
//        assertNotNull(teacherList);

        List<TeacherDTO> objects = teacherService.getAllTeacherPaging(1, 10, " , ");
        assertNotNull(objects);
    }

    @Test
    public void getTeacherWithSpecification(){
        Specification<Teacher> specification = hasFirstName("Test");

        List<Teacher> teachers = teacherRepository.findAll(specification);

        assertEquals(teachers.size(), 3);
    }
}
