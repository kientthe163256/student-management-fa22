package com.example.studentmanagementfa22.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "mark")
public class Mark {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull(message = "Mark ID can not be null")
    private Integer id;

    @Column(name = "subject_id")
    private int subjectId;

    @Column(name = "student_id")
    private int studentId;

    @Column(name ="mark_item")
    private String markItem;

    @Column(name = "grade")
    @Min(value = 0, message = "Minimum grade is 0")
    @Max(value = 10, message = "Maximum grade is 10")
    private double grade;

    @Column(name = "weight")
    private double weight;

    private boolean deleted;

    @Column(name = "create_date")
    private Date createDate;

    @Column(name = "modify_date")
    private Date modifyDate;

    @Column(name = "delete_date")
    private Date deleteDate;
}
