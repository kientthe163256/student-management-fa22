package com.example.studentmanagementfa22.controller.admin;

import com.example.studentmanagementfa22.dto.ClassroomDTO;
import com.example.studentmanagementfa22.dto.TeacherDTO;
import com.example.studentmanagementfa22.entity.Account;
import com.example.studentmanagementfa22.entity.Teacher;
import com.example.studentmanagementfa22.service.AccountService;
import com.example.studentmanagementfa22.service.TeacherService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/admin/teacher")
public class TeacherManagementController {
    @Autowired
    private TeacherService teacherService;

    @Autowired
    private AccountService accountService;

    @GetMapping()
    public ResponseEntity<?> viewTeacherList(
            @RequestParam(required = false, defaultValue = "1") int pageNumber,
            @RequestParam(required = false, defaultValue = "5") int pageSize,
            @RequestParam(required = false, defaultValue = "id") String sortCriteria,
            @RequestParam(required = false, defaultValue = "ASC") String direction){
       try{
            Page<TeacherDTO> teacherDTOPage = teacherService.findAllTeacherPaging(pageNumber, pageSize, sortCriteria, direction);
            List<TeacherDTO> teacherDTOList = teacherDTOPage.getContent();
            return ResponseEntity.ok(teacherDTOList);
        } catch (IllegalArgumentException illegalArgument){
            return ResponseEntity.badRequest().body(illegalArgument.getMessage());
        }
    }

    @Operation(summary = "Find teacher by ID", description = "Returns teacher dto")
    @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(schema = @Schema(implementation = TeacherDTO.class)))
    @ApiResponse(responseCode = "400", description = "Invalid ID supplied", content = @Content)
    @ApiResponse(responseCode = "404", description = "Teacher is not found", content = @Content)
    @GetMapping("/{id}")
    public ResponseEntity<?> getTeacherById(@PathVariable(name = "id") Integer teacherId) {
        TeacherDTO teacherDTO = teacherService.getTeacherDTOById(teacherId);
        if (teacherDTO != null) {
            return ResponseEntity.ok(teacherDTO);
        } else {
            return new ResponseEntity("Teacher not found", HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTeacher(@PathVariable Integer id){
        Teacher teacher = teacherService.findById(id);
        teacherService.deleteTeacher(id);
        accountService.disableAccount(teacher.getAccountId());
        return ResponseEntity.ok("Teacher deleted successfully");
    }

}
