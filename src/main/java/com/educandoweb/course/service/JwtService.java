package com.educandoweb.course.service;

import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;
import io.jsonwebtoken.Jwts;

import java.nio.charset.StandardCharsets;

@Service
public class JwtService {

    private final String secretKey = "minha-chave-secreta-com-mais-de-32-bytes";

    public String generateToken(String email) {
        return Jwts.builder()
                .subject(email)
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8)))
                .compact();
    }
}
