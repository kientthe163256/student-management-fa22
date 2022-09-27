package com.example.studentmanagementfa22.service;

import com.example.studentmanagementfa22.entity.Account;
import com.example.studentmanagementfa22.exception.UserAlreadyExistException;
import com.example.studentmanagementfa22.repository.AccountRepository;
import com.example.studentmanagementfa22.service.AccountService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
//@DataJpaTest
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class AccountServiceTest {
    @Autowired
    private AccountService accountService;

    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Test
    public void findByUsername(){
        Account account = accountService.findAccountByUsername("HE163256");
        assertThat(account).isNotNull();
    }

    @Test
    public void registerNewAccount(){
        Account account = Account.builder()
                .username("HE163256")
                .password(passwordEncoder.encode("12345"))
                .enabled(true)
                .firstName("Trung")
                .lastName("Kien")
                .roleId(3)
                .dob(new Date())
                .build();
        try{
            accountService.registerNewAccount(account);
        } catch (UserAlreadyExistException e) {
            System.out.println(e.getMessage());;
        }
    }

}
