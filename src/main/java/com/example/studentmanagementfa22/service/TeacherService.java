package com.example.studentmanagementfa22.service;

import com.example.studentmanagementfa22.dto.TeacherDTO;
import com.example.studentmanagementfa22.entity.Account;
import com.example.studentmanagementfa22.entity.Pagination;
import com.example.studentmanagementfa22.entity.Teacher;

import java.util.List;

public interface TeacherService {
    void addTeacherWithNewAccount(Account account);

    List<Teacher> findAllTeachers();

    Teacher getById(int id);

    TeacherDTO getTeacherDTOById(int teacherId);

    void deleteTeacher(Integer teacherId);

    boolean checkTeacherAssignedClass( Integer teacherId, Integer classroomId);

    Pagination<TeacherDTO> getAllTeacherPaging(int pageNumber, int pageSize, String sort);
}
