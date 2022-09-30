package com.example.studentmanagementfa22.controller;

import com.example.studentmanagementfa22.dto.ClassroomDTO;
import com.example.studentmanagementfa22.entity.Account;
import com.example.studentmanagementfa22.entity.Classroom;
import com.example.studentmanagementfa22.repository.AccountRepository;
import com.example.studentmanagementfa22.repository.ClassroomRepository;
import com.example.studentmanagementfa22.service.ClassroomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.NoSuchElementException;
import java.util.Optional;

@Controller
@RequestMapping("student/classroom")
public class ClassroomController {

    @Autowired
    private ClassroomService classroomService;

    @Autowired
    private ClassroomRepository classroomRepo;

    @Autowired
    private HttpSession session;

    @Autowired
    private AccountRepository accountRepo;

    @GetMapping("/classroomList")
    public String displayClassroom(Model model, @RequestParam(required = false, defaultValue = "1") int pageNumber,
                                   @RequestParam int subjectId ) {
        Page<ClassroomDTO> classroomPage = classroomService.getAllAvailClassroom(pageNumber, subjectId);
        model.addAttribute("classroomList", classroomPage.getContent());
        model.addAttribute("pageNumber", pageNumber);
        model.addAttribute("totalPages", classroomPage.getTotalPages());
        model.addAttribute("subjectId", subjectId);
        return "classroom/classroomList";
    }

    @GetMapping("/registerClassroom")
    public String registerClassroom(@RequestParam int classId ) {
        Account account = (Account) session.getAttribute("account");
        Optional<Account> account1 = accountRepo.findById(account.getId());
        if (account1.isEmpty()) {
            throw new NoSuchElementException("User is not found");
        }
        Classroom classroom =  classroomRepo.findById(classId);
        if (classroomRepo.numOfSubjectClassbyStudent(classroom.getSubjectId(), account1.get().getId()) == 0) {
            classroomRepo.registerClassroom(account1.get().getId(), classId);
            classroomRepo.updateNoStudentOfClass(classId);
        }
        return "redirect:/student/subjectList";
    }
}
