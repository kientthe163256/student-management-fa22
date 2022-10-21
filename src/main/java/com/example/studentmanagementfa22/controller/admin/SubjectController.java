package com.example.studentmanagementfa22.controller.admin;

import com.example.studentmanagementfa22.dto.SubjectDTO;
import com.example.studentmanagementfa22.entity.Classroom;
import com.example.studentmanagementfa22.service.SubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/admin/subject")
public class SubjectController {
    @Autowired
    private SubjectService subjectService;

    @GetMapping()
    public List<SubjectDTO> displaySubjectList(@RequestParam(required = false, defaultValue = "1") int pageNumber){
        List<SubjectDTO> subjectDTOList = subjectService.getSubjectDTOList(pageNumber);
        return subjectDTOList;
    }

    @GetMapping("/{id}")
    public SubjectDTO getSubjectById(@PathVariable Integer id) {
        SubjectDTO subjectDTO = subjectService.getSubjectDTOByID(id);
        return subjectDTO;
    }

    @PostMapping()
    public ResponseEntity<?> addNewSubject(@Valid @RequestBody SubjectDTO subjectDTO){
        subjectService.addNewSubject(subjectDTO);
        return new ResponseEntity("Subject added successfully", HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> disableAccount(@PathVariable Integer id){
        subjectService.deleteSubject(id);
        return ResponseEntity.ok("Subject deleted successfully");
    }


}
