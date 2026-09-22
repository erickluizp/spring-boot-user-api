package com.educandoweb.course.resources;

import com.educandoweb.course.dto.LoginRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/login")
public class AuthResource {

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping
    public void login(@RequestBody LoginRequestDTO dto) {
        UsernamePasswordAuthenticationToken usernamePassword =
                new UsernamePasswordAuthenticationToken(
                dto.getEmail(),
                dto.getPassword()
        );
        authenticationManager.authenticate(usernamePassword);
    }
}
