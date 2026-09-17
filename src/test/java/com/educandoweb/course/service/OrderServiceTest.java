package com.educandoweb.course.service;

import com.educandoweb.course.entities.Order;
import com.educandoweb.course.entities.enums.OrderStatus;
import com.educandoweb.course.repository.OrderRepository;
import com.educandoweb.course.service.exceptions.ResourcesNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderService orderService;

    @Test
    public void findByIdShouldReturnOrderWhenIdExists(){
        Order order = new Order(
                1L,
                Instant.parse("2019-06-20T19:53:07Z"),
                OrderStatus.PAID,
                null
        );
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        Order result = orderService.findById(1l);
        assertEquals(1L, result.getId());
        assertEquals(Instant.parse("2019-06-20T19:53:07Z"), result.getMoment());
        assertEquals(OrderStatus.PAID, result.getOrderStatus());
        assertNull(result.getClient());
    }

    @Test
    public void findByIdShouldThrowResourcesNotFoundExceptionWhenIdDoesNotExist() {
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourcesNotFoundException.class, () -> orderService.findById(1L));
    }

    @Test
    public void findAllShouldReturnOrderList() {
        Order order1 = new Order(
                1L,
                Instant.parse("2019-06-20T19:53:07Z"),
                OrderStatus.PAID,
                null
        );
        Order order2 = new Order(
                2L,
                Instant.parse("2019-07-21T03:42:10Z"),
                OrderStatus.WAITING_PAYMENT,
                null
        );
        when(orderRepository.findAll()).thenReturn(List.of(order1, order2));
        List<Order> result = orderService.findAll();

        assertEquals(2, result.size());
        assertEquals(1L, result.getFirst().getId());
        assertEquals(Instant.parse("2019-06-20T19:53:07Z"), result.getFirst().getMoment());
        assertEquals(OrderStatus.PAID, result.getFirst().getOrderStatus());
        assertNull(result.getFirst().getClient());

        assertEquals(2L, result.get(1).getId());
        assertEquals(Instant.parse("2019-07-21T03:42:10Z"), result.get(1).getMoment());
        assertEquals(OrderStatus.WAITING_PAYMENT, result.get(1).getOrderStatus());
        assertNull(result.get(1).getClient());
    }
}
