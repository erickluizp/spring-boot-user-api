package com.educandoweb.course.service;

import com.educandoweb.course.entities.Product;
import com.educandoweb.course.repository.ProductRepository;
import com.educandoweb.course.service.exceptions.ResourcesNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @Test
    public void findByIdShouldReturnProductWhenIdExists() {
        Product product = new Product(
                1L,
                "The Lord of the Rings",
                "Lorem ipsum dolor sit amet, consectetur.",
                90.5,
                ""
        );
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        Product result = productService.findById(1L);
        assertEquals(1L, result.getId());
        assertEquals("The Lord of the Rings", result.getName());
        assertEquals("Lorem ipsum dolor sit amet, consectetur.", result.getDescription());
        assertEquals(90.5, result.getPrice());
        assertEquals("", result.getImgUrl());
    }
    @Test
    public void findByIdShouldThrowResourcesNotFoundExceptionWhenIdDoesNotExist() {

        when(productRepository.findById(999L)).thenReturn(Optional.empty());
        assertThrows(ResourcesNotFoundException.class, () -> productService.findById(999L));
    }

    @Test
    public void findAllShouldReturnProductList() {
        Product product1 = new Product(
                1L,
                "The Lord of the Rings",
                "Lorem ipsum dolor sit amet, consectetur.",
                90.5,
                ""
        );
        Product product2 = new Product(
                2L,
                "Smart TV",
                "Nulla eu imperdiet purus. Maecenas ante.",
                2190.0,
                ""
        );
        when(productRepository.findAll()).thenReturn(List.of(product1, product2));
        List<Product> result = productService.findAll();
        assertEquals(2, result.size());

        assertEquals(1L, result.getFirst().getId());
        assertEquals("The Lord of the Rings", result.getFirst().getName());
        assertEquals("Lorem ipsum dolor sit amet, consectetur.", result.getFirst().getDescription());
        assertEquals(90.5, result.getFirst().getPrice());
        assertEquals("", result.getFirst().getImgUrl());

        assertEquals(2L, result.get(1).getId());
        assertEquals("Smart TV", result.get(1).getName());
        assertEquals("Nulla eu imperdiet purus. Maecenas ante.", result.get(1).getDescription());
        assertEquals(2190.0, result.get(1).getPrice());
        assertEquals("", result.get(1).getImgUrl());
    }
}
