package com.example.studentmanagementfa22.controller;

import com.example.studentmanagementfa22.dto.AccountDTO;
import com.example.studentmanagementfa22.entity.Account;
import com.example.studentmanagementfa22.repository.service.AccountService;
import com.example.studentmanagementfa22.repository.service.RoleService;
import com.example.studentmanagementfa22.repository.service.StudentService;
import com.example.studentmanagementfa22.repository.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class AccountController {
    @Autowired
    private RoleService roleService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private StudentService studentService;

    @Autowired
    private TeacherService teacherService;


    @PostMapping("/register/student")
    public ResponseEntity<?> registerStudent(@Valid Account account, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            StringBuilder message = new StringBuilder();
            for (ObjectError error : bindingResult.getAllErrors()) {
                message.append(error.getDefaultMessage()).append("\n");
            }
            return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
        }
        accountService.registerNewAccount(account);
        studentService.addStudentWithNewAccount(account);

        return new ResponseEntity(account, HttpStatus.CREATED);
    }

    @PostMapping("/admin/teacher")
    public ResponseEntity<?> addNewTeacher(@Valid Account account, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            StringBuilder message = new StringBuilder();
            for (ObjectError error : bindingResult.getAllErrors()) {
                if (error.getCode().equals("typeMismatch")) {
                    message.append("Date format must be yyyy-MM-dd (2022-05-26)");
                    continue;
                }
                message.append(error.getDefaultMessage()).append("\n");
            }
            return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
        }

        accountService.registerNewAccount(account);
        teacherService.addTeacherWithNewAccount(account);

        return new ResponseEntity<>(account, HttpStatus.OK);
    }

    @GetMapping("/admin/account")
    public ResponseEntity<?> displayAllAccount(@RequestParam(required = false, defaultValue = "1") int pageNumber) {
        if (pageNumber <= 0) {
            return new ResponseEntity<>("Invalid page number", HttpStatus.BAD_REQUEST);
        }
        Page<Account> accountPage = accountService.findAllAccount(pageNumber);
        List<Account> accountList = accountPage.getContent();
        return ResponseEntity.ok(accountList);
    }

    @PutMapping("/admin/account/{id}")
    public ResponseEntity<?> updateAccountById(@PathVariable Integer id, @Valid AccountDTO accountDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            StringBuilder message = new StringBuilder();
            for (ObjectError error : bindingResult.getAllErrors()) {
                if (error.getCode().equals("typeMismatch")) {
                    message.append("Date format must be yyyy-MM-dd (2022-05-26)");
                    continue;
                }
                message.append(error.getDefaultMessage()).append("\n");
            }
            return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
        }

        Account account = accountService.findById(id);
        accountService.updateAccount(accountDTO, account);


        return ResponseEntity.ok("Edit successfully");
    }
}
