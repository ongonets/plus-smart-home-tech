package ru.yandex.practicum.service;

import ru.yandex.practicum.dto.BookedProductsDto;
import ru.yandex.practicum.dto.ChangeProductQuantityRequest;
import ru.yandex.practicum.dto.ShoppingCartDto;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface ShoppingCartService {

    ShoppingCartDto findShoppingCarte(String username);

    ShoppingCartDto addProductToShoppingCart(String username, Map<UUID, Integer> products);

    ShoppingCartDto removeProductToShoppingCart(String username, List<UUID> products);

    void deactivateShoppingCart(String username);

    ShoppingCartDto updateProductQuantity(String username, ChangeProductQuantityRequest request);

    BookedProductsDto bookProductFromShoppingCart(String username);
}
