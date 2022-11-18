package com.example.studentmanagementfa22.service.impl;

import com.example.studentmanagementfa22.dto.ClassroomDTO;
import com.example.studentmanagementfa22.entity.*;
import com.example.studentmanagementfa22.exception.customExceptions.ActionNotAllowedException;
import com.example.studentmanagementfa22.exception.customExceptions.ElementAlreadyExistException;
import com.example.studentmanagementfa22.exception.customExceptions.InvalidSortFieldException;
import com.example.studentmanagementfa22.repository.ClassroomRepository;
import com.example.studentmanagementfa22.repository.StudentRepository;
import com.example.studentmanagementfa22.repository.TeacherRepository;
import com.example.studentmanagementfa22.service.*;
import com.example.studentmanagementfa22.utility.ClassroomMapper;
import com.example.studentmanagementfa22.utility.PagingHelper;
import com.example.studentmanagementfa22.utility.SubjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class ClassroomServiceImpl implements ClassroomService {
    @Autowired
    private ClassroomRepository classroomRepository;

    @Autowired
    private ClassroomMapper mapper;

    @Autowired
    private SubjectService subjectService;

    @Autowired
    private TeacherService teacherService;

    @Autowired
    private StudentService studentService;

    @Autowired
    private MarkService markService;

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
                    classroom.getSubject().getId());
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
                .map(classroom -> mapper.mapToDTO(classroom))
                .collect(Collectors.toList());
    }

    @Override
    public Pagination<ClassroomDTO> getAllTeachingClassrooms(int accountID,int pageNumber, int pageSize, String sort) {
        Optional<Teacher> teacher = teacherRepository.findTeacherByAccountId(accountID);
        if (teacher.isEmpty()){
            throw new NoSuchElementException("teacher not found");
        }
        Map<String, Object> validatedSort = PagingHelper.getCriteriaAndDirection(sort);
        String criteria = (String) validatedSort.get("criteria");
        Sort.Direction direction = (Sort.Direction) validatedSort.get("direction");

        Sort sortObject;
        //Check if criteria is a ClassroomDTO attribute
        if (PagingHelper.objectContainsField(Classroom.class, criteria)) {
            sortObject = Sort.by(direction, PagingHelper.objectFieldtoColumn(Classroom.class, criteria));
        }
        else
            throw new InvalidSortFieldException(Classroom.class);
        PageRequest pageRequest = PageRequest.of(pageNumber - 1, pageSize, sortObject);

        Page<Classroom> classrooms = classroomRepository.findClassroomsByTeacherId(teacher.get().getId(), pageRequest);
        Map<String, Integer> fields = PagingHelper.getPaginationFields(classrooms, pageNumber);
        List<ClassroomDTO> classroomDTOList = classrooms.stream()
                .map(classroom -> {
                    ClassroomDTO classroomDTO = mapper.mapToDTO(classroom);
                    classroomDTO.setCreateDate(classroom.getCreateDate());
                    return  classroomDTO;
                })
                .collect(Collectors.toList());
        return new Pagination<>(classroomDTOList, fields.get("first"), fields.get("previous"), fields.get("next"), fields.get("last"), fields.get("total"));
    }

    @Override
    public Page<ClassroomDTO> getAllAvailClassroom(int pageNumber, int subjectID) {
        PageRequest pageRequest = PageRequest.of(pageNumber - 1, 5);
        Page<Classroom> classroomPage = classroomRepository.findAllAvailClassroom(pageRequest, subjectID);
        return classroomPage.map(classroom -> mapper.mapToDTO(classroom));
    }

    @Override
    public List<ClassroomDTO> getAllRegisteredClass(int pageNumber, int studentId) {
        PageRequest pageRequest = PageRequest.of(pageNumber - 1, 5);
        Page<Classroom> classroomPage = classroomRepository.findAllRegisteredClass(pageRequest, studentId);
        if (classroomPage.getTotalPages() < pageNumber) {
            throw new IllegalArgumentException ("The last page is "+classroomPage.getTotalPages());
        }
        if (pageNumber <= 0) {
            throw new IllegalArgumentException("Invalid page number");
        }
        Page<ClassroomDTO> classroomDTOPage = classroomPage.map(classroom -> mapper.mapToDTO(classroom));
        List<ClassroomDTO> classroomDTOList = classroomDTOPage.getContent();
        return classroomDTOList ;
    }

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void registerClassroom(int classId, int accountId) {
        Optional<Classroom> optionalClassroom = classroomRepository.findById(classId);
        if (optionalClassroom.isEmpty()) {
            throw new NoSuchElementException("Class not found with id " + classId);
        }
        Classroom classroom = optionalClassroom.get();

        Student student = studentService.getStudentByAccountId(accountId);
        if (classroomRepository.numOfSubjectClassByStudent(classroom.getSubject().getId(), student.getId()) == 0) {
            classroomRepository.registerClassroom(student.getId(), classId);
            markService.addStudentSubjectMark(student.getId(), classroom.getSubject().getId());
            classroomRepository.updateNoStudentOfClass(classId);
        } else {
            throw new IllegalArgumentException("You have already registered for this subject");
        }
    }

    @Override
    public ClassroomDTO assignClassroom(Integer teacherId, Integer classId) {
        //try to find teacher and classroom if not exist throw NoSuchElement
        Teacher teacher = teacherService.getById(teacherId);
        Classroom classroom = getById(classId);

        classroom.setTeacher(teacher);
        classroom.setModifyDate(new Date());
        Classroom savedClassroom = classroomRepository.save(classroom);
        return mapper.mapToDTO(savedClassroom);
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
    public ClassroomDTO updateClassroom(Classroom classroom, Integer classId) {
        Classroom currentClassroom = getById(classId);
        currentClassroom.setClassroomName(classroom.getClassroomName());
        currentClassroom.setModifyDate(new Date());
        Classroom savedClassroom = classroomRepository.save(currentClassroom);
        return mapper.mapToDTO(savedClassroom);
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
    public ClassroomDTO getClassDTOById(Integer classId) {
        Classroom classroom = getById(classId);
        return mapper.mapToDTO(classroom);
    }

    @Override
    public Pagination<ClassroomDTO> getAllClassroomsPaging(int pageNumber, int pageSize, String sort){
        //use PagingHelper to validate raw sort input
        Map<String, Object> validatedSort = PagingHelper.getCriteriaAndDirection(sort);
        String criteria = (String) validatedSort.get("criteria");
        Sort.Direction direction = (Sort.Direction) validatedSort.get("direction");

        Sort sortObject;
        //Check if criteria is a ClassroomDTO attribute
        if (PagingHelper.objectContainsField(ClassroomDTO.class, criteria))
            sortObject = Sort.by(direction, criteria);
         else
            throw new InvalidSortFieldException(ClassroomDTO.class);

        PageRequest pageRequest = PageRequest.of(pageNumber - 1, pageSize, sortObject);
        Page<Classroom> classroomPage = classroomRepository.findAll(pageRequest);
        List<ClassroomDTO> classroomDTOList = classroomPage.stream()
                .map(t -> mapper.mapToDTO(t))
                .collect(Collectors.toList());

        //get first, previous, next, last, total
        Map<String, Integer> fields = PagingHelper.getPaginationFields(classroomPage, pageNumber);
        return new Pagination<>(classroomDTOList, fields.get("first"), fields.get("previous"), fields.get("next"), fields.get("last"), fields.get("total"));
    }


}
