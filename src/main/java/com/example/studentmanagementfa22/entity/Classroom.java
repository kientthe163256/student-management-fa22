package com.example.studentmanagementfa22.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.OptBoolean;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Collection;
import java.util.Date;
import java.util.Set;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "classroom")
@Where(clause = "deleted = false")
public class Classroom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "classroom_name")
    @NotNull(message = "Classname can't be null")
    @Pattern(regexp = "[A-Z]{2}\\d{4}", message = "Classname must be 2 letters followed by 4 numbers!")
    private String classroomName;

    @Column(name = "current_no_student")
    private Integer currentNoStudent;

    @Column(name = "no_student")
    @NotNull(message = "Number of student can't be null")
    private Integer noStudent;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "class_type")
    @NotNull(message = "ClassType can't be null")
    private ClassType classType;

    @ManyToOne
    private Teacher teacher;

    @ManyToOne
    private Subject subject;

    private boolean deleted;

    @Column(name = "create_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", lenient = OptBoolean.FALSE, timezone= "Asia/Ho_Chi_Minh")
    private Date createDate;

    @Column(name = "modify_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", lenient = OptBoolean.FALSE, timezone= "Asia/Ho_Chi_Minh")
    private Date modifyDate;

    @Column(name = "delete_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", lenient = OptBoolean.FALSE, timezone= "Asia/Ho_Chi_Minh")
    private Date deleteDate;

    @ManyToMany(mappedBy = "classrooms")
    Collection<Student> students;
}