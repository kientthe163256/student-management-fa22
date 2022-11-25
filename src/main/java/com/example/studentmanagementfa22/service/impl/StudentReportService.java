package com.example.studentmanagementfa22.service.impl;

import com.example.studentmanagementfa22.entity.StudentReport;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class StudentReportService {
    //Get best/worst student report. Quality should be "highest" or "lowest"
    public static List<StudentReport> getTopReports(List<StudentReport> reports, String quality) {
        StudentReport maxOrMinReport = null;
        if (quality.equals("highest"))
            maxOrMinReport = Collections.max(reports);
        else if (quality.equals("lowest"))
            maxOrMinReport = Collections.min(reports);

        List<StudentReport> topReports = new ArrayList<>();
        for (StudentReport r : reports) {
            if (r.compareTo(maxOrMinReport) == 0) {
                topReports.add(r);
            }
        }
        return topReports;
    }

    public static List<StudentReport> getReportsInRange(List<StudentReport> reports, double min, double max) {
        return reports.stream().filter(r -> r.getAverageScore() >= min && r.getAverageScore() <= max).toList();
    }
}

