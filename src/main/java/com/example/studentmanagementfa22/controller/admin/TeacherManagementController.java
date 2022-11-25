package com.example.studentmanagementfa22.controller.admin;

import com.example.studentmanagementfa22.dto.ErrorResponseDTO;
import com.example.studentmanagementfa22.dto.TeacherDTO;
import com.example.studentmanagementfa22.entity.Pagination;
import com.example.studentmanagementfa22.entity.Teacher;
import com.example.studentmanagementfa22.service.AccountService;
import com.example.studentmanagementfa22.service.TeacherService;
import com.example.studentmanagementfa22.utility.TranslationCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/teachers")
public class TeacherManagementController {
    @Autowired
    private TeacherService teacherService;

    @Autowired
    private AccountService accountService;

    @Operation(summary = "View teacher list", description = "Returns list of teacher dto")
    @ApiResponse(responseCode = "200", description = "Successful operation",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = TeacherDTO.class))))
    @ApiResponse(responseCode = "400", description = "Invalid input format", content = @Content(mediaType = "String"))
    @GetMapping()
    public ResponseEntity<?> viewTeacherList(
            @RequestParam(required = false, defaultValue = "1") int pageNumber,
            @RequestParam(required = false, defaultValue = "5") int pageSize,
            @RequestParam(required = false, defaultValue = "id,ASC") String sort){
        Pagination<TeacherDTO> teachers = teacherService.getAllTeacherPaging(pageNumber, pageSize, sort);
        return ResponseEntity.ok(teachers);
    }

    @Operation(summary = "Find teacher by ID", description = "Find teacher by teacher id then returns teacher dto")
    @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(schema = @Schema(implementation = TeacherDTO.class)))
    @ApiResponse(responseCode = "400", description = "Invalid ID supplied", content = @Content(mediaType = "String"))
    @ApiResponse(responseCode = "404", description = "Teacher is not found", content = @Content(mediaType = "String"))
    @GetMapping("/{id}")
    public ResponseEntity<?> getTeacherById(@PathVariable(name = "id") Integer teacherId) {
        TeacherDTO teacherDTO = teacherService.getTeacherDTOById(teacherId);
        return ResponseEntity.ok(teacherDTO);
    }

    @Operation(summary = "Delete teacher", description = "Delete teacher and disable account by teacher id")
    @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(mediaType = "String"))
    @ApiResponse(responseCode = "400", description = "Invalid ID supplied", content = @Content(mediaType = "String"))
    @ApiResponse(responseCode = "404", description = "Teacher is not found", content = @Content(mediaType = "String"))
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTeacher(@PathVariable Integer id) {
        Teacher teacher = teacherService.getById(id);
        teacherService.deleteTeacher(teacher.getId());
        accountService.disableAccount(teacher.getAccount().getId());
        return new ResponseEntity<>(new ErrorResponseDTO(TranslationCode.TEACHER, TranslationCode.DELETED), HttpStatus.BAD_REQUEST);
    }

}
