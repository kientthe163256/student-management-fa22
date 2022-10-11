package com.example.studentmanagementfa22.controller;

import com.example.studentmanagementfa22.dto.ClassroomDTO;
import com.example.studentmanagementfa22.entity.Account;
import com.example.studentmanagementfa22.repository.AccountRepository;
import com.example.studentmanagementfa22.repository.ClassroomRepository;
import com.example.studentmanagementfa22.repository.StudentRepository;
import com.example.studentmanagementfa22.service.ClassroomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Optional;

@RestController
@RequestMapping("student/classroom")
public class ClassroomController {

    @Autowired
    private ClassroomService classroomService;

    @Autowired
    private HttpSession session;

    @GetMapping("/classroomList")
    public Page<ClassroomDTO> displayClassroom( @RequestParam(required = false, defaultValue = "1") int pageNumber,
                                               @RequestParam int subjectId ) {
        Page<ClassroomDTO> classroomPage = classroomService.getAllAvailClassroom(pageNumber, subjectId);
        return classroomPage;
    }

    @PostMapping("/registerClassroom")
    public ResponseEntity<?> registerClassroom(@RequestParam int classId ) {
        Account account = (Account) session.getAttribute("account");
        try {
            classroomService.registerClassroom(classId, account.getId());
        } catch (Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity("Register successfully", HttpStatus.OK);
    }

}
