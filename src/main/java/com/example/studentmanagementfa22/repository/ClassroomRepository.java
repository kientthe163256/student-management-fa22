package com.example.studentmanagementfa22.repository;

import com.example.studentmanagementfa22.entity.Classroom;
import com.example.studentmanagementfa22.entity.Teacher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface ClassroomRepository extends JpaRepository<Classroom, Integer> {
    Classroom findByClassroomName(String classroomName);

    Classroom findById(int classId);
    @Query(value = "SELECT * FROM student_management_fa22.classroom \n" +
            "WHERE current_no_student < no_student\n" +
            "AND class_type = 'SUBJECT'\n" +
            "AND deleted = 0\n" +
            "AND subject_id = :subject_id", nativeQuery = true)
    Page<Classroom> findAllAvailClassroom(Pageable pageable, @Param("subject_id") int subjectId);

    @Query(value = "INSERT INTO `student_management_fa22`.`student_classroom`\n" +
            "(`student_id`,\n" +
            "`classroom_id`)\n" +
            "VALUES\n" +
            "(:student_id,\n" +
            ":classsroom_id)", nativeQuery = true)
    @Modifying
    @Transactional
    void registerClassroom(@Param("student_id") int studentId, @Param("classsroom_id") int classroomId);

    @Query(value = "SELECT COUNT(student_id) FROM student_management_fa22.student_classroom sc\n" +
            "INNER JOIN student_management_fa22.classroom c\n" +
            "ON c.id = sc.classroom_id\n" +
            "INNER JOIN student_management_fa22.subject sub\n" +
            "ON c.subject_id = sub.id\n" +
            "WHERE sub.id = :subject_id\n" +
            "AND sc.student_id = :student_id", nativeQuery = true)
    Integer numOfSubjectClassByStudent (@Param("subject_id") int subjectId, @Param("student_id") int studentId);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO classroom (classroom_name, no_student, class_type) VALUES (?1, ?2, ?3)",
            nativeQuery = true)
    void addSessionClassroom(String className, int noStudent, String classType);

    @Modifying
    @Query(value = "INSERT INTO classroom (classroom_name, no_student, class_type, subject_id) VALUES (?1, ?2, ?3, ?4)",
            nativeQuery = true)
    void addSubjectClassroom(String className, int noStudent, String classType, int subjectId);

    @Query (value = "UPDATE classroom SET current_no_student = current_no_student + 1 where id = ?1", nativeQuery = true)
    @Modifying
    @Transactional
    void updateNoStudentOfClass(@Param("id") int classId);
    @Query(value = "SELECT id, classroom_name, current_no_student, no_student, deleted,class_type, teacher_id, subject_id, create_date, modify_date, delete_date\n" +
            "FROM student_management_fa22.classroom \n" +
            "INNER JOIN student_management_fa22.student_classroom \n" +
            "ON classroom.id = student_classroom.classroom_id\n" +
            "WHERE student_id = :student_id", nativeQuery = true)
    Page<Classroom> findAllRegisteredClass(Pageable pageable,@Param("student_id") int studentId);

    @Override
    @Query(value = "SELECT * from classroom where deleted = 0", nativeQuery = true)
    Page<Classroom> findAll(Pageable pageable);
}
