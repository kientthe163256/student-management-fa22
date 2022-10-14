package com.example.studentmanagementfa22.service.impl;

import com.example.studentmanagementfa22.dto.AccountDTO;
import com.example.studentmanagementfa22.dto.StudentDTO;
import com.example.studentmanagementfa22.entity.Account;
import com.example.studentmanagementfa22.entity.Role;
import com.example.studentmanagementfa22.exception.ElementAlreadyExistException;
import com.example.studentmanagementfa22.repository.AccountRepository;
import com.example.studentmanagementfa22.service.AccountService;
import com.example.studentmanagementfa22.service.RoleService;
import com.example.studentmanagementfa22.service.StudentService;
import com.example.studentmanagementfa22.utility.IGenericMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class AccountServiceImpl implements AccountService {
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private RoleService roleService;

    @Autowired
    private StudentService studentService;

    @Autowired
    private IGenericMapper<Account, AccountDTO> mapper;

    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public UserDetails loadUserByUsername(String username) {
        Account optionalAccount = accountRepository.findByUsername(username);
        if (optionalAccount == null) {
            throw new UsernameNotFoundException("Username is not found!");
        }

        Account validAccount = optionalAccount;
        UserDetails userDetails = new User(validAccount.getUsername(),
                validAccount.getPassword(),
                mapRoleToAuthorities(validAccount.getRoleId()));
        return userDetails;
    }

    public Collection<? extends GrantedAuthority> mapRoleToAuthorities(Integer roleId) {
        Role role = roleService.findRoleById(roleId);
        Collection<String> roleList = new ArrayList<>();
        roleList.add(role.getRoleName());

        return roleList.stream()
                .map(roleName -> new SimpleGrantedAuthority(roleName))
                .collect(Collectors.toList());
    }


    @Override
    public Account findAccountByUsername(String username) {
        Account optionalAccount = accountRepository.findByUsername(username);
        if (optionalAccount == null) {
            throw new UsernameNotFoundException("Username is not found!");
        }
        return optionalAccount;
    }

    @Override
    public void registerNewAccount(Account account) throws ElementAlreadyExistException {
        Account existAccount = accountRepository.findByUsername(account.getUsername());
        if (existAccount != null) {
            throw new ElementAlreadyExistException("There is already an account with that username!");
        }
        //Add a new account
        account.setEnabled(true);
        account.setRoleId(roleService.findByRoleName("ROLE_STUDENT").getId());
        account.setPassword(passwordEncoder.encode(account.getPassword()));
        Date today = new Date();
        account.setCreateDate(today);
        account.setModifyDate(today);
        accountRepository.save(account);
    }

    @Override
    public Account findById(int id) {
        Optional<Account> optionalAccount = accountRepository.findById(id);
        if (optionalAccount.isEmpty()) {
            throw new NoSuchElementException("Account not found");
        }
        Account account = optionalAccount.get();
        return account;

    }

    @Override
    public Page<Account> findAllAccount(int pageNumber) {
        PageRequest pageRequest = PageRequest.of(pageNumber - 1, 5);
        return accountRepository.findAll(pageRequest);
    }

    @Override
    public AccountDTO getAccountDTOById(int accountId) {
        Account account = findById(accountId);
        return mapper.toDTO(account);
    }

    @Override
    public void editInformation(Account account, StudentDTO student) {
        account.setFirstName(student.getFirstName());
        account.setLastName(student.getLastName());
        account.setDob(student.getDob());
        accountRepository.save(account);
    }

    @Override
    public Account updateAccount(AccountDTO editAccount, Account account) {
        account.setFirstName(editAccount.getFirstName());
        account.setLastName(editAccount.getLastName());
        account.setDob(editAccount.getDob());
        accountRepository.save(account);
        return account;
    }


    @Override
    public void disableAccount(Integer accountId) {
        accountRepository.disableAccount(accountId);
    }
}
