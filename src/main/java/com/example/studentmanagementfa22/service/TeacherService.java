package com.example.studentmanagementfa22.service;

import com.example.studentmanagementfa22.dto.TeacherDTO;
import com.example.studentmanagementfa22.entity.Account;
import com.example.studentmanagementfa22.entity.Pagination;
import com.example.studentmanagementfa22.entity.Teacher;

import java.util.List;

public interface TeacherService {
    TeacherDTO addTeacherWithNewAccount(Account account);

    Teacher getById(int id);

    Teacher getTeacherByAccountId(int accountId);

    TeacherDTO getTeacherDTOById(int teacherId);

    void deleteTeacher(Integer teacherId);

    void checkTeacherAssignedClass( Integer teacherId, Integer classroomId);

    void checkTeacherClassroomStudent(Integer teacherAccountId, Integer classId,Integer studentId);

    Pagination<TeacherDTO> getAllTeacherPaging(int pageNumber, int pageSize, String sort);

    void removeStudentClassroom(Integer teacherAccountId, Integer studentId, Integer classId);

    void addStudentToSessionClass(Integer teacherAccountId, Integer classId, Integer studentId);
}
