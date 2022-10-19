package com.example.studentmanagementfa22.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SubjectDTO {
    private Integer id;

    private String subjectName;

    private int numberOfCredit;
}
