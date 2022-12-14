package com.example.studentmanagementfa22.controller.admin;

import com.example.studentmanagementfa22.dto.ErrorResponseDTO;
import com.example.studentmanagementfa22.dto.SubjectDTO;
import com.example.studentmanagementfa22.repository.SubjectRepository;
import com.example.studentmanagementfa22.service.SubjectService;
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
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin/subjects")
public class SubjectController {
    @Autowired
    private SubjectService subjectService;

    @Autowired
    private SubjectRepository subjectRepository;

    @Operation(summary = "View subject list", description = "Returns list of subject dto")
    @ApiResponse(responseCode = "200", description = "Successful operation",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = SubjectDTO.class))))
    @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content(mediaType = "String"))
    @GetMapping()
    public List<SubjectDTO> displaySubjectList(@RequestParam(required = false, defaultValue = "1") int pageNumber){
        return subjectService.getSubjectDTOList(pageNumber);
    }

    @Operation(summary = "Get subject by ID", description = "Get by id then returns subject dto")
    @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(schema = @Schema(implementation = SubjectDTO.class)))
    @ApiResponse(responseCode = "400", description = "Invalid ID supplied", content = @Content(mediaType = "String"))
    @ApiResponse(responseCode = "404", description = "Subject is not found", content = @Content(mediaType = "String"))
    @GetMapping("/{id}")
    public SubjectDTO getSubjectById(@PathVariable Integer id) {
        return subjectService.getSubjectDTOByID(id);
    }

    @Operation(summary = "Add new subject", description = "Add new subject")
    @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(mediaType = "String"))
    @ApiResponse(responseCode = "400", description = "Invalid parameters supplied", content = @Content(mediaType = "String"))
    @PostMapping()
    public ResponseEntity<?> addNewSubject(@Valid @RequestBody SubjectDTO subjectDTO){
        SubjectDTO addedSubject = subjectService.addNewSubject(subjectDTO);
//        return new ResponseEntity<>(new ErrorResponseDTO<>("Subject added successfully",201), HttpStatus.CREATED);
        return new ResponseEntity<>(addedSubject, HttpStatus.CREATED);
    }

    @Operation(summary = "Delete subject", description = "Delete subject by id")
    @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(mediaType = "String"))
    @ApiResponse(responseCode = "400", description = "Invalid ID supplied", content = @Content(mediaType = "String"))
    @ApiResponse(responseCode = "404", description = "Subject is not found", content = @Content(mediaType = "String"))
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSubject(@PathVariable Integer id){
        subjectService.deleteSubject(id);
//        return ResponseEntity.ok(new ErrorResponseDTO<>("Subject deleted successfully", 200));
        return new ResponseEntity<>(new ErrorResponseDTO(null, "400"), HttpStatus.BAD_REQUEST);
    }

    @Operation(summary = "Update subject", description = "Update subject by id")
    @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(mediaType = "String"))
    @ApiResponse(responseCode = "400", description = "Invalid parameters supplied", content = @Content(mediaType = "String"))
    @ApiResponse(responseCode = "404", description = "Subject is not found", content = @Content(mediaType = "String"))
    @PutMapping("/{id}")
    public ResponseEntity<?> updateSubject(@Valid @RequestBody SubjectDTO subject, @PathVariable Integer id){
        subject.setId(id);
        SubjectDTO editedSubject = subjectService.updateSubject(subject);
        return ResponseEntity.ok(editedSubject);
    }

    @PostMapping("/{subjectId}")
    public ResponseEntity<?> addAssessmentOfSubject(@PathVariable Integer subjectId, @RequestBody Map<String, Integer> params){
        int markTypeId = params.get("markTypeId");
        int noMarks = params.get("noMarks");
        return ResponseEntity.ok(subjectService.addMarkTypeToSubject(subjectId, markTypeId, noMarks));
    }
}