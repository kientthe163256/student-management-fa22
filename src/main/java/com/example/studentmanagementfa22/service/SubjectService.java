package com.example.studentmanagementfa22.service;

import com.example.studentmanagementfa22.dto.MarkTypeDTO;
import com.example.studentmanagementfa22.dto.SubjectDTO;
import com.example.studentmanagementfa22.entity.Subject;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;


public interface SubjectService {
    Page<Subject> getAllSubject(int pageNumber);

    List<SubjectDTO> getSubjectDTOList(int pageNumber);

    Subject getById(int id);

    Subject getByName(String subjectName);

    SubjectDTO getSubjectDTOByID(Integer id);

    SubjectDTO addNewSubject(SubjectDTO subjectDTO);

    void deleteSubject(Integer subjectId);

    SubjectDTO updateSubject(SubjectDTO editSubject);

    Map<MarkTypeDTO, Integer> addMarkTypeToSubject(int subjectId, int markTypeId, int noMarks);

}
