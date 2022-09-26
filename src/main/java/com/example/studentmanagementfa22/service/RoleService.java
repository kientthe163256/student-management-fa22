package com.example.studentmanagementfa22.service;

import com.example.studentmanagementfa22.entity.Role;
import org.springframework.stereotype.Service;


public interface RoleService {
    Role findRoleById(int roleId);
}
