package com.example.studentmanagementfa22.service.impl;

import com.example.studentmanagementfa22.dto.StudentDTO;
import com.example.studentmanagementfa22.entity.Account;
import com.example.studentmanagementfa22.entity.Student;
import com.example.studentmanagementfa22.entity.Teacher;
import com.example.studentmanagementfa22.repository.AccountRepository;
import com.example.studentmanagementfa22.repository.StudentRepository;
import com.example.studentmanagementfa22.repository.TeacherRepository;
import com.example.studentmanagementfa22.service.AccountService;
import com.example.studentmanagementfa22.service.StudentService;
import com.example.studentmanagementfa22.utility.StudentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


@Service
public class StudentServiceImpl implements StudentService {
    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AccountService accountService;

    @Autowired
    private StudentMapper mapper;

    @Override
    public StudentDTO addStudentWithNewAccount(Account account) {
        Date today = new Date();
        //Add a new student with registered account
        Student student = Student.builder()
                .academicSession(Integer.parseInt(account.getUsername().substring(2,4)))
                .account(account)
                .createDate(today)
                .modifyDate(today)
                .build();
        Student savedStudent = studentRepository.save(student);
        return mapper.mapToDTO(savedStudent);
    }
    private static final List<String> CRITERIA =  Arrays.asList("first_name", "last_name", "account_id", "dob", "username", "academicSession");
    @Override
    public List<StudentDTO> getStudentsByClassroomandTeacher(Integer classID, Integer accountID, int pageNumber, int pageSize, String sort) {
        Optional<Teacher> optionalTeacher = teacherRepository.findTeacherByAccountId(accountID);
        if (optionalTeacher.isEmpty()) {
            throw  new NoSuchElementException("Teacher not found");
        }
        //handle invalid format
        if (!Pattern.matches(".+,[A-Za-z]+", sort)){
            throw new IllegalArgumentException("Sort must be in format 'criteria,direction'. Ex: first_name,ASC");
        }
        //handle invalid sort criteria
        String criteria = sort.split(",")[0].trim();
        if (!CRITERIA.contains(criteria)){
            throw new IllegalArgumentException("Sort criteria must be first_name, last_name, account_id, dob or username!");
        }
        String rawdirection = sort.split(",")[1].trim().toUpperCase();
        Sort.Direction direction = Sort.Direction.fromString(rawdirection);
        Sort sortObject = criteria.equals("account_id")
                ? Sort.by(direction, "id")
                : Sort.by(direction, criteria);

        PageRequest pageRequest = PageRequest.of(pageNumber - 1, pageSize, sortObject);
        Page<Account> accounts = accountRepository.findStudentAccountsByClassroomandTeacher(classID, optionalTeacher.get().getId(), pageRequest);
        List<StudentDTO> studentDTOList = accounts.stream().map(account -> {
            Optional<Student> optionalStudent = studentRepository.findStudentByAccountId(account.getId());
            if (optionalStudent.isEmpty()) {
                throw new NoSuchElementException("Student not found");
            }
            StudentDTO studentDTO = mapper.mapToDTO(optionalStudent.get());
           return  studentDTO;
        }).collect(Collectors.toList());
        return studentDTOList;
    }

    @Override
    public void checkStudentJoinedClass(Integer studentId, Integer classId) {
        if(studentRepository.getStudentClassroom(studentId,classId) == 0) {
            throw new IllegalArgumentException("Student does not join this class");
//            throw new NoSuchElementException(TranslationCode.STUDENT);
        }
    }

    @Override
    public void checkStudentTeacher(Integer studentId, Integer teacherId) {
        if(studentRepository.getStudentbyTeacher(studentId,teacherId).isEmpty()) {
//            throw new NoSuchElementException(TranslationCode.STUDENT);
           throw  new IllegalArgumentException("You are not the teacher of this student");
        }
    }

    @Override
    public Student getStudentByAccountId(int accountId) {
        Optional<Student> student = studentRepository.findStudentByAccountId(accountId);
        if (student.isEmpty()) {
            throw new NoSuchElementException("Student does not exists");
        }
        return student.get();
    }

    @Override
    public StudentDTO getStudentDTOByAccountId(Integer accountId) {
        Student student = getStudentByAccountId(accountId);
        Account account = accountService.getById(accountId);
        String username = account.getUsername();
        StudentDTO studentDTO = mapper.mapToDTO(student);
        studentDTO.setUsername(username);
        studentDTO.setPassword(account.getPassword());
        studentDTO.setRoleId(account.getRoleId());
        studentDTO.setFirstName(account.getFirstName());
        studentDTO.setLastName(account.getLastName());
        return studentDTO;
    }
}
