package ru.yandex.practicum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.controller.DeliveryOperations;
import ru.yandex.practicum.controller.PaymentOperations;
import ru.yandex.practicum.controller.WarehouseOperations;
import ru.yandex.practicum.dto.*;
import ru.yandex.practicum.exception.NoOrderFoundException;
import ru.yandex.practicum.exception.NotAuthorizedUserException;
import ru.yandex.practicum.mapper.OrderMapper;
import ru.yandex.practicum.model.Order;
import ru.yandex.practicum.repository.OrderRepository;


import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository repository;
    private final OrderMapper mapper;
    private final WarehouseOperations warehouseClient;
    private final DeliveryOperations deliveryClient;
    private final PaymentOperations paymentClient;

    @Override
    public List<OrderDto> findOrder(String username, int page, int size) {
        validateUsername(username);
        Pageable pageable = PageRequest.of(page, size);
        List<Order> orders = repository.findAllByUsername(username, pageable);
        return mapper.map(orders);
    }

    @Override
    public OrderDto createOrder(String username, CreateNewOrderRequest request) {
        validateUsername(username);
        Order order = mapper.map(username, request);
        order.setState(OrderState.NEW);
        repository.save(order);
        log.info("Created order ID: {}", order.getId());
        return mapper.map(order);
    }

    @Override
    public OrderDto returnProduct(String username, ProductReturnRequest request) {
        validateUsername(username);
        Map<UUID, Integer> products = request.getProducts();
        Order order = getOrder(request.getOrderId());
        reduceProductQuantity(order, products);
        repository.save(order);
        warehouseClient.returnProduct(products);
        log.info("Return products from order ID: {}", order.getId());
        return mapper.map(order);
    }

    @Override
    public OrderDto paymentOrder(UUID orderId) {
        Order order = getOrder(orderId);
        createDelivery(order);
        repository.save(order);
        log.info("Successful payment for order ID: {}", order.getId());
        return mapper.map(order);
    }

    @Override
    public OrderDto failedPaymentOrder(UUID orderId) {
        Order order = getOrder(orderId);
        order.setState(OrderState.PAYMENT_FAILED);
        repository.save(order);
        log.info("Failed payment for order ID: {}", order.getId());
        return mapper.map(order);
    }

    @Override
    public OrderDto deliveryOrder(UUID orderId) {
        Order order = getOrder(orderId);
        order.setState(OrderState.DELIVERED);
        repository.save(order);
        log.info("Successful delivery for order ID: {}", order.getId());
        return mapper.map(order);
    }

    @Override
    public OrderDto failedDeliveryOrder(UUID orderId) {
        Order order = getOrder(orderId);
        order.setState(OrderState.DELIVERY_FAILED);
        repository.save(order);
        log.info("Failed delivery for order ID: {}", order.getId());
        return mapper.map(order);
    }

    @Override
    public OrderDto completedOrder(UUID orderId) {
        Order order = getOrder(orderId);
        order.setState(OrderState.COMPLETED);
        repository.save(order);
        log.info("Completed order ID: {}", order.getId());
        return mapper.map(order);
    }

    @Override
    public OrderDto calculateTotalPrice(UUID orderId) {
        Order order = getOrder(orderId);
        Double totalCost = paymentClient.calculateTotalCost(mapper.map(order));
        order.setTotalPrice(totalCost);
        repository.save(order);
        return mapper.map(order);
    }

    @Override
    public OrderDto calculateDeliveryPrice(UUID orderId) {
        Order order = getOrder(orderId);
        AddressDto warehouseAddress = warehouseClient.findAddress();
        DeliveryDto deliveryDto = mapper.mapToDelivery(warehouseAddress, order);
        Double deliveryCost = deliveryClient.calculateDeliveryCost(deliveryDto);
        order.setDeliveryPrice(deliveryCost);
        repository.save(order);
        return mapper.map(order);
    }

    @Override
    public OrderDto assemblyOrder(UUID orderId) {
        Order order = getOrder(orderId);
        assemblyProductForOrder(order);
        createPayment(order);
        repository.save(order);
        return mapper.map(order);
    }

    @Override
    public OrderDto failedAssemblyOrder(UUID orderId) {
        Order order = getOrder(orderId);
        order.setState(OrderState.ASSEMBLY_FAILED);
        repository.save(order);
        return mapper.map(order);
    }

    private Order getOrder(UUID orderId) {
        return repository.findById(orderId)
                .orElseThrow(() ->
                        {
                            log.error("Not found product ID: {} ", orderId);
                            return new NoOrderFoundException(String.format("Not found product ID:  %s", orderId));
                        }
                );
    }

    private void validateUsername(String username) {
        if (!username.equals(username)) {
            throw new NotAuthorizedUserException(String.format("Not authorized user: %s", username));
        }
    }

    private void reduceProductQuantity(Order order, Map<UUID, Integer> returnProducts) {
        Map<UUID, Integer> oldProducts = order.getProducts();
        for (UUID productId : returnProducts.keySet()) {
            int newQuantity = oldProducts.get(productId) - returnProducts.get(productId);
            oldProducts.put(productId, newQuantity);
        }
    }

    private void assemblyProductForOrder(Order order) {
        AssemblyProductsForOrderRequest request =
                new AssemblyProductsForOrderRequest(order.getId(), order.getProducts());
        BookedProductsDto bookedProductsDto = warehouseClient.assemblyProduct(request);
        order.setDeliveryVolume(bookedProductsDto.getDeliveryVolume());
        order.setDeliveryWeight(bookedProductsDto.getDeliveryWeight());
        order.setFragile(bookedProductsDto.isFragile());
        log.info("Successful assembly for order ID: {}", order.getId());
    }

    private void createDelivery(Order order) {
        AddressDto warehouseAddress = warehouseClient.findAddress();
        DeliveryDto deliveryDto = mapper.mapToDelivery(warehouseAddress, order);
        deliveryDto = deliveryClient.createDelivery(deliveryDto);
        order.setDeliveryId(deliveryDto.getDeliveryId());
        order.setState(OrderState.ON_DELIVERY);
        log.info("Create delivery for order ID: {}", order.getId());
    }

    private void createPayment(Order order) {
        PaymentDto payment = paymentClient.createPayment(mapper.map(order));
        order.setPaymentId(payment.getPaymentId());
        order.setState(OrderState.ON_PAYMENT);
        log.info("Create payment for order ID: {}", order.getId());
    }
}
