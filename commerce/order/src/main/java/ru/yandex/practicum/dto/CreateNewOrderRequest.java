package ru.yandex.practicum.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateNewOrderRequest {

    @NotNull
    private ShoppingCartDto shoppingCart;

    @NotNull
    private AddressDto deliveryAddress;
}
