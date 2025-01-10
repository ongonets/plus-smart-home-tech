package ru.yandex.practicum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.dto.*;
import ru.yandex.practicum.exception.NoSpecifiedProductInWarehouseException;
import ru.yandex.practicum.exception.ProductInShoppingCartLowQuantityInWarehouse;
import ru.yandex.practicum.mapper.WarehouseMapper;
import ru.yandex.practicum.model.WarehouseProduct;
import ru.yandex.practicum.repository.WarehouseRepository;

import java.security.SecureRandom;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Stream;

@Service
@Slf4j
@RequiredArgsConstructor
public class WarehouseServiceImpl implements WarehouseService {

    private static final AddressDto[] ADDRESSES =
            new AddressDto[]{
                    new AddressDto("ADDRESS_1",
                            "ADDRESS_1",
                            "ADDRESS_1",
                            "ADDRESS_1",
                            "ADDRESS_1"),
                    new AddressDto("ADDRESS_2",
                            "ADDRESS_2",
                            "ADDRESS_2",
                            "ADDRESS_2",
                            "ADDRESS_2")};

    private static final AddressDto CURRENT_ADDRESS =
            ADDRESSES[Random.from(new SecureRandom()).nextInt(0, 1)];

    private final WarehouseRepository repository;
    private final WarehouseMapper mapper;

    @Override
    public void createProduct(NewProductInWarehouseRequest productDto) {
        checkProductExist(productDto.getProductId());
        WarehouseProduct product = mapper.map(productDto);
        repository.save(product);
        log.info("Saved product ID: {}", product.getId());
    }

    @Override
    public BookedProductsDto checkShoppingCart(ShoppingCartDto shoppingCartDto) {
        String shoppingCartId = shoppingCartDto.getShoppingCartId();
        Map<String, Integer> products = shoppingCartDto.getProducts();
        Supplier<Stream<WarehouseProduct>> streamSupplier = () -> repository.findAllById(products.keySet()).stream();
        checkProductQuantity(streamSupplier.get(), products, shoppingCartId);
        BookedProductsDto bookedProductsDto = calculateDeliveryParams(streamSupplier);
        log.info("Delivery parameters for shopping cart ID: {} are calculated", shoppingCartId);
        return bookedProductsDto;
    }


    @Override
    public void addProduct(AddProductToWarehouseRequest request) {
        WarehouseProduct product = getProduct(request.getProductId());
        addQuantity(product, request.getQuantity());
        repository.save(product);
        log.info("Add product ID: {} to warehouse, new quantity = {} ", product.getId(), product.getQuantity());
    }

    @Override
    public AddressDto findAddress() {
        return CURRENT_ADDRESS;
    }

    private WarehouseProduct getProduct(String productId) {
        return repository.findById(productId)
                .orElseThrow(() ->
                        {
                            log.error("Not found product ID: {} ", productId);
                            return new NoSpecifiedProductInWarehouseException(
                                    String.format("Not found product ID:  %s", productId)
                            );
                        }
                );
    }

    private void checkProductExist(String productId) {
        if (repository.existsById(productId)) {
            log.error("Product ID: {} already exist", productId);
            throw new NoSpecifiedProductInWarehouseException(String.format("Product ID: %s already exist", productId));
        }
    }

    private void addQuantity(WarehouseProduct product, int newQuantity) {
        int oldQuantity = product.getQuantity();
        product.setQuantity(oldQuantity + newQuantity);
    }

    private void checkProductQuantity(Stream<WarehouseProduct> stream,
                                      Map<String, Integer> products,
                                      String shoppingCartId) {
        if (stream.anyMatch(product -> product.getQuantity() < products.get(product.getId()))) {
            log.error("Quantity of products is less than necessary for shopping cart ID: {}", shoppingCartId);
            throw new ProductInShoppingCartLowQuantityInWarehouse(
                    String.format("Quantity of products is less than necessary for shopping cart ID: %s",
                            shoppingCartId)
            );
        }
    }

    private BookedProductsDto calculateDeliveryParams(Supplier<Stream<WarehouseProduct>> streamSupplier) {
        Double deliveryVolume = streamSupplier.get()
                .map(product -> product.getWidth() * product.getHeight() * product.getDepth())
                .reduce(0.0, Double::sum);
        Double deliveryWeight = streamSupplier.get()
                .map(WarehouseProduct::getWeight)
                .reduce(0.0, Double::sum);
        boolean isFragile = streamSupplier.get().anyMatch(WarehouseProduct::isFragile);
        return new BookedProductsDto(deliveryVolume, deliveryWeight, isFragile);
    }
}
