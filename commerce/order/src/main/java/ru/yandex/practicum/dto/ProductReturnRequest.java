package ru.yandex.practicum.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Map;

@Data
public class ProductReturnRequest {

    @NotBlank
    private String orderId;

    @NotNull
    private Map<String, Integer> products;
}
