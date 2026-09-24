package com.educandoweb.course.resources;

import com.educandoweb.course.dto.LoginRequestDTO;
import com.educandoweb.course.service.JwtService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/login")
public class AuthResource {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    @PostMapping
    public ResponseEntity<String> login(@Valid @RequestBody LoginRequestDTO dto) {
        UsernamePasswordAuthenticationToken usernamePassword =
                new UsernamePasswordAuthenticationToken(
                dto.getEmail(),
                dto.getPassword()
        );
        authenticationManager.authenticate(usernamePassword);
        String token = jwtService.generateToken(dto.getEmail());
        return ResponseEntity.ok(token);
    }
}
