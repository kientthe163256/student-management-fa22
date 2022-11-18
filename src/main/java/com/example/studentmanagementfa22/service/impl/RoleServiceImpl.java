package com.example.studentmanagementfa22.service.impl;

import com.example.studentmanagementfa22.entity.Role;
import com.example.studentmanagementfa22.repository.RoleRepository;
import com.example.studentmanagementfa22.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;
@Service
public class RoleServiceImpl implements RoleService {
    @Autowired
    private RoleRepository roleRepository;

    @Override
    public Role findRoleById(int roleId) {
        Optional<Role> optionalRole = roleRepository.findById(roleId);
        if (optionalRole.isEmpty()){
            throw new NoSuchElementException("There is no role with given id");
        }
        return optionalRole.get();
    }

    @Override
    public Role findByRoleName(String roleName) {
        return roleRepository.findByRoleName(roleName);
    }
}
