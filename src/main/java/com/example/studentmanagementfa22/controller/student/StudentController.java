package com.example.studentmanagementfa22.controller.student;

import com.example.studentmanagementfa22.dto.ClassroomDTO;
import com.example.studentmanagementfa22.dto.MarkDTO;
import com.example.studentmanagementfa22.dto.StudentDTO;
import com.example.studentmanagementfa22.entity.Account;
import com.example.studentmanagementfa22.entity.Mark;
import com.example.studentmanagementfa22.entity.Student;
import com.example.studentmanagementfa22.entity.Subject;
import com.example.studentmanagementfa22.repository.StudentRepository;
import com.example.studentmanagementfa22.service.*;
import com.example.studentmanagementfa22.utility.StudentMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/student")
public class StudentController {
    @Autowired
    private StudentRepository studentRepo;

    @Autowired
    private AccountService accountService;

    @Autowired
    private SubjectService subjectService;
    @Autowired
    private HttpSession session;

    @Autowired
    private ClassroomService classroomService;

    @Autowired
    private MarkService markService;

    @Autowired
    private StudentService studentService;

    @GetMapping()
    public ResponseEntity<String> getHomePage(){
        return ResponseEntity.ok("Login successfully");
    }

    @GetMapping("/information")
    @Operation(summary = "View information", description = "Student can view his personal information")
    public ResponseEntity<StudentDTO> displayInformation() {
        Account account = (Account) session.getAttribute("account");
        StudentDTO studentDTO = studentService.getStudentDTOByAccountId(account.getId());
        return ResponseEntity.ok(studentDTO);
    }
    @GetMapping("/subjectList")
    @Operation(summary = "Subject List", description = "List of all course in student 's major")
    public ResponseEntity<?> displaySubject( @RequestParam(required = false, defaultValue = "1") int pageNumber) {
        Page<Subject> subjectPage = subjectService.getAllSubject(pageNumber);
        List<Subject> subjectList = subjectPage.getContent();
        return ResponseEntity.ok(subjectList);
    }
    @PutMapping("/information")
    @Operation(summary = "Change information", description = "Student can change his personal information")
    @ApiResponse(responseCode = "201", description = "Update information successfully")
    public ResponseEntity<?> editInformation(@Valid @RequestBody StudentDTO student) {
        Account account = (Account) session.getAttribute("account");
        accountService.editInformation(account, student);
        return new ResponseEntity<>(account, HttpStatus.OK);
    }
    @GetMapping("/subjectRegistered")
    @Operation(summary = "List subject registered", description = "Display list of classroom that student has registered")
    public ResponseEntity<?> displaySubjectRegistered ( @RequestParam(required = false, defaultValue = "1") int pageNumber) {
        Account account = (Account) session.getAttribute("account");
        Student student = studentService.getStudentByAccountId(account.getId());
        List<ClassroomDTO> classroomDTOList  = classroomService.getAllRegisteredClass(pageNumber, student.getId());
        return ResponseEntity.ok(classroomDTOList);
    }
    @GetMapping("/mark/{subjectId}")
    @Operation(summary = "View Mark ", description = "Student can view mark of each subject")
    public ResponseEntity<?> displayMarkbySubject(@PathVariable Integer subjectId  ) {
        Account account = (Account) session.getAttribute("account");
        List<MarkDTO> markList = markService.getMarksBySubject(account, subjectId);
        return ResponseEntity.ok(markList);
    }
}
