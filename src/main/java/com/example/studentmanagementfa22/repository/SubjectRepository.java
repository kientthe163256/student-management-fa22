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

import javax.persistence.Tuple;
import javax.transaction.Transactional;
import java.util.List;
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

    Subject findBySubjectName(String subjectName);

    @Query(value = "INSERT INTO subject_mark_type(`subject_id`, `mark_type_id`, `no_marks`) VALUES (?1, ?2, ?3)", nativeQuery = true)
    @Modifying
    @Transactional
    void addMarkTypeToSubject(int subjectId, int markTypeId, int noMarks);

    @Query(value = "select mt.name, mt.weight, smt.no_marks from mark_type mt\n" +
            "join subject_mark_type smt on mt.id = smt.mark_type_id\n" +
            "where smt.subject_id = :subjectId", nativeQuery = true)
    List<Tuple> getAssessmentsBySubjectId(@Param("subjectId") int subjectId);
    @Query(value = "SELECT sum(no_marks) from student_management_fa22.subject_marktype\n" +
            "WHERE subject_id = ?1",
    nativeQuery = true)
    int numberOfSubjectMarks(int subjectId);


}
