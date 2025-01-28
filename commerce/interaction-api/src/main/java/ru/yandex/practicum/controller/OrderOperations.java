package ru.yandex.practicum.controller;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.dto.OrderDto;

import java.util.UUID;

@FeignClient(name = "order", path = "/api/v1/order")
public interface OrderOperations {

    @PostMapping("/payment")
    OrderDto paymentOrder(@RequestBody UUID orderId);

    @PostMapping("/payment/failed")
    OrderDto failedPaymentOrder(@RequestBody UUID orderId);

    @PostMapping("/delivery")
    OrderDto deliveryOrder(@RequestBody UUID orderId);

    @PostMapping("/delivery/failed")
    OrderDto failedDeliveryOrder(@RequestBody UUID orderId);

    @PostMapping("/assembly")
    OrderDto assemblyOrder(@RequestBody UUID orderId);

    @PostMapping("/assembly/failed")
    OrderDto failedAssemblyOrder(@RequestBody UUID orderId);

    @PostMapping("/completed")
    OrderDto completedOrder(@RequestBody UUID orderId);
}
