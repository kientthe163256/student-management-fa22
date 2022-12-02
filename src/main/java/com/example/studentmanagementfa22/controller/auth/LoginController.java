package com.example.studentmanagementfa22.controller.auth;

import com.example.studentmanagementfa22.config.security.filters.TokenAuthenticationService;
import com.example.studentmanagementfa22.dto.LoginResponseDTO;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
public class LoginController {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenAuthenticationService tokenAuthenticationService;

    @GetMapping("/login")
    public String displayLogin() {
        return "login";
    }

    @ApiOperation("Login")
    @PostMapping("/login")
    public LoginResponseDTO login(@RequestParam String username, @RequestParam String password) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String token = tokenAuthenticationService.generateToken(userDetails);
        return new LoginResponseDTO(token);
    }
}