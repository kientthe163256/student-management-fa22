package com.example.studentmanagementfa22.service;

import com.example.studentmanagementfa22.entity.StudentReport;
import com.example.studentmanagementfa22.service.impl.StudentReportService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class StudentReportServiceTest {
    private List<StudentReport> createReportList(){
        StudentReport report1 = new StudentReport(1, 6.3);
        StudentReport report2 = new StudentReport(2, 6.4);
        StudentReport report3 = new StudentReport(7, 6.4);
        List<StudentReport> reports = List.of(report1, report2, report3);
        return reports;
    }

    @Test
    public void getTopReports(){
        List<StudentReport> reports = createReportList();
        List<StudentReport> highest = StudentReportService.getTopReports(reports, "highest");
        List<StudentReport> lowest = StudentReportService.getTopReports(reports, "lowest");

        Assertions.assertEquals(2, highest.size());
        Assertions.assertEquals(6.4, highest.get(0).getAverageScore());

        Assertions.assertEquals(1, lowest.size());
        Assertions.assertEquals(6.3, lowest.get(0).getAverageScore());
    }

    @Test
    public void getReportsInRange(){
        List<StudentReport> reports = createReportList();
        double min = 6.4, max = 7.4;
        List<StudentReport> reportInRange = StudentReportService.getReportsInRange(reports, min, max);

        Assertions.assertEquals(2, reportInRange.size());
        Assertions.assertEquals(6.4, reportInRange.get(0).getAverageScore());
    }
}
