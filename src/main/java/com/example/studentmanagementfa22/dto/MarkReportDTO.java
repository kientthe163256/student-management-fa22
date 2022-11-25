package com.example.studentmanagementfa22.dto;

import com.example.studentmanagementfa22.entity.StudentReport;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MarkReportDTO {
    private int total;          //total records
    private List<StudentReport> highest;
    private List<StudentReport> lowest;

    private List<StudentReport> excellent;   // >=9
    private List<StudentReport> good;        // >=8
    private List<StudentReport> fair;        // >=5
    private List<StudentReport> failed;        // <5
}
