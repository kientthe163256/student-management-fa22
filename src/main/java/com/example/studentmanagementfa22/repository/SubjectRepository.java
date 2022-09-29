package com.example.studentmanagementfa22.repository;

import com.example.studentmanagementfa22.entity.Subject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface SubjectRepository extends JpaRepository<Subject, Integer> {
    @Override
    Page<Subject> findAll(Pageable pageable);
}
