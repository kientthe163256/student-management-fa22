package com.example.studentmanagementfa22.service.impl;

import com.example.studentmanagementfa22.dto.ClassroomDTO;
import com.example.studentmanagementfa22.entity.Classroom;
import com.example.studentmanagementfa22.exception.ElementAlreadyExistException;
import com.example.studentmanagementfa22.repository.ClassroomRepository;
import com.example.studentmanagementfa22.service.AccountService;
import com.example.studentmanagementfa22.service.ClassroomService;
import com.example.studentmanagementfa22.service.TeacherService;
import com.example.studentmanagementfa22.utility.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClassroomServiceImpl implements ClassroomService {
    @Autowired
    private ClassroomRepository classroomRepository;

    @Autowired
    private Mapper mapper;


    @Override
    public void addNewClassroom(Classroom classroom) throws ElementAlreadyExistException{
        if (classroomExisted(classroom.getClassroomName())){
            throw new ElementAlreadyExistException("There is already a class with given name!");
        }
        if (classroom.getClassType().equals("SES")){
            classroomRepository.addSessionClassroom(
                    classroom.getClassroomName(),
                    classroom.getNoStudent(),
                    classroom.getClassType()
            );
        } else if (classroom.getClassType().equals("SUB")) {
            classroomRepository.addSubjectClassroom(
                    classroom.getClassroomName(),
                    classroom.getNoStudent(),
                    classroom.getClassType(),
                    classroom.getSubjectId());
        }
    }

    @Override
    public boolean classroomExisted(String classroomName) {
        Classroom classroom = classroomRepository.findByClassroomName(classroomName);
        return classroom != null;
    }

    @Override
    public List<ClassroomDTO> getAllClassrooms() {
        List<Classroom> classrooms = classroomRepository.findAll();
        return classrooms
                .stream()
                .map(classroom -> mapper.mapClassroom(classroom))
                .collect(Collectors.toList());
    }

    @Override
    public Page<ClassroomDTO> getAllAvailClassroom (int pageNumber, int subjectID) {
        PageRequest pageRequest = PageRequest.of(pageNumber-1, 5);
        Page<Classroom> classroomPage = classroomRepository.findAllAvailClassroom(pageRequest, subjectID);
        Page<ClassroomDTO> classroomDTOPage = classroomPage.map(classroom -> mapper.mapClassroom(classroom));
        return classroomDTOPage;
    }



}
