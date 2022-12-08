package com.example.studentmanagementfa22.utility.jwt;


import com.example.studentmanagementfa22.service.RoleService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class JwtUtils {
    @Autowired
    private RoleService roleService;

    private final long EXPIRATION_TIME = 1000 * 60 * 60 * 24 * 2; // 2 days
    private final String SECRET = "ThisIsASecret";


    public String generateToken(UserDetails userDetails) {
        Set<String> authorities = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet());
        return Jwts.builder()
                .claim("authorities", authorities)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512, SECRET)
                .compact();
    }

    public Boolean validateToken(String token) {
        return !isExpired(token);
    }

    private Claims getAllClaims(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET)
                .parseClaimsJws(token)
                .getBody();
    }

    public  <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String getUsername(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    public Date getExpirationDate(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    private Boolean isExpired(String token) {
        final Date expiration = getExpirationDate(token);
        return expiration.before(new Date());
    }

    public Set<SimpleGrantedAuthority> getAuthorities(String token) {//Claims::get("authorities")
        List<String> authorities = (List<String>) getClaimFromToken(token, claims -> claims.get("authorities"));
        return authorities.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toSet());
    }
}