package ru.yandex.practicum.service;

import ru.yandex.practicum.dto.*;

public interface WarehouseService {

    void createProduct(NewProductInWarehouseRequest productDto);

    BookedProductsDto checkShoppingCart(ShoppingCartDto shoppingCartDto);

    void addProduct(AddProductToWarehouseRequest request);

    AddressDto findAddress();
}
