package com.example.studentmanagementfa22.repository;

import com.example.studentmanagementfa22.entity.Teacher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Optional;


@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Integer> {
    @Modifying
    @Transactional
    @Query(value = "UPDATE teacher set deleted = 1, delete_date = curdate() where id = :teacherId", nativeQuery = true)
    void deleteTeacher(@Param("teacherId") Integer teacherId);

    @Override
    @Query(value = "SELECT * from teacher where deleted = 0", nativeQuery = true)
    Page<Teacher> findAll(Pageable pageable);

    Optional<Teacher> findTeacherByAccountId(int accountID);
}
