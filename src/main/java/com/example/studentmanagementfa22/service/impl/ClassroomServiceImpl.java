package com.example.studentmanagementfa22.service.impl;

import com.example.studentmanagementfa22.entity.Classroom;
import com.example.studentmanagementfa22.exception.ElementAlreadyExistException;
import com.example.studentmanagementfa22.repository.ClassroomRepository;
import com.example.studentmanagementfa22.service.ClassroomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClassroomServiceImpl implements ClassroomService {
    @Autowired
    private ClassroomRepository classroomRepository;

    @Override
    public void addNewClassroom(Classroom classroom) throws ElementAlreadyExistException{
        if (classroomExisted(classroom.getClassroomName())){
            throw new ElementAlreadyExistException("There is already a class with given name!");
        }
        classroomRepository.save(classroom);
    }

    @Override
    public boolean classroomExisted(String classroomName) {
        Classroom classroom = classroomRepository.findByClassroomName(classroomName);
        return classroom != null;
    }


}
