package com.example.studentmanagementfa22.service.impl;

import com.example.studentmanagementfa22.dto.AccountDTO;
import com.example.studentmanagementfa22.dto.ClassroomDTO;
import com.example.studentmanagementfa22.dto.TeacherDTO;
import com.example.studentmanagementfa22.entity.*;
import com.example.studentmanagementfa22.exception.customExceptions.ActionNotAllowedException;
import com.example.studentmanagementfa22.exception.customExceptions.InvalidSortFieldException;
import com.example.studentmanagementfa22.repository.ClassroomRepository;
import com.example.studentmanagementfa22.repository.StudentRepository;
import com.example.studentmanagementfa22.repository.TeacherRepository;
import com.example.studentmanagementfa22.service.StudentService;
import com.example.studentmanagementfa22.service.TeacherService;
import com.example.studentmanagementfa22.utility.PagingHelper;
import com.example.studentmanagementfa22.utility.TeacherMapper;
import com.example.studentmanagementfa22.utility.TranslationCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class TeacherServiceImpl implements TeacherService {
    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private TeacherMapper mapper;

    @Autowired
    private StudentService studentService;

    @Autowired
    private ClassroomRepository classroomRepository;

    @Autowired
    private StudentRepository studentRepository;


    @Override
    public TeacherDTO addTeacherWithNewAccount(Account account) {
        Date today = new Date();
        Teacher teacher = Teacher.builder()
                .account(account)
                .createDate(today)
                .modifyDate(today)
                .build();
        Teacher savedTeacher = teacherRepository.save(teacher);
        return mapper.mapToDTO(savedTeacher);
    }

    @Override
    public Teacher getById(int id) {
        Optional<Teacher> optionalTeacher = teacherRepository.findById(id);
        if (optionalTeacher.isEmpty()) {
            throw new NoSuchElementException(TranslationCode.TEACHER);
        }
        return optionalTeacher.get();
    }

    @Override
    public Teacher getTeacherByAccountId(int accountId) {
        Optional<Teacher> optionalTeacher = teacherRepository.findTeacherByAccountId(accountId);
        if (optionalTeacher.isEmpty()) {
            throw new NoSuchElementException(TranslationCode.TEACHER);
        }
        return optionalTeacher.get();
    }

    @Override
    public Pagination<TeacherDTO> getAllTeacherPaging(int pageNumber, int pageSize, String sort) {
        //use PagingHelper to validate raw sort input
        Map<String, Object> validatedSort = PagingHelper.getCriteriaAndDirection(sort);
        String criteria = (String) validatedSort.get("criteria");
        Sort.Direction direction = (Sort.Direction) validatedSort.get("direction");

        Sort sortObject;
        //Check if criteria is a ClassroomDTO attribute
        if (PagingHelper.objectContainsField(TeacherDTO.class, criteria))
            sortObject = Sort.by(direction, criteria);
        else if (PagingHelper.objectContainsField(AccountDTO.class, criteria))
            sortObject = Sort.by(direction, "account." + criteria);
        else
            throw new InvalidSortFieldException(ClassroomDTO.class);

        PageRequest pageRequest = PageRequest.of(pageNumber - 1, pageSize, sortObject);
        Page<Teacher> teacherPage = teacherRepository.findAll(pageRequest);
        List<TeacherDTO> teacherDTOList = teacherPage.stream()
                .map(t -> mapper.mapToDTO(t))
                .collect(Collectors.toList());

        //get first, previous, next, last, total
        Map<String, Integer> fields = PagingHelper.getPaginationFields(teacherPage, pageNumber);
        return new Pagination<>(teacherDTOList, fields.get("first"), fields.get("previous"), fields.get("next"), fields.get("last"), fields.get("total"));
    }

    @Override
    public void checkTeacherAssignedClass(Integer loggInTeacherId, Integer classroomId) {
        Optional<Teacher> classroomTeacher = teacherRepository.findTeacherByClassroomId(classroomId);
        if(classroomTeacher.isEmpty()) {
            throw new NoSuchElementException("No teacher have been assigned to this classroom");
        }
        if(loggInTeacherId != classroomTeacher.get().getId()) {
            throw new ActionNotAllowedException("You are not the teacher of this classroom");
        }
    }

    @Override
    public void checkTeacherClassroomStudent(Integer teacherAccountId, Integer classId, Integer studentId) {
        Optional<Student> student = studentRepository.findById(studentId);
        if(student.isEmpty()) {
            throw new NoSuchElementException("Student ID not exists");
        }
        Teacher teacher = getTeacherByAccountId(teacherAccountId);

        studentService.checkStudentJoinedClass(student.get().getId(), classId);
        studentService.checkStudentTeacher(student.get().getId(), teacher.getId());
        checkTeacherAssignedClass(teacher.getId(),classId);
    }

    @Override
    public TeacherDTO getTeacherDTOById(int teacherId) {
        Teacher teacher = getById(teacherId);
        return mapper.mapToDTO(teacher);
    }

    @Override
    @Transactional
    public void deleteTeacher(Integer teacherId) {
        Teacher teacher = getById(teacherId);
        teacher.setDeleted(true);
        teacher.setDeleteDate(new Date());
        teacherRepository.save(teacher);
    }
}
