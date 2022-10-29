package com.example.studentmanagementfa22.controller.teacher;

import com.example.studentmanagementfa22.entity.Account;
import com.example.studentmanagementfa22.entity.Mark;
import com.example.studentmanagementfa22.service.MarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@RestController
@RequestMapping("/teacher")
public class MarkManageController {

    @Autowired
    private MarkService markService;

    @Autowired
    private HttpSession session;
    @PutMapping("/marks/{id}")
    public ResponseEntity<?> editStudentMark(@PathVariable(name = "id") Integer markID,
                                             @Valid @RequestBody Mark editMark) {
        Account account = (Account) session.getAttribute("account");
        Mark mark = markService.editStudentMark(markID, editMark, account.getId());
        return new ResponseEntity<>(mark, HttpStatus.OK);
    }

//    @PostMapping("/marks/{studentId}")
//    public ResponseEntity<?> addStudentMark(@PathVariable(name = "") Integer markID,
//                                             @Valid @RequestBody Mark editMark) {
//        Account account = (Account) session.getAttribute("account");
//        Mark mark = markService.editStudentMark(markID, editMark, account.getId());
//        return new ResponseEntity<>(mark, HttpStatus.OK);
//    }

}
