package com.vcncam.productservice.response.general;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GeneralResponseStatus {

    private String code;
    private String message;
}
