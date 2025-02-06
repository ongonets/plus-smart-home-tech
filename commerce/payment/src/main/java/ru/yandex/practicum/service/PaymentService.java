package ru.yandex.practicum.service;

import ru.yandex.practicum.dto.OrderDto;
import ru.yandex.practicum.dto.PaymentDto;

import java.util.UUID;

public interface PaymentService {

    PaymentDto createPayment(OrderDto order);

    Double calculateTotalCost(OrderDto order);

    void refundPayment(UUID orderId);

    Double calculateProductCost(OrderDto order);

    void failedPayment(UUID orderId);
}
