package com.example.studentmanagementfa22.controller.teacher;

import com.example.studentmanagementfa22.dto.ClassroomDTO;
import com.example.studentmanagementfa22.dto.StudentDTO;
import com.example.studentmanagementfa22.entity.Account;
import com.example.studentmanagementfa22.entity.Mark;
import com.example.studentmanagementfa22.service.ClassroomService;
import com.example.studentmanagementfa22.service.StudentService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.studentmanagementfa22.service.MarkService;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping("/teacher/classrooms")
public class ClassroomManageController {


    @Autowired
    private ClassroomService classroomService;

    @Autowired
    private StudentService studentService;
    @Autowired
    private HttpSession session;

    @Autowired
    private MarkService markService;



    @GetMapping("")
    public ResponseEntity<?> displayListTeachingClassrooms () {
        Account account = (Account) session.getAttribute("account");
        List<ClassroomDTO> classroomList = classroomService.getAllTeachingClassrooms(account.getId());
        return ResponseEntity.ok(classroomList);
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> displayTeachingClassroom () {
        Account account = (Account) session.getAttribute("account");
        List<ClassroomDTO> classroomList = classroomService.getAllTeachingClassrooms(account.getId());
        return ResponseEntity.ok(classroomList);
    }
    @GetMapping("/{id}/students")
    @Operation(summary = "View students ", description = "Teacher can view all student in a classrooms")
    public ResponseEntity<?> displayStudentsbyClassroom(@PathVariable(name = "id") Integer classID,
            @RequestParam(required = false, defaultValue = "1") int pageNumber,
            @RequestParam(required = false, defaultValue = "5") int pageSize,
            @RequestParam(required = false, defaultValue = "id,ASC") String sort) {
        Account account = (Account) session.getAttribute("account");
        List<StudentDTO> studentDTOList = studentService.getStudentsByClassroomandTeacher(classID, account.getId(), pageNumber, pageSize, sort)  ;
        return ResponseEntity.ok(studentDTOList);
    }
    @PostMapping("/{id}/students/{studentId}/marks")
    @Operation(summary = "Add Mark ", description = "Teacher can add student mark ")
    public ResponseEntity<?> addStudentMark(@PathVariable(name = "id") Integer classId,
                                            @PathVariable(name = "studentId") Integer studentId,
                                            @Valid @RequestBody Mark newMark) {
        Account account = (Account) session.getAttribute("account");
        newMark.setStudentId(studentId);
        markService.addStudentMark(newMark, account.getId(), classId);
        return new ResponseEntity<>(newMark, HttpStatus.OK);
    }



//    @GetMapping("/{id}/students/{studentID}/marks")
//    @Operation(summary = "View Mark ", description = "Teacher can view student mark ")
//    public ResponseEntity<?> displayStudentMark(@PathVariable Integer subjectId  ) {
//        Account account = (Account) session.getAttribute("account");
//        List<Mark> markList = markService.getMarksBySubject(account, subjectId);
//        return ResponseEntity.ok(markList);
//    }
//
//    @PostMapping("/{id}/students/{studentID}/mark")
//    @Operation(summary = "View Mark ", description = "Teacher can view student mark ")
//    public ResponseEntity<?> addStudentMark(@PathVariable Integer subjectId  ) {
//        Account account = (Account) session.getAttribute("account");
//        List<Mark> markList = markService.getMarksBySubject(account, subjectId);
//        return ResponseEntity.ok(markList);
//    }





}
