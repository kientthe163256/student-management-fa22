package com.example.studentmanagementfa22.repository;

import com.example.studentmanagementfa22.entity.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.persistence.Tuple;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;


@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Integer>, JpaSpecificationExecutor<Teacher> {
    Optional<Teacher> findTeacherByAccountId(int accountID);
    @Query(value = "SELECT t.* FROM student_management_fa22.teacher t\n" +
            "join student_management_fa22.classroom c\n" +
            "on t.id = c.teacher_id\n" +
            "where c.id = ?1",nativeQuery = true)
    Optional<Teacher> findTeacherByClassroomId(int classroomId);
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM student_management_fa22.student_classroom\n" +
            "WHERE student_id = ?1 AND classroom_id = ?2", nativeQuery = true)
    void deleteStudentClassroom(Integer studentId, Integer classId);
}
