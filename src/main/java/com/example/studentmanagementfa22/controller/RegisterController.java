package com.example.studentmanagementfa22.controller;

import com.example.studentmanagementfa22.entity.Account;
import com.example.studentmanagementfa22.service.AccountService;
import com.example.studentmanagementfa22.service.StudentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class RegisterController {
    @Autowired
    private AccountService accountService;

    @Autowired
    private StudentService studentService;

    @Operation(summary = "Register student account", description = "Student register a new account")
    @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(mediaType = "String"))
    @ApiResponse(responseCode = "400", description = "Invalid parameters supplied", content = @Content(mediaType = "String"))
    @PostMapping("/register/student")
    public ResponseEntity<?> registerStudent(@Valid @RequestBody Account account) {
        accountService.registerNewAccount(account, "ROLE_STUDENT");
        studentService.addStudentWithNewAccount(account);

        return new ResponseEntity("Account created successfully", HttpStatus.CREATED);
    }
}
