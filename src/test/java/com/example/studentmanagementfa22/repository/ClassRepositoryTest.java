package com.example.studentmanagementfa22.repository;

import com.example.studentmanagementfa22.entity.Account;
import com.example.studentmanagementfa22.entity.ClassType;
import com.example.studentmanagementfa22.entity.Classroom;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringRunner;


import java.util.List;


import static org.junit.jupiter.api.Assertions.assertNotNull;

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
                .build();
        classroomRepository.save(classroom);
        Classroom foundClassroom = classroomRepository.findByClassroomName("AB1521");
        assertNotNull(foundClassroom);
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
        assertNotNull(foundClassroom);
    }

    @Test
    public void saveSessionClassroom(){
        classroomRepository.addSessionClassroom("TE1616", 12, ClassType.SESSION.name());
        Classroom classroom = classroomRepository.findByClassroomName("TE1616");
        assertNotNull(classroom);
    }

    @Test
    public void getAllClassroom(){
        List<Classroom> classroomList = classroomRepository.findAll();
        assertNotNull(classroomList);
    }

    @Test
    public void getTeachingClassrooms() {
        Sort sortObject = Sort.by(Sort.Direction.DESC,"id");
        PageRequest pageRequest = PageRequest.of(1, 3, sortObject);
        Page<Classroom> classrooms =  classroomRepository.findClassroomsByTeacherId(2, pageRequest);
        assertNotNull(classrooms);
    }

}
