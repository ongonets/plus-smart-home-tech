package ru.yandex.practicum.service;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.controller.ShoppingStoreOperations;
import ru.yandex.practicum.dto.*;
import ru.yandex.practicum.exception.NoOrderBookingInWarehouseException;
import ru.yandex.practicum.exception.NoSpecifiedProductInWarehouseException;
import ru.yandex.practicum.exception.ProductInShoppingCartLowQuantityInWarehouse;
import ru.yandex.practicum.exception.SpecifiedProductAlreadyInWarehouseException;
import ru.yandex.practicum.mapper.WarehouseMapper;
import ru.yandex.practicum.model.OrderBooking;
import ru.yandex.practicum.model.WarehouseProduct;
import ru.yandex.practicum.repository.WarehouseBookingRepository;
import ru.yandex.practicum.repository.WarehouseProductRepository;

import java.security.SecureRandom;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
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

    private final WarehouseProductRepository productRepository;
    private final WarehouseBookingRepository bookingRepository;
    private final WarehouseMapper mapper;
    private final ShoppingStoreOperations shoppingStoreClient;

    @Override
    public void createProduct(NewProductInWarehouseRequest productDto) {
        checkProductExist(productDto.getProductId());
        WarehouseProduct product = mapper.map(productDto);
        productRepository.save(product);
        log.info("Saved product ID: {}", product.getId());
    }

    @Override
    public BookedProductsDto checkShoppingCart(ShoppingCartDto shoppingCartDto) {
        UUID shoppingCartId = shoppingCartDto.getShoppingCartId();
        Map<UUID, Integer> products = shoppingCartDto.getProducts();
        Supplier<Stream<WarehouseProduct>> streamSupplier =
                () -> productRepository.findAllById(products.keySet()).stream();
        checkProductQuantity(streamSupplier.get(), products);
        BookedProductsDto bookedProductsDto = calculateDeliveryParams(streamSupplier, products);
        log.info("Delivery parameters for shopping cart ID: {} are calculated", shoppingCartId);
        return bookedProductsDto;
    }


    @Override
    public void addProduct(AddProductToWarehouseRequest request) {
        WarehouseProduct product = getProduct(request.getProductId());
        addQuantity(product, request.getQuantity());
        productRepository.save(product);
        updateQuantityInShoppingStore(product);
        log.info("Add product ID: {} to warehouse, new quantity = {} ", product.getId(), product.getQuantity());
    }

    @Override
    public AddressDto findAddress() {
        return CURRENT_ADDRESS;
    }

    @Override
    public void returnProduct(Map<UUID, Integer> products) {
        List<WarehouseProduct> warehouseProducts = productRepository.findAllById(products.keySet());
        warehouseProducts.forEach(product -> addQuantity(product, products.get(product.getId())));
        productRepository.saveAll(warehouseProducts);
        log.info("Return products {} to warehouse", products);
    }

    @Override
    public void shippedProductToDelivery(ShippedToDeliveryRequest request) {
        UUID orderId = request.getOrderId();
        OrderBooking orderBooking = getOrderBooking(orderId);
        orderBooking.setDeliveryId(request.getDeliveryID());
        bookingRepository.save(orderBooking);
        log.info("Shipped product to delivery for order ID: {}", orderId);
    }

    @Override
    public BookedProductsDto assemblyProduct(AssemblyProductsForOrderRequest request) {
        Map<UUID, Integer> products = request.getProducts();
        Supplier<Stream<WarehouseProduct>> streamSupplier =
                () -> productRepository.findAllById(products.keySet()).stream();
        checkProductQuantity(streamSupplier.get(), products);
        BookedProductsDto bookedProductsDto = calculateDeliveryParams(streamSupplier, products);
        List<WarehouseProduct> warehouseProducts = streamSupplier.get()
                .peek(product -> reduceQuantity(product, products.get(product.getId())))
                .toList();
        productRepository.saveAll(warehouseProducts);
        OrderBooking orderBooking = mapper.map(request);
        bookingRepository.save(orderBooking);
        log.info("Booked products for order ID: {}", orderBooking.getOrderId());
        return bookedProductsDto;
    }

    private WarehouseProduct getProduct(UUID productId) {
        return productRepository.findById(productId)
                .orElseThrow(() ->
                        {
                            log.error("Not found product ID: {} ", productId);
                            return new NoSpecifiedProductInWarehouseException(
                                    String.format("Not found product ID:  %s", productId)
                            );
                        }
                );
    }

    private OrderBooking getOrderBooking(UUID orderId) {
        return bookingRepository.findById(orderId)
                .orElseThrow(() ->
                        {
                            log.error("Not found order ID: {} ", orderId);
                            return new NoOrderBookingInWarehouseException(
                                    String.format("Not found order ID:  %s", orderId)
                            );
                        }
                );
    }

    private void checkProductExist(UUID productId) {
        if (productRepository.existsById(productId)) {
            log.error("Product ID: {} already exist", productId);
            throw new SpecifiedProductAlreadyInWarehouseException(
                    String.format("Product ID: %s already exist", productId));
        }
    }

    private void addQuantity(WarehouseProduct product, int newQuantity) {
        int oldQuantity = product.getQuantity();
        product.setQuantity(oldQuantity + newQuantity);
    }

    private void reduceQuantity(WarehouseProduct product, int newQuantity) {
        int oldQuantity = product.getQuantity();
        product.setQuantity(oldQuantity - newQuantity);
    }

    private void updateQuantityInShoppingStore(WarehouseProduct product) {
        int quantity = product.getQuantity();
        QuantityState quantityState;
        if (quantity == 0) {
            quantityState = QuantityState.ENDED;
        } else if (0 < quantity && quantity <= 10) {
            quantityState = QuantityState.FEW;
        } else if (10 < quantity && quantity <= 100) {
            quantityState = QuantityState.ENOUGH;
        } else {
            quantityState = QuantityState.MANY;
        }
        try {
            shoppingStoreClient.updateProductQuantity(product.getId(), quantityState);
        } catch (FeignException e) {
            log.error("Product quantity in store not update", e);
        }
    }

    private void checkProductQuantity(Stream<WarehouseProduct> stream,
                                      Map<UUID, Integer> products) {
        if (stream.anyMatch(product -> product.getQuantity() < products.get(product.getId()))) {
            log.error("Quantity of products is less than necessary");
            throw new ProductInShoppingCartLowQuantityInWarehouse("Quantity of products is less than necessary");
        }
    }

    private BookedProductsDto calculateDeliveryParams(Supplier<Stream<WarehouseProduct>> streamSupplier,
                                                      Map<UUID, Integer> products) {
        Double deliveryVolume = streamSupplier.get()
                .map(product ->
                        product.getWidth() * product.getHeight() * product.getDepth() * products.get(product.getId())
                )
                .reduce(0.0, Double::sum);
        Double deliveryWeight = streamSupplier.get()
                .map(product -> product.getWeight() * products.get(product.getId()))
                .reduce(0.0, Double::sum);
        boolean isFragile = streamSupplier.get().anyMatch(WarehouseProduct::isFragile);
        return new BookedProductsDto(deliveryVolume, deliveryWeight, isFragile);
    }
}
