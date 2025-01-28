package ru.yandex.practicum.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.dto.CreateNewOrderRequest;
import ru.yandex.practicum.dto.OrderDto;
import ru.yandex.practicum.dto.ProductReturnRequest;
import ru.yandex.practicum.service.OrderService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "/api/v1/order")
@Slf4j
@RequiredArgsConstructor
public class OrderController {

    private final OrderService service;

    @GetMapping
    public List<OrderDto> findOrder(@RequestParam String username,
                                    @RequestParam(defaultValue = "0") int page,
                                    @RequestParam(defaultValue = "10") int size) {
        return service.findOrder(username, page, size);
    }

    @PutMapping
    public OrderDto createOrder(@RequestBody CreateNewOrderRequest request) {
        log.info("Received request to create order from shopping cart ID: {}",
                request.getShoppingCart().getShoppingCartId());
        return service.createOrder(request);
    }

    @PostMapping("/return")
    public OrderDto returnProduct(@RequestBody ProductReturnRequest request) {
        log.info("Received request to return product from order ID: {}", request.getOrderId());
        return service.returnProduct(request);
    }

    @PostMapping("/payment")
    public OrderDto paymentOrder(@RequestBody UUID orderId) {
        log.info("Received request to payment order ID: {}", orderId);
        return service.paymentOrder(orderId);
    }

    @PostMapping("/payment/failed")
    public OrderDto failedPaymentOrder(@RequestBody UUID orderId) {
        log.info("Received request to failed payment order ID: {}", orderId);
        return service.failedPaymentOrder(orderId);
    }

    @PostMapping("/delivery")
    public OrderDto deliveryOrder(@RequestBody UUID orderId) {
        log.info("Received request to delivery order ID: {}", orderId);
        return service.deliveryOrder(orderId);
    }

    @PostMapping("/delivery/failed")
    public OrderDto failedDeliveryOrder(@RequestBody UUID orderId) {
        log.info("Received request to failed delivery order ID: {}", orderId);
        return service.failedDeliveryOrder(orderId);
    }

    @PostMapping("/completed")
    public OrderDto completedOrder(@RequestBody UUID orderId) {
        log.info("Received request to completed order ID: {}", orderId);
        return service.completedOrder(orderId);
    }

    @PostMapping("/calculate/total")
    public OrderDto calculateTotalPrice(@RequestBody UUID orderId) {
        log.info("Received request to calculate total price of order ID: {}", orderId);
        return service.calculateTotalPrice(orderId);
    }

    @PostMapping("/calculate/delivery")
    public OrderDto calculateDeliveryPrice(@RequestBody UUID orderId) {
        log.info("Received request to calculate delivery price of order ID: {}", orderId);
        return service.calculateDeliveryPrice(orderId);
    }

    @PostMapping("/assembly")
    public OrderDto assemblyOrder(@RequestBody UUID orderId) {
        log.info("Received request to assembly order ID: {}", orderId);
        return service.assemblyOrder(orderId);
    }

    @PostMapping("/assembly/failed")
    public OrderDto failedAssemblyOrder(@RequestBody UUID orderId) {
        log.info("Received request to failed assembly order ID: {}", orderId);
        return service.failedAssemblyOrder(orderId);
    }
}
