package com.educandoweb.course.resources;

import com.educandoweb.course.dto.LoginRequestDTO;
import com.educandoweb.course.service.JwtService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(
            summary = "Realiza login",
            description = "Autentica o usuário e retorna um token JWT."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Login realizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "401", description = "E-mail ou senha inválidos")
    })
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
