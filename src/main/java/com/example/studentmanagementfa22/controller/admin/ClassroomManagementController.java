package com.example.studentmanagementfa22.controller.admin;

import com.example.studentmanagementfa22.dto.ClassroomDTO;
import com.example.studentmanagementfa22.entity.Classroom;
import com.example.studentmanagementfa22.entity.Subject;
import com.example.studentmanagementfa22.entity.Teacher;
import com.example.studentmanagementfa22.exception.ElementAlreadyExistException;
import com.example.studentmanagementfa22.service.ClassroomService;
import com.example.studentmanagementfa22.service.SubjectService;
import com.example.studentmanagementfa22.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/admin/classroom")
public class ClassroomManagementController {
    @Autowired
    private TeacherService teacherService;

    @Autowired
    private ClassroomService classroomService;

    @Autowired
    private SubjectService subjectService;

    @GetMapping("/add")
    public String displayAddClassroomForm(Model model){
        Classroom classroom = new Classroom();
        model.addAttribute("classroom", classroom);
        List<Subject> subjectList = subjectService.getSubjectList();
        model.addAttribute("subjectList", subjectList);
        return "admin/classroomManagement/addNewClassroom";
    }

    @PostMapping("/add")
    public String handleAddClassroom(@Valid Classroom classroom, BindingResult bindingResult, Model model){
        if (bindingResult.hasErrors()){
            return "admin/classroomManagement/addNewClassroom";
        }
        try{
            classroomService.addNewClassroom(classroom);
        } catch (ElementAlreadyExistException ex){
            //add subject list to model
            List<Subject> subjectList = subjectService.getSubjectList();
            model.addAttribute("subjectList", subjectList);
            //add error message
            model.addAttribute("message", "Classname existed!");
            return "admin/classroomManagement/addNewClassroom";
        }
        return "redirect:/admin/classroom/all";
    }

    @GetMapping("/all")
    public String displayAllClassrooms(Model model){
        List<ClassroomDTO> classroomDTOS = classroomService.getAllClassrooms();
        model.addAttribute("classroomList", classroomDTOS);
        return "admin/classroomManagement/allClassroom";
    }
}
