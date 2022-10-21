package com.example.studentmanagementfa22.controller;

import com.example.studentmanagementfa22.dto.AccountDTO;
import com.example.studentmanagementfa22.entity.Account;
import com.example.studentmanagementfa22.service.AccountService;
import com.example.studentmanagementfa22.service.RoleService;
import com.example.studentmanagementfa22.service.StudentService;
import com.example.studentmanagementfa22.service.TeacherService;
import com.example.studentmanagementfa22.utility.AccountMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class AccountController {
    @Autowired
    private RoleService roleService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private StudentService studentService;

    @Autowired
    private TeacherService teacherService;

    @Autowired
    private AccountMapper accountMapper;


    @PostMapping("/register/student")
    public ResponseEntity<?> registerStudent(@Valid @RequestBody Account account) {
        accountService.registerNewAccount(account);
        studentService.addStudentWithNewAccount(account);

        return new ResponseEntity("Account created successfully", HttpStatus.CREATED);
    }

    @PostMapping("/admin/teacher")
    public ResponseEntity<?> addNewTeacher(@Valid @RequestBody Account account) {
        accountService.registerNewAccount(account);
        teacherService.addTeacherWithNewAccount(account);

        return new ResponseEntity<>("Teacher added successfully", HttpStatus.CREATED);
    }

    @GetMapping("/admin/account")
    public ResponseEntity<?> displayAllAccount(@RequestParam(required = false, defaultValue = "1") int pageNumber) {
        if (pageNumber <= 0) {
            return new ResponseEntity<>("Invalid page number", HttpStatus.BAD_REQUEST);
        }
        List<AccountDTO> accountList = accountService.getAccountDTOList(pageNumber);
        return ResponseEntity.ok(accountList);
    }

    @GetMapping("/admin/account/{id}")
    public ResponseEntity<?> getAccountById(@PathVariable Integer id) {
        AccountDTO accountDTO = accountService.getAccountDTOById(id);
        return ResponseEntity.ok(accountDTO);
    }

    @PutMapping("/admin/account/{id}")
    public ResponseEntity<?> updateAccountById(@PathVariable Integer id, @Valid @RequestBody AccountDTO accountDTO) {
        Account account = accountService.getById(id);
        accountService.updateAccount(accountDTO, account);

        Account editedAccount = accountService.getById(account.getId());
        AccountDTO editedAccountDTO = accountMapper.mapToDTO(editedAccount);
        return new ResponseEntity<>(editedAccountDTO, HttpStatus.OK);
    }

    @DeleteMapping("/admin/account/{id}")
    public ResponseEntity<?> disableAccount(@PathVariable Integer id){
        accountService.disableAccount(id);
        return ResponseEntity.ok("Account disabled successfully");
    }
}
