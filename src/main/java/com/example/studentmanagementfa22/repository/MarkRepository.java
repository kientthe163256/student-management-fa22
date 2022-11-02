package com.example.studentmanagementfa22.repository;

import com.example.studentmanagementfa22.entity.Mark;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public interface MarkRepository extends JpaRepository<Mark, Integer> {
    @Query(value = "SELECT * FROM student_management_fa22.mark\n" +
            "Where student_id = :student_id\n" +
            "AND subject_id = :subject_id AND deleted = false", nativeQuery = true)
    List<Mark> getMarkbySubject(@Param("student_id") int  studentId, @Param("subject_id")  int subjectId);

    @Query(value = "SELECT distinct m.id, m.subject_id, m.student_id, m.mark_item, m.grade, m.weight, m.create_date, m.modify_date, m.delete_date, m.deleted FROM student_management_fa22.mark m\n" +
            "JOIN student_management_fa22.student_classroom sc\n" +
            "ON m.student_id = sc.student_id\n" +
            "JOIN student_management_fa22.classroom c\n" +
            "ON c.id = sc.classroom_id\n" +
            "WHERE c.teacher_id = :teacher_id\n" +
            "AND m.id = :mark_id", nativeQuery = true)
    Optional<Mark> getMarkByIDandTeacherID(@Param("teacher_id") int  teacherId, @Param("mark_id")  int markId);

    Optional<Mark> findMarkById( int markId);
    @Query(value = "SELECT IFNULL(SUM(weight), 0)AS TotalWeight FROM student_management_fa22.mark\n" +
            "WHERE student_id = ?1 and subject_id = ?2", nativeQuery = true)
    double getTotalWeightofStudentMark( int studentId, Integer subjectId);
    @Modifying
    @Transactional
    @Query(value = "UPDATE mark set deleted = 1, delete_date = CURRENT_TIMESTAMP() where id = ?1", nativeQuery = true)
    void deleteMark(Integer id);
}
