package com.example.studentmanagementfa22.service;

import com.example.studentmanagementfa22.dto.StudentDTO;
import com.example.studentmanagementfa22.entity.Account;
import com.example.studentmanagementfa22.entity.Student;

import java.util.List;

public interface StudentService {
    StudentDTO addStudentWithNewAccount(Account account);

    List<StudentDTO> getStudentsByClassroomandTeacher(Integer classID, Integer accountID, int pageNumber, int pageSize, String sort);

    boolean checkStudentJoinedClass(Integer id, Integer classId);

    void checkStudentTeacher(Integer studentId, Integer teacherId);

    Student getStudentByAccountId(int accountId);

    StudentDTO getStudentDTOByAccountId(Integer id);
}
