package com.example.studentmanagementfa22.dto;

import lombok.Builder;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import java.util.Date;

@Data
@Builder
public class StudentDTO {
    private String username;

    private String password;

    private boolean enabled;

    private int roleId;

    private String firstName;

    private String lastName;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dob;

    private int academicSession;

    private int accountId;
}
