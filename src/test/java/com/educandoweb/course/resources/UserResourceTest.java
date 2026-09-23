package com.educandoweb.course.resources;

import com.educandoweb.course.dto.UserDTO;
import com.educandoweb.course.dto.UserInsertDTO;
import com.educandoweb.course.entities.User;
import com.educandoweb.course.resources.exceptions.ResourceExceptionHandler;
import com.educandoweb.course.service.UserService;
import com.educandoweb.course.service.exceptions.ResourcesNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.http.MediaType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class UserResourceTest {

    @Mock
    private UserService service;

    @Mock
    private PasswordEncoder passwordEncoder;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        UserResource resource = new UserResource();

        ReflectionTestUtils.setField(resource, "service", service);

        mockMvc = MockMvcBuilders.standaloneSetup(resource).setControllerAdvice(new ResourceExceptionHandler()).build();
    }

    @Test
    public void findByIdShouldReturnUserWhenIdExists() throws Exception {
        UserDTO user = new UserDTO(
                1L,
                "Maria Brown",
                "maria@gmail.com",
                "4599925511"
        );
        when(service.findById(1L)).thenReturn(user);
        mockMvc.perform(get("/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Maria Brown"))
                .andExpect(jsonPath("$.email").value("maria@gmail.com"))
                .andExpect(jsonPath("$.phone").value("4599925511"));
    }

    @Test
    public void findByIdShouldReturnNotFoundWhenIdDoesNotExist() throws Exception {
        when(service.findById(999L)).thenThrow(new ResourcesNotFoundException(999L));
        mockMvc.perform(get("/users/999")).andExpect(status().isNotFound());
    }

    @Test
    public void findAllShouldReturnUserList() throws Exception {
        UserDTO user = new UserDTO(
                1L,
                "Maria Brown",
                "maria@gmail.com",
                "4599925511"
        );
        UserDTO user2 = new UserDTO(
                2L,
                "Alex Green",
                "alex@gmail.com",
                "974563221"
        );
        when(service.findAll()).thenReturn(List.of(user, user2));
        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Maria Brown"))
                .andExpect(jsonPath("$[0].email").value("maria@gmail.com"))
                .andExpect(jsonPath("$[0].phone").value("4599925511"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("Alex Green"))
                .andExpect(jsonPath("$[1].email").value("alex@gmail.com"))
                .andExpect(jsonPath("$[1].phone").value("974563221"))
        ;
    }

    @Test
    public void insertShouldReturnCreated() throws Exception {
        User user = new User(
                1L,
                "Maria Brown",
                "maria@gmail.com",
                "4599925511",
                null,
                "ROLE_USER"
        );
        when(service.insert(any(UserInsertDTO.class))).thenReturn(user);
        String json = """
                {
                    "name": "Maria Brown",
                    "email": "maria@gmail.com",
                    "phone": "4599925511",
                    "password": "123456"
                }
                """;

        mockMvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Maria Brown"))
                .andExpect(jsonPath("$.email").value("maria@gmail.com"))
                .andExpect(jsonPath("$.phone").value("4599925511"))
                .andExpect(header().string("Location", "http://localhost/users/1"));
    }

    @Test
    public void deleteShouldReturnNoContent() throws Exception {
        doNothing().when(service).delete(1L);
        mockMvc.perform(delete("/users/1")).andExpect(status().isNoContent());
    }

    @Test
    public void deleteShouldReturnNotFoundWhenIdDoesNotExist() throws Exception {
        doThrow(new ResourcesNotFoundException(999L)).when(service).delete(999L);
        mockMvc.perform(delete("/users/999")).andExpect(status().isNotFound());
    }

    @Test
    public void updateShouldReturnUserWhenIdExists() throws Exception {
        User user = new User(
                1L,
                "Carlos Silva",
                "carlos@gmail.com",
                "11999999999",
                null,
                "ROLE_USER"
        );
        String json = """
                {
                    "name": "Carlos Silva",
                    "email": "carlos@gmail.com",
                    "phone": "11999999999"
                }
                """;
        when(service.update(eq(1L), any(UserDTO.class))).thenReturn(user);
        mockMvc.perform(put("/users/1").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Carlos Silva"))
                .andExpect(jsonPath("$.email").value("carlos@gmail.com"))
                .andExpect(jsonPath("$.phone").value("11999999999"));
    }

    @Test
    public void updateShouldReturnNotFoundWhenIdDoesNotExist() throws Exception {
        String json = """
                {
                    "name": "Carlos Silva",
                    "email": "carlos@gmail.com",
                    "phone": "11999999999"
                }
                """;
        when(service.update(eq(999L), any(UserDTO.class))).thenThrow(new ResourcesNotFoundException(999L));
        mockMvc.perform(put("/users/999").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isNotFound());
    }

    @Test
    public void insertShouldReturnBadRequestWhenNameIsBlank() throws Exception {
        String json = """
                {
                    "name": "",
                    "email": "maria@gmail.com",
                    "phone": "4599925511"
                }
                """;
        mockMvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void insertShouldReturnBadRequestWhenEmailIsInvalid() throws Exception {
        String json = """
                {
                    "name": "Maria Brown",
                        "email": "email-invalido",
                        "phone": "4599925511"
                }
                """;
        mockMvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isBadRequest());
    }
}
