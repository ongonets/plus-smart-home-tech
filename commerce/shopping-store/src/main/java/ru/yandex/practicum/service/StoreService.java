package ru.yandex.practicum.service;


import ru.yandex.practicum.dto.PageableDto;
import ru.yandex.practicum.dto.ProductDto;
import ru.yandex.practicum.dto.SetProductQuantityStateRequest;

import java.util.List;
import java.util.UUID;

public interface StoreService {

    List<ProductDto> findProduct(String category, PageableDto pageableDto);

    ProductDto findProduct(UUID productId);

    ProductDto createProduct(ProductDto productDto);

    ProductDto updateProduct(ProductDto productDto);

    void removeProduct(UUID productId);

    void updateQuantity(SetProductQuantityStateRequest request);
}
