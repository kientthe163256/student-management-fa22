package com.example.studentmanagementfa22.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentDTO {
    private Integer id;

    private String username;

    private String password;

    private boolean enabled;

    private int roleId;

    @NotNull(message ="First Name can not be blank")
    @Size(min=1, max = 10, message = "First name must be between 1 or 10 characters")
    private String firstName;

    @NotNull(message ="Last name can not be blank")
    @Size(min=1, max = 10, message = "Last name must be between 1 or 10 characters")
    private String lastName;

    @NotNull(message = "Date of birth can not be blank")
    @Past(message = "Date of birth must be in the past")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dob;

    private int academicSession;

    private int accountId;
}
