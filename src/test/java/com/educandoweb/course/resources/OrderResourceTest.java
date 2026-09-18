package com.educandoweb.course.resources;

import com.educandoweb.course.entities.Order;
import com.educandoweb.course.entities.enums.OrderStatus;
import com.educandoweb.course.resources.exceptions.ResourceExceptionHandler;
import com.educandoweb.course.service.OrderService;
import com.educandoweb.course.service.exceptions.ResourcesNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.Instant;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(MockitoExtension.class)
public class OrderResourceTest {

    @Mock
    private OrderService service;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        OrderResource resource = new OrderResource();

        ReflectionTestUtils.setField(resource, "service", service);

        mockMvc = MockMvcBuilders.standaloneSetup(resource).setControllerAdvice(new ResourceExceptionHandler()).build();
    }

    @Test
    public void findAllShouldReturnOrderList() throws Exception {
        Order order1 = new Order(
                1L,
                Instant.parse("2026-09-16T10:00:00Z"),
                OrderStatus.PAID,
                null
        );

        Order order2 = new Order(
                2L,
                Instant.parse("2026-09-16T14:30:00Z"),
                OrderStatus.WAITING_PAYMENT,
                null
        );

        when(service.findAll()).thenReturn(List.of(order1, order2));
        mockMvc.perform(get("/orders")).andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }
    @Test
    public void findByIdShouldReturnOrderWhenIdExists() throws Exception {
        Order order = new Order(
                1L,
                Instant.parse("2026-09-16T10:00:00Z"),
                OrderStatus.PAID,
                null
        );
        when(service.findById(1L)).thenReturn(order);
        mockMvc.perform(get("/orders/1")).andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.moment").value("2026-09-16T10:00:00Z"))
                .andExpect(jsonPath("$.orderStatus").value("PAID"))
                .andExpect(jsonPath("$.client").isEmpty());
    }
    @Test
    public void findByIdShouldReturnNotFoundWhenIdDoesNotExist() throws Exception {
       when(service.findById(999L)).thenThrow(new ResourcesNotFoundException(999L));
       mockMvc.perform(get("/orders/999")).andExpect(status().isNotFound());
    }
}
