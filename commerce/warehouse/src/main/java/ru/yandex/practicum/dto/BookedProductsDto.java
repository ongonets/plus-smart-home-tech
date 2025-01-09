package ru.yandex.practicum.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BookedProductsDto {

    @NotNull
    private int deliveryVolume;

    @NotNull
    private int deliveryWeight;

    @NotNull
    private boolean fragile;
}
