package com.example.studentmanagementfa22.service;

import com.example.studentmanagementfa22.dto.MarkDTO;
import com.example.studentmanagementfa22.entity.Account;
import com.example.studentmanagementfa22.entity.Mark;

import java.util.List;

public interface MarkService {
    List<Mark> getMarksBySubject (Account account, int subjectId);

    MarkDTO editStudentMark(int markID, Mark editMark, int accountID);

    MarkDTO addStudentMark(Mark studentId, Integer id, Integer classId, Integer integer);

    void deleteMark(Integer id, Integer accountId);

    List<MarkDTO> getMarksByClassroomStudent(Integer teacherAccountId, Integer classId, Integer studentID);
}
