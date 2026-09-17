package com.educandoweb.course.service;

import com.educandoweb.course.entities.Category;
import com.educandoweb.course.repository.CategoryRepository;
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
public class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    @Test
    public void findByIdShouldReturnCategoryWhenIdExists() {
        Category category = new Category(1L, "Electronics");
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        Category result = categoryService.findById(1L);

        assertEquals(1L, result.getId());
        assertEquals("Electronics", result.getName());
    }

    @Test
    public void findByIdShouldThrowResourcesNotFoundExceptionWhenIdDoesNotExist() {

        when(categoryRepository.findById(999L)).thenReturn(Optional.empty());
        assertThrows(ResourcesNotFoundException.class, () -> categoryService.findById(999L));
    }

    @Test
    public void findAllShouldReturnCategoryList() {
        Category category1 = new Category(1L, "Electronics");
        Category category2 = new Category(2L, "Furniture");

        when(categoryRepository.findAll()).thenReturn(List.of(category1, category2));
        List<Category> result = categoryService.findAll();

        assertEquals(2, result.size());
        assertEquals(1L, result.getFirst().getId());
        assertEquals("Electronics", result.getFirst().getName());
        assertEquals(2L, result.get(1).getId());
        assertEquals("Furniture", result.get(1).getName());
    }
}
