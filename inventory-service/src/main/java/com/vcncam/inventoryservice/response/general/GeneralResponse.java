package com.vcncam.inventoryservice.response.general;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GeneralResponse <T>{

    private GeneralResponseStatus status;
    private LocalDateTime timestamp;
    private T data;
}
