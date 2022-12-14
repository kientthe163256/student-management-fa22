package com.example.studentmanagementfa22.service.impl;

import com.example.studentmanagementfa22.dto.AccountDTO;
import com.example.studentmanagementfa22.dto.StudentDTO;
import com.example.studentmanagementfa22.entity.Account;
import com.example.studentmanagementfa22.entity.Role;
import com.example.studentmanagementfa22.exception.customExceptions.ElementAlreadyExistException;
import com.example.studentmanagementfa22.repository.AccountRepository;
import com.example.studentmanagementfa22.service.AccountService;
import com.example.studentmanagementfa22.service.RoleService;
import com.example.studentmanagementfa22.utility.mapper.AccountMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AccountServiceImpl implements AccountService {
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private RoleService roleService;


    @Autowired
    private HttpSession session;
    @Autowired
    private AccountMapper mapper;

    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public UserDetails loadUserByUsername(String username) {
        Account account = accountRepository.getByUsername(username);
        if (account == null) {
            throw new UsernameNotFoundException("Username is not found!");
        }
        if (!account.isEnabled()){
            throw new DisabledException("Your account is disabled");
        }
        session.setAttribute("account", account);
        return new User(account.getUsername(),
                account.getPassword(),
                mapRoleToAuthorities(account.getRoleId()));
    }

    public Collection<? extends GrantedAuthority> mapRoleToAuthorities(Integer roleId) {
        Role role = roleService.findRoleById(roleId);
        Collection<String> roleList = new ArrayList<>();
        roleList.add(role.getRoleName());

        return roleList.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }


    @Override
    public Account findAccountByUsername(String username) {
        Account account = accountRepository.getByUsername(username);
        if (account == null) {
            throw new UsernameNotFoundException("Username is not found!");
        }
        return account;
    }

    @Override
    public AccountDTO registerNewAccount(Account account, String roleName) {
        Account existAccount = accountRepository.getByUsername(account.getUsername());
        if (existAccount != null) {
            throw new ElementAlreadyExistException("There is already an account with that username!");
        }
        //Add a new account
        account.setEnabled(true);
        account.setRoleId(roleService.findByRoleName(roleName).getId());
        account.setPassword(passwordEncoder.encode(account.getPassword()));
        Date today = new Date();
        account.setCreateDate(today);
        account.setModifyDate(today);
        Account registeredAccount = accountRepository.save(account);
        return mapper.mapToDTO(registeredAccount);
    }

    @Override
    public Account getById(int id) {
        Optional<Account> optionalAccount = accountRepository.findById(id);
        if (optionalAccount.isEmpty()) {
            throw new NoSuchElementException("Can not find account with id = " + id);
        }
        return optionalAccount.get();
    }

    @Override
    public List<AccountDTO> getAccountDTOList(int pageNumber) {
        PageRequest pageRequest = PageRequest.of(pageNumber - 1, 5);
        Page<Account> accountPage = accountRepository.findAll(pageRequest);
        return accountPage.map(account -> mapper.mapToDTO(account)).stream().toList();
    }

    @Override
    public AccountDTO getAccountDTOById(int accountId) {
        Account account = getById(accountId);
        return mapper.mapToDTO(account);
    }

    @Override
    public void editInformation(Account account, StudentDTO student) {
        account.setFirstName(student.getFirstName());
        account.setLastName(student.getLastName());
        account.setDob(student.getDob());
        account.setModifyDate(new Date());
        accountRepository.save(account);
    }

    @Override
    public AccountDTO updateAccount(AccountDTO editAccount) {
        Account account = getById(editAccount.getId());
        account.setFirstName(editAccount.getFirstName());
        account.setLastName(editAccount.getLastName());
        account.setDob(editAccount.getDob());
        account.setModifyDate(new Date());
        Account savedAccount = accountRepository.save(account);

        return mapper.mapToDTO(savedAccount);
    }


    @Override
    @Transactional
    public void disableAccount(Integer accountId) {
        Account account = getById(accountId);
        account.setEnabled(false);
        account.setDisableDate(new Date());
        accountRepository.save(account);
    }
}
