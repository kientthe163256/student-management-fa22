package com.example.studentmanagementfa22.utility.mapper;

import com.example.studentmanagementfa22.dto.AccountDTO;
import com.example.studentmanagementfa22.entity.Account;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AccountMapper extends IGenericMapper<Account, AccountDTO>{
}
