package com.clane.walletservice.domain.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppResponse<T> {

    private int status;
    private String message;
    private T data;
    @Builder.Default
    private Object error = new ArrayList<>();


}
