package com.example.studentmanagementfa22.repository;

import com.example.studentmanagementfa22.entity.ClassType;
import com.example.studentmanagementfa22.entity.Classroom;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.modelmapper.internal.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
public class ClassRepositoryTest {
    @Autowired
    private ClassroomRepository classroomRepository;


    @Test
    public void saveClassroom(){
        Classroom classroom = Classroom.builder()
                .classroomName("AB1521")
                .classType(ClassType.SESSION)
                .noStudent(12)
                .teacherId(1)
                .build();
        classroomRepository.save(classroom);
        Classroom foundClassroom = classroomRepository.findByClassroomName("AB1521");
        Assert.notNull(foundClassroom);
    }

    @Test
    public void saveClassroomWithoutTeacherId(){
        Classroom classroom = Classroom.builder()
                .classroomName("TE1521")
                .classType(ClassType.SESSION)
                .noStudent(12)
                .build();
        classroomRepository.save(classroom);
        Classroom foundClassroom = classroomRepository.findByClassroomName("TE1521");
        Assert.notNull(foundClassroom);
    }

    @Test
    public void saveSessionClassroom(){
        classroomRepository.addSessionClassroom("TE1616", 12, ClassType.SESSION.name());
        Classroom classroom = classroomRepository.findByClassroomName("TE1616");
        Assert.notNull(classroom);
    }

    @Test
    public void getAllClassroom(){
        ClassType classType = ClassType.SESSION;
        System.out.println(classType.name());
        List<Classroom> classroomList = classroomRepository.findAll();
        Assert.notNull(classroomList);
    }

    @org.junit.jupiter.api.Test
    public void getAllClassroom2(){
        PageRequest pageRequest = PageRequest.of(1, 2);
        Page<Classroom> classroomList = classroomRepository.findAllRegisteredClass2(pageRequest, 1);
        Assert.notNull(classroomList);
    }

}
