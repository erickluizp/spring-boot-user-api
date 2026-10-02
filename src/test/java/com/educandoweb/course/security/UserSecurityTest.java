package com.educandoweb.course.security;

import com.educandoweb.course.service.JwtService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties = "JWT_SECRET=minha-chave-secreta-com-mais-de-32-bytes")
public class UserSecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtService jwtService;

    @Value("${jwt.secret}")
    private String secretKey;

    @Test
    public void shouldReturnUnauthorizedWhenAccessWithoutToken() throws Exception {
        mockMvc.perform(get("/users"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void shouldReturnOkWhenAccessWithValidToken() throws Exception {
        String token = jwtService.generateToken("maria@gmail.com");

        mockMvc.perform(get("/users")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldReturnUnauthorizedWhenAccessWithInvalidToken() throws Exception {
        mockMvc.perform(get("/users")
                .header("Authorization", "Bearer token-invalido"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void shouldReturnUnauthorizedWhenTokenIsExpired() throws Exception {
        String token = Jwts.builder()
                .subject("maria@gmail.com")
                .expiration(new Date(System.currentTimeMillis() - 1000))
                .signWith(Keys.hmacShaKeyFor(
                        secretKey.getBytes(StandardCharsets.UTF_8)
                ))
                .compact();

        mockMvc.perform(get("/users")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void shouldReturnForbiddenWhenUserTriesToUpdate() throws Exception {
        String token = jwtService.generateToken("maria@gmail.com");

        mockMvc.perform(put("/users/1")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                                "name": "Maria Atualizada",
                                "email": "maria@gmail.com",
                                "phone": "999999999"
                            }
                            """))
                .andExpect(status().isForbidden());
    }

    @Test
    public void shouldReturnOkWhenAdminTriesToUpdate() throws Exception {
        String token = jwtService.generateToken("alex@gmail.com");

        mockMvc.perform(put("/users/1")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                                "name": "Maria Atualizada",
                                "email": "maria@gmail.com",
                                "phone": "999999999"
                            }
                            """))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldReturnForbiddenWhenUserTriesToDelete() throws Exception {
        String token = jwtService.generateToken("maria@gmail.com");

        mockMvc.perform(delete("/users/1")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isForbidden());
    }

    @Test
    public void shouldReturnBadRequestWhenAdminTriesToDeleteUserWithOrders() throws Exception {
        String token = jwtService.generateToken("alex@gmail.com");

        mockMvc.perform(delete("/users/2")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldReturnNoContentWhenAdminTriesToDeleteUser() throws Exception {
        String token = jwtService.generateToken("alex@gmail.com");

        mockMvc.perform(delete("/users/3")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isNoContent());
    }

    @Test
    public void shouldReturnUnauthorizedWhenTokenUserDoesNotExist() throws Exception {
        String token = jwtService.generateToken("usuario@naoexiste.com");

        mockMvc.perform(get("/users")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void shouldReturnNotFoundWhenAdminTriesToDeleteNonExistingUser() throws Exception {
        String token = jwtService.generateToken("alex@gmail.com");

        mockMvc.perform(delete("/users/999")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isNotFound());
    }
}