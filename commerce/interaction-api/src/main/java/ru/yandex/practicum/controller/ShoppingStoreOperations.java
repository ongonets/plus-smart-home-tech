package ru.yandex.practicum.controller;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.yandex.practicum.dto.QuantityState;

@FeignClient(name = "shopping-store", path = "/api/v1/shopping-store")
public interface ShoppingStoreOperations {

    @PostMapping("/quantityState")
    void updateProductQuantity(@RequestParam String productId, @RequestParam QuantityState quantityState);
}
