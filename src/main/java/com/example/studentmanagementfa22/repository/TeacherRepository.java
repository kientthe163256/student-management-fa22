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
    @Modifying
    @Transactional
    @Query(value = "UPDATE teacher set deleted = 1, delete_date = curdate() where id = :teacherId", nativeQuery = true)
    void deleteTeacher(@Param("teacherId") Integer teacherId);

    Optional<Teacher> findTeacherByAccountId(int accountID);
    @Query(value = "SELECT t.* FROM student_management_fa22.teacher t\n" +
            "join student_management_fa22.classroom c\n" +
            "on t.id = c.teacher_id\n" +
            "where c.id = ?1",nativeQuery = true)
    Optional<Teacher> findTeacherByClassroomId(int classroomId);

    @Query(value = "select t.id as tea, a.id as acc, a.username, a.first_name, a.last_name, a.dob from teacher t\n" +
            "                join account a on t.account_id = a.id", nativeQuery = true)
    List<Tuple> getTeacherDTO();


}
