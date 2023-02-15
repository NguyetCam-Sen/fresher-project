package com.vcncam.orderservice.model;

import jakarta.persistence.*;
import lombok.*;

import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;

@Entity
@Table (name = "order_line_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderLineItems {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "sku_code")
    private String skuCode;
    private BigDecimal price;
    private Integer quantity;
    @Column(name = "order_id")
    private Long orderId;
}
