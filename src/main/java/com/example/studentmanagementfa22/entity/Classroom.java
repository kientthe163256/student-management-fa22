package com.example.studentmanagementfa22.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "classroom")
public class Classroom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "classroom_name")
    private String classroomName;

    @Column(name = "current_no_student")
    private int currentNoStudent;

    @Column(name = "no_student")
    private int maxNoStudent;

    @Column(name = "class_type")
    private String classType;

    @Column(name = "teacher_id")
    private int teacherId;

    private boolean deleted;
}