package com.example.studentmanagementfa22.repository;

import com.example.studentmanagementfa22.entity.MarkType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MarkTypeRepository extends JpaRepository<MarkType, Integer> {
    Optional<MarkType> findById(int markTypeId);
    @Query(value = "SELECT marktype_id from student_management_fa22.subject_marktype\n" +
            "WHERE subject_id = ?1 ", nativeQuery = true)
    List<Integer> listOfMarkTypesBySubject (int subjectId);

    @Query(value = "SELECT no_marks from student_management_fa22.subject_marktype\n" +
            "WHERE subject_id = ?1 AND marktype_id = ?2",
            nativeQuery = true)
    int numberOfSubjectMarksTypes(Integer subjectId, int markTypeId);
}
