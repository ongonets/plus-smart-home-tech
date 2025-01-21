package ru.yandex.practicum.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AddProductToWarehouseRequest {

    @NotNull
    private String productId;

    @Min(1)
    private int quantity;
}
