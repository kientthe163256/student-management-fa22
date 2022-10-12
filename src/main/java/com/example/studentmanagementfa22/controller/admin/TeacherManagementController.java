package com.example.studentmanagementfa22.controller.admin;

import com.example.studentmanagementfa22.dto.ClassroomDTO;
import com.example.studentmanagementfa22.dto.TeacherDTO;
import com.example.studentmanagementfa22.entity.Account;
import com.example.studentmanagementfa22.entity.Teacher;
import com.example.studentmanagementfa22.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/teacher")
public class TeacherManagementController {
    @Autowired
    private TeacherService teacherService;

    @GetMapping()
    public ResponseEntity<?> viewTeacherList(@RequestParam(required = false, defaultValue = "1") int pageNumber) {
        if (pageNumber <= 0) {
            return new ResponseEntity<>("Invalid page number", HttpStatus.BAD_REQUEST);
        }
        Page<TeacherDTO> teacherDTOPage = teacherService.findAllTeacherPaging(pageNumber);
        List<TeacherDTO> teacherDTOList = teacherDTOPage.getContent();
        return ResponseEntity.ok(teacherDTOList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getTeacherById(@PathVariable(name = "id") Integer teacherId){
        TeacherDTO teacherDTO = teacherService.getTeacherDTOById(teacherId);
        if (teacherDTO != null){
            return ResponseEntity.ok(teacherDTO);
        } else {
            return new ResponseEntity("Teacher not found", HttpStatus.NOT_FOUND);
        }
    }
}
