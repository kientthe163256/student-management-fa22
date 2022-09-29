package com.example.studentmanagementfa22.repository;

import com.example.studentmanagementfa22.entity.Classroom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClassroomRepository extends JpaRepository<Classroom, Integer> {
    Classroom findByClassroomName(String classroomName);
}
