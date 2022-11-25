package com.example.studentmanagementfa22.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StudentReport implements Comparable<StudentReport>{
    @Id
    @Column(name = "student_id")
    private int studentId;

    @Column(name = "average_score")
    private double averageScore;

    @Override
    public int compareTo(StudentReport o) {
        return Double.compare(averageScore, o.getAverageScore());
    }
}
