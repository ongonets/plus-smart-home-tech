package ru.yandex.practicum.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BookedProductsDto {

    @NotNull
    private double deliveryVolume;

    @NotNull
    private double deliveryWeight;

    @NotNull
    private boolean fragile;
}
