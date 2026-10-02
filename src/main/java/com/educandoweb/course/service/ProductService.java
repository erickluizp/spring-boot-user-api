package com.educandoweb.course.service;

import com.educandoweb.course.entities.Product;
import com.educandoweb.course.repository.ProductRepository;
import com.educandoweb.course.service.exceptions.ResourcesNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository repository;

    public ProductService(ProductRepository repository) {
        this.repository = repository;
    }

    public List<Product> findAll() {
        return repository.findAll();
    }

    public Product findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourcesNotFoundException(id));
    }
}
