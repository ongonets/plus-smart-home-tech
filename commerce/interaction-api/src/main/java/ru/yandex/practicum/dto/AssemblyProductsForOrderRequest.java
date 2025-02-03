package ru.yandex.practicum.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;
import java.util.UUID;

@Data
@AllArgsConstructor
public class AssemblyProductsForOrderRequest {

    @NotNull
    private UUID orderId;

    @NotNull
    private Map<UUID, Integer> products;
}
