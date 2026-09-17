package com.educandoweb.course.resources;

import com.educandoweb.course.entities.Category;
import com.educandoweb.course.resources.exceptions.ResourceExceptionHandler;
import com.educandoweb.course.service.CategoryService;
import com.educandoweb.course.service.exceptions.ResourcesNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;


@ExtendWith(MockitoExtension.class)
public class CategoryResourceTest {

    @Mock
    private CategoryService service;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        CategoryResource resource = new CategoryResource();

        ReflectionTestUtils.setField(resource, "service", service);

        mockMvc = MockMvcBuilders.standaloneSetup(resource).setControllerAdvice(new ResourceExceptionHandler()).build();
    }

    @Test
    public void findAllShouldReturnCategoryList() throws Exception {
        Category category1 = new Category(1L, "Electronics");
        Category category2 = new Category(2L, "Books");

        when(service.findAll()).thenReturn(List.of(category1, category2));
        mockMvc.perform(get("/categories")).andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }
    @Test
    public void findByIdShouldReturnCategoryWhenIdExists() throws Exception {
        Category category = new Category(1L, "Electronics");
        when(service.findById(1L)).thenReturn(category);
        mockMvc.perform(get("/categories/1")).andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Electronics"));
    }
    @Test
    public void findByIdShouldReturnNotFoundWhenIdDoesNotExist() throws Exception {
       when(service.findById(999L)).thenThrow(new ResourcesNotFoundException(999L));
       mockMvc.perform(get("/categories/999")).andExpect(status().isNotFound());
    }
}
