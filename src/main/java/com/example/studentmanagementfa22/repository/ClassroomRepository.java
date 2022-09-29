package com.example.studentmanagementfa22.repository;

import com.example.studentmanagementfa22.entity.ClassType;
import com.example.studentmanagementfa22.entity.Classroom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

public interface ClassroomRepository extends JpaRepository<Classroom, Integer> {
    Classroom findByClassroomName(String classroomName);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO classroom (classroom_name, no_student, class_type) VALUES (?1, ?2, ?3)",
        nativeQuery = true)
    void addSessionClassroom(String className, int noStudent, ClassType classType);

    @Query(value = "INSERT INTO classroom (classroom_name, no_student, class_type, subject_id) VALUES (?1, ?2, ?3, ?4)",
        nativeQuery = true)
    @Modifying
    void addSubjectClassroom(String className, int noStudent, ClassType classType, int subjectId);
}
