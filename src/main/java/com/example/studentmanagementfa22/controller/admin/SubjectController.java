package com.example.studentmanagementfa22.controller.admin;

import com.example.studentmanagementfa22.dto.SubjectDTO;
import com.example.studentmanagementfa22.service.SubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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


}
