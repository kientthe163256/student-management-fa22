package com.example.studentmanagementfa22.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "student")
@SQLDelete(sql = "UPDATE student SET deleted = true WHERE id=?")
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "academic_session")
    private int academicSession;

    @Column(name = "account_id")
    private int accountId;

    @Transient
    private List<Classroom> classroom;

    private boolean deleted;
}
