package com.example.studentmanagementfa22.service;

import com.example.studentmanagementfa22.dto.ClassroomDTO;
import com.example.studentmanagementfa22.entity.Classroom;
import com.example.studentmanagementfa22.entity.Pagination;
import com.example.studentmanagementfa22.exception.customExceptions.ElementAlreadyExistException;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ClassroomService {
    ClassroomDTO addNewClassroom(Classroom classroom);

    Pagination<ClassroomDTO> getAllTeachingClassrooms(int accountID, int pageNumber, int pageSize, String sort);

    Page<ClassroomDTO> getAllAvailClassroom (int pageNumber, int subjectId);

    Pagination<ClassroomDTO> getAllClassroomsPaging(int pageNumber, int pageSize, String sort);

    Page<ClassroomDTO> getAllRegisteredClass(int pageNumber, int studentId);

    void registerClassroom(int classId, int accountId) ;

    ClassroomDTO assignClassroom(Integer teacherId, Integer classId);

    Classroom getById(Integer classId);

    ClassroomDTO updateClassroom(Classroom classroom, Integer classId);

    void deleteClassroom(Integer classId);

    ClassroomDTO getClassDTOById(Integer classId);
}
