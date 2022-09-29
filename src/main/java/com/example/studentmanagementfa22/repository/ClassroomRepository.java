package com.example.studentmanagementfa22.repository;

import com.example.studentmanagementfa22.entity.Classroom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ClassroomRepository extends JpaRepository<Classroom, Integer> {
    Classroom findByClassroomName(String classroomName);
    @Query(value = "SELECT ca.deleted, ca.id, ca.class_type, ca.classroom_name, ca.current_no_student, ca.teacher_id, ca.no_student, ca.subject_id\n" +
            "FROM classroom ca\n" +
            "JOIN classroom cb\n" +
            "ON ca.id  = cb.id\n" +
            "WHERE ca.current_no_student < cb.no_student\n" +
            "AND ca.deleted = 0\n" +
            "AND ca.subject_id = ?1", nativeQuery = true)
    Page<Classroom> findAllAvailClassroom(Pageable pageable, @Param("subject_id") int subjectId);
}
