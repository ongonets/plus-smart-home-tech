package ru.yandex.practicum.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SetProductQuantityStateRequest {
    private String productId;
    private QuantityState quantityState;
}
