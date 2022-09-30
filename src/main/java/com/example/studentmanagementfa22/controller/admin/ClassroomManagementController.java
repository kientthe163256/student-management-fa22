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
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("")
    public String displayClassManagement(){
        return "admin/classroomManagement/manageClassroom";
    }

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
    public String displayAllClassrooms(Model model, @RequestParam(required = false, defaultValue = "1") int pageNumber){
        Page<ClassroomDTO> classroomPage = classroomService.getAllClassroomsPaging(pageNumber);
        model.addAttribute("classroomList", classroomPage.getContent());
        model.addAttribute("pageNumber", pageNumber);
        model.addAttribute("totalPages", classroomPage.getTotalPages());
        return "admin/classroomManagement/allClassroom";
    }
}
