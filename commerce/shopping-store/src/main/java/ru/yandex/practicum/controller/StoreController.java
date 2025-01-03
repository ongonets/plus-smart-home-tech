package ru.yandex.practicum.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.dto.Pageable;
import ru.yandex.practicum.dto.ProductDto;
import ru.yandex.practicum.dto.QuantityState;
import ru.yandex.practicum.dto.SetProductQuantityStateRequest;
import ru.yandex.practicum.service.StoreService;

import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/shopping-store")
@Slf4j
@RequiredArgsConstructor
public class StoreController {

    private final StoreService service;

    @GetMapping
    public List<ProductDto> findProduct(@RequestParam String category,
                                         @RequestParam(defaultValue = "0") int page,
                                         @RequestParam(defaultValue = "0") int size,
                                         @RequestParam(defaultValue = "rating") List<String> sort) {
        Pageable pageable = new Pageable(page, size, sort);
        return service.findProduct(category, pageable);
    }

    @PutMapping
    public ProductDto createProduct(@RequestBody ProductDto productDto) {
        log.info("Received request to create product: {}", productDto);
        return service.createProduct(productDto);
    }


    @PostMapping
    public ProductDto updateProduct(@RequestBody ProductDto productDto) {
        log.info("Received request to update product: {}", productDto);
        return service.updateDto(productDto);
    }

    @PostMapping(path = "/removeProductFromStore")
    public void removeProduct(@RequestParam String productId) {
        log.info("Received request to remove product ID: {}", productId);
        service.removeProduct(productId);
    }

    @PostMapping(path = "/quantityState")
    public void updateProductQuantity(@RequestParam String productId,
                                      @RequestParam QuantityState quantityState) {
        SetProductQuantityStateRequest request = new SetProductQuantityStateRequest(productId, quantityState);
        log.info("Received request to update quantity: {}", request);
        service.updateQuantity(request);
    }

    @GetMapping(path = "/{productID}")
    public ProductDto findProduct(@PathVariable String productId) {
        return service.findProduct(productId);
    }

}
