package com.example.studentmanagementfa22.utility;

import com.example.studentmanagementfa22.dto.StudentDTO;
import com.example.studentmanagementfa22.entity.Account;
import org.modelmapper.ModelMapper;

public class Utility {
    private static final ModelMapper mapper = new ModelMapper();

    public static StudentDTO mapAccount(Account account) {
        StudentDTO studentDTO = mapper.map(account, StudentDTO.class);
        return studentDTO;
    }
}
