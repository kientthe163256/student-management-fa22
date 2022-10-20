package com.example.studentmanagementfa22.service;

import com.example.studentmanagementfa22.StudentManagementFa22Application;
import com.example.studentmanagementfa22.entity.Role;
import com.example.studentmanagementfa22.repository.AccountRepository;
import com.example.studentmanagementfa22.repository.RoleRepository;
import com.example.studentmanagementfa22.repository.service.RoleService;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;

//@DataJpaTest
@RunWith(SpringRunner.class)
@SpringBootTest(classes= StudentManagementFa22Application.class)
public class RoleServiceTest {
    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private RoleService roleService;

    @Autowired
    private AccountRepository accountRepository;

    @Test
    public void getAllRoles(){
        List<Role> roleList = roleRepository.findAll();
        Assertions.assertNotNull(roleList);
    }

    @Test
    public void getRoleById(){
        Optional<Role> optionalRole = roleRepository.findById(3);
        Role role = optionalRole.get();
        Assertions.assertNotNull(role);
    }

    @Test
    public void getRoleById2(){
        Role role = roleService.findRoleById(3);
        Assertions.assertNotNull(role);
    }
}
