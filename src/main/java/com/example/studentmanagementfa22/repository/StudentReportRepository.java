package com.example.studentmanagementfa22.repository;

import com.example.studentmanagementfa22.entity.StudentReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentReportRepository extends JpaRepository<StudentReport, Integer> {
    @Procedure(procedureName = "get_students_in_range")
    List<StudentReport> getReportByQuality(String quality, int classId);


    @Query(value = "select m.student_id, sum(m.grade * mt.weight) as average_score from mark m \n" +
            "join mark_type mt on m.mark_type_id = mt.id\n" +
            "join subject s on m.subject_id = s.id\n" +
            "join classroom c on c.subject_id = s.id\n" +
            "where c.id = :classId\n" +
            "group by student_id", nativeQuery = true)
    List<StudentReport> getReportsByClassId(@Param("classId") int classId);

}
