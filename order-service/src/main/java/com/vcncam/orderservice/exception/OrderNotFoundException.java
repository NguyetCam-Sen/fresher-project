package com.vcncam.orderservice.exception;

public class OrderNotFoundException extends CustomException {
    public OrderNotFoundException() {
        super("002", "Order Not Found");
    }
}
