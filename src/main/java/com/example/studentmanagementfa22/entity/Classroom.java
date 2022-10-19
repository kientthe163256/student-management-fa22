package com.example.studentmanagementfa22.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
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
    @NotNull(message = "Classname can't be null")
    @Pattern(regexp = "[A-Z]{2}\\d{4}", message = "Classname must be 2 letter followed by 4 numbers!")
    private String classroomName;

    @Column(name = "current_no_student")
    private Integer currentNoStudent;

    @Column(name = "no_student")
    @NotNull(message = "Number of student can't be null")
    private Integer noStudent;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "class_type")
    @NotNull(message = "Classtype can't be null")
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