package com.example.studentmanagementfa22.controller;

import com.example.studentmanagementfa22.dto.StudentDTO;
import com.example.studentmanagementfa22.entity.Account;
import com.example.studentmanagementfa22.entity.Student;
import com.example.studentmanagementfa22.repository.AccountRepository;
import com.example.studentmanagementfa22.repository.StudentRepository;
import com.example.studentmanagementfa22.utility.Utility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Controller
@RequestMapping("/student")
public class StudentController {
    @Autowired
    private StudentRepository studentRepo;

    @Autowired
    private AccountRepository accountRepo;

    @GetMapping("")
    public String getHomePage(){
        return "student/studentHomePage";
    }

    @GetMapping("/information/{accountId}")
    public String displayInformation(@PathVariable Integer accountId, Model model) {

        Optional<Account> account = accountRepo.findById(accountId);
        Account account1 = account.get();
        Optional<Student> student = studentRepo.findStudentByAccountId(accountId);
        Student student1 = student.get();
        Utility utility;
        utility = new Utility();
        StudentDTO studentDTO = utility.mapAccount(account1);
        studentDTO.setAcademicSession(student1.getAcademicSession());
        model.addAttribute("student",studentDTO);
        return "student/viewStudentInformation";
    }
}
