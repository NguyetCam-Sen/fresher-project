package com.vcncam.productservice.exception;

public class ProductNotFoundException extends CustomException{
    public ProductNotFoundException() {
        super("002", "Product Not Found");
    }
}
