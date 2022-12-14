package com.example.studentmanagementfa22.controller.admin;

import com.example.studentmanagementfa22.dto.AccountDTO;
import com.example.studentmanagementfa22.dto.ErrorResponseDTO;
import com.example.studentmanagementfa22.dto.TeacherDTO;
import com.example.studentmanagementfa22.entity.Account;
import com.example.studentmanagementfa22.service.AccountService;
import com.example.studentmanagementfa22.service.TeacherService;
import com.example.studentmanagementfa22.utility.i18n.MessageCode;
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

@RequestMapping("/admin/accounts")
@RestController
public class AccountManagementController {
    @Autowired
    private AccountService accountService;

    @Autowired
    private TeacherService teacherService;


    @Operation(summary = "Register teacher account", description = "Admin add a new teacher account")
    @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(mediaType = "String"))
    @ApiResponse(responseCode = "400", description = "Invalid parameters supplied", content = @Content(mediaType = "String"))
    @PostMapping("/teacher")
    public ResponseEntity<?> addNewTeacher(@Valid @RequestBody Account account) {
        accountService.registerNewAccount(account, "ROLE_TEACHER");
        TeacherDTO registeredTeacher = teacherService.addTeacherWithNewAccount(account);
        return new ResponseEntity<>(registeredTeacher, HttpStatus.CREATED);
    }

    @Operation(summary = "View account list", description = "Returns list of account dto")
    @ApiResponse(responseCode = "200", description = "Successful operation",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = AccountDTO.class))))
    @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content(mediaType = "String"))
    @GetMapping()
    public ResponseEntity<?> displayAllAccount(@RequestParam(required = false, defaultValue = "1") int pageNumber) {
        if (pageNumber <= 0) {
//            return new ResponseEntity<>(new ErrorResponseDTO<>("Invalid page number", 400), HttpStatus.BAD_REQUEST);
            return new ResponseEntity<>(new ErrorResponseDTO(MessageCode.PAGE400), HttpStatus.BAD_REQUEST);
        }
        List<AccountDTO> accountList = accountService.getAccountDTOList(pageNumber);
        return ResponseEntity.ok(accountList);
    }

    @Operation(summary = "Get account by ID", description = "Get by id then returns account dto")
    @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(schema = @Schema(implementation = AccountDTO.class)))
    @ApiResponse(responseCode = "400", description = "Invalid ID supplied", content = @Content(mediaType = "String"))
    @ApiResponse(responseCode = "404", description = "Account is not found", content = @Content(mediaType = "String"))
    @GetMapping("/{id}")
    public ResponseEntity<?> getAccountById(@PathVariable Integer id) {
        AccountDTO accountDTO = accountService.getAccountDTOById(id);
        return ResponseEntity.ok(accountDTO);
    }

    @Operation(summary = "Update account", description = "Update account")
    @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(schema = @Schema(implementation = AccountDTO.class)))
    @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content(mediaType = "String"))
    @ApiResponse(responseCode = "404", description = "Account is not found", content = @Content(mediaType = "String"))
    @PutMapping("/{id}")
    public ResponseEntity<?> updateAccountById(@PathVariable Integer id, @Valid @RequestBody AccountDTO accountDTO) {
        accountDTO.setId(id);
        AccountDTO editedAccount = accountService.updateAccount(accountDTO);
        return new ResponseEntity<>(editedAccount, HttpStatus.OK);
    }

    @Operation(summary = "Disable account", description = "Disable account")
    @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(mediaType = "String"))
    @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content(mediaType = "String"))
    @ApiResponse(responseCode = "404", description = "Account is not found", content = @Content(mediaType = "String"))
    @DeleteMapping("/{id}")
    public ResponseEntity<?> disableAccount(@PathVariable Integer id){
        accountService.disableAccount(id);
        return new ResponseEntity<>(new ErrorResponseDTO(MessageCode.ACCOUNT, MessageCode.DELETED), HttpStatus.BAD_REQUEST);
    }
}


