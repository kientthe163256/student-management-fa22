package com.example.studentmanagementfa22.service.impl;

import com.example.studentmanagementfa22.config.CustomLogoutSuccessHandler;
import com.example.studentmanagementfa22.dto.TeacherDTO;
import com.example.studentmanagementfa22.entity.Account;
import com.example.studentmanagementfa22.entity.Teacher;
import com.example.studentmanagementfa22.repository.TeacherRepository;
import com.example.studentmanagementfa22.service.TeacherService;
import com.example.studentmanagementfa22.utility.IGenericMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class TeacherServiceImpl implements TeacherService {
    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private IGenericMapper<Teacher, TeacherDTO> mapper;

    private static final Logger logger
            = LoggerFactory.getLogger(TeacherService.class);

    @Override
    public void addTeacherWithNewAccount(Account account) {
        Date today = new Date();
        Teacher teacher = Teacher.builder()
                .accountId(account.getId())
                .createDate(today)
                .modifyDate(today)
                .build();
        teacherRepository.save(teacher);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        logger.info(authentication.getName() + " added new teacher account: " + teacher);
    }

    @Override
    public List<Teacher> findAllTeachers() {
        return teacherRepository.findAll();
    }

    @Override
    public Teacher findById(int id) {
        Optional<Teacher> optionalTeacher = teacherRepository.findById(id);
        if (optionalTeacher.isEmpty()){
            throw new NoSuchElementException("Teacher not found");
        }
        Teacher teacher = optionalTeacher.get();
        return teacher;
    }

    @Override
    public Page<TeacherDTO> findAllTeacherPaging(int pageNumber, int pageSize, String sortCriteria, String direction) {
        PageRequest pageRequest = PageRequest.of(pageNumber-1, pageSize, Sort.by(Sort.Direction.fromString(direction), sortCriteria));
        Page<Teacher> teacherPage = teacherRepository.findAll(pageRequest);
        return teacherPage.map(teacher -> mapper.toDTO(teacher));
    }

    @Override
    public TeacherDTO getTeacherDTOById(int teacherId) {
        Optional<Teacher> optionalTeacher = teacherRepository.findById(teacherId);
        if (optionalTeacher.isPresent()){
            Teacher teacher = optionalTeacher.get();
            TeacherDTO teacherDTO = mapper.toDTO(teacher);
            return teacherDTO;
        }
        return null;
    }

    @Override
    public void deleteTeacher(Integer teacherId) {
        teacherRepository.deleteTeacher(teacherId);
    }
}
