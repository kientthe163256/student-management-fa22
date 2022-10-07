package com.example.studentmanagementfa22.controller.admin;

import com.example.studentmanagementfa22.dto.ClassroomDTO;
import com.example.studentmanagementfa22.entity.Subject;
import com.example.studentmanagementfa22.service.SubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/subject")
public class SubjectController {
    @Autowired
    private SubjectService subjectService;

    @GetMapping("/all")
    public Page<Subject> displayAllClassrooms(@RequestParam(required = false, defaultValue = "1") int pageNumber){
        Page<Subject> subjectPage = subjectService.getAllSubject(pageNumber);
        return subjectPage;
    }
}
