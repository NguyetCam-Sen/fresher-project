package com.vcncam.orderservice.service;

import com.vcncam.orderservice.dto.InventoryResponse;
import com.vcncam.orderservice.dto.OrderLineItemsDto;
import com.vcncam.orderservice.exception.NullDataException;
import com.vcncam.orderservice.exception.OrderNotFoundException;
import com.vcncam.orderservice.exception.ProductNotInStockException;
import com.vcncam.orderservice.model.Order;
import com.vcncam.orderservice.model.OrderLineItems;
import com.vcncam.orderservice.repository.OrderLineItemsRepository;
import com.vcncam.orderservice.repository.OrderRepository;
import com.vcncam.orderservice.request.OrderRequest;
import com.vcncam.orderservice.response.OrderResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderLineItemsRepository orderLineItemsRepository;
    private final WebClient.Builder webClientBuilder;

    public OrderResponse placeOrder(OrderRequest orderRequest) {
        if (orderRequest.getOrderLineItemsDtoList().isEmpty()) {
            log.error("Order Line Item List Is Empty");
            throw new NullDataException("001", "Order Line Items List is Empty");
        }
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());

        List<String> skuCodeList = orderRequest.getOrderLineItemsDtoList().stream()
                .map(OrderLineItemsDto::getSkuCode).toList();

        // Call Inventory Service, place an order if the product
        // is in stock
        InventoryResponse[] inventoryResponseArray = webClientBuilder.build().get()
                .uri("http://inventory-service/api/inventory",
                        uriBuilder -> uriBuilder.queryParam("skuCode", skuCodeList).build())
                .retrieve()
                .bodyToMono(InventoryResponse[].class)
                .block();

        assert inventoryResponseArray != null;
        boolean allProductsInStock = Arrays.stream(inventoryResponseArray).allMatch(InventoryResponse::isInStock);

        if (allProductsInStock) {
            orderRepository.save(order);
        } else {
            log.error("Product is not in stock");
            throw new ProductNotInStockException();
        }
        List<OrderLineItems> orderLineItems = orderRequest.getOrderLineItemsDtoList().stream()
                .map(orderLineItemsDto
                        -> mapToOrderLineItems(orderLineItemsDto, order.getId()))
                .toList();
        order.setOrderLineItemsList(orderLineItems);

        try {
            orderLineItemsRepository.saveAll(order.getOrderLineItemsList());
        } catch (Exception e) {
            log.error("Given Data Includes Null Value");
            throw new NullDataException("001", e.getMessage());
        }

        log.info("Placed Order {} Successfully", order.getId());

        return OrderResponse.builder()
                .id(order.getId())
                .orderNumber(order.getOrderNumber())
                .orderLineItemsList(mapToOrderLineItemsDtoList(order.getOrderLineItemsList()))
                .build();
    }

    private OrderLineItems mapToOrderLineItems(OrderLineItemsDto orderLineItemsDto, Long orderId) {
        return OrderLineItems.builder()
                .skuCode(orderLineItemsDto.getSkuCode())
                .price(orderLineItemsDto.getPrice())
                .quantity(orderLineItemsDto.getQuantity())
                .orderId(orderId)
                .build();
    }

    private List<OrderLineItemsDto> mapToOrderLineItemsDtoList(List<OrderLineItems> orderLineItemsList) {
        return orderLineItemsList.stream().map(orderLineItems -> OrderLineItemsDto.builder()
                .id(orderLineItems.getId())
                .skuCode(orderLineItems.getSkuCode())
                .price(orderLineItems.getPrice())
                .quantity(orderLineItems.getQuantity())
                .build()).collect(Collectors.toList());
    }

    public OrderResponse getOrderById(Long id) {
        checkOrderIdNull(id);

        Order order = orderRepository.findById(id).orElseThrow(OrderNotFoundException::new);

        Optional<List<OrderLineItems>> orderLineItemsOptional = orderLineItemsRepository.findByOrderId(id);
        orderLineItemsOptional.ifPresent(order::setOrderLineItemsList);
        log.info("Getting Order {}", order.getId());

        return OrderResponse.builder()
                .id(order.getId())
                .orderNumber(order.getOrderNumber())
                .orderLineItemsList(mapToOrderLineItemsDtoList(order.getOrderLineItemsList()))
                .build();
    }

    private static void checkOrderIdNull(Long id) {
        if (id == null || id == 0) {
            throw new NullDataException("001", "Id Must Not Be Null");
        }
    }

    public List<OrderResponse> getAllOrders() {
        List<Order> orderList = orderRepository.findAll();
        return orderList.stream().map(order -> OrderResponse.builder()
                .id(order.getId())
                .orderNumber(order.getOrderNumber())
                .build()).collect(Collectors.toList());
    }

    public void deleteOrder(Long orderId) {
        checkOrderIdNull(orderId);

        boolean isExist = orderRepository.existsById(orderId);
        if (!isExist) {
            throw new OrderNotFoundException();
        }

        orderLineItemsRepository.deleteAllByOrderId(orderId);
        orderRepository.deleteById(orderId);
        log.info("Deleted Order {}", orderId);
    }
}
