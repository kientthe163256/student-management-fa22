package com.example.studentmanagementfa22.repository;

import com.example.studentmanagementfa22.entity.Mark;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MarkRepository extends JpaRepository<Mark, Integer> {
    @Query(value = "SELECT * FROM student_management_fa22.mark\n" +
            "Where student_id = :student_id\n" +
            "AND subject_id = :subject_id", nativeQuery = true)
    List<Mark> getMarkbySubject(@Param("student_id") int  studentId, @Param("subject_id")  int subjectId);

    Optional<Mark> findMarkByStudentIdAndId(int studentId, int markId);
}
