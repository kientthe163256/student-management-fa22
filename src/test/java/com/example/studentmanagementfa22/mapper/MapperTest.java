package com.example.studentmanagementfa22.mapper;

import com.example.studentmanagementfa22.dto.AccountDTO;
import com.example.studentmanagementfa22.dto.ClassroomDTO;
import com.example.studentmanagementfa22.entity.Account;
import com.example.studentmanagementfa22.entity.ClassType;
import com.example.studentmanagementfa22.entity.Classroom;
import com.example.studentmanagementfa22.utility.*;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.runner.RunWith;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

@SpringBootTest
@RunWith(SpringRunner.class)
public class MapperTest {
    private AccountMapper accountMapper = Mappers.getMapper(AccountMapper.class);

    @Autowired
    private ClassroomMapper classroomMapper;


    @Test
    public void mapAccountToDTO() {
        Account account = Account.builder()
                .username("HE163256")
                .password("12345")
                .enabled(true)
                .firstName("Trung")
                .lastName("Kien")
                .roleId(3)
                .dob(new Date())
                .build();
        AccountDTO accountDTO = accountMapper.mapToDTO(account);
        assertNotNull(accountDTO);
    }

    @Test
    public void mapClassroomToDTO() {
        Classroom classroom = Classroom.builder()
                .classroomName("SE1231")
                .classType(ClassType.SESSION)
                .noStudent(1)
                .teacherId(1)
                .build();
        ClassroomDTO classroomDTO = classroomMapper.mapToDTO(classroom);
        assertNotNull(classroomDTO);
    }

}
