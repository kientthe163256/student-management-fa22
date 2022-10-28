package com.example.studentmanagementfa22.service;

import com.example.studentmanagementfa22.dto.SubjectDTO;
import com.example.studentmanagementfa22.entity.Subject;
import org.springframework.data.domain.Page;

import java.util.List;


public interface SubjectService {
    Page<Subject> getAllSubject(int pageNumber);

    List<SubjectDTO> getSubjectDTOList(int pageNumber);

    List<Subject> getSubjectList();

    Subject getById(int id);

    Subject getByName(String subjectName);

    SubjectDTO getSubjectDTOByID(Integer id);

    void addNewSubject(SubjectDTO subjectDTO);

    void deleteSubject(Integer subjectId);

    SubjectDTO updateSubject(SubjectDTO editSubject);
}
