package ru.yandex.practicum.controller;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.dto.OrderDto;
import ru.yandex.practicum.dto.PaymentDto;

@FeignClient(name = "payment", path = "/api/v1/payment")
public interface PaymentOperations {

    @PostMapping
    PaymentDto createPayment(@RequestBody OrderDto order);

    @PostMapping("/totalCost")
    Double calculateTotalCost(@RequestBody OrderDto order);

    @PostMapping("/productCost")
    Double calculateProductCost(@RequestBody OrderDto order);
}
