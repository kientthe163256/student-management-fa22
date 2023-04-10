//package com.example.authorizationserver.config;
//
//import com.example.authorizationserver.model.Account;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.web.authentication.AuthenticationConverter;
//import org.springframework.stereotype.Component;
//
//import javax.servlet.http.HttpServletRequest;
//import java.io.IOException;
//
//@Component
//public class UserAuthenticationConverter implements AuthenticationConverter {
//    private static final ObjectMapper MAPPER = new ObjectMapper();
//
//    @Override
//    public Authentication convert(HttpServletRequest request) {
//        Account account = null;
//        try {
//            account = MAPPER.readValue(request.getInputStream(), Account.class);
//        } catch (IOException e) {
//            return null;
//        }
//        return new UsernamePasswordAuthenticationToken(account.getUsername(), account.getPassword());
//    }
//}
