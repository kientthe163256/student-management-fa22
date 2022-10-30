package com.example.studentmanagementfa22.service;

import com.example.studentmanagementfa22.dto.TeacherDTO;
import com.example.studentmanagementfa22.entity.Account;
import com.example.studentmanagementfa22.entity.Teacher;

import java.util.List;

public interface TeacherService {
    void addTeacherWithNewAccount(Account account);

    List<Teacher> findAllTeachers();

    Teacher getById(int id);

    TeacherDTO getTeacherDTOById(int teacherId);

    void deleteTeacher(Integer teacherId);

    List<TeacherDTO> getAllTeacherPaging(int pageNumber, int pageSize, String sort);

    boolean checkStudentExistbyCriteria(Integer studentId, Integer teacherAccountId, Integer classroomId);
}
