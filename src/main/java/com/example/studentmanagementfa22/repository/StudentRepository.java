package com.example.studentmanagementfa22.repository;

import com.example.studentmanagementfa22.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Integer> {
    @Query(value = "SELECT a.username ,a.first_name, a.last_name, a.dob, s.academic_session  FROM student_management_fa22.student s\n" +
            "INNER JOIN student_management_fa22.account a\n" +
            "ON s.account_id = a.id\n" +
            "WHERE s.id = ?1", nativeQuery = true)
    Optional<Student> findById(Integer id);
}
