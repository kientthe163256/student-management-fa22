package com.example.studentmanagementfa22.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MarkReportDTO {
    private int total;          //total records
    private double highest;
    private double lowest;
    private double average;
    private int excellent;   // >=9
    private int good;        // >=8
    private int fair;        // >=5
    private int poor;        // <5
}
