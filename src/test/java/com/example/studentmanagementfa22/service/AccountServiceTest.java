package com.example.studentmanagementfa22.service;

import com.example.studentmanagementfa22.dto.AccountDTO;
import com.example.studentmanagementfa22.entity.Account;
import com.example.studentmanagementfa22.exception.ElementAlreadyExistException;
import com.example.studentmanagementfa22.repository.AccountRepository;
import com.example.studentmanagementfa22.service.impl.AccountServiceImpl;
import com.example.studentmanagementfa22.utility.IGenericMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {
    @Mock
    private AccountRepository accountRepository;

    @Mock
    private IGenericMapper<Account, AccountDTO> mapper;

    @InjectMocks
    private AccountServiceImpl accountService;

    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Test
    public void whenUpdateValidAccount_thenSuccess() {
        String dateString = "2001-09-11";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date editedDate;
        try {
            editedDate = simpleDateFormat.parse(dateString);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        Account mockAccount = Account.builder()
                .username("HE163256")
                .password("12345")
                .enabled(true)
                .firstName("Trung")
                .lastName("Kien")
                .roleId(3)
                .dob(new Date())
                .build();
        AccountDTO editAccountDTO = AccountDTO.builder()
                .firstName("Trung2")
                .lastName("KienEdited")
                .dob(editedDate)
                .build();

        doAnswer(new Answer<Void>() {
            public Void answer(InvocationOnMock invocation) {
                mockAccount.setFirstName(editAccountDTO.getFirstName());
                mockAccount.setLastName(editAccountDTO.getLastName());
                mockAccount.setDob(editAccountDTO.getDob());
                return null;
            }
        }).when(accountRepository).save(mockAccount);

        Account expectedAccount = Account.builder()
                .username("HE163256")
                .password("12345")
                .enabled(true)
                .firstName("Trung2")
                .lastName("KienEdited")
                .roleId(3)
                .dob(editedDate)
                .build();

        accountService.updateAccount(editAccountDTO, mockAccount);
        assertEquals(expectedAccount, mockAccount);
    }


    @Test
    public void disableAccount(){
        Account account = Account.builder()
                .id(1)
                .username("HE163256")
                .password(passwordEncoder.encode("12345"))
                .enabled(true)
                .firstName("Trung")
                .lastName("Kien")
                .roleId(3)
                .dob(new Date())
                .build();
        doAnswer(new Answer<Void>() {
            public Void answer(InvocationOnMock invocation) {
                account.setEnabled(false);
                return null;
            }
        }).when(accountRepository).disableAccount(1);

        accountService.disableAccount(1);
        assertEquals(account.isEnabled(), false);
    }

    @Test
    public void findByUsername() {
        Account account = accountService.findAccountByUsername("HE163256");
        assertThat(account).isNotNull();
    }

    @Test
    public void registerNewAccount() {
        Account account = Account.builder()
                .username("HE163256")
                .password(passwordEncoder.encode("12345"))
                .enabled(true)
                .firstName("Trung")
                .lastName("Kien")
                .roleId(3)
                .dob(new Date())
                .build();
        try {
            accountService.registerNewAccount(account);
        } catch (ElementAlreadyExistException e) {
            System.out.println(e.getMessage());
            ;
        }
    }

    @Test
    public void findAll() {
        Page<Account> accountPage = accountService.findAllAccount(1);
        assertThat(accountPage).isNotNull();
    }

}
