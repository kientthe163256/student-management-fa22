package com.example.studentmanagementfa22.service.impl;

import com.example.studentmanagementfa22.dto.AccountDTO;
import com.example.studentmanagementfa22.dto.TeacherDTO;
import com.example.studentmanagementfa22.entity.Account;
import com.example.studentmanagementfa22.entity.Teacher;
import com.example.studentmanagementfa22.repository.TeacherRepository;
import com.example.studentmanagementfa22.service.AccountService;
import com.example.studentmanagementfa22.service.TeacherService;
import com.example.studentmanagementfa22.utility.IGenericMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.Tuple;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.Metamodel;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TeacherServiceImpl implements TeacherService {
    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private IGenericMapper<Teacher, TeacherDTO> mapper;

    @Autowired
    private AccountService accountService;

    @Autowired
    private EntityManager em;

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
    public Teacher findById(int id) {
        Optional<Teacher> optionalTeacher = teacherRepository.findById(id);
        if (optionalTeacher.isEmpty()) {
            throw new NoSuchElementException("Teacher not found");
        }
        Teacher teacher = optionalTeacher.get();
        return teacher;
    }

    @Override
    public List<TeacherDTO> findAllTeacherPaging(int pageNumber, int pageSize, String sortCriteria, String direction) {
        PageRequest pageRequest = PageRequest.of(pageNumber - 1, pageSize);
        Page<Teacher> teacherPage = teacherRepository.findAll(pageRequest);
        // map to dto
        Page<TeacherDTO> teacherDTOPage = teacherPage.map(teacher -> {
            TeacherDTO teacherDTO = mapper.mapToDTO(teacher);
//            teacherDTO.setAccount(accountService.getAccountDTOById(teacher.getAccountId()));
            return teacherDTO;
        });

        List<TeacherDTO> teacherDTOList;
        switch (sortCriteria) {
            case "firstName":
                teacherDTOList = teacherDTOPage.stream().sorted(Comparator.comparing(o -> o.getAccount().getFirstName())).collect(Collectors.toList());
                break;
            case "lastName":
                teacherDTOList = teacherDTOPage.stream().sorted(Comparator.comparing(o -> o.getAccount().getLastName())).collect(Collectors.toList());
                break;
            case "dob":
                teacherDTOList = teacherDTOPage.stream().sorted(Comparator.comparing(o -> o.getAccount().getDob())).collect(Collectors.toList());
                break;
            case "username":
                teacherDTOList = teacherDTOPage.stream().sorted(Comparator.comparing(o -> o.getAccount().getUsername())).collect(Collectors.toList());
                break;
            case "id":
                teacherDTOList = teacherDTOPage.stream().sorted(Comparator.comparing(o -> o.getId())).collect(Collectors.toList());
                break;
            default:
                throw new IllegalArgumentException("Sort criteria must be firstName, lastName, id, dob or username!");
        }
        //sort with direction
        try {
            Sort.Direction sortDirection = Sort.Direction.fromString(direction);
            if (sortDirection.equals(Sort.Direction.DESC)) {
                Collections.reverse(teacherDTOList);
            }
        } catch (IllegalArgumentException illegalArgumentException) {
            throw new IllegalArgumentException(illegalArgumentException.getMessage());
        }
        return teacherDTOList;
    }

    @Override
    public List<TeacherDTO> findTeacherWithCriteria(int pageNumber, int pageSize, String sort) {
        String criteria = sort.split(",")[0];
        String direction = sort.split(",")[1].toUpperCase();
        Sort sortObject = criteria.equals("id")?Sort.by("id"):Sort.by("account."+criteria);
        sortObject = direction.equals("ASC")?sortObject.ascending():sortObject.descending();

        PageRequest pageRequest = PageRequest.of(pageNumber - 1, pageSize, sortObject);
        Page<Teacher> teacherPage = teacherRepository.findAll(pageRequest);
        List<TeacherDTO> teacherDTOList = teacherPage.stream()
                .map(t -> mapper.mapToDTO(t))
                .collect(Collectors.toList());
        return teacherDTOList;
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
