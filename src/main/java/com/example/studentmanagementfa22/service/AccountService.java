package com.example.studentmanagementfa22.service;

import com.example.studentmanagementfa22.dto.AccountDTO;
import com.example.studentmanagementfa22.dto.StudentDTO;
import com.example.studentmanagementfa22.entity.Account;
import com.example.studentmanagementfa22.exception.customExceptions.ElementAlreadyExistException;
import org.springframework.data.domain.Page;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import javax.transaction.Transactional;
import java.util.List;


public interface AccountService extends UserDetailsService {
    @Override
    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;

    Account findAccountByUsername(String username);

    void registerNewAccount(Account account) throws ElementAlreadyExistException;

    Account getById(int id);

    Page<Account> findAllAccount(int pageNumber);

    List<AccountDTO> getAccountDTOList(int pageNumber);

    AccountDTO getAccountDTOById(int accountId);

    void editInformation(Account account, StudentDTO student);

    Account updateAccount(AccountDTO editAccount, Account account);
    @Transactional
    void disableAccount(Integer accountId);
}
