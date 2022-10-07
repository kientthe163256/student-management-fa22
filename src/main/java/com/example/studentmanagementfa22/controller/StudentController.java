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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Optional;

@RestController
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
    public ResponseEntity<StudentDTO> displayInformation() {
        Account account = (Account) session.getAttribute("account");
        Optional<Student> student = studentRepo.findStudentByAccountId(account.getId());
        Student student1 = student.get();
        Mapper utility = new Mapper();
        StudentDTO studentDTO = utility.mapAccount(account);
        studentDTO.setAcademicSession(student1.getAcademicSession());
        return ResponseEntity.ok(studentDTO);
    }
    @GetMapping("/subjectList")
    public Page<Subject> displaySubject( @RequestParam(required = false, defaultValue = "1") int pageNumber) {
        Page<Subject> subjectPage = subjectService.getAllSubject(pageNumber);
        return subjectPage;
    }
    @PostMapping("/information")
    public ResponseEntity<?> editInformation(@Valid StudentDTO student) {
        Account account = (Account) session.getAttribute("account");
        account.setFirstName(student.getFirstName());
        account.setLastName(student.getLastName());
        account.setDob(student.getDob());
        accountRepo.save(account);
        session.setAttribute("account",account);
        return new ResponseEntity<>(account, HttpStatus.OK);
    }
    @GetMapping("/subjectRegistered")
    public Page<ClassroomDTO> displaySubjectRegistered ( @RequestParam(required = false, defaultValue = "1") int pageNumber) {
        Account account = (Account) session.getAttribute("account");
        Optional<Student> student = studentRepo.findStudentByAccountId(account.getId());
        Page<ClassroomDTO> classroomDTOPage = classroomService.getAllRegisteredClass(pageNumber, student.get().getId());
        return classroomDTOPage;
    }
}
