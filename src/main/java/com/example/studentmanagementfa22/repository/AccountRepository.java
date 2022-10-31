package com.example.studentmanagementfa22.repository;

import com.example.studentmanagementfa22.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, String> {
    Account findByUsername(String username);
    Optional<Account> findById(Integer id);

    @Modifying
    @Query(value = "UPDATE account set enabled = 0, delete_date = curdate() where id = :accountId", nativeQuery = true)
    Integer disableAccount(@Param("accountId") Integer accountId);//return account
    @Query(value = "SELECT a.id, a.create_date, a.delete_date, a.modify_date, a.enabled, a.first_name, a.last_name, a.dob, a.password, a.role_id, a.username\n" +
            "FROM student_management_fa22.account a\n" +
            "JOIN student_management_fa22.student AS s\n" +
            "ON a.id = s.account_id\n" +
            " JOIN student_management_fa22.student_classroom AS sc\n" +
            "ON s.id = sc.student_id\n" +
            "JOIN student_management_fa22.classroom AS c\n" +
            "ON sc.classroom_id = c.id\n" +
            "WHERE c.id = ?1\n" +
            "AND c.teacher_id = ?2  ",

            countQuery = "select count(a.id) FROM student_management_fa22.student AS student JOIN student_management_fa22.student_classroom AS sc\n" +
                    "ON student.id = sc.student_id\n" +
                    "JOIN student_management_fa22.classroom c\n" +
                    "ON c.id = sc.classroom_id\n" +
                    "JOIN student_management_fa22.account a\n" +
                    "ON a.id = student.account_id\n" +
                    "WHERE c.id = ?1\n" +
                    "AND c.teacher_id = ?2 "
            ,nativeQuery = true)
    //Pagination of native query results requires an extra step.
    Page<Account> findStudentAccountsByClassroomandTeacher( int  teacherId,  int classId, Pageable pageable);
}
