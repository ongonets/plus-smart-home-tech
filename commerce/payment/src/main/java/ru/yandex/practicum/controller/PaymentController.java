package ru.yandex.practicum.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.dto.OrderDto;
import ru.yandex.practicum.dto.PaymentDto;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/payment")
@Slf4j
@RequiredArgsConstructor
public class PaymentController implements PaymentOperations {

    @Override
    public PaymentDto createPayment(@RequestBody OrderDto order) {
        log.info("Received request to create payment for order ID: {}", order.getOrderId());
        return null;
    }

    @Override
    public Double calculateTotalCost(@RequestBody OrderDto order) {
        log.info("Received request to calculate total cost for order ID: {}", order.getOrderId());
        return null;
    }

    @PostMapping("/refund")
    public void refundPayment(@RequestBody UUID paymentID) {
        log.info("Received request to refund payment ID: {}", paymentID);
    }

    @Override
    public Double calculateProductCost(@RequestBody OrderDto order) {
        log.info("Received request to calculate product cost for order ID: {}", order.getOrderId());
        return null;
    }

    @PostMapping("/failed")
    public void failedPayment(@RequestBody UUID paymentID) {
        log.info("Received request to failed payment ID: {}", paymentID);
    }
}
