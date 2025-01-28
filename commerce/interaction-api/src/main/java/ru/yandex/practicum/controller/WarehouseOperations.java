package ru.yandex.practicum.controller;

import feign.FeignException;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.dto.*;

import java.util.Map;
import java.util.UUID;

@FeignClient(name = "warehouse", path = "/api/v1/warehouse")
public interface WarehouseOperations {

    @PostMapping("/check")
    BookedProductsDto checkShoppingCart(@RequestBody ShoppingCartDto shoppingCartDto) throws FeignException;

    @GetMapping("/address")
    AddressDto findAddress();

    @PostMapping("/return")
    void returnProduct(@RequestBody Map<UUID, Integer> products);

    @PostMapping("/shipped")
    void shippedProductToDelivery(@RequestBody ShippedToDeliveryRequest request);

    @PostMapping("/assembly")
    BookedProductsDto assemblyProduct(@RequestBody AssemblyProductsForOrderRequest request);
}
