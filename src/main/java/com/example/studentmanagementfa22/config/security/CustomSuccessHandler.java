package com.example.studentmanagementfa22.config.security;

import com.example.studentmanagementfa22.entity.Account;
import com.example.studentmanagementfa22.entity.Role;
import com.example.studentmanagementfa22.service.AccountService;
import com.example.studentmanagementfa22.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
@Configuration
public class CustomSuccessHandler implements AuthenticationSuccessHandler {
    @Autowired
    private AccountService accountService;

    @Autowired
    private RoleService roleService;

    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String username = authentication.getName();
        Account account = accountService.findAccountByUsername(username);
        if (!account.isEnabled()) {
            redirectStrategy.sendRedirect(request, response, "/login?deactivated");
        } else {
            HttpSession session = request.getSession();
            session.setAttribute("account", account);

            Role role = roleService.findRoleById(account.getRoleId());
            if (role.getRoleName().equals("ROLE_ADMIN")) {
//                redirectStrategy.sendRedirect(request, response, "/admin");
            } else if (role.getRoleName().equals("ROLE_TEACHER")) {
                redirectStrategy.sendRedirect(request, response, "/teacher");
            } else if (role.getRoleName().equals("ROLE_STUDENT")) {
                redirectStrategy.sendRedirect(request, response, "/student");
            } else {
                throw new IllegalStateException();
            }
        }
    }
}
