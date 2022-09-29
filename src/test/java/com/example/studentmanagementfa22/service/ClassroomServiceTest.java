package com.example.studentmanagementfa22.service;

import com.example.studentmanagementfa22.dto.ClassroomDTO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.modelmapper.internal.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.util.List;

@SpringBootTest
@Transactional
@RunWith(SpringRunner.class)
public class ClassroomServiceTest {
    @Autowired
    private ClassroomService classroomService;

    @Test
    public void checkExistedClassroom(){
        boolean classExisted = classroomService.classroomExisted("SE1605");
        Assert.isTrue(classExisted);
    }

    @Test
    public void getAllClassroom(){
        List<ClassroomDTO> classroomDTOList = classroomService.getAllClassrooms();
        Assert.notNull(classroomDTOList);
    }

}
