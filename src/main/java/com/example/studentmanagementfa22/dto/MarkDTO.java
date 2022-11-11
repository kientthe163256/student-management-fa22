package com.example.studentmanagementfa22.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MarkDTO {
    private Integer id;

    private double grade;

    private MarkTypeDTO markType;

    private StudentDTO student;

}
