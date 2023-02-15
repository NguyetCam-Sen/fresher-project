package com.vcncam.orderservice.exception;

public class ProductNotInStockException extends CustomException{
    public ProductNotInStockException() {
        super("003", "Product Not In Stock");
    }
}
