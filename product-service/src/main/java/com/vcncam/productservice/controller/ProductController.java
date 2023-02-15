package com.vcncam.productservice.controller;

import com.vcncam.productservice.request.ProductRequest;
import com.vcncam.productservice.response.ProductResponse;
import com.vcncam.productservice.response.general.GeneralResponse;
import com.vcncam.productservice.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor
public class ProductController extends BaseController {

    private final ProductService productService;

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public GeneralResponse<ProductResponse> createProduct(@RequestBody ProductRequest productRequest) {
        return success(productService.createProduct(productRequest));
    }

    @GetMapping("/products")
    @ResponseStatus(HttpStatus.OK)
    public GeneralResponse<List<ProductResponse>> getAllProducts() {
        return success(productService.getALlProducts());
    }

    @PutMapping("/edit")
    public GeneralResponse<ProductResponse> editProduct(@RequestBody ProductRequest productRequest) {
        return success(productService.editProduct(productRequest));
    }

    @DeleteMapping("/delete/{id}")
    public GeneralResponse<Object> deleteProduct(@PathVariable(value = "id") String id) {
        return success(productService.deleteProduct(id));
    }
}
