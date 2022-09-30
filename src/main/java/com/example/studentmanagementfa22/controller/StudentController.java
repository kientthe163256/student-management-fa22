package com.example.studentmanagementfa22.controller;

import com.example.studentmanagementfa22.dto.ClassroomDTO;
import com.example.studentmanagementfa22.dto.StudentDTO;
import com.example.studentmanagementfa22.entity.Account;
import com.example.studentmanagementfa22.entity.Student;
import com.example.studentmanagementfa22.entity.Subject;
import com.example.studentmanagementfa22.repository.AccountRepository;
import com.example.studentmanagementfa22.repository.StudentRepository;
import com.example.studentmanagementfa22.service.ClassroomService;
import com.example.studentmanagementfa22.service.SubjectService;
import com.example.studentmanagementfa22.utility.Mapper;
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

    @Autowired
    private ClassroomService classroomService;

    @GetMapping("")
    public String getHomePage(){
        return "student/studentHomePage";
    }

    @GetMapping("/information")
    public String displayInformation( Model model) {
        Account account = (Account) session.getAttribute("account");
        Optional<Student> student = studentRepo.findStudentByAccountId(account.getId());
        Student student1 = student.get();
        Mapper utility = new Mapper();
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
        account.setFirstName(student.getFirstName());
        account.setLastName(student.getLastName());
        account.setDob(student.getDob());
        accountRepo.save(account);
        session.setAttribute("account",account);
        return "redirect:/student/information";
    }
    @GetMapping("/subjectRegistered")
    public String displaySubjectRegistered (Model model, @RequestParam(required = false, defaultValue = "1") int pageNumber) {
        Account account = (Account) session.getAttribute("account");
        Optional<Student> student = studentRepo.findStudentByAccountId(account.getId());
        Page<ClassroomDTO> classroomDTOPage = classroomService.getAllRegisteredClass(pageNumber, student.get().getId());
        model.addAttribute("classroomList", classroomDTOPage.getContent());
        model.addAttribute("pageNumber", pageNumber);
        model.addAttribute("totalPages", classroomDTOPage.getTotalPages());
        return "student/subjectRegisteredList";
    }
}
