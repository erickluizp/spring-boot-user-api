package com.educandoweb.course.resources;

import com.educandoweb.course.entities.Product;
import com.educandoweb.course.resources.exceptions.ResourceExceptionHandler;
import com.educandoweb.course.service.ProductService;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(MockitoExtension.class)
public class ProductResourceTest {

    @Mock
    private ProductService service;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        ProductResource resource = new ProductResource();

        ReflectionTestUtils.setField(resource, "service", service);

        mockMvc = MockMvcBuilders.standaloneSetup(resource).setControllerAdvice(new ResourceExceptionHandler()).build();
    }

    @Test
    public void findAllShouldReturnProductList() throws Exception {
        Product product1 = new Product(
                1L,
                "The Lord of the Rings",
                "Fantasy book",
                39.90,
                ""
        );

        Product product2 = new Product(
                2L,
                "Mechanical Keyboard",
                "RGB mechanical keyboard",
                249.90,
                ""
        );

        when(service.findAll()).thenReturn(List.of(product1, product2));
        mockMvc.perform(get("/products")).andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }
    @Test
    public void findByIdShouldReturnProductWhenIdExists() throws Exception {
        Product product = new Product(
                1L,
                "The Lord of the Rings",
                "Fantasy book",
                39.90,
                ""
        );
        when(service.findById(1L)).thenReturn(product);
        mockMvc.perform(get("/products/1")).andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("The Lord of the Rings"))
                .andExpect(jsonPath("$.price").value(39.90))
                .andExpect(jsonPath("$.imgUrl").value(""));
    }
    @Test
    public void findByIdShouldReturnNotFoundWhenIdDoesNotExist() throws Exception {
       when(service.findById(999L)).thenThrow(new ResourcesNotFoundException(999L));
       mockMvc.perform(get("/products/999")).andExpect(status().isNotFound());
    }
}
