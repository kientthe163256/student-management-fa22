package com.example.studentmanagementfa22.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "classroom")
public class Classroom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "classroom_name")
    private String classroomName;

    @Column(name = "current_no_student")
    private Integer currentNoStudent;

    @Column(name = "no_student")
    private Integer noStudent;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "class_type")
    private ClassType classType;

    @Column(name = "teacher_id")
    private Integer teacherId;

    @Column(name = "subject_id")
    private Integer subjectId;

    private boolean deleted;

    @Column(name = "create_date")
    private Date createDate;

    @Column(name = "modify_date")
    private Date modifyDate;

    @Column(name = "delete_date")
    private Date deleteDate;
}