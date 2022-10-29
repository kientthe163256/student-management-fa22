package com.example.studentmanagementfa22.utility;
import com.example.studentmanagementfa22.dto.StudentDTO;
import com.example.studentmanagementfa22.entity.Account;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface StudentMapper extends IGenericMapper<Account, StudentDTO>{

}
