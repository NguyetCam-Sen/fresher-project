package com.vcncam.orderservice.service;

import com.vcncam.orderservice.dto.OrderLineItemsDto;
import com.vcncam.orderservice.exception.NullDataException;
import com.vcncam.orderservice.exception.OrderNotFoundException;
import com.vcncam.orderservice.model.Order;
import com.vcncam.orderservice.repository.OrderLineItemsRepository;
import com.vcncam.orderservice.repository.OrderRepository;
import com.vcncam.orderservice.request.OrderRequest;
import com.vcncam.orderservice.response.OrderResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class OrderServiceTest {
    private OrderService orderService;
    private OrderRepository orderRepository;
    private OrderLineItemsRepository orderLineItemsRepository;
    private WebClient.Builder webClientBuilder;

    @BeforeEach
    void init() {
        this.orderRepository = mock(OrderRepository.class);
        this.orderLineItemsRepository = mock(OrderLineItemsRepository.class);
        this.webClientBuilder = mock(WebClient.Builder.class);
        this.orderService = new OrderService(orderRepository, orderLineItemsRepository, webClientBuilder);
    }

    @Nested
    class PlaceOrderTest {
        @Test
        void should_ReturnOrderedItems_When_OrderLineItemsListNotEmpty() {
            OrderLineItemsDto orderLineItemsDto = OrderLineItemsDto.builder()
                    .skuCode("iPhone_14")
                    .price(new BigDecimal(1400))
                    .quantity(2)
                    .build();
            List<OrderLineItemsDto> orderLineItemsDtoList = List.of(
                    orderLineItemsDto, orderLineItemsDto
            );
            OrderRequest request = new OrderRequest(orderLineItemsDtoList);

            OrderResponse actual = orderService.placeOrder(request);

            assertAll(
                    () -> assertNotNull(actual.getOrderNumber()),
                    () -> assertNotNull(actual.getOrderLineItemsList())
            );
        }

        @Test
        void should_ThrowNullDataException_When_OrderLineItemsListIncludesNullValue() {
            OrderRequest request = new OrderRequest(new ArrayList<>());
            when(orderLineItemsRepository.saveAll(anyList()))
                    .thenThrow(NullDataException.class);

            Executable execute = () -> orderService.placeOrder(request);

            assertThrows(NullDataException.class, execute);
        }

        @Test
        void should_ThrowNullDataException_When_OrderLineItemsListEmpty() {
            OrderRequest request = new OrderRequest();

            Executable execute = () -> orderService.placeOrder(request);

            assertThrows(NullDataException.class, execute);
        }
    }

    @Nested
    class GetOrderByIdTest {
        @Test
        void should_ReturnOrderResponse_When_IdExist() {
            Order order = Order.builder()
                    .id((long) (Math.random()*10))
                    .orderNumber(UUID.randomUUID().toString())
                    .orderLineItemsList(new ArrayList<>())
                    .build();

            OrderResponse expect = OrderResponse.builder()
                            .id(order.getId())
                            .orderNumber(order.getOrderNumber())
                            .orderLineItemsList(new ArrayList<>())
                            .build();

            when(orderRepository.findById(order.getId()))
                    .thenReturn(Optional.of(order));

            OrderResponse actual = orderService.getOrderById(order.getId());

            assertAll(
                    () -> assertEquals(expect.getId(), actual.getId()),
                    () -> assertEquals(expect.getOrderNumber(), actual.getOrderNumber())
            );
        }

        @Test
        void should_ThrowNullDataException_When_GivenIdNull() {
            Order order = new Order();

            Executable execute = () -> orderService.getOrderById(order.getId());

            assertThrows(NullDataException.class, execute);
        }

        @Test
        void should_ThrowOrderNotFound_When_IdNotExist() {
            when(orderRepository.findById(any()))
                    .thenThrow(OrderNotFoundException.class);

            Executable execute = () -> orderService.getOrderById(1L);

            assertThrows(OrderNotFoundException.class, execute);
        }
    }

}