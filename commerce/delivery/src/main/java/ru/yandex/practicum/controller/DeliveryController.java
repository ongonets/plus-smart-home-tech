package ru.yandex.practicum.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.dto.DeliveryDto;
import ru.yandex.practicum.dto.OrderDto;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/delivery")
@Slf4j
@RequiredArgsConstructor
public class DeliveryController {

    @PutMapping
    public DeliveryDto createDelivery(@RequestBody DeliveryDto deliveryDto) {
        log.info("Received request to create delivery for order ID: {}", deliveryDto.getOrderId());
        return null;
    }

    @PostMapping("/successful")
    public void successfulDelivery(@RequestBody UUID orderId) {
        log.info("Received request to successful delivery for order ID: {}", orderId);
    }

    @PostMapping("/picked")
    public void pickedDelivery(@RequestBody UUID orderId) {
        log.info("Received request to picked delivery for order ID: {}", orderId);
    }

    @PostMapping("/failed")
    public void failedDelivery(@RequestBody UUID orderId) {
        log.info("Received request to failed delivery for order ID: {}", orderId);
    }

    @PostMapping("/cost")
    public Double calculateDeliveryCost(@RequestBody OrderDto order) {
        log.info("Received request to calculate delivery cost for order ID: {}", order.getOrderId());
        return null;
    }
}
