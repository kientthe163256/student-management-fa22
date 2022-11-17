package com.example.studentmanagementfa22.service;

import com.example.studentmanagementfa22.dto.AccountDTO;
import com.example.studentmanagementfa22.entity.Account;
import com.example.studentmanagementfa22.entity.Role;
import com.example.studentmanagementfa22.exception.customExceptions.ElementAlreadyExistException;
import com.example.studentmanagementfa22.repository.AccountRepository;
import com.example.studentmanagementfa22.service.impl.AccountServiceImpl;
import com.example.studentmanagementfa22.utility.AccountMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {
    @Mock
    private AccountRepository accountRepository;

    @Mock
    private RoleService roleService;

    @Mock
    private AccountMapper mapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AccountServiceImpl accountService;

    private final PasswordEncoder myEncoder = new BCryptPasswordEncoder();
    //constants
    private static final int EXIST_ID = 1;
    private static final String EXIST_USERNAME = "HE163256";
    private static final String ROLE_STUDENT = "ROLE_STUDENT";
    private static final String NEW_USERNAME = "HE169999";
    private static final String PASSWORD = "ValidPass1!";
    private Account createMockAccount(){
        Account mockAccount = Account.builder()
                .id(EXIST_ID)
                .username(EXIST_USERNAME)
                .password(myEncoder.encode(PASSWORD))
                .enabled(true)
                .firstName("Trung")
                .lastName("Kien")
                .roleId(3)
                .dob(new Date())
                .build();
        return mockAccount;
    }

    private Account save(AccountDTO accountDTO, Account account){
        account.setFirstName(accountDTO.getFirstName());
        account.setLastName(accountDTO.getLastName());
        account.setDob(accountDTO.getDob());
        return account;
    }

    private AccountDTO mapToAccountDTO(Account account){
        return AccountDTO.builder()
                .id(account.getId())
                .username(account.getUsername())
                .firstName(account.getFirstName())
                .lastName(account.getLastName())
                .dob(account.getDob())
                .build();
    }

    @Test
    public void whenUpdateValidAccountThenSuccess() {
        Date editedDob = new Date(1000*60);
        String editedFirstName = "Minh";
        String editedLastName = "Duc";

        Account mockAccount = createMockAccount();
        AccountDTO editAccountDTO = AccountDTO.builder()
                .id(EXIST_ID)
                .firstName(editedFirstName)
                .lastName(editedLastName)
                .dob(editedDob)
                .build();

        when(accountRepository.findById(EXIST_ID)).thenReturn(Optional.of(mockAccount));
        when(accountRepository.save(mockAccount)).thenReturn(save(editAccountDTO, mockAccount));
        when(mapper.mapToDTO(mockAccount)).thenReturn(mapToAccountDTO(mockAccount));

        accountService.updateAccount(editAccountDTO);
        assertEquals(editedFirstName, mockAccount.getFirstName());
        assertEquals(editedLastName, mockAccount.getLastName());
        assertEquals(editedDob, mockAccount.getDob());

        verify(accountRepository, times(1)).save(mockAccount);
    }


    @Test
    public void disableAccount(){
        Account account = createMockAccount();
        when(accountRepository.findById(EXIST_ID)).thenReturn(Optional.of(account));
        Date disableDate = new Date();
        doAnswer((Answer<Void>) invocation -> {
            account.setEnabled(false);
            account.setDisableDate(disableDate);
            return null;
        }).when(accountRepository).save(account);

        accountService.disableAccount(EXIST_ID);
        assertFalse(account.isEnabled());
        assertEquals(disableDate, account.getDisableDate());
    }

    @Test
    public void getAccountByValidId() {
        AccountDTO mockAccountDTO = AccountDTO.builder()
                .username("HE163256")
                .dob(new Date(1000))
                .firstName("Trung")
                .lastName("Kien")
                .build();
        Account mockAccount = Account.builder()
                .username("HE163256")
                .dob(new Date(1000))
                .firstName("Trung")
                .lastName("Kien")
                .build();

        when(accountRepository.findById(1)).thenReturn(Optional.of(mockAccount));
        when(mapper.mapToDTO(mockAccount)).thenReturn(mockAccountDTO);

        AccountDTO actualAccount = accountService.getAccountDTOById(1);
        assertEquals(mockAccountDTO.getUsername(), actualAccount.getUsername());
    }

    @Test
    public void getAccountByNonExistId() {
        Optional<Account> optionalAccount = Optional.empty();
        when(accountRepository.findById(100)).thenReturn(optionalAccount);

        assertThrows(NoSuchElementException.class, () -> {accountService.getAccountDTOById(100);});
    }

    @Test
    public void registerNewAccount() {
        Date dob = new Date(1000*60);
        Date today = new Date();
        Account account = Account.builder()
                .username(NEW_USERNAME)
                .password(PASSWORD)
                .firstName("Trung")
                .lastName("Kien")
                .dob(dob)
                .build();

        when(accountRepository.findByUsername(NEW_USERNAME)).thenReturn(null);
        Role role = Role.builder().id(3).roleName(ROLE_STUDENT).build();
        when(roleService.findByRoleName(ROLE_STUDENT)).thenReturn(role);
        when(passwordEncoder.encode(PASSWORD)).thenReturn(myEncoder.encode(PASSWORD));
        when(accountRepository.save(account)).thenReturn(account);
        when(mapper.mapToDTO(account)).thenReturn(mapToAccountDTO(account));
        AccountDTO registeredAccount = accountService.registerNewAccount(account, ROLE_STUDENT);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String todayString = dateFormat.format(today);
        String createDateString = dateFormat.format(account.getCreateDate());
        assertEquals(todayString, createDateString);
        assertEquals(NEW_USERNAME, registeredAccount.getUsername());

        verify(accountRepository, times(1)).save(account);
    }

    @Test
    public void registerAccountWithExistUsername(){
        Account account = Account.builder().username(EXIST_USERNAME).build();
        when(accountRepository.findByUsername(EXIST_USERNAME)).thenReturn(createMockAccount());
        assertThrows(ElementAlreadyExistException.class, () -> accountService.registerNewAccount(account, ROLE_STUDENT));

        verify(accountRepository, times(1)).findByUsername(EXIST_USERNAME);
    }



}
