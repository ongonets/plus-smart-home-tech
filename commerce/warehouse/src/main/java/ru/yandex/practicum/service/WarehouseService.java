package ru.yandex.practicum.service;

import ru.yandex.practicum.dto.*;

import java.util.Map;
import java.util.UUID;

public interface WarehouseService {

    void createProduct(NewProductInWarehouseRequest productDto);

    BookedProductsDto checkShoppingCart(ShoppingCartDto shoppingCartDto);

    void addProduct(AddProductToWarehouseRequest request);

    AddressDto findAddress();

    void returnProduct(Map<UUID, Integer> products);

    void shippedProductToDelivery(ShippedToDeliveryRequest request);

    BookedProductsDto assemblyProduct(AssemblyProductsForOrderRequest request);
}
