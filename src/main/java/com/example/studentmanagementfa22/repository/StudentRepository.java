package com.example.studentmanagementfa22.repository;

import com.example.studentmanagementfa22.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Integer> {
    Optional<Student> findById(Integer id);

    Optional<Student> findStudentByAccountId(Integer id);

}
