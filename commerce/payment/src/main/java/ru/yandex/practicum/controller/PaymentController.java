package ru.yandex.practicum.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.dto.OrderDto;
import ru.yandex.practicum.dto.PaymentDto;
import ru.yandex.practicum.service.PaymentService;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/payment")
@Slf4j
@RequiredArgsConstructor
public class PaymentController implements PaymentOperations {

    private final PaymentService service;

    @Override
    public PaymentDto createPayment(@RequestBody OrderDto order) {
        log.info("Received request to create payment for order ID: {}", order.getOrderId());
        return service.createPayment(order);
    }

    @Override
    public Double calculateTotalCost(@RequestBody OrderDto order) {
        log.info("Received request to calculate total cost for order ID: {}", order.getOrderId());
        return service.calculateTotalCost(order);
    }

    @PostMapping("/refund")
    public void refundPayment(@RequestBody UUID orderId) {
        log.info("Received request to refund payment ID: {}", orderId);
        service.refundPayment(orderId);
    }

    @Override
    public Double calculateProductCost(@RequestBody OrderDto order) {
        log.info("Received request to calculate product cost for order ID: {}", order.getOrderId());
        return service.calculateProductCost(order);
    }

    @PostMapping("/failed")
    public void failedPayment(@RequestBody UUID orderId) {
        log.info("Received request to failed payment ID: {}", orderId);
        service.failedPayment(orderId);
    }
}
