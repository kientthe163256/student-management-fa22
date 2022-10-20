package com.example.studentmanagementfa22.controller.admin;

import com.example.studentmanagementfa22.dto.ClassroomDTO;
import com.example.studentmanagementfa22.entity.Classroom;
import com.example.studentmanagementfa22.repository.service.ClassroomService;
import com.example.studentmanagementfa22.repository.service.SubjectService;
import com.example.studentmanagementfa22.repository.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
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

    @PostMapping()
    public ResponseEntity<?> addNewClassroom(@Valid @RequestBody Classroom classroom, BindingResult bindingResult){
//        if (bindingResult.hasErrors()){
//            return new ResponseEntity("Check your request", HttpStatus.BAD_REQUEST);
//        }
        classroomService.addNewClassroom(classroom);
        return new ResponseEntity(classroom, HttpStatus.CREATED);
    }

    @GetMapping()
    public ResponseEntity<?> displayAllClassrooms(@RequestParam(required = false, defaultValue = "1") int pageNumber){
        if (pageNumber <= 0) {
            return new ResponseEntity<>("Invalid page number", HttpStatus.BAD_REQUEST);
        }
        Page<ClassroomDTO> classroomPage = classroomService.getAllClassroomsPaging(pageNumber);
        List<ClassroomDTO> classroomDTOList = classroomPage.getContent();
        return ResponseEntity.ok(classroomDTOList);
    }
}
