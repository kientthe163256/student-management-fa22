package com.example.studentmanagementfa22.service;

import com.example.studentmanagementfa22.entity.Role;
import com.example.studentmanagementfa22.repository.RoleRepository;
import com.example.studentmanagementfa22.service.impl.RoleServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RoleServiceTest {
    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private RoleServiceImpl roleService;

    private Role mockRole(){
        return Role.builder()
                .id(1)
                .roleName("ROLE_STUDENT")
                .build();
    }
    @Test
    public void findRoleById() {
        Role role = mockRole();
        int roleId = role.getId();
        when(roleRepository.findById(roleId)).thenReturn(Optional.of(role));

        Role actualRole = roleService.findRoleById(roleId);

        assertEquals(role.getRoleName(), actualRole.getRoleName());
        assertEquals(roleId, actualRole.getId());

        verify(roleRepository).findById(roleId);
    }

    @Test
    public void findRoleByNotExistId(){
        int notExistId = 100;
        when(roleRepository.findById(notExistId)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> roleService.findRoleById(notExistId));

        verify(roleRepository).findById(notExistId);
    }
}
