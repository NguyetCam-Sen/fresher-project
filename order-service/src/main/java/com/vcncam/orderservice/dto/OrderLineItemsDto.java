package com.vcncam.orderservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderLineItemsDto {
    private Long id;
    @NotBlank(message = "Sku Code is mandatory")
    private String skuCode;
    @NotNull(message = "Price is mandatory")
    private BigDecimal price;
    @NotNull(message = "Quantity is mandatory")
    private Integer quantity;
}
