package com.example.studentmanagementfa22.controller.auth;

import com.example.studentmanagementfa22.utility.jwt.JwtUtils;
import com.example.studentmanagementfa22.dto.LoginResponseDTO;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class LoginController {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;

    @GetMapping("/login")
    public String displayLogin() {
        return "login";
    }

    @ApiOperation("Login")
    @PostMapping("/login")
    @ResponseBody
    public LoginResponseDTO login(@RequestParam String username, @RequestParam String password) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        username,
                        password
                )
        );
        //get UserDetails from authentication
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        String jwt = jwtUtils.generateToken(userDetails);
        return new LoginResponseDTO(jwt);
    }
}