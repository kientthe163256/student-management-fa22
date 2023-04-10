package com.example.authorizationserver.service;

import com.example.authorizationserver.model.Role;
import com.example.authorizationserver.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleService {
    @Autowired
    private RoleRepository roleRepository;

    public Role findRoleById(int id){
        Role role = roleRepository.findById(id).get();
        return role;
    }
}
