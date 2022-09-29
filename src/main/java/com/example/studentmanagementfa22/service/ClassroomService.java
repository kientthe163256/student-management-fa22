package com.example.studentmanagementfa22.service;

import com.example.studentmanagementfa22.entity.Classroom;
import com.example.studentmanagementfa22.exception.ElementAlreadyExistException;
import org.springframework.data.domain.Page;

public interface ClassroomService {
    void addNewClassroom(Classroom classroom) throws ElementAlreadyExistException;

    boolean classroomExisted(String classroomName);

    Page<Classroom> getAllAvailClassroom (int pageNumber, int subjectId);
}
