package com.example.studentmanagementfa22.repository;

import com.example.studentmanagementfa22.entity.Classroom;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.modelmapper.internal.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;

@SpringBootTest
@Transactional
@RunWith(SpringRunner.class)
public class ClassRepositoryTest {
    @Autowired
    private ClassroomRepository classroomRepository;

    @Test
    public void saveClassroom(){
        Classroom classroom = Classroom.builder()
                .classroomName("AB1521")
                .classType("SUB")
                .currentNoStudent(0)
                .teacherId(1)
                .build();
        classroomRepository.save(classroom);
        Classroom foundClassroom = classroomRepository.findByClassroomName("AB1521");
        Assert.notNull(foundClassroom);
    }
}
