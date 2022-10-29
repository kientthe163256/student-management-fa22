package com.example.studentmanagementfa22.controller.teacher;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/teacher")
public class TeacherController {

    @GetMapping("")
    public String getHomePage(){
        return "teacher/Home";
    }
}
