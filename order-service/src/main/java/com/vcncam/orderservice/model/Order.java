package com.vcncam.orderservice.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table (name = "`order`")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "order_number")
    private String orderNumber;
    @Transient
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<OrderLineItems> orderLineItemsList;
}
