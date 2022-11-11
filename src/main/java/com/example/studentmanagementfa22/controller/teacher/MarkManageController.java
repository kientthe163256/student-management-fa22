package com.example.studentmanagementfa22.controller.teacher;

import com.example.studentmanagementfa22.dto.MarkDTO;
import com.example.studentmanagementfa22.dto.ResponseDTO;
import com.example.studentmanagementfa22.entity.Account;
import com.example.studentmanagementfa22.entity.Mark;
import com.example.studentmanagementfa22.entity.Teacher;
import com.example.studentmanagementfa22.repository.MarkRepository;
import com.example.studentmanagementfa22.service.MarkService;
import com.example.studentmanagementfa22.service.TeacherService;
import com.example.studentmanagementfa22.utility.MarkMapper;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@RestController
@RequestMapping("/teacher/marks")
public class MarkManageController {

    @Autowired
    private MarkService markService;

    @Autowired
    private HttpSession session;

    @Autowired
    private TeacherService teacherService;

    @PutMapping("/{id}")
    @Operation(summary = "Edit mark", description = "Eidt mark of Student by mark id")
    public ResponseEntity<?> editStudentMark(@PathVariable(name = "id") Integer markID,
                                             @Valid @RequestBody Mark editMark) {
        Account account = (Account) session.getAttribute("account");
        MarkDTO mark = markService.editStudentMark(markID, editMark, account.getId());
        return new ResponseEntity<>(mark, HttpStatus.OK);
    }
    @Operation(summary = "Delete mark", description = "Delete mark of Student by mark id")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMark(@PathVariable(name="id") Integer markId) {
        Account account = (Account) session.getAttribute("account");
        markService.deleteMark(markId, account.getId());
        return ResponseEntity.ok(new ResponseDTO<>("Mark deleted successfully", 200));
    }

    @Operation(summary = "View report", description = "Teacher can view mark report of a class by class id")
    @GetMapping("/{classId}")
    public ResponseEntity<?> viewReport(@PathVariable int classId) {
        Account account = (Account) session.getAttribute("account");
        Teacher teacher = teacherService.getTeacherByAccountId(account.getId());
        teacherService.checkTeacherAssignedClass(teacher.getId(), classId);

        return ResponseEntity.ok(markService.getMarkReportByClassId(classId));
    }

}
