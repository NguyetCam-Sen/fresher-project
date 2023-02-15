package com.vcncam.productservice.request;

import jakarta.annotation.Nonnull;
import lombok.*;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequest {

    private String id;
    private String name;
    private String description;
    private BigDecimal price;
}
