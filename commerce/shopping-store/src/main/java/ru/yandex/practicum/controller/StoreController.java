package ru.yandex.practicum.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.dto.PageableDto;
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
                                         @RequestParam(defaultValue = "10") int size,
                                         @RequestParam(defaultValue = "rating") List<String> sort) {
        PageableDto pageableDto = new PageableDto(page, size, sort);
        return service.findProduct(category, pageableDto);
    }

    @PutMapping
    public ProductDto createProduct(@RequestBody ProductDto productDto) {
        log.info("Received request to create product: {}", productDto);
        return service.createProduct(productDto);
    }


    @PostMapping
    public ProductDto updateProduct(@RequestBody ProductDto productDto) {
        log.info("Received request to update product: {}", productDto);
        return service.updateProduct(productDto);
    }

    @PostMapping("/removeProductFromStore")
    public void removeProduct(@RequestBody String productId) {
        productId = productId.substring(1, productId.length()-1);
        log.info("Received request to remove product ID: {}", productId);
        service.removeProduct(productId);
    }

    @PostMapping("/quantityState")
    public void updateProductQuantity(@RequestParam String productId,
                                      @RequestParam QuantityState quantityState) {
        SetProductQuantityStateRequest request = new SetProductQuantityStateRequest(productId, quantityState);
        log.info("Received request to update quantity: {}", request);
        service.updateQuantity(request);
    }

    @GetMapping("/{productId}")
    public ProductDto findProduct(@PathVariable String productId) {
        return service.findProduct(productId);
    }

}
