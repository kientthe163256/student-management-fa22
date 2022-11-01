package com.example.studentmanagementfa22.repository;

import com.example.studentmanagementfa22.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Integer> {
    Optional<Student> findById(Integer id);

//    @Query(value = "SELECT count(sc.student_id) FROM student_management_fa22.student_classroom sc\n" +
//            "WHERE sc.student_id = ?1 \n" +
//            "AND (SELECT c.id FROM student_management_fa22.classroom c WHERE c.teacher_id = ?2 AND c.subject_id = ?3) = sc.classroom_id",
//            nativeQuery = true)
//    int getNoStudentbyCriteria(int studentId,int teacherId,int subjectId);
    @Query(value = "SELECT count(sc.student_id) FROM student_management_fa22.student_classroom sc\n" +
        "JOIN student_management_fa22.classroom c\n" +
        "ON sc.classroom_id = c.id\n" +
        "WHERE sc.student_id = 1 AND c.teacher_id = 2 AND c.subject_id = 2",
        nativeQuery = true)
    int getNoStudentbyCriteria(int studentId,int teacherId,int subjectId);
    Optional<Student> findStudentByAccountId(Integer id);
    @Query(value = "SELECT count(sc.student_id) FROM student_management_fa22.student_classroom sc Where sc.student_id =?1 AND sc.classroom_id = ?2",
            nativeQuery = true)
    int getStudentClassroom(Integer studentId, Integer classId);
}
