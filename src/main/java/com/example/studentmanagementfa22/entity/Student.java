package com.example.studentmanagementfa22.entity;

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
    @ManyToMany(fetch = FetchType.LAZY)
    private List<Classroom> classrooms;

    private boolean deleted;

    @Column(name = "create_date")
    private Date createDate;

    @Column(name = "modify_date")
    private Date modifyDate;

    @Column(name = "delete_date")
    private Date deleteDate;
}
