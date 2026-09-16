package com.educandoweb.course.service;

import com.educandoweb.course.dto.UserDTO;
import com.educandoweb.course.entities.User;
import com.educandoweb.course.repository.UserRepository;
import com.educandoweb.course.service.exceptions.DatabaseException;
import com.educandoweb.course.service.exceptions.ResourcesNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository repository;

    public List<UserDTO> findAll() {
        List<User> list = repository.findAll();

        return list.stream()
                .map(user -> new UserDTO(
                        user.getId(),
                        user.getName(),
                        user.getEmail(),
                        user.getPhone()
                )).toList();
    }

    public UserDTO findById(Long id) {
        User obj = repository.findById(id)
                .orElseThrow(() -> new ResourcesNotFoundException(id));

        return new UserDTO(
                obj.getId(),
                obj.getName(),
                obj.getEmail(),
                obj.getPhone()
        );
    }

    public User insert(UserDTO obj) {
        User user = new User(
                null,
                obj.getName(),
                obj.getEmail(),
                obj.getPhone(),
                null
        );

        return repository.save(user);
    }

    public void delete(Long id) {
        if (!repository.existsById(id)){
            throw new ResourcesNotFoundException(id);
        }
        try {
            repository.deleteById(id);
        }catch (EmptyResultDataAccessException e){
            throw new ResourcesNotFoundException(id);
        }catch (DataIntegrityViolationException e) {
            throw new DatabaseException(e.getMessage());
        }
    }

    public User update(Long id, UserDTO obj) {
        try {
            User entity = repository.getReferenceById(id);
            updateData(entity, obj);
            return repository.save(entity);
        } catch (EntityNotFoundException e) {
            throw new ResourcesNotFoundException(id);
        }
    }

    private void updateData(User entity, UserDTO obj) {
        entity.setName(obj.getName());
        entity.setEmail(obj.getEmail());
        entity.setPhone(obj.getPhone());
    }
}
