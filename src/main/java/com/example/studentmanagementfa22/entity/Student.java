package com.example.studentmanagementfa22.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@SQLDelete(sql = "UPDATE student SET deleted = true WHERE id=?")
public class Student {
    @Id
    private Integer id;

    private String first_name;
    private String last_name;
    private Date dob;
    private String student_code;
    private int academic_session;
    private int class_id;
    private boolean deleted;
}
