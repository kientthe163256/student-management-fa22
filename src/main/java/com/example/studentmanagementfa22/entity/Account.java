package com.example.studentmanagementfa22.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.*;
import java.util.Date;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "account")
public class Account {
    @Id
    private String username;

    @NotBlank(message = "Password cannot be blank")
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
    private Date dob;
}
