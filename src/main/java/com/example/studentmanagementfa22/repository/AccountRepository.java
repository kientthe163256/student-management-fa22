package com.example.studentmanagementfa22.repository;

import com.example.studentmanagementfa22.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, String> {
    Account findByUsername(String username);
    Optional<Account> findById(Integer id);

    @Query(value = "UPDATE account set enabled = 0 where id = :accountId", nativeQuery = true)
    void disableAccount(@Param("accountId") Integer accountId);
}
