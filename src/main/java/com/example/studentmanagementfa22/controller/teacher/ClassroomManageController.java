package com.example.studentmanagementfa22.controller.teacher;

import com.example.studentmanagementfa22.dto.ClassroomDTO;
import com.example.studentmanagementfa22.dto.ErrorResponseDTO;
import com.example.studentmanagementfa22.dto.MarkDTO;
import com.example.studentmanagementfa22.dto.StudentDTO;
import com.example.studentmanagementfa22.entity.Account;
import com.example.studentmanagementfa22.entity.Pagination;
import com.example.studentmanagementfa22.service.ClassroomService;
import com.example.studentmanagementfa22.service.StudentService;
import com.example.studentmanagementfa22.service.TeacherService;
import com.example.studentmanagementfa22.utility.i18n.MessageCode;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.studentmanagementfa22.service.MarkService;
import javax.servlet.http.HttpSession;
import java.util.List;


@RestController
@RequestMapping("/teacher/classrooms")
public class ClassroomManageController {


    @Autowired
    private ClassroomService classroomService;

    @Autowired
    private StudentService studentService;

    @Autowired
    private TeacherService teacherService;
    @Autowired
    private HttpSession session;

    @Autowired
    private MarkService markService;



    @GetMapping("")
    @Operation(summary = "View classrooms ", description = "Teacher can view the classrooms he teaches")
    public ResponseEntity<?> displayListTeachingClassrooms (@RequestParam(required = false, defaultValue = "1") int pageNumber,
                                                            @RequestParam(required = false, defaultValue = "5") int pageSize,
                                                            @RequestParam(required = false, defaultValue = "id,ASC") String sort) {
        Account account = (Account) session.getAttribute("account");
        Pagination<ClassroomDTO> classroomList = classroomService.getAllTeachingClassrooms(account.getId(), pageNumber, pageSize, sort);
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

    @DeleteMapping("{id}/students/{studentId}")
    public ResponseEntity<?> deleteTeacher(@PathVariable(name = "studentId") Integer studentId,
                                           @PathVariable(name = "id") Integer classId) {
        Account account = (Account) session.getAttribute("account");
        teacherService.removeStudentClassroom(account.getId(), studentId, classId);
        return new ResponseEntity<>(new ErrorResponseDTO(MessageCode.STUDENT, MessageCode.DELETED), HttpStatus.OK);
    }
    @PostMapping("/{classId}/students/{studentId}")
    @Operation(summary = "Add Student ", description = "Teacher can add student to session class ")
    public ResponseEntity<?> addStudentToSessionClass(@PathVariable(name = "classId") Integer classId,
                                            @PathVariable(name = "studentId") Integer studentId) {
        Account account = (Account) session.getAttribute("account");
        teacherService.addStudentToSessionClass( account.getId(), classId, studentId);
        return new ResponseEntity<>(new ErrorResponseDTO(MessageCode.STUDENT, MessageCode.ADDED), HttpStatus.OK);
    }


    @GetMapping("/{id}/students/{studentID}/marks")
    @Operation(summary = "View Mark ", description = "Teacher can view student mark ")
    public ResponseEntity<?> displayStudentMark(@PathVariable(name = "id") Integer classId,
                                                @PathVariable(name = "studentID") Integer studentID) {
        Account account = (Account) session.getAttribute("account");
        List<MarkDTO> markList = markService.getMarksByClassroomStudent(account.getId(),classId , studentID);
        return ResponseEntity.ok(markList);
    }






}
