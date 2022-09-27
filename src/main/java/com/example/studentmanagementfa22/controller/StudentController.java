package com.example.studentmanagementfa22.controller;

import com.example.studentmanagementfa22.entity.Student;
import com.example.studentmanagementfa22.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Controller
@RequestMapping("/student")
public class StudentController {
    @Autowired
    private StudentRepository studentRepo;

    @GetMapping("")
    public String getHomePage(){
        return "student/studentHomePage";
    }

    @GetMapping("/information/{id}")
    public String displayInformation(@PathVariable Integer id, Model model) {
        if (id.equals(null)) {
            id = 1;
        } else {
            return "ID: Default Employee";
        }
        Optional<Student> student = studentRepo.findById(id);
        Student student1 = student.get();
        model.addAttribute("student1", student1);
        return "viewStudentInformation";
    }
}
