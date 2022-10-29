package com.example.studentmanagementfa22.controller.student;

import com.example.studentmanagementfa22.dto.ClassroomDTO;
import com.example.studentmanagementfa22.entity.Account;
import com.example.studentmanagementfa22.service.ClassroomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpSession;
import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
@RestController
@RequestMapping("/student/classroom")
public class ClassroomController {

    @Autowired
    private ClassroomService classroomService;

    @Autowired
    private HttpSession session;

    @GetMapping("/classroomList")
    @Operation(summary = "Display list of classroom", description = "A List of classroom that has the subject student has not registered")
    public ResponseEntity<?> displayClassroom( @RequestParam(required = false, defaultValue = "1") int pageNumber,
                                               @RequestParam int subjectId ) {
        Page<ClassroomDTO> classroomPage = classroomService.getAllAvailClassroom(pageNumber, subjectId);
        if (classroomPage.getTotalPages() < pageNumber) {
            return new ResponseEntity<>("The last page is "+classroomPage.getTotalPages(), HttpStatus.BAD_REQUEST);
        }
        if (pageNumber <= 0) {
            return new ResponseEntity<>("Invalid page number", HttpStatus.BAD_REQUEST);
        }
        List<ClassroomDTO> classroomDTOList = classroomPage.getContent();
        return ResponseEntity.ok(classroomDTOList);
    }

    @PostMapping("/registerClassroom")
    @Operation(summary = "Regiter a classroom", description = "Student can only register for a classroom per subject")
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
