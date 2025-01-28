package ru.yandex.practicum.service;

import ru.yandex.practicum.dto.CreateNewOrderRequest;
import ru.yandex.practicum.dto.OrderDto;
import ru.yandex.practicum.dto.ProductReturnRequest;

import java.util.List;
import java.util.UUID;

public interface OrderService {

    List<OrderDto> findOrder(String username, int page, int size);

    OrderDto createOrder(String username, CreateNewOrderRequest request);

    OrderDto returnProduct(String username, ProductReturnRequest request);

    OrderDto paymentOrder(UUID orderId);

    OrderDto failedPaymentOrder(UUID orderId);

    OrderDto deliveryOrder(UUID orderId);

    OrderDto failedDeliveryOrder(UUID orderId);

    OrderDto completedOrder(UUID orderId);

    OrderDto calculateTotalPrice(UUID orderId);

    OrderDto calculateDeliveryPrice(UUID orderId);

    OrderDto assemblyOrder(UUID orderId);

    OrderDto failedAssemblyOrder(UUID orderId);
}
