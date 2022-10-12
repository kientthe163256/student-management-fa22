package com.example.studentmanagementfa22.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class AccountDTO {
    private String username;
    private String firstName;
    private String lastName;
    private Date dob;
}
