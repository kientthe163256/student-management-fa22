package com.example.studentmanagementfa22.controller;

import com.example.studentmanagementfa22.entity.Account;
import com.example.studentmanagementfa22.exception.UserAlreadyExistException;
import com.example.studentmanagementfa22.service.AccountService;
import com.example.studentmanagementfa22.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
public class AccountController {
    @Autowired
    private RoleService roleService;

    @Autowired
    private AccountService accountService;

    @GetMapping("/student/register")
    public String displayRegisterStudentAccount(Model model){
        Account account = new Account();
        model.addAttribute("account", account);
        return "student/register";
    }

    @PostMapping("/student/register")
    public String handleRegisterStudent(@Valid Account account, BindingResult bindingResult, Model model){
        if (bindingResult.hasErrors()){
            return "student/register";
        }

        account.setEnabled(true);
        account.setRoleId(roleService.findByRoleName("ROLE_STUDENT").getId());
        try{
            accountService.registerNewAccount(account);
            return "redirect:/login";
        } catch (UserAlreadyExistException ex){
            model.addAttribute("message", "There is already an account with given username!");
            return "student/register";
        }
    }
}
