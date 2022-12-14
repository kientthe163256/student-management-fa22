package com.example.studentmanagementfa22.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.OptBoolean;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Date;


@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "account")
@Where(clause = "deleted = false")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "Username cannot be blank")
    @Pattern(regexp = "[A-Z]{2}\\d{6}")
    private String username;

    @NotBlank(message = "Password cannot be blank")
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*\\d)(?=.*[^\\da-zA-Z]).{8,}$"
            , message = "Password must be at least 8 characters, contains at least 1 number, 1 capital, 1 special letter!")
    private String password;

    private boolean enabled;

    private boolean deleted;

    @Column(name = "role_id")
    private int roleId;

    @Column(name = "first_name")
    @NotBlank(message = "Firstname cannot be blank")
    private String firstName;

    @Column(name = "last_name")
    @NotBlank(message = "Lastname cannot be blank")
    private String lastName;


    @NotNull(message = "Dob can not be empty")
    private Date dob;

    @Column(name = "create_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", lenient = OptBoolean.FALSE, timezone= "Asia/Ho_Chi_Minh")

    private Date createDate;

    @Column(name = "modify_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", lenient = OptBoolean.FALSE, timezone= "Asia/Ho_Chi_Minh")
    private Date modifyDate;

    @Column(name = "delete_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", lenient = OptBoolean.FALSE, timezone= "Asia/Ho_Chi_Minh")
    private Date deleteDate;

    @Column(name = "disable_date")
    private Date disableDate;
}
