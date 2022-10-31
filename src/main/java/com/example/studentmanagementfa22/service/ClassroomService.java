package com.example.studentmanagementfa22.service;

import com.example.studentmanagementfa22.dto.ClassroomDTO;
import com.example.studentmanagementfa22.entity.Classroom;
import com.example.studentmanagementfa22.exception.customExceptions.ElementAlreadyExistException;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ClassroomService {
    void addNewClassroom(Classroom classroom) throws ElementAlreadyExistException;

    boolean classroomExisted(String classroomName);

    List<ClassroomDTO> getAllClassrooms();

    List<ClassroomDTO> getAllTeachingClassrooms(int accountID, int pageNumber, int pageSize, String sort);

    Page<ClassroomDTO> getAllAvailClassroom (int pageNumber, int subjectId);

    Page<ClassroomDTO> getAllClassroomsPaging(int pageNumber);

    Page<ClassroomDTO> getAllRegisteredClass(int pageNumber, int studentId);

    void registerClassroom(int classId, int accountId) throws Exception;

    ClassroomDTO mapToClassroomDTO(Classroom classroom);

    Integer assignClassroom(Integer teacherId, Integer classId);

    Classroom getById(Integer classId);

    List<Classroom> getBySubjectId(Integer subjectId);

    ClassroomDTO updateClassroom(String newClassName, Integer classId);

    void deleteClassroom(Integer classId);
}
