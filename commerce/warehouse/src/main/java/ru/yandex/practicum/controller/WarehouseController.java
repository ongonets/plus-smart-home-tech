package ru.yandex.practicum.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.dto.*;
import ru.yandex.practicum.service.WarehouseService;

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

    @GetMapping("/address")
    public AddressDto findAddress() {
        log.info("Received request to find warehouse address");
        return service.findAddress();
    }


}
