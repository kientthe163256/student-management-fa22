package com.example.studentmanagementfa22.controller;

import com.example.studentmanagementfa22.entity.Classroom;
import com.example.studentmanagementfa22.service.ClassroomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/classroom")
public class ClassroomController {

    @Autowired
    private ClassroomService classroomService;

    @GetMapping("/classroomList")
    public String displayClassroom(Model model, @RequestParam(required = false, defaultValue = "1") int pageNumber,
                                   @RequestParam() int subjectId ) {
        Page<Classroom> classroomPage = classroomService.getAllAvailClassroom(pageNumber, subjectId);
        model.addAttribute("classroomList", classroomPage.getContent());
        model.addAttribute("pageNumber", pageNumber);
        model.addAttribute("totalPages", classroomPage.getTotalPages());
        return "classroom/classroomList";
    }
}
