package ru.yandex.practicum.service;


import ru.yandex.practicum.dto.Pageable;
import ru.yandex.practicum.dto.ProductDto;
import ru.yandex.practicum.dto.SetProductQuantityStateRequest;

import java.util.List;

public interface StoreService {

    List<ProductDto> findProduct(String category, Pageable pageable);

    ProductDto findProduct(String productId);

    ProductDto createProduct(ProductDto productDto);

    ProductDto updateDto(ProductDto productDto);

    void removeProduct(String productId);

    void updateQuantity(SetProductQuantityStateRequest request);
}
