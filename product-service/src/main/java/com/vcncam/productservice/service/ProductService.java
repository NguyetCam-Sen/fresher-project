package com.vcncam.productservice.service;

import com.vcncam.productservice.exception.NullDataException;
import com.vcncam.productservice.exception.ProductNotFoundException;
import com.vcncam.productservice.model.Product;
import com.vcncam.productservice.repository.ProductRepository;
import com.vcncam.productservice.request.ProductRequest;
import com.vcncam.productservice.response.ProductResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

    @Autowired
    private final ProductRepository productRepository;

    public ProductResponse createProduct(ProductRequest productRequest) {
        if (productRequest.getName()==null
            || productRequest.getDescription()==null
            || Objects.isNull(productRequest.getPrice())) {
            log.error("Given Data Includes Null Value");
            throw new NullDataException("001", "Given Data Includes Null Value");
        }

        Product product = Product.builder()
                .name(productRequest.getName())
                .description(productRequest.getDescription())
                .price(productRequest.getPrice())
                .build();

        productRepository.save(product);
        log.info("Product {} is created", product.getId());

        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .build();
    }

    public List<ProductResponse> getALlProducts() {
        List<Product> products = productRepository.findAll();
        log.info( "Get All Products");

        return products.stream()
                .map(this::mapToProductResponse)
                .collect(Collectors.toList());
    }

    private ProductResponse mapToProductResponse(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .build();
    }

    public ProductResponse editProduct(ProductRequest productRequest) {
        checkNullProductId(productRequest.getId());
        Product product = findProductById(productRequest.getId());

        product.setName(productRequest.getName() != null ? productRequest.getName() : product.getName());
        product.setDescription(productRequest.getDescription() != null ?
                productRequest.getDescription() : product.getDescription());
        product.setPrice(Objects.nonNull(productRequest.getPrice()) ?
                productRequest.getPrice() : product.getPrice());

        productRepository.save(product);
        log.info("Edited Product {} Successfully", product.getId());

        return mapToProductResponse(product);
    }

    private Product findProductById(String id) {
        return productRepository.findById(id).orElseThrow(ProductNotFoundException::new);
    }

    private void checkNullProductId(String id) {
        if (id == null || id.equals("")) {
            log.error("Product Id Must Not Be Null");
            throw new NullDataException("001", "Product Id Must Not Be Null");
        }
    }

    public String deleteProduct(String id) {
        checkNullProductId(id);

        productRepository.deleteById(id);
        log.info("Delete Product {} Successfully", id);
        return String.format("Delete Product %s Successfully", id);
    }
}
