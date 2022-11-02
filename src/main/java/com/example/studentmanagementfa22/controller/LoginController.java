package com.example.studentmanagementfa22.controller;

import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;

@Controller
public class LoginController {
    @GetMapping("/login")
    public String displayLogin() {
        return "login";
    }

    @ApiOperation("Login")
    @PostMapping("/login")
    public @ResponseBody String login(@RequestParam String username, @RequestParam String password, HttpServletResponse response) {
        return "Login success";
    }
}
