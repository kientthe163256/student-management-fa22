package com.example.studentmanagementfa22.service;

import com.example.studentmanagementfa22.entity.Account;
import com.example.studentmanagementfa22.entity.Mark;

import java.util.List;

public interface MarkService {
    List<Mark> getMarksBySubject (Account account, int subjectId);

    Mark editStudentMark(int markID, Mark editMark);
}
