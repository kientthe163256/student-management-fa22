package com.example.studentmanagementfa22.service.impl;

import com.example.studentmanagementfa22.dto.ClassroomDTO;
import com.example.studentmanagementfa22.entity.ClassType;
import com.example.studentmanagementfa22.entity.Classroom;
import com.example.studentmanagementfa22.entity.Student;
import com.example.studentmanagementfa22.exception.ElementAlreadyExistException;
import com.example.studentmanagementfa22.repository.ClassroomRepository;
import com.example.studentmanagementfa22.repository.StudentRepository;
import com.example.studentmanagementfa22.service.AccountService;
import com.example.studentmanagementfa22.service.ClassroomService;
import com.example.studentmanagementfa22.service.TeacherService;
import com.example.studentmanagementfa22.utility.ClassroomMapper;
import com.example.studentmanagementfa22.utility.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ClassroomServiceImpl implements ClassroomService {
    @Autowired
    private ClassroomRepository classroomRepository;

    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private Mapper mapper;

    @Autowired
    private AccountService accountService;

    @Autowired
    private TeacherService teacherService;

    @Autowired
    private ClassroomMapper classroomMapper;

    @Override
    public void addNewClassroom(Classroom classroom) throws ElementAlreadyExistException{
        if (classroomExisted(classroom.getClassroomName())){
            throw new ElementAlreadyExistException("There is already a class with given name!");
        }
        if (classroom.getClassType().equals(ClassType.SESSION)){
            classroomRepository.addSessionClassroom(
                    classroom.getClassroomName(),
                    classroom.getNoStudent(),
                    ClassType.SESSION.name()
            );
        } else if (classroom.getClassType().equals(ClassType.SUBJECT)) {
            classroomRepository.addSubjectClassroom(
                    classroom.getClassroomName(),
                    classroom.getNoStudent(),
                    ClassType.SUBJECT.name(),
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

    @Override
    public Page<ClassroomDTO> getAllRegisteredClass (int pageNumber, int studentId) {
        PageRequest pageRequest = PageRequest.of(pageNumber-1, 5);
        Page<Classroom> classroomPage = classroomRepository.findAllRegisteredClass(pageRequest, studentId);
        Page<ClassroomDTO> classroomDTOPage = classroomPage.map(classroom -> mapper.mapClassroom(classroom));
        return classroomDTOPage;
    }

    @Override
    public void registerClassroom(int classId, int accountId) throws Exception {
        Classroom classroom =  classroomRepository.findById(classId);
        Optional<Student> student = studentRepository.findStudentByAccountId(accountId);
        if (student.isEmpty()) {
            throw new Exception("User is not found");
        }
        if (classroomRepository.numOfSubjectClassbyStudent(classroom.getSubjectId(), student.get().getId() ) == 0 ) {
            classroomRepository.registerClassroom(student.get().getId(), classId);
            classroomRepository.updateNoStudentOfClass(classId);
        }
        else {
            throw new Exception ("You have already registered for this subject");
        }

    }

    @Override
    public Page<ClassroomDTO> getAllClassroomsPaging(int pageNumber) {
        PageRequest pageRequest = PageRequest.of(pageNumber-1, 5);
        Page<Classroom> classroomPage = classroomRepository.findAll(pageRequest);
        return classroomPage.map(classroom -> classroomMapper.toDTO(classroom));
    }



}
