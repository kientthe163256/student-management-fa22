package com.example.studentmanagementfa22.service;

import com.example.studentmanagementfa22.entity.Account;
import com.example.studentmanagementfa22.entity.Mark;

import java.util.List;

public interface MarkService {
    List<Mark> getMarksBySubject (Account account, int subjectId);

    Mark editStudentMark(int markID, Mark editMark, int accountID);

    void addStudentMark(Mark studentId, Integer id, Integer classId, Integer integer);

    void deleteMark(Integer id, Integer accountId);

}
