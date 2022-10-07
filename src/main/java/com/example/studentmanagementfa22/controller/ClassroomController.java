package com.example.studentmanagementfa22.controller;

import com.example.studentmanagementfa22.dto.ClassroomDTO;
import com.example.studentmanagementfa22.entity.Account;
import com.example.studentmanagementfa22.entity.Classroom;
import com.example.studentmanagementfa22.entity.Student;
import com.example.studentmanagementfa22.repository.AccountRepository;
import com.example.studentmanagementfa22.repository.ClassroomRepository;
import com.example.studentmanagementfa22.repository.StudentRepository;
import com.example.studentmanagementfa22.service.ClassroomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpSession;
import java.util.Optional;

@RestController
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
    @Autowired
    private StudentRepository studentRepo;

    @GetMapping("/classroomList")
    public Page<ClassroomDTO> displayClassroom( @RequestParam(required = false, defaultValue = "1") int pageNumber,
                                               @RequestParam int subjectId ) {
        Page<ClassroomDTO> classroomPage = classroomService.getAllAvailClassroom(pageNumber, subjectId);
        return classroomPage;
    }

    @GetMapping("/registerClassroom")
    public ResponseEntity<?> registerClassroom(@RequestParam int classId ) {
        Account account = (Account) session.getAttribute("account");
        Optional<Account> account1 = accountRepo.findById(account.getId());
        if (account1.isEmpty()) {
            return new ResponseEntity<>("User is not found", HttpStatus.BAD_REQUEST);
        }
        Classroom classroom =  classroomRepo.findById(classId);
        Optional<Student> student = studentRepo.findStudentByAccountId(account1.get().getId());
        if (classroomRepo.numOfSubjectClassbyStudent(classroom.getSubjectId(), student.get().getId() ) == 0 ) {
            classroomRepo.registerClassroom(student.get().getId(), classId);
            classroomRepo.updateNoStudentOfClass(classId);
            return new ResponseEntity<>("Register successfully", HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>("You have already registered for this subject",HttpStatus.BAD_REQUEST);
        }
    }

}
