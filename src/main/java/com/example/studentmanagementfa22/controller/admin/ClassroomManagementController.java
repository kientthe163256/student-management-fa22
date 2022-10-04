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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/admin/classroom")
public class ClassroomManagementController {
    @Autowired
    private TeacherService teacherService;

    @Autowired
    private ClassroomService classroomService;

    @Autowired
    private SubjectService subjectService;

    @PostMapping("/add")
    public ResponseEntity handleAddClassroom(@Valid Classroom classroom, BindingResult bindingResult, Model model){
        if (bindingResult.hasErrors()){
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        try{
            classroomService.addNewClassroom(classroom);
        } catch (ElementAlreadyExistException ex){
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @GetMapping("/all")
    public Page<ClassroomDTO> displayAllClassrooms(@RequestParam(required = false, defaultValue = "1") int pageNumber){
//        try{
//            int page = Integer.parseInt(pageNumber);
//        }
        Page<ClassroomDTO> classroomPage = classroomService.getAllClassroomsPaging(pageNumber);
        return classroomPage;
    }
}
