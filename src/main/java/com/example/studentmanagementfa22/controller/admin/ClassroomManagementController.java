package com.example.studentmanagementfa22.controller.admin;

import com.example.studentmanagementfa22.entity.Classroom;
import com.example.studentmanagementfa22.entity.Teacher;
import com.example.studentmanagementfa22.exception.ElementAlreadyExistException;
import com.example.studentmanagementfa22.service.ClassroomService;
import com.example.studentmanagementfa22.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/admin/classroom")
public class ClassroomManagementController {
    @Autowired
    private TeacherService teacherService;

    @Autowired
    private ClassroomService classroomService;

    @GetMapping("/add")
    public String displayAddClassroomForm(Model model){
        Classroom classroom = new Classroom();
        model.addAttribute("classroom", classroom);
        List<Teacher> teacherList = teacherService.findAllTeachers();
        model.addAttribute("teacherList", teacherList);
        return "admin/classroomManagement/addNewClassroom";
    }

    @PostMapping("/add")
    public String handleAddClassroom(@Valid Classroom classroom, BindingResult bindingResult, Model model){
        if (bindingResult.hasErrors()){
            return "admin/teacherManagement/addNewTeacher";
        }
        try{
            classroomService.addNewClassroom(classroom);
        } catch (ElementAlreadyExistException ex){
            model.addAttribute("message", "Duplicated teacher account!");
            return "admin/teacherManagement/addNewTeacher";
        }
        return "redirect:/admin/teacher/all";
    }

    @GetMapping("/all")
    public String displayAllClassrooms(){
        return "admin/classroomManagement/allClassroom";
    }
}
