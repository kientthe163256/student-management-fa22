package com.example.studentmanagementfa22.service;

import com.example.studentmanagementfa22.dto.TeacherDTO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.modelmapper.internal.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;

@SpringBootTest
@Transactional
@RunWith(SpringRunner.class)
public class TeacherServiceTest {
    @Autowired
    private TeacherService teacherService;

    @Test
    public void showTeacherList(){
//        Page<TeacherDTO> teacherDTOPage = teacherService.findAllTeacherPaging(1);
//        Assert.notNull(teacherDTOPage);
    }

}
