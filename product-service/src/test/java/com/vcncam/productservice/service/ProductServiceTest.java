package com.vcncam.productservice.service;

import com.vcncam.productservice.exception.NullDataException;
import com.vcncam.productservice.exception.ProductNotFoundException;
import com.vcncam.productservice.model.Product;
import com.vcncam.productservice.repository.ProductRepository;
import com.vcncam.productservice.request.ProductRequest;
import com.vcncam.productservice.response.ProductResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mock;
import org.mockito.stubbing.Answer;
import org.testcontainers.shaded.org.bouncycastle.voms.VOMSAttribute;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ProductServiceTest {

    private ProductService productService;
    private ProductRepository productRepository;

    @BeforeEach
    void init() {
        this.productRepository = mock(ProductRepository.class);
        this.productService = new ProductService(productRepository);
    }

    @Nested
    class CreateProductTest {
        @Test
        void should_ReturnCreatedProduct_When_CreateProduct() {
            ProductRequest productRequest = ProductRequest.builder()
                    .name("iPhone 14")
                    .description("iPhone 14")
                    .price(new BigDecimal(1400))
                    .build();
            ProductResponse expect = ProductResponse.builder()
                    .name(productRequest.getName())
                    .description(productRequest.getDescription())
                    .price(productRequest.getPrice())
                    .build();

            ProductResponse actual = productService.createProduct(productRequest);

            assertAll(
                    () -> assertEquals(expect.getName(), actual.getName()),
                    () -> assertEquals(expect.getDescription(), actual.getDescription()),
                    () -> assertEquals(expect.getPrice(), actual.getPrice())
            );
        }

        @Test
        void should_ThrowNullDataException_When_ProductRequestHasNullField() {
            ProductRequest productRequest = ProductRequest.builder()
                    .name("iPhone 12")
                    .build();

            Executable execute = () -> productService.createProduct(productRequest);

            assertThrows(NullDataException.class, execute);
        }
    }

    @Nested
    class EditProductTest {
        @Test
        void should_ReturnEditedProduct_When_EditProduct() {
            ProductRequest productRequest = ProductRequest.builder()
                    .id("test")
                    .name("Test product")
                    .description("Test product")
                    .price(new BigDecimal(200))
                    .build();
            when(productRepository.findById(productRequest.getId()))
                    .thenReturn(Optional.of(new Product(
                            productRequest.getId(),
                            productRequest.getName(),
                            productRequest.getDescription(),
                            productRequest.getPrice()
                    )));
            ProductResponse expect = ProductResponse.builder()
                    .id(productRequest.getId())
                    .name(productRequest.getName())
                    .description(productRequest.getDescription())
                    .price(productRequest.getPrice())
                    .build();

            ProductResponse actual = productService.editProduct(productRequest);

            assertAll(
                    () -> assertEquals(expect.getId(), actual.getId()),
                    () -> assertEquals(expect.getName(), actual.getName()),
                    () -> assertEquals(expect.getDescription(), actual.getDescription()),
                    () -> assertEquals(expect.getPrice(), actual.getPrice())
            );
        }

        @Test
        void should_ThrowNullDataException_When_EditProductHasNullId() {
            ProductRequest productRequest = new ProductRequest();

            Executable execute = () -> productService.editProduct(productRequest);

            assertThrows(NullDataException.class, execute);
        }

        @Test
        void should_ThrowNotFoundException_When_EditedProductNotExist() {
            ProductRequest productRequest = new ProductRequest();
            productRequest.setId("test");

            Executable execute = () -> productService.editProduct(productRequest);

            assertThrows(ProductNotFoundException.class, execute);
        }
    }

    @Test
    void should_ThrowNullDataException_When_DeleteProductIsNull() {
        String id = "";

        Executable execute = () -> productService.deleteProduct(id);

        assertThrows(NullDataException.class, execute);
    }
}