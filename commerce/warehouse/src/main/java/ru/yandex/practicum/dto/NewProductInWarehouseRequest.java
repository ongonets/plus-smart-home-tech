package ru.yandex.practicum.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class NewProductInWarehouseRequest {

    @NotBlank
    private UUID productId;

    @Min(1)
    private double weight;

    @NotNull
    private DimensionDto dimension;

    private boolean fragile;
}
