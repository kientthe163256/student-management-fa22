package com.example.studentmanagementfa22.repository;

import com.example.studentmanagementfa22.entity.ClassType;
import com.example.studentmanagementfa22.entity.Classroom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface ClassroomRepository extends JpaRepository<Classroom, Integer> {
    Classroom findByClassroomName(String classroomName);

    Classroom findById(int classId);
    @Query(value = "SELECT * FROM student_management_fa22.classroom ca\n" +
            "WHERE ca.current_no_student < ca.no_student\n" +
            "AND ca.deleted = 0\n" +
            "AND ca.subject_id = ?1", nativeQuery = true)
    Page<Classroom> findAllAvailClassroom(Pageable pageable, @Param("subject_id") int subjectId);

    @Query(value = "INSERT INTO `student_management_fa22`.`student_classroom`\n" +
            "(`student_id`,\n" +
            "`classroom_id`)\n" +
            "VALUES\n" +
            "(?1,\n" +
            "?2)", nativeQuery = true)
    @Modifying
    @Transactional
    void registerClassroom(int studentId, int classroomId);

    @Query(value = "SELECT COUNT(student_id) FROM student_management_fa22.student_classroom sc\n" +
            "INNER JOIN student_management_fa22.classroom c\n" +
            "ON c.id = sc.classroom_id\n" +
            "INNER JOIN student_management_fa22.subject sub\n" +
            "ON c.subject_id = sub.id\n" +
            "WHERE sub.id = ?1\n" +
            "AND sc.student_id =?2", nativeQuery = true)
    Integer numOfSubjectClassbyStudent (@Param("subject_id") int subjectId, @Param("student_id") int studentId);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO classroom (classroom_name, no_student, class_type) VALUES (?1, ?2, ?3)",
            nativeQuery = true)
    void addSessionClassroom(String className, int noStudent, String classType);

    @Modifying
    @Query(value = "INSERT INTO classroom (classroom_name, no_student, class_type, subject_id) VALUES (?1, ?2, ?3, ?4)",
            nativeQuery = true)
    void addSubjectClassroom(String className, int noStudent, String classType, int subjectId);
}
