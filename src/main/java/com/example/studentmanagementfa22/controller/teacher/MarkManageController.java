package com.example.studentmanagementfa22.controller.teacher;

import com.example.studentmanagementfa22.entity.Account;
import com.example.studentmanagementfa22.entity.Mark;
import com.example.studentmanagementfa22.service.MarkService;
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
    @PutMapping("/{id}")
    @Operation(summary = "Edit mark", description = "Eidt mark of Student by mark id")
    public ResponseEntity<?> editStudentMark(@PathVariable(name = "id") Integer markID,
                                             @Valid @RequestBody Mark editMark) {
        Account account = (Account) session.getAttribute("account");
        Mark mark = markService.editStudentMark(markID, editMark, account.getId());
        return new ResponseEntity<>(mark, HttpStatus.OK);
    }
    @Operation(summary = "Delete mark", description = "Delete mark of Student by mark id")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteMark(@PathVariable Integer markId) {
        Account account = (Account) session.getAttribute("account");
        markService.deleteMark(markId, account.getId());
        return ResponseEntity.ok("Mark deleted successfully");
    }


}
