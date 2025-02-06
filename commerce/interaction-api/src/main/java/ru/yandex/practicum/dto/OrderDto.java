package ru.yandex.practicum.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Map;
import java.util.UUID;

@Data
public class OrderDto {

    @NotBlank
    private UUID orderId;

    @NotBlank
    private UUID shoppingCartId;

    @NotNull
    private Map<UUID, Integer> products;

    private UUID paymentId;

    private UUID deliveryId;

    private OrderState state;

    @NotNull
    private double deliveryVolume;

    @NotNull
    private double deliveryWeight;

    @NotNull
    private boolean fragile;

    private double totalPrice;

    private double deliveryPrice;

    private double productPrice;
}
