package com.educandoweb.course.service;

import com.educandoweb.course.entities.Category;
import com.educandoweb.course.repository.CategoryRepository;
import com.educandoweb.course.service.exceptions.ResourcesNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    private final CategoryRepository repository;

    public CategoryService(CategoryRepository repository) {
        this.repository = repository;
    }

    public List<Category> findAll() {
        return repository.findAll();
    }

    public Category findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourcesNotFoundException(id));
    }
}
