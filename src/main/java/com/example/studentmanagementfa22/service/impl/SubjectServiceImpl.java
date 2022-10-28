package com.example.studentmanagementfa22.service.impl;

import com.example.studentmanagementfa22.dto.SubjectDTO;
import com.example.studentmanagementfa22.entity.Classroom;
import com.example.studentmanagementfa22.entity.Subject;
import com.example.studentmanagementfa22.exception.customExceptions.ElementAlreadyExistException;
import com.example.studentmanagementfa22.exception.customExceptions.InvalidInputException;
import com.example.studentmanagementfa22.exception.customExceptions.ActionNotAllowedException;
import com.example.studentmanagementfa22.repository.SubjectRepository;
import com.example.studentmanagementfa22.service.ClassroomService;
import com.example.studentmanagementfa22.service.SubjectService;
import com.example.studentmanagementfa22.utility.SubjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;


@Service
public class SubjectServiceImpl implements SubjectService {
    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private SubjectMapper mapper;

//    @Autowired
//    private ClassroomService classroomService;

    @Override
    public Page<Subject> getAllSubject(int pageNumber){
        PageRequest pageRequest = PageRequest.of(pageNumber-1, 5);
        Page<Subject> subjectPage = subjectRepository.findAll(pageRequest);
        return subjectPage;
    }

    @Override
    public List<SubjectDTO> getSubjectDTOList(int pageNumber) {
        PageRequest pageRequest = PageRequest.of(pageNumber-1, 5);
        Page<Subject> subjectPage = subjectRepository.findAll(pageRequest);
        List<SubjectDTO> subjectDTOList = subjectPage.stream().map(subject -> mapper.mapToDTO(subject)).toList();
        return subjectDTOList;
    }

    @Override
    public List<Subject> getSubjectList() {
        return subjectRepository.findAll();
    }

    @Override
    public Subject getById(int id) {
        Optional<Subject> optionalSubject = subjectRepository.findById(id);
        if (optionalSubject.isEmpty()){
            throw new NoSuchElementException("Can not find subject with id = " + id);
        }
        Subject subject = optionalSubject.get();
        return subject;
    }

    @Override
    public Subject getByName(String subjectName) {
        Subject subject = subjectRepository.findBySubjectName(subjectName);
        return subject;
    }

    @Override
    public SubjectDTO getSubjectDTOByID(Integer id) {
        Subject subject = getById(id);
        return mapper.mapToDTO(subject);
    }

    @Override
    public void addNewSubject(SubjectDTO subjectDTO) {
        Subject existedSubject = subjectRepository.findBySubjectName(subjectDTO.getSubjectName());
        if (existedSubject != null){
            throw new ElementAlreadyExistException("There is already a subject with given name");
        }
        Subject subject = mapper.mapToEntity(subjectDTO);
        Date today = new Date();
        subject.setCreateDate(today);
        subject.setModifyDate(today);
        subjectRepository.save(subject);
    }

    @Override
    public void deleteSubject(Integer subjectId) {
        Subject subject = getById(subjectId);
        if (subject.isDeleted()){
            throw new InvalidInputException("Subject already deleted");
        }
//        List<Classroom> classroomList = classroomService.getBySubjectId(subjectId);
//        if (!classroomList.isEmpty()){
//            throw new ActionNotAllowedException("Can not delete cause there is still a classroom with this subject");
//        }
        subjectRepository.deleteSubject(subjectId);
    }

    @Override
    public SubjectDTO updateSubject(SubjectDTO editSubject) {
        Subject currentSubject = getById(editSubject.getId());
        currentSubject.setSubjectName(editSubject.getSubjectName());
        currentSubject.setNumberOfCredit(editSubject.getNumberOfCredit());
        currentSubject.setModifyDate(new Date());
        Subject savedSubject = subjectRepository.save(currentSubject);

        return mapper.mapToDTO(savedSubject);
    }
}