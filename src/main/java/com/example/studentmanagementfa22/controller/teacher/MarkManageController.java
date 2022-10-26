package com.example.studentmanagementfa22.controller.teacher;

import com.example.studentmanagementfa22.entity.Mark;
import com.example.studentmanagementfa22.service.ClassroomService;
import com.example.studentmanagementfa22.service.MarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.text.ParseException;

@RestController
@RequestMapping("/teacher")
public class MarkManageController {

    @Autowired
    private MarkService markService;

    @PutMapping("/marks/{id}")
    public ResponseEntity<?> editStudentMark(@PathVariable(name = "id") Integer markID, @Valid @RequestBody Mark editMark) {
        Mark mark = markService.editStudentMark(markID, editMark);
        return new ResponseEntity<>(mark, HttpStatus.OK);
    }
}
