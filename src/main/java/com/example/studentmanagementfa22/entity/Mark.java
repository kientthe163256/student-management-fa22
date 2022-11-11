package com.example.studentmanagementfa22.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.OptBoolean;
import lombok.*;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.Date;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "mark")
@Where(clause = "deleted = false")
public class Mark {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne
    private  Subject subject;

    @OneToOne
    private  Student student;

    @OneToOne
    private  MarkType markType;

    @Column(name = "grade")
    @Min(value = 0, message = "Minimum grade is 0")
    @Max(value = 10, message = "Maximum grade is 10")
    private double grade;

    private boolean deleted;

    @Column(name = "create_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", lenient = OptBoolean.FALSE, timezone= "Asia/Ho_Chi_Minh")
    private Date createDate;

    @Column(name = "modify_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", lenient = OptBoolean.FALSE, timezone= "Asia/Ho_Chi_Minh")
    private Date modifyDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", lenient = OptBoolean.FALSE, timezone= "Asia/Ho_Chi_Minh")
    @Column(name = "delete_date")
    private Date deleteDate;
}
