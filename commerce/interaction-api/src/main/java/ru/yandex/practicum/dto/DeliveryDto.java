package ru.yandex.practicum.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class DeliveryDto {

    @NotNull
    private UUID orderId;

    private UUID deliveryId;

    @NotNull
    private AddressDto fromAddress;

    @NotNull
    private AddressDto toAddress;

    @NotNull
    private double deliveryVolume;

    @NotNull
    private double deliveryWeight;

    @NotNull
    private boolean fragile;

    private DeliveryState deliveryState;
}
