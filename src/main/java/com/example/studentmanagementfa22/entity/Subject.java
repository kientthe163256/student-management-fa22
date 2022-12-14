package com.example.studentmanagementfa22.entity;
import com.fasterxml.jackson.annotation.JsonIgnore;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.OptBoolean;
import lombok.*;
import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Collection;
import java.util.Date;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "subject")
@Where(clause = "deleted = false")
public class Subject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "subject_name")
    private String subjectName;

    @Column(name = "no_credit")
    private int numberOfCredit;

    private boolean deleted;

    @Column(name = "create_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", lenient = OptBoolean.FALSE, timezone = "Asia/Ho_Chi_Minh")
    private Date createDate;

    @Column(name = "modify_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", lenient = OptBoolean.FALSE, timezone = "Asia/Ho_Chi_Minh")
    private Date modifyDate;

    @Column(name = "delete_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", lenient = OptBoolean.FALSE, timezone = "Asia/Ho_Chi_Minh")
    private Date deleteDate;

    @JsonIgnore
    @OneToMany(mappedBy = "subject")//, fetch = FetchType.EAGER)
    @JsonManagedReference
    Collection<Classroom> classrooms;

    @JsonIgnore
    @ManyToMany
    @JoinTable(name = "subject_mark_type",
            joinColumns = @JoinColumn(name = "subject_id"),
            inverseJoinColumns = @JoinColumn(name = "mark_type_id"))
    @JsonManagedReference
    private Collection<MarkType> markTypes;
}
