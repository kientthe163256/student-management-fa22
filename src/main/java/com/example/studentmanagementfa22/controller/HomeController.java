package com.example.studentmanagementfa22.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;



@Controller
public class HomeController {
    @GetMapping("/user/home")
    public String getHomePage(){
        return "index";
    }
    @GetMapping("/admin")
    public String getAdmin(){
        return "admin";
    }
}
