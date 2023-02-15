package com.vcncam.orderservice.controller;

import com.vcncam.orderservice.request.OrderRequest;
import com.vcncam.orderservice.response.OrderResponse;
import com.vcncam.orderservice.response.general.GeneralResponse;
import com.vcncam.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController extends BaseController{
    private final OrderService orderService;
    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public OrderResponse placeOrder(@RequestBody OrderRequest orderRequest) {
        return orderService.placeOrder(orderRequest);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public GeneralResponse<OrderResponse> getOrderById(@PathVariable(value = "id") Long orderId) {
        return success(orderService.getOrderById(orderId));
    }

    @GetMapping("/orders")
    public GeneralResponse<List<OrderResponse>> getAllOrders() {
        return success(orderService.getAllOrders());
    }

    @DeleteMapping("/delete/{id}")
    public GeneralResponse deleteOrder(@PathVariable(value = "id") Long orderId) {
        orderService.deleteOrder(orderId);
        return success("Deleted Order " + orderId);
    }
}
