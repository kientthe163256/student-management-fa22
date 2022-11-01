package com.example.studentmanagementfa22.repository;

import com.example.studentmanagementfa22.entity.Subject;
import org.hibernate.annotations.Where;
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
public interface SubjectRepository extends JpaRepository<Subject, Integer> {
    @Override
    Page<Subject> findAll(Pageable pageable);

    @Query(value = "SELECT s.id,s.no_credit, s.deleted , s.subject_name FROM student_management_fa22.subject s \n" +
            "INNER JOIN student_management_fa22.classroom c \n" +
            "ON s.id = c.subject_id\n" +
            "INNER JOIN student_management_fa22.student_classroom sc\n" +
            "ON c.id = sc.classroom_id\n" +
            "WHERE sc.student_id = ?1", nativeQuery = true)
    Page<Subject> findAllSubjectRegistered(Pageable pageable, @Param("student_id") int studentId);

    @Modifying
    @Transactional
    @Query(value = "UPDATE subject set deleted = 1, delete_date = curdate() where id = :subjectId", nativeQuery = true)
    void deleteSubject(@Param("subjectId") Integer subjectId);

    Subject findBySubjectName(String subjectName);

}
