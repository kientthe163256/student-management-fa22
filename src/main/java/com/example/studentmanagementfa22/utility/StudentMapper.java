package com.example.studentmanagementfa22.utility;
import com.example.studentmanagementfa22.dto.StudentDTO;
import com.example.studentmanagementfa22.entity.Account;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class StudentMapper implements IGenericMapper<Account, StudentDTO>{


    @Override
    public abstract StudentDTO mapToDTO(Account source);
}
