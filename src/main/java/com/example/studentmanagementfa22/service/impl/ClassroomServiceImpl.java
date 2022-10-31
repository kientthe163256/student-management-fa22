package com.example.studentmanagementfa22.service.impl;

import com.example.studentmanagementfa22.dto.ClassroomDTO;
import com.example.studentmanagementfa22.entity.*;
import com.example.studentmanagementfa22.exception.customExceptions.ActionNotAllowedException;
import com.example.studentmanagementfa22.exception.customExceptions.ElementAlreadyExistException;
import com.example.studentmanagementfa22.repository.ClassroomRepository;
import com.example.studentmanagementfa22.repository.StudentRepository;
import com.example.studentmanagementfa22.repository.TeacherRepository;
import com.example.studentmanagementfa22.service.ClassroomService;
import com.example.studentmanagementfa22.service.SubjectService;
import com.example.studentmanagementfa22.service.TeacherService;
import com.example.studentmanagementfa22.utility.IGenericMapper;
import com.example.studentmanagementfa22.utility.SubjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class ClassroomServiceImpl implements ClassroomService {
    @Autowired
    private ClassroomRepository classroomRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private IGenericMapper<Classroom, ClassroomDTO> classroomMapper;

    @Autowired
    private SubjectService subjectService;

    @Autowired
    private TeacherService teacherService;

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private SubjectMapper subjectMapper;


    @Override
    @Transactional
    public void addNewClassroom(Classroom classroom) throws ElementAlreadyExistException {
        if (classroomExisted(classroom.getClassroomName())) {
            throw new ElementAlreadyExistException("There is already a class with given name!");
        }
        if (classroom.getClassType().equals(ClassType.SESSION)) {
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
                .map(classroom -> mapToClassroomDTO(classroom))
                .collect(Collectors.toList());
    }

    private static final List<String> CRITERIA =  Arrays.asList("classroom_name", "create_date", "id");
    @Override
    public List<ClassroomDTO> getAllTeachingClassrooms(int accountID, int pageNumber, int pageSize, String sort) {
        Optional<Teacher> teacher = teacherRepository.findTeacherByAccountId(accountID);
        if (teacher.isEmpty()){
            throw new NoSuchElementException("teacher not found");
        }
        //handle invalid format
        if (!Pattern.matches(".+,[A-Za-z]+", sort)){
            throw new IllegalArgumentException("Sort must be in format 'criteria,direction'. Ex: classroom_name,ASC");
        }
        //handle invalid sort criteria
        String criteria = sort.split(",")[0].trim();
        if (!CRITERIA.contains(criteria)){
            throw new IllegalArgumentException("Sort criteria must be classroom_name,  id, create_date!");
        }
        String rawdirection = sort.split(",")[1].trim().toUpperCase();
        Sort.Direction direction = Sort.Direction.fromString(rawdirection);
        Sort sortObject =  Sort.by(direction, criteria);
        PageRequest pageRequest = PageRequest.of(pageNumber - 1, pageSize, sortObject);

        Page<Classroom> classrooms = classroomRepository.findClassroomsByTeacherId(teacher.get().getId(), pageRequest);
//        List<ClassroomDTO> classroomDTOList = classrooms.stream()
//                .map(classroom -> mapToClassroomDTO(classroom))
//                .collect(Collectors.toList());
        List<ClassroomDTO> classroomDTOList = classrooms.stream()
                .map(classroom -> {
                    ClassroomDTO classroomDTO = mapToClassroomDTO(classroom);
                    classroomDTO.setCreate_date(classroom.getCreateDate());
                    return  classroomDTO;
                })
                .collect(Collectors.toList());
        return classroomDTOList;
    }

    @Override
    public Page<ClassroomDTO> getAllAvailClassroom(int pageNumber, int subjectID) {
        PageRequest pageRequest = PageRequest.of(pageNumber - 1, 5);
        Page<Classroom> classroomPage = classroomRepository.findAllAvailClassroom(pageRequest, subjectID);
        return classroomPage.map(classroom -> mapToClassroomDTO(classroom));
    }

    @Override
    public Page<ClassroomDTO> getAllRegisteredClass(int pageNumber, int studentId) {
        PageRequest pageRequest = PageRequest.of(pageNumber - 1, 5);
        Page<Classroom> classroomPage = classroomRepository.findAllRegisteredClass(pageRequest, studentId);
        return classroomPage.map(classroom -> mapToClassroomDTO(classroom));
    }

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void registerClassroom(int classId, int accountId) throws Exception {
        Optional<Classroom> optionalClassroom = classroomRepository.findById(classId);
        if (optionalClassroom.isEmpty()) {
            throw new NoSuchElementException("Class not found");
        }
        Classroom classroom = optionalClassroom.get();
        Optional<Student> student = studentRepository.findStudentByAccountId(accountId);
        if (student.isEmpty()) {
            throw new Exception("User is not found");
        }
        if (classroomRepository.numOfSubjectClassByStudent(classroom.getSubjectId(), student.get().getId()) == 0) {
            classroomRepository.registerClassroom(student.get().getId(), classId);
            classroomRepository.updateNoStudentOfClass(classId);
        } else {
            throw new Exception("You have already registered for this subject");
        }

    }

    @Override
    public ClassroomDTO mapToClassroomDTO(Classroom classroom) {
        ClassroomDTO classroomDTO = classroomMapper.mapToDTO(classroom);
        if (classroom.getSubjectId() != null) {
            classroomDTO.setSubject(subjectMapper.mapToDTO(subjectService.getById(classroom.getSubjectId())));
        }
        if (classroom.getTeacherId() != null) {
            classroomDTO.setTeacher(teacherService.getTeacherDTOById(classroom.getTeacherId()));
        }
        return classroomDTO;
    }

    @Override
    public Integer assignClassroom(Integer teacherId, Integer classId) {
        //try to find teacher and classroom if not exist throw NoSuchElement
        Teacher teacher = teacherService.getById(teacherId);
        Classroom classroom = getById(classId);
        return classroomRepository.assignClassroom(teacherId, classId);
    }

    @Override
    public Classroom getById(Integer classId) {
        Optional<Classroom> optionalClassroom = classroomRepository.findById(classId);
        if (optionalClassroom.isEmpty()){
            throw new NoSuchElementException("Can not find classroom with id = " + classId);
        }
        return optionalClassroom.get();
    }

    @Override
    public List<Classroom> getBySubjectId(Integer subjectId) {
        List<Classroom> classrooms = classroomRepository.findBySubjectId(subjectId);
        return classrooms;
    }

    @Override
    public ClassroomDTO updateClassroom(String newClassName, Integer classId) {
        Classroom currentClassroom = getById(classId);
        currentClassroom.setClassroomName(newClassName);
        Classroom savedClassroom = classroomRepository.save(currentClassroom);
        return classroomMapper.mapToDTO(savedClassroom);
    }

    @Override
    @Transactional
    public void deleteClassroom(Integer classId) {
        Classroom classroom = getById(classId);
        if (classroom.getCurrentNoStudent() > 0){
            throw new ActionNotAllowedException("Can not delete because there is still student in classroom");
        }
        classroomRepository.deleteClassroom(classId);
    }

    @Override
    public Page<ClassroomDTO> getAllClassroomsPaging(int pageNumber) {
        PageRequest pageRequest = PageRequest.of(pageNumber - 1, 5);
        Page<Classroom> classroomPage = classroomRepository.findAll(pageRequest);
        return classroomPage.map(classroom -> mapToClassroomDTO(classroom));
    }


}
