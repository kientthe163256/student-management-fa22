package com.example.studentmanagementfa22.utility;

import com.example.studentmanagementfa22.entity.Role;
import com.example.studentmanagementfa22.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

public class Utility {
    @Autowired
    private static RoleService roleService;

    public static Collection<? extends GrantedAuthority> mapRoleToAuthorities(Integer role_id) {
        Role role = roleService.findRoleById(role_id);
        Collection<String> roleList = new ArrayList<>();
        roleList.add(role.getRoleName());

        return roleList.stream()
                .map(role_name -> new SimpleGrantedAuthority(role_name))
                .collect(Collectors.toList());
    }
}
