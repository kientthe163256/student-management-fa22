package com.example.studentmanagementfa22.controller.admin;

import com.example.studentmanagementfa22.dto.ClassroomDTO;
import com.example.studentmanagementfa22.dto.TeacherDTO;
import com.example.studentmanagementfa22.entity.Account;
import com.example.studentmanagementfa22.entity.Teacher;
import com.example.studentmanagementfa22.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/teacher")
public class TeacherManagementController {
    @Autowired
    private TeacherService teacherService;
    @GetMapping("")
    public String displayTeacherManagement(){
        return "admin/teacherManagement/manageTeacher";
    }

    @GetMapping("/all")
    public Page<Teacher> viewTeacherList(@RequestParam(required = false, defaultValue = "1") int pageNumber){
        Page<Teacher> teacherDTOPage = teacherService.findAllTeacherPaging(pageNumber);
        return teacherDTOPage;
    }

}
