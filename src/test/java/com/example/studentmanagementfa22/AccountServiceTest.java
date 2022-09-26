package com.example.studentmanagementfa22;

import com.example.studentmanagementfa22.entity.Account;
import com.example.studentmanagementfa22.repository.AccountRepository;
import com.example.studentmanagementfa22.service.AccountService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
//@SpringBootTest
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class AccountServiceTest {
    @Autowired
    private AccountService accountService;
    @Autowired
    private AccountRepository accountRepository;
    @Test
    public void saveAccount(){
//        accountRepository.save(account);
        Account optionalAccount = accountRepository.findByUsername("HE163256");
        Assertions.assertNotNull(optionalAccount);
    }

    @Test
    public void findByUsername(){
        Account account = accountService.findAccountByUsername("HE163256");
        assertThat(account).isNotNull();
    }

    @Test
    public void getAllAccount(){
        List<Account> accounts = accountRepository.findAll();
        assertThat(accounts).isNotNull();
    }

    @Test
    public void loadUserDetails(){

    }
}
