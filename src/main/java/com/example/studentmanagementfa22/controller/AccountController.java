package com.example.studentmanagementfa22.controller;

import com.example.studentmanagementfa22.entity.Account;
import com.example.studentmanagementfa22.exception.ElementAlreadyExistException;
import com.example.studentmanagementfa22.exception.ErrorResponse;
import com.example.studentmanagementfa22.service.AccountService;
import com.example.studentmanagementfa22.service.RoleService;
import com.example.studentmanagementfa22.service.StudentService;
import com.example.studentmanagementfa22.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.text.SimpleDateFormat;

@Controller
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
    public ResponseEntity registerStudent(@Valid Account account, BindingResult bindingResult){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String date = formatter.format(account.getDob());
        System.out.println(date);
        if (bindingResult.hasErrors()){
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        try{
            accountService.registerNewAccount(account);
            studentService.addStudentWithNewAccount(account);
        } catch (ElementAlreadyExistException ex){
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @PostMapping("/admin/teacher/add")
    public ResponseEntity<?> addNewTeacher(@Valid Account account, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            String date = formatter.format(account.getDob());
            return new ResponseEntity<>("Please check your request!", HttpStatus.BAD_REQUEST);
        }
        try{
            accountService.registerNewAccount(account);
            teacherService.addTeacherWithNewAccount(account);
        } catch (ElementAlreadyExistException ex){
            return new ResponseEntity<>("Username already exists!", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(account, HttpStatus.OK);
    }
}
