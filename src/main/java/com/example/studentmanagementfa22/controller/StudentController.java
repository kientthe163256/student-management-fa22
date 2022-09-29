package com.example.studentmanagementfa22.controller;

import com.example.studentmanagementfa22.dto.StudentDTO;
import com.example.studentmanagementfa22.entity.Account;
import com.example.studentmanagementfa22.entity.Student;
import com.example.studentmanagementfa22.entity.Subject;
import com.example.studentmanagementfa22.repository.AccountRepository;
import com.example.studentmanagementfa22.repository.StudentRepository;
import com.example.studentmanagementfa22.service.SubjectService;
import com.example.studentmanagementfa22.utility.Utility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.NoSuchElementException;
import java.util.Optional;

@Controller
@RequestMapping("/student")
public class StudentController {
    @Autowired
    private StudentRepository studentRepo;

    @Autowired
    private AccountRepository accountRepo;

    @Autowired
    private SubjectService subjectService;
    @Autowired
    private HttpSession session;

    @GetMapping("")
    public String getHomePage(){
        return "student/studentHomePage";
    }

    @GetMapping("/information")
    public String displayInformation( Model model) {
        Account account = (Account) session.getAttribute("account");
        Optional<Student> student = studentRepo.findStudentByAccountId(account.getId());
        Student student1 = student.get();
        Utility utility = new Utility();
        StudentDTO studentDTO = utility.mapAccount(account);
        studentDTO.setAcademicSession(student1.getAcademicSession());
        model.addAttribute("student",studentDTO);
        return "student/viewStudentInformation";
    }
    @GetMapping("/subjectList")
    public String displaySubject( Model model, @RequestParam(required = false, defaultValue = "1") int pageNumber) {
        Page<Subject> subjectPage = subjectService.getAllSubject(pageNumber);
        model.addAttribute("subjectList", subjectPage.getContent());
        model.addAttribute("pageNumber", pageNumber);
        model.addAttribute("totalPages", subjectPage.getTotalPages());
        return "subject/subjectList";
    }
    @PostMapping("/information")
    public String editInformation(@Valid StudentDTO student) {
        Account account = (Account) session.getAttribute("account");
        Optional<Account> account1 = accountRepo.findById(account.getId());
        if (account1.isEmpty()) {
            throw new NoSuchElementException("User is not found");
        }
        Account account2 = account1.get();
        account2.setFirstName(student.getFirstName());
        account2.setLastName(student.getLastName());
        account2.setDob(student.getDob());
        accountRepo.save(account2);
        session.setAttribute("account",account2);
        return "redirect:/student/information";
    }
}
