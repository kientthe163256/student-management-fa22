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

    @Query(value = "SELECT distinct m.id, m.subject_id, m.student_id, m.grade, m.mark_type_id, m.create_date, m.modify_date, m.delete_date, m.deleted FROM student_management_fa22.mark m\n" +
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

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO mark (grade, marktype_id, mark_item, student_id, subject_id, create_date) VALUES (?1, ?2, ?3, ?4, ?5,  CURRENT_TIMESTAMP)",
            nativeQuery = true)
    void addMark(double grade, double markTypeId, String markItem, int studentId, int subjectId);

    @Modifying
    @Transactional
    @Query(value = "UPDATE mark set grade = ?1  ,modify_date = CURRENT_TIMESTAMP() where id = ?2", nativeQuery = true)
    void updateMark(double grade, int markId);

    @Query(value = "select max(average_score) from \n" +
            "(select sum(m.grade * mt.weight) as average_score from mark m \n" +
            "join mark_type mt on m.mark_type_id = mt.id\n" +
            "join subject s on m.subject_id = s.id\n" +
            "join classroom c on c.subject_id = s.id\n" +
            "where c.id = :class_id\n" +
            "group by student_id) sub;", nativeQuery = true)
    double getHighestScore(@Param("class_id") int classId);

    @Query(value = "select min(average_score) from \n" +
            "(select sum(m.grade * mt.weight) as average_score from mark m \n" +
            "join mark_type mt on m.mark_type_id = mt.id\n" +
            "join subject s on m.subject_id = s.id\n" +
            "join classroom c on c.subject_id = s.id\n" +
            "where c.id = :class_id\n" +
            "group by student_id) sub;", nativeQuery = true)
    double getLowestScore(@Param("class_id") int classId);

    @Query(value = "select round(avg(average_score), 2) from \n" +
            "(select sum(m.grade * mt.weight) as average_score from mark m \n" +
            "join mark_type mt on m.mark_type_id = mt.id\n" +
            "join subject s on m.subject_id = s.id\n" +
            "join classroom c on c.subject_id = s.id\n" +
            "where c.id = :class_id\n" +
            "group by student_id) sub;", nativeQuery = true)
    double getAverageScore(@Param("class_id") int classId);

    @Query(value = "select count(sub.student_id) from\n" +
            "(select m.student_id, sum(m.grade * mt.weight) as average_score from mark m \n" +
            "join mark_type mt on m.mark_type_id = mt.id\n" +
            "join subject s on m.subject_id = s.id\n" +
            "join classroom c on c.subject_id = s.id\n" +
            "where c.id = :class_id\n" +
            "group by student_id\n" +
            "having average_score >= :min and average_score <= :max) sub;", nativeQuery = true)
    int getNoStudentsInRange(@Param("class_id") int classId, @Param("min") double min, @Param("max") double max);
    @Query(value = "select count(m.id) from mark m join classroom c on m.subject_id = c.subject_id where c.id", nativeQuery = true)
    int getTotalMarkByClassId(@Param("class_id") int classId);


    @Modifying
    @Transactional
    @Query(value = "INSERT INTO mark ( student_id, subject_id, mark_type_id, create_date) VALUES (?1, ?2, ?3,  CURRENT_TIMESTAMP)",
            nativeQuery = true)
    void addStudentSubjectMark(Integer studentId, Integer subjectId, int markTypeId);
}
