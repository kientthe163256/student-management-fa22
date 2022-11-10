package com.example.studentmanagementfa22.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ScoreReportDTO {
    private int total;
    private double highest;
    private double lowest;
    private double average;
    private double excellent;
    private double good;
    private double fair;
    private double poor;
}
