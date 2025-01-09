package ru.yandex.practicum.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class NewProductInWarehouseRequest {

    @NotBlank
    private String productId;

    @Min(1)
    private double weight;

    @NotNull
    private DimensionDto dimension;

    private boolean fragile;
}
