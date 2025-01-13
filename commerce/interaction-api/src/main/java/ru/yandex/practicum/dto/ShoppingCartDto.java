package ru.yandex.practicum.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Map;

@Data
public class ShoppingCartDto {

    @NotBlank
    private String shoppingCartId;

    @NotNull
    private Map<String, Integer> products;
}
