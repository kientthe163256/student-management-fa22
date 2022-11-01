package com.example.studentmanagementfa22.service.impl;

import com.example.studentmanagementfa22.dto.TeacherDTO;
import com.example.studentmanagementfa22.entity.Account;
import com.example.studentmanagementfa22.entity.Teacher;
import com.example.studentmanagementfa22.repository.StudentRepository;
import com.example.studentmanagementfa22.repository.TeacherRepository;
import com.example.studentmanagementfa22.service.TeacherService;
import com.example.studentmanagementfa22.utility.IGenericMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class TeacherServiceImpl implements TeacherService {
    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private IGenericMapper<Teacher, TeacherDTO> mapper;



    @Override
    public void addTeacherWithNewAccount(Account account) {
        Date today = new Date();
        Teacher teacher = Teacher.builder()
//                .accountId(account.getId())
                .createDate(today)
                .modifyDate(today)
                .build();
        teacherRepository.save(teacher);
    }

    @Override
    public List<Teacher> findAllTeachers() {
        return teacherRepository.findAll();
    }

    @Override
    public Teacher getById(int id) {
        Optional<Teacher> optionalTeacher = teacherRepository.findById(id);
        if (optionalTeacher.isEmpty()) {
            throw new NoSuchElementException("Can not find teacher with id = " + id);
        }
        Teacher teacher = optionalTeacher.get();
        return teacher;
    }

    private static final List<String> CRITERIA =  Arrays.asList("firstName", "lastName", "id", "dob", "username");
    @Override
    public List<TeacherDTO> getAllTeacherPaging(int pageNumber, int pageSize, String sort) {
        //handle invalid format
        if (!Pattern.matches("[A-Za-z]+,[A-Za-z]+", sort)){
            throw new IllegalArgumentException("Sort must be in format 'criteria,direction'. Ex: firstName,ASC");
        }
        //handle invalid sort criteria
        String criteria = sort.split(",")[0].trim();
        if (!CRITERIA.contains(criteria)){
            throw new IllegalArgumentException("Sort criteria must be firstName, lastName, id, dob or username!");
        }
        //split raw input and initialize Direction (throw InvalidFormatException)
        String rawDirection = sort.split(",")[1].trim().toUpperCase();
        Sort.Direction direction = Sort.Direction.fromString(rawDirection);
        //if criteria is account's field, add 'account.'
        Sort sortObject = criteria.equals("id")
                ? Sort.by(direction, criteria)
                : Sort.by(direction, "account."+criteria);

        PageRequest pageRequest = PageRequest.of(pageNumber - 1, pageSize, sortObject);
        Page<Teacher> teacherPage = teacherRepository.findAll(pageRequest);
        List<TeacherDTO> teacherDTOList = teacherPage.stream()
                .map(t -> mapper.mapToDTO(t))
                .collect(Collectors.toList());
        return teacherDTOList;
    }

    @Override
    public boolean checkTeacherAssignedClass(Integer loggInTeacherId, Integer classroomId) {
        Optional<Teacher> classroomTeacher = teacherRepository.findTeacherByClassroomId(classroomId);
        if(classroomTeacher.isEmpty()) {
            throw new NoSuchElementException("No teacher have been assigned to this classroom");
        }
        if(loggInTeacherId != classroomTeacher.get().getId()) {
            return false;
        }
        return true;
    }

    @Override
    public TeacherDTO getTeacherDTOById(int teacherId) {
        Optional<Teacher> optionalTeacher = teacherRepository.findById(teacherId);
        if (optionalTeacher.isPresent()) {
            Teacher teacher = optionalTeacher.get();
            TeacherDTO teacherDTO = mapper.mapToDTO(teacher);
//            teacherDTO.setAccount(accountService.getAccountDTOById(teacher.getAccountId()));
            return teacherDTO;
        }
        return null;
    }

    @Override
    public void deleteTeacher(Integer teacherId) {
        teacherRepository.deleteTeacher(teacherId);
    }
}
