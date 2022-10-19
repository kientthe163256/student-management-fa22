package com.example.studentmanagementfa22.repository.service;

import com.example.studentmanagementfa22.entity.Role;
import org.springframework.stereotype.Service;


public interface RoleService {
    Role findRoleById(int roleId);

    Role findByRoleName(String roleName);
}
