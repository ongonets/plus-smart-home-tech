package ru.yandex.practicum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.dto.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class WarehouseServiceImpl implements WarehouseService{

    @Override
    public void createProduct(NewProductInWarehouseRequest productDto) {

    }

    @Override
    public BookedProductsDto checkShoppingCart(ShoppingCartDto shoppingCartDto) {
        return null;
    }

    @Override
    public void addProduct(AddProductToWarehouseRequest request) {

    }

    @Override
    public AddressDto findAddress() {
        return null;
    }
}
