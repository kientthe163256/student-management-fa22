package com.example.studentmanagementfa22.controller.teacher;

import com.example.studentmanagementfa22.dto.ClassroomDTO;
import com.example.studentmanagementfa22.entity.Account;
import com.example.studentmanagementfa22.entity.Mark;
import com.example.studentmanagementfa22.service.ClassroomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.studentmanagementfa22.service.MarkService;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/teacher")
public class ClassroomManageController {


    @Autowired
    private ClassroomService classroomService;

    @Autowired
    private MarkService markService;
    @Autowired
    private HttpSession session;

    @GetMapping("")
    public String getHomePage(){
        return "teacher/Home";
    }

    @GetMapping("/classrooms")
    public ResponseEntity<?> displayTeachingClassroom () {
        Account account = (Account) session.getAttribute("account");
        List<ClassroomDTO> classroomList = classroomService.getAllTeachingClassrooms(account.getId());
        return ResponseEntity.ok(classroomList);
    }

    @PutMapping("/marks/{studentID}")
    public ResponseEntity<?> editStudentMark(@PathVariable int studentID, @Valid @RequestBody Mark editMark) {
        Mark mark = markService.editStudentMark(studentID, editMark);
        return new ResponseEntity<>(mark, HttpStatus.OK);
    }

}