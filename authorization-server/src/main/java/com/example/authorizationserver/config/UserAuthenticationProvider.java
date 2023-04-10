//package com.example.authorizationserver.config;
//
//import com.example.authorizationserver.model.Account;
//import com.example.authorizationserver.model.Role;
//import com.example.authorizationserver.repository.AccountRepository;
//import com.example.authorizationserver.service.RoleService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.authentication.AuthenticationProvider;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Component;
//
//import java.nio.CharBuffer;
//import java.util.ArrayList;
//import java.util.Collection;
//import java.util.Collections;
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Component
//@RequiredArgsConstructor
//public class UserAuthenticationProvider implements AuthenticationProvider {
//    private final AccountRepository accountRepository;
//    private final PasswordEncoder passwordEncoder;
//    private final RoleService roleService;
//
//
//    @Override
//    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
//        String username = authentication.getName();
//        String password = authentication.getCredentials().toString();
//
//        Account account = accountRepository.getByUsername(username);
//        if (account == null) {
//            return null;
//        }
//        Collection<? extends GrantedAuthority> authorities = mapRoleToAuthorities(account.getRoleId());
//
//        if (passwordEncoder.matches(CharBuffer.wrap(password), account.getPassword())) {
//            return new UsernamePasswordAuthenticationToken(username, password, authorities);
//        }
//        return null;
//    }
//
//    public Collection<? extends GrantedAuthority> mapRoleToAuthorities(Integer roleId) {
//        Role role = roleService.findRoleById(roleId);
//        Collection<String> roleList = new ArrayList<>();
//        roleList.add(role.getRoleName());
//
//        return roleList.stream()
//                .map(SimpleGrantedAuthority::new)
//                .collect(Collectors.toList());
//    }
//
//    @Override
//    public boolean supports(Class<?> authentication) {
//        return UsernamePasswordAuthenticationToken.class.equals(authentication);
//    }
//
//
//}
