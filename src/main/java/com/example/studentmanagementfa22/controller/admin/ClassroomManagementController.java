package com.example.studentmanagementfa22.controller.admin;

import com.example.studentmanagementfa22.dto.ClassroomDTO;
import com.example.studentmanagementfa22.dto.ErrorResponseDTO;
import com.example.studentmanagementfa22.entity.Classroom;
import com.example.studentmanagementfa22.entity.Pagination;
import com.example.studentmanagementfa22.service.ClassroomService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

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
//        return new ResponseEntity(new ErrorResponseDTO<>("Classroom added successfully", 201), HttpStatus.CREATED);
        return new ResponseEntity<>(new ErrorResponseDTO(null, "400"), HttpStatus.BAD_REQUEST);
    }

    @Operation(summary = "View classroom list", description = "Returns list of classroom dto")
    @ApiResponse(responseCode = "200", description = "Successful operation",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = ClassroomDTO.class))))
    @ApiResponse(responseCode = "400", description = "Invalid parameters supplied", content = @Content(mediaType = "String"))
    @GetMapping()
    public ResponseEntity<?> displayAllClassrooms(
            @RequestParam(required = false, defaultValue = "1") int pageNumber,
            @RequestParam(required = false, defaultValue = "5") int pageSize,
            @RequestParam(required = false, defaultValue = "id,ASC") String sort){
        if (pageNumber <= 0) {
//            return new ResponseEntity<>(new ErrorResponseDTO<>("Invalid page number", 400), HttpStatus.BAD_REQUEST);
            return new ResponseEntity<>(new ErrorResponseDTO(null, "400"), HttpStatus.BAD_REQUEST);
        }
        Pagination<ClassroomDTO> classroomPage = classroomService.getAllClassroomsPaging(pageNumber, pageSize, sort);
        return ResponseEntity.ok(classroomPage);
    }

    @Operation(summary = "Get classroom", description = "Get classroom by id")
    @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(mediaType = "String"))
    @ApiResponse(responseCode = "400", description = "Invalid parameters supplied", content = @Content(mediaType = "String"))
    @ApiResponse(responseCode = "404", description = "Classroom is not found", content = @Content(mediaType = "String"))
    @GetMapping("/{id}")
    public ResponseEntity<?> getClassroomById(@PathVariable Integer id){
        ClassroomDTO classroomDTO = classroomService.getClassDTOById(id);
        return new ResponseEntity(classroomDTO, HttpStatus.OK);
    }

    @Operation(summary = "Assign classroom", description = "Assign classroom to a teacher")
    @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(mediaType = "String"))
    @ApiResponse(responseCode = "400", description = "Invalid input format", content = @Content(mediaType = "String"))
    @ApiResponse(responseCode = "404", description = "Classroom or teacher is not found", content = @Content(mediaType = "String"))
    @PutMapping("/assign/{id}")
    public ResponseEntity<?> assignClassroom(@RequestBody Integer teacherId, @PathVariable Integer id){
        ClassroomDTO savedClass = classroomService.assignClassroom(teacherId, id);
        return ResponseEntity.ok(savedClass);
    }

    @Operation(summary = "Update classroom", description = "Change classroom name")
    @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(mediaType = "String"))
    @ApiResponse(responseCode = "400", description = "Invalid parameters supplied", content = @Content(mediaType = "String"))
    @ApiResponse(responseCode = "404", description = "Classroom is not found", content = @Content(mediaType = "String"))
    @PutMapping("/{id}")
    public ResponseEntity<?> updateClassroom(@RequestBody Classroom classroom, @PathVariable Integer id){
        ClassroomDTO updatedClass = classroomService.updateClassroom(classroom, id);
        return new ResponseEntity(updatedClass, HttpStatus.OK);
    }

    @Operation(summary = "Delete classroom", description = "Delete classroom by classId")
    @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(mediaType = "String"))
    @ApiResponse(responseCode = "400", description = "Invalid parameters supplied", content = @Content(mediaType = "String"))
    @ApiResponse(responseCode = "404", description = "Classroom is not found", content = @Content(mediaType = "String"))
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteClassroom(@PathVariable Integer id){
        classroomService.deleteClassroom(id);
//        return new ResponseEntity(new ErrorResponseDTO<>("Classroom deleted successfully", 200), HttpStatus.OK);
        return new ResponseEntity<>(new ErrorResponseDTO(null, "400"), HttpStatus.BAD_REQUEST);
    }
}