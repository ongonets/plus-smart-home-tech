package ru.yandex.practicum.service;

import ru.yandex.practicum.dto.DeliveryDto;
import ru.yandex.practicum.dto.OrderDto;

import java.util.UUID;

public interface DeliveryService {

    DeliveryDto createDelivery(DeliveryDto deliveryDto);

    void successfulDelivery(UUID orderId);

    void pickedDelivery(UUID orderId);

    void failedDelivery(UUID orderId);

    Double calculateDeliveryCost(DeliveryDto deliveryDto);
}
