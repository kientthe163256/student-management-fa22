package com.example.studentmanagementfa22.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SubjectDTO {
    private Integer id;

    @NotBlank(message = "Subject name can not be blank")
    private String subjectName;

    @NotNull(message = "Number of credits can not be empty")
    @Min(value = 1, message = "Number of credits must be greater than 0")
    private Integer numberOfCredit;
}
