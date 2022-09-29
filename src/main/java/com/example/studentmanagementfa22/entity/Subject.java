package com.example.studentmanagementfa22.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;

import javax.persistence.*;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "subject")
@SQLDelete(sql = "UPDATE subject SET deleted = true WHERE id=?")
public class Subject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "subject_name")
    private String subjectName;

    @Column(name = "no_credit")
    private int numberOfCredit;

    private boolean deleted;
}
