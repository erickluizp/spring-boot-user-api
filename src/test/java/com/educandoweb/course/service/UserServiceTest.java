package com.educandoweb.course.service;

import com.educandoweb.course.dto.UserDTO;
import com.educandoweb.course.entities.User;
import com.educandoweb.course.repository.UserRepository;
import com.educandoweb.course.service.exceptions.DatabaseException;
import com.educandoweb.course.service.exceptions.ResourcesNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    public void findByIdShouldReturnUserDTOWhenIdExists() {

        User user = new User(
                1L,
                "Maria Brown",
                "maria@gmail.com",
                "4599925511",
                "123456"
        );
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        UserDTO result = userService.findById(1L);
        assertEquals(1L, result.getId());
        assertEquals("Maria Brown", result.getName());
        assertEquals("maria@gmail.com", result.getEmail());
        assertEquals("4599925511", result.getPhone());
    }

    @Test
    public void findByIdShouldThrowResourcesNotFoundExceptionWhenIdDoesNotExist() {

        when(userRepository.findById(999L)).thenReturn(Optional.empty());
        assertThrows(ResourcesNotFoundException.class, () -> userService.findById(999L));
    }

    @Test
    public void findAllShouldReturnUserDTOList() {
        User user1 = new User(
                1L,
                "Maria Brown",
                "maria@gmail.com",
                "4599925511",
                "123456"
        );

        User user2 = new User(
                2L,
                "Alex Green",
                "alex@gmail.com",
                "97777777777",
                "123456"
        );
        when(userRepository.findAll()).thenReturn(List.of(user1, user2));
        List<UserDTO> result = userService.findAll();

        assertEquals(2, result.size());
        assertEquals(1L, result.getFirst().getId());
        assertEquals("Maria Brown", result.getFirst().getName());
        assertEquals("maria@gmail.com", result.get(0).getEmail());
        assertEquals("4599925511", result.get(0).getPhone());

        assertEquals(2L, result.get(1).getId());
        assertEquals("Alex Green", result.get(1).getName());
        assertEquals("alex@gmail.com", result.get(1).getEmail());
        assertEquals("97777777777", result.get(1).getPhone());

    }

    @Test
    public void insertShouldReturnUser() {
        UserDTO dto = new UserDTO(
                null,
                "Carlos Silva",
                "carlos@gmail.com",
                "11999999999"
        );

        User user = new User(
                1L,
                "Carlos Silva",
                "carlos@gmail.com",
                "11999999999",
                null
        );

        when(userRepository.save(any(User.class))).thenReturn(user);
        User result = userService.insert(dto);
        assertEquals(1L, result.getId());
        assertEquals("Carlos Silva", result.getName());
        assertEquals("carlos@gmail.com", result.getEmail());
        assertEquals("11999999999", result.getPhone());
    }

    @Test
    public void updateShouldReturnUser() {
        UserDTO dto = new UserDTO(
                null,
                "Carlos Silva",
                "carlos@gmail.com",
                "11999999999"
        );

        User user = new User(
                1L,
                "Maria Brown",
                "maria@gmail.com",
                "4599925511",
                "123456"
        );
        when(userRepository.getReferenceById(user.getId())).thenReturn(user);
        when(userRepository.save(any(User.class))).thenReturn(user);
        User result = userService.update(user.getId(), dto);

        assertEquals(user.getId(), result.getId());
        assertEquals(dto.getName(), result.getName());
        assertEquals(dto.getEmail(), result.getEmail());
        assertEquals(dto.getPhone(), result.getPhone());
    }

    @Test
    public void updateShouldThrowResourcesNotFoundExceptionWhenIdDoesNotExist() {
        UserDTO dto = new UserDTO(
                2L,
                "Carlos Silva",
                "carlos@gmail.com",
                "11999999999"
        );

        User user = new User(
                1L,
                "Maria Brown",
                "maria@gmail.com",
                "4599925511",
                "123456"
        );
        when(userRepository.getReferenceById(999L)).thenThrow(new EntityNotFoundException());
        assertThrows(ResourcesNotFoundException.class, () -> userService.update(999L, dto));
    }

    @Test
    public void deleteShouldDoNothingWhenIdExists() {
        User user = new User(
                1L,
                "Maria Brown",
                "maria@gmail.com",
                "4599925511",
                "123456"
        );
        when(userRepository.existsById(user.getId())).thenReturn(true);
        userService.delete(user.getId());
        verify(userRepository).deleteById(user.getId());
    }

    @Test
    public void deleteShouldThrowResourcesNotFoundExceptionWhenIdDoesNotExist() {
        User user = new User(
                1L,
                "Maria Brown",
                "maria@gmail.com",
                "4599925511",
                "123456"
        );
        when(userRepository.existsById(user.getId())).thenReturn(false);
        assertThrows(ResourcesNotFoundException.class, () -> userService.delete(user.getId()));
    }

    @Test
    public void deleteShouldThrowDatabaseExceptionWhenIntegrityViolation() {
        User user = new User(
                1L,
                "Maria Brown",
                "maria@gmail.com",
                "4599925511",
                "123456"
        );
        when(userRepository.existsById(user.getId())).thenReturn(true);
        doThrow(new DataIntegrityViolationException("")).when(userRepository).deleteById(user.getId());
        assertThrows(DatabaseException.class, () -> userService.delete(user.getId()));
    }
}
