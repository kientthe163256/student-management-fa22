package com.example.studentmanagementfa22.utility;

import com.example.studentmanagementfa22.dto.AccountDTO;
import com.example.studentmanagementfa22.dto.TeacherDTO;
import com.example.studentmanagementfa22.entity.Teacher;
import com.example.studentmanagementfa22.service.AccountService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
@Mapper(componentModel = "spring")
public abstract class TeacherMapper implements IGenericMapper<Teacher, TeacherDTO> {
    @Autowired
    private AccountService accountService;

    @Override
    @Mapping(source = "accountId", target = "account", qualifiedByName = "mapAccountById")
    public abstract TeacherDTO toDTO(Teacher source);

    @Named("mapAccountById")
    AccountDTO mapAccountById(Integer accountId) {
        AccountDTO accountDTO = accountService.getAccountDTOById(accountId);
        return accountDTO;
    }
}
