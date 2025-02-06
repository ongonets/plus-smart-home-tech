package ru.yandex.practicum.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.dto.*;
import ru.yandex.practicum.service.WarehouseService;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping(path = "/api/v1/warehouse")
@Slf4j
@RequiredArgsConstructor
public class WarehouseController implements WarehouseOperations {

    private final WarehouseService service;

    @PutMapping
    public void createProduct(@RequestBody NewProductInWarehouseRequest productDto) {
        log.info("Received request to create product: {}", productDto.getProductId());
        service.createProduct(productDto);
    }

    @Override
    public BookedProductsDto checkShoppingCart(ShoppingCartDto shoppingCartDto) {
        log.info("Received request to check shopping cart ID: {}", shoppingCartDto.getShoppingCartId());
        return service.checkShoppingCart(shoppingCartDto);
    }

    @PostMapping("/add")
    public void addProduct(@RequestBody AddProductToWarehouseRequest request) {
        log.info("Received request to add product: {}", request);
        service.addProduct(request);
    }

    @Override
    public AddressDto findAddress() {
        log.info("Received request to find warehouse address");
        return service.findAddress();
    }

    @Override
    public void returnProduct(@RequestBody Map<UUID, Integer> products) {
        log.info("Received request to return product: {}", products);
        service.returnProduct(products);
    }

    @Override
    public void shippedProductToDelivery(@RequestBody ShippedToDeliveryRequest request) {
        log.info("Received request to shipped product for order ID: {}", request.getOrderId());
        service.shippedProductToDelivery(request);
    }

    @Override
    public BookedProductsDto assemblyProduct(@RequestBody AssemblyProductsForOrderRequest request) {
        log.info("Received request to assembly product for order ID: {}", request.getOrderId());
        return service.assemblyProduct(request);
    }
}
