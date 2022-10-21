package com.example.studentmanagementfa22.service;

import com.example.studentmanagementfa22.dto.TeacherDTO;
import com.example.studentmanagementfa22.entity.Account;
import com.example.studentmanagementfa22.entity.Teacher;
import org.springframework.data.domain.Page;

import javax.transaction.Transactional;
import java.util.List;

public interface TeacherService {
    void addTeacherWithNewAccount(Account account);

    List<Teacher> findAllTeachers();

    Teacher findById(int id);

    List<TeacherDTO> findAllTeacherPaging(int pageNumber, int pageSize, String sortCriteria, String direction);

    TeacherDTO getTeacherDTOById(int teacherId);

    void deleteTeacher(Integer teacherId);
}
