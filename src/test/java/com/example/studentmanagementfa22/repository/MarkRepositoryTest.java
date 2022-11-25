package com.example.studentmanagementfa22.repository;

import com.example.studentmanagementfa22.entity.StudentReport;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
public class MarkRepositoryTest {
    @Autowired
    private MarkRepository markRepository;

    @Autowired
    private MarkTypeRepository markTypeRepository;

    @Autowired
    private StudentReportRepository reportRepository;

    @Test
    public void getStudentReport(){
        List<StudentReport> report = reportRepository.getReportsByClassId(1);
    }

    @Test
    public void testComparable(){
        Assertions.assertEquals(1, Double.compare(2.3, 2.09));
        Assertions.assertEquals(-1, Double.compare(2.3, 2.31));
        Assertions.assertEquals(0, Double.compare(2.3, 2.30));
    }

    @Test
    public void compareStudentReports(){
        StudentReport report1 = new StudentReport(1, 6.3);
        StudentReport report2 = new StudentReport(1, 6.4);
        int result = report1.compareTo(report2);
        Assertions.assertEquals(-1, result);
    }

    @Test
    public void getMaxOfReportList(){
        StudentReport report1 = new StudentReport(1, 6.3);
        StudentReport report2 = new StudentReport(2, 6.4);
        StudentReport report3 = new StudentReport(7, 6.4);
        List<StudentReport> reports = List.of(report1, report2, report3);

        StudentReport highest = Collections.max(reports);

        Assertions.assertEquals(report2, highest);
    }
}