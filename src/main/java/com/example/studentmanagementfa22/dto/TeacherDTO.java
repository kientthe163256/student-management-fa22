package com.example.studentmanagementfa22.dto;

import com.example.studentmanagementfa22.entity.Account;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TeacherDTO {
    private Integer id;
    private AccountDTO account;
}
