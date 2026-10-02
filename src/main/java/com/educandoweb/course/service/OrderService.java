package com.educandoweb.course.service;

import com.educandoweb.course.entities.Order;
import com.educandoweb.course.repository.OrderRepository;
import com.educandoweb.course.service.exceptions.ResourcesNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

    private final OrderRepository repository;

    public OrderService(OrderRepository repository) {
        this.repository = repository;
    }

    public List<Order> findAll() {
        return repository.findAll();
    }

    public Order findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourcesNotFoundException(id));
    }
}
