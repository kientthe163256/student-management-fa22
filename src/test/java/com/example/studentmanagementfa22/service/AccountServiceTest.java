package com.example.studentmanagementfa22.service;

import com.example.studentmanagementfa22.dto.AccountDTO;
import com.example.studentmanagementfa22.dto.StudentDTO;
import com.example.studentmanagementfa22.entity.Account;
import com.example.studentmanagementfa22.entity.Role;
import com.example.studentmanagementfa22.exception.customExceptions.ElementAlreadyExistException;
import com.example.studentmanagementfa22.repository.AccountRepository;
import com.example.studentmanagementfa22.service.impl.AccountServiceImpl;
import com.example.studentmanagementfa22.utility.mapper.AccountMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
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

    private Account createMockAccount() {
        return Account.builder()
                .id(EXIST_ID)
                .username(EXIST_USERNAME)
                .password(myEncoder.encode(PASSWORD))
                .enabled(true)
                .firstName("Trung")
                .lastName("Kien")
                .roleId(3)
                .dob(new Date())
                .build();
    }

    private Account createDisabledAccount() {
        return Account.builder()
                .enabled(false)
                .disableDate(new Date())
                .build();
    }

    private Account save(AccountDTO accountDTO, Account account) {
        account.setFirstName(accountDTO.getFirstName());
        account.setLastName(accountDTO.getLastName());
        account.setDob(accountDTO.getDob());
        return account;
    }

    private AccountDTO mapToAccountDTO(Account account) {
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
        Date editedDob = new Date(1000 * 60);
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
    public void disableAccount() {
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

        assertThrows(NoSuchElementException.class, () -> {
            accountService.getAccountDTOById(100);
        });
    }

    private Role getRoleStudent() {
        return Role.builder().id(3).roleName(ROLE_STUDENT).build();
    }

    @Test
    public void registerNewAccount() {
        Date dob = new Date(1000 * 60);
        Date today = new Date();
        Account account = Account.builder()
                .username(NEW_USERNAME)
                .password(PASSWORD)
                .firstName("Trung")
                .lastName("Kien")
                .dob(dob)
                .build();

        when(accountRepository.getByUsername(NEW_USERNAME)).thenReturn(null);
        Role role = getRoleStudent();
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
    public void registerAccountWithExistUsername() {
        Account account = Account.builder().username(EXIST_USERNAME).build();
        when(accountRepository.getByUsername(EXIST_USERNAME)).thenReturn(createMockAccount());
        assertThrows(ElementAlreadyExistException.class, () -> accountService.registerNewAccount(account, ROLE_STUDENT));

        verify(accountRepository, times(1)).getByUsername(EXIST_USERNAME);
    }

    @Test
    public void loadByUsername() {
        Account account = createMockAccount();
        when(accountRepository.getByUsername(account.getUsername())).thenReturn(account);
        Role roleStudent = getRoleStudent();
        when(roleService.findRoleById(account.getRoleId())).thenReturn(roleStudent);

        UserDetails userDetails = accountService.loadUserByUsername(account.getUsername());

        assertEquals(account.getUsername(), userDetails.getUsername());
        assertEquals(account.getPassword(), userDetails.getPassword());
        assertTrue(userDetails.getAuthorities().contains(new SimpleGrantedAuthority(roleStudent.getRoleName())));
        verify(accountRepository, times(1)).getByUsername(account.getUsername());
    }

    @Test
    public void loadByUsernameNotExist() {
        when(accountRepository.getByUsername(NEW_USERNAME)).thenReturn(null);
        assertThrows(UsernameNotFoundException.class, () -> accountService.loadUserByUsername(NEW_USERNAME));
        verify(accountRepository, times(1)).getByUsername(NEW_USERNAME);
    }

    @Test
    public void loadByUsernameOfDisabledAccount() {
        Account disabledAccount = createDisabledAccount();
        when(accountRepository.getByUsername(disabledAccount.getUsername())).thenReturn(disabledAccount);
        assertThrows(DisabledException.class, () -> accountService.loadUserByUsername(disabledAccount.getUsername()));
        verify(accountRepository, times(1)).getByUsername(disabledAccount.getUsername());
    }

    @Test
    public void findAccountByUsername() {
        Account account = createMockAccount();
        when(accountRepository.getByUsername(account.getUsername())).thenReturn(account);

        Account actualAccount = accountService.findAccountByUsername(account.getUsername());
        assertNotNull(actualAccount);
        verify(accountRepository, times(1)).getByUsername(account.getUsername());
    }

    @Test
    public void findAccountByUsernameNotExist() {
        when(accountRepository.getByUsername(NEW_USERNAME)).thenReturn(null);

        assertThrows(UsernameNotFoundException.class, () -> accountService.findAccountByUsername(NEW_USERNAME));
        verify(accountRepository, times(1)).getByUsername(NEW_USERNAME);
    }

    @Test
    public void getAccountDTOList() {
        int pageNumber = 1;
        Account account = createMockAccount();
        List<Account> accountList = List.of(account);
        Page<Account> accountPage = new PageImpl<>(accountList);

        when(accountRepository.findAll(any(Pageable.class))).thenReturn(accountPage);
        when(mapper.mapToDTO(account)).thenReturn(mapToAccountDTO(account));

        List<AccountDTO> accountDTOS = accountService.getAccountDTOList(pageNumber);
        assertEquals(account.getFirstName(), accountDTOS.get(0).getFirstName());
        verify(accountRepository, times(1)).findAll(any(Pageable.class));
    }

    @Test
    public void editInformation() {
        Account account = Account.builder().id(1).firstName("mock").lastName("acc").dob(null).modifyDate(null).build();
        StudentDTO student = StudentDTO.builder().firstName("mock").lastName("studentDTO").dob(null).build();
        when(accountRepository.save(any(Account.class))).thenReturn(account);
        accountService.editInformation(account, student);
        verify(accountRepository, times(1)).save(account);
    }
}
