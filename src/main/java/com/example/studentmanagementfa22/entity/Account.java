package com.example.studentmanagementfa22.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

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
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "Username cannot be blank")
    @Pattern(regexp = "[A-Z]{2}\\d{6}")
    private String username;

    @NotBlank(message = "Password cannot be blank")
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*\\d)(?=.*[^\\da-zA-Z]).{8,}$"
            , message = "Password must be at least 8, contains 1 capital, 1 special letter!")
    private String password;

    private boolean enabled;

    @Column(name = "role_id")
    private int roleId;

    @Column(name = "first_name")
    @NotBlank(message = "Firstname cannot be blank")
    private String firstName;

    @Column(name = "last_name")
    @NotBlank(message = "Lastname cannot be blank")
    private String lastName;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull
    private Date dob;

    @Column(name = "create_date")
    private Date createDate;

    @Column(name = "modify_date")
    private Date modifyDate;

    @Column(name = "delete_date")
    private Date deleteDate;
}
