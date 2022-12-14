package com.example.studentmanagementfa22.repository;

import com.example.studentmanagementfa22.entity.Account;
import com.example.studentmanagementfa22.repository.AccountRepository;
import com.example.studentmanagementfa22.service.impl.AccountServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DemoMockito {
    @Mock
    AccountRepository accountRepository;

    @InjectMocks
    AccountServiceImpl accountService;

    @Test
    public void testLogin() {
        // 1. create mock data
        Account mockAccount = Account.builder()
                .username("HE16356")
                .password("12345")
                .enabled(true)
                .roleId(3)
                .build();

        // 2. define behavior of Repository
        when(accountRepository.getByUsername("HE163256")).thenReturn(mockAccount);

        // 3. call service method
        Account actualAccount = accountService.findAccountByUsername("HE163256");

        // 4. assert the result
        assertThat(actualAccount).isEqualTo(mockAccount);

        // 4.1 ensure repository is called
        verify(accountRepository).getByUsername("HE163256");
    }
}