package com.example.studentmanagementfa22.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.OptBoolean;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "student")
@SQLDelete(sql = "UPDATE student SET deleted = true WHERE id=?")
@Where(clause = "deleted = false")
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "academic_session")
    private int academicSession;

    @Column(name = "account_id")
    private int accountId;

    @Transient
    private List<Classroom> classrooms;

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
}
