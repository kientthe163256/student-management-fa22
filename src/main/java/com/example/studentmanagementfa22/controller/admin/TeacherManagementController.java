package com.example.studentmanagementfa22.controller.admin;

import com.example.studentmanagementfa22.entity.Account;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/teacher")
public class TeacherManagementController {
    @GetMapping("/all")
    public String viewTeacherList(){
        return "admin/teacherManagement/allTeacher";
    }
}
