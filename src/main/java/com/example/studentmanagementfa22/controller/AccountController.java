package com.example.studentmanagementfa22.controller;

import com.example.studentmanagementfa22.entity.Account;
import com.example.studentmanagementfa22.exception.ElementAlreadyExistException;
import com.example.studentmanagementfa22.service.AccountService;
import com.example.studentmanagementfa22.service.RoleService;
import com.example.studentmanagementfa22.service.StudentService;
import com.example.studentmanagementfa22.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @GetMapping("/register/student")
    public String displayRegisterStudentAccount(Model model){
        Account account = new Account();
        model.addAttribute("account", account);
        return "student/register";
    }

    @PostMapping("/register/student")
    public String handleRegisterStudent(@Valid Account account, BindingResult bindingResult, Model model){
        if (bindingResult.hasErrors()){
            model.addAttribute("account", account);
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            String date = formatter.format(account.getDob());
            model.addAttribute("dob", date);
            return "student/register";
        }
        try{
            accountService.registerNewAccount(account);
            studentService.addStudentWithNewAccount(account);
        } catch (ElementAlreadyExistException ex){
            model.addAttribute("message", "There is already an account with given username!");
            return "student/register";
        }
        return "redirect:/login";
    }

    @GetMapping("/admin/teacher/add")
    public String displayAddTeacherForm(Model model){
        Account account = new Account();
        model.addAttribute("account", account);
        return "admin/teacherManagement/addNewTeacher";
    }

    @PostMapping("/admin/teacher/add")
    public String handleAddTeacher(@Valid Account account, BindingResult bindingResult, Model model){
        if (bindingResult.hasErrors()){
            model.addAttribute("account", account);
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            String date = formatter.format(account.getDob());
            model.addAttribute("dob", date);
            return "admin/teacherManagement/addNewTeacher";
        }
        try{
            accountService.registerNewAccount(account);
            teacherService.addTeacherWithNewAccount(account);
        } catch (ElementAlreadyExistException ex){
            model.addAttribute("message", "Duplicated teacher account!");
            return "admin/teacherManagement/addNewTeacher";
        }
        return "redirect:/admin/teacher/all";
    }
}
