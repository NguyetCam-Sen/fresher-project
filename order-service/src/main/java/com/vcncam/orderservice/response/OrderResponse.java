package com.vcncam.orderservice.response;

import com.vcncam.orderservice.dto.OrderLineItemsDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse {
    private Long id;
    private String orderNumber;
    private List<OrderLineItemsDto> orderLineItemsList;
}
