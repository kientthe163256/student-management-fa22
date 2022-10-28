package com.example.studentmanagementfa22.controller.admin;

import com.example.studentmanagementfa22.dto.ClassroomDTO;
import com.example.studentmanagementfa22.entity.Classroom;
import com.example.studentmanagementfa22.service.ClassroomService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/admin/classrooms")
public class ClassroomManagementController {
    @Autowired
    private ClassroomService classroomService;

    @Operation(summary = "Add new classroom", description = "Add new classroom")
    @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(mediaType = "String"))
    @ApiResponse(responseCode = "400", description = "Invalid parameters supplied", content = @Content(mediaType = "String"))
    @PostMapping()
    public ResponseEntity<?> addNewClassroom(@Valid @RequestBody Classroom classroom){
        classroomService.addNewClassroom(classroom);
        return new ResponseEntity("Classroom added successfully", HttpStatus.CREATED);
    }

    @Operation(summary = "View classroom list", description = "Returns list of classroom dto")
    @ApiResponse(responseCode = "200", description = "Successful operation",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = ClassroomDTO.class))))
    @ApiResponse(responseCode = "400", description = "Invalid parameters supplied", content = @Content(mediaType = "String"))
    @GetMapping()
    public ResponseEntity<?> displayAllClassrooms(@RequestParam(required = false, defaultValue = "1") int pageNumber){
        if (pageNumber <= 0) {
            return new ResponseEntity<>("Invalid page number", HttpStatus.BAD_REQUEST);
        }
        Page<ClassroomDTO> classroomPage = classroomService.getAllClassroomsPaging(pageNumber);
        List<ClassroomDTO> classroomDTOList = classroomPage.getContent();
        return ResponseEntity.ok(classroomDTOList);
    }

    @Operation(summary = "Assign classroom", description = "Assign classroom to a teacher")
    @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(mediaType = "String"))
    @ApiResponse(responseCode = "400", description = "Invalid input format", content = @Content(mediaType = "String"))
    @ApiResponse(responseCode = "404", description = "Classroom or teacher is not found", content = @Content(mediaType = "String"))
    @PostMapping("/assign")
    public ResponseEntity<?> assignClassroom(@RequestParam Integer teacherId, @RequestParam Integer classId){
        classroomService.assignClassroom(teacherId, classId);
        return ResponseEntity.ok("Assign successfully!");
    }

    @Operation(summary = "Update classroom", description = "Change classroom name")
    @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(mediaType = "String"))
    @ApiResponse(responseCode = "400", description = "Invalid parameters supplied", content = @Content(mediaType = "String"))
    @ApiResponse(responseCode = "404", description = "Classroom is not found", content = @Content(mediaType = "String"))
    @PutMapping("/{id}")
    public ResponseEntity<?> updateClassroom(@RequestBody String className, @PathVariable Integer id){
        ClassroomDTO updatedClass = classroomService.updateClassroom(className, id);
        return new ResponseEntity(updatedClass, HttpStatus.OK);
    }

    @Operation(summary = "Delete classroom", description = "Delete classroom by classId")
    @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(mediaType = "String"))
    @ApiResponse(responseCode = "400", description = "Invalid parameters supplied", content = @Content(mediaType = "String"))
    @ApiResponse(responseCode = "404", description = "Classroom is not found", content = @Content(mediaType = "String"))
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteClassroom(@PathVariable Integer id){
        classroomService.deleteClassroom(id);
        return new ResponseEntity("Classroom deleted successfully", HttpStatus.OK);
    }
}