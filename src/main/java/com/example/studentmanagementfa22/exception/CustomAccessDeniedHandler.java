package com.example.studentmanagementfa22.exception;

import com.example.studentmanagementfa22.dto.ErrorResponseDTO;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        response.setStatus(403);
//        ErrorResponseDTO<String> errorResponseDTO = new ErrorResponseDTO<>("You are not allowed to access this", 403);
        ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO(null, "403");
        response.getWriter().println(errorResponseDTO);
    }
}
