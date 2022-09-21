package com.example.studentmanagementfa22.service.impl;

import com.example.studentmanagementfa22.entity.Role;
import com.example.studentmanagementfa22.repository.RoleRepository;
import com.example.studentmanagementfa22.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.NoSuchElementException;
import java.util.Optional;

public class RoleServiceImpl implements RoleService {
    @Autowired
    private RoleRepository roleRepository;

    @Override
    public Role findRoleById(int role_id) {
        Optional<Role> optionalRole = roleRepository.findById(role_id);
        if (!optionalRole.isPresent()){
            throw new NoSuchElementException("There is no role with given id");
        }
        return optionalRole.get();
    }
}
