package com.vcncam.orderservice.request;

import com.vcncam.orderservice.dto.OrderLineItemsDto;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class  OrderRequest {
    private List<OrderLineItemsDto> orderLineItemsDtoList = new ArrayList<>();
}
