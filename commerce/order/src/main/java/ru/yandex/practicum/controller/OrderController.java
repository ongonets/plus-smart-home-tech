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
public class OrderController implements OrderOperations {

    private final OrderService service;

    @GetMapping
    public List<OrderDto> findOrder(@RequestParam String username,
                                    @RequestParam(defaultValue = "0") int page,
                                    @RequestParam(defaultValue = "10") int size) {
        return service.findOrder(username, page, size);
    }

    @PutMapping
    public OrderDto createOrder(@RequestParam String username,
                                @RequestBody CreateNewOrderRequest request) {
        log.info("Received request to create order from shopping cart ID: {}",
                request.getShoppingCart().getShoppingCartId());
        return service.createOrder(username, request);
    }

    @PostMapping("/return")
    public OrderDto returnProduct(@RequestParam String username,
                                  @RequestBody ProductReturnRequest request) {
        log.info("Received request to return product from order ID: {}", request.getOrderId());
        return service.returnProduct(username, request);
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

    @Override
    public OrderDto paymentOrder(@RequestBody UUID orderId) {
        log.info("Received request to payment order ID: {}", orderId);
        return service.paymentOrder(orderId);
    }

    @Override
    public OrderDto failedPaymentOrder(@RequestBody UUID orderId) {
        log.info("Received request to failed payment order ID: {}", orderId);
        return service.failedPaymentOrder(orderId);
    }

    @Override
    public OrderDto deliveryOrder(@RequestBody UUID orderId) {
        log.info("Received request to delivery order ID: {}", orderId);
        return service.deliveryOrder(orderId);
    }

    @Override
    public OrderDto failedDeliveryOrder(@RequestBody UUID orderId) {
        log.info("Received request to failed delivery order ID: {}", orderId);
        return service.failedDeliveryOrder(orderId);
    }

    @Override
    public OrderDto completedOrder(@RequestBody UUID orderId) {
        log.info("Received request to completed order ID: {}", orderId);
        return service.completedOrder(orderId);
    }

    @Override
    public OrderDto assemblyOrder(@RequestBody UUID orderId) {
        log.info("Received request to assembly order ID: {}", orderId);
        return service.assemblyOrder(orderId);
    }

    @Override
    public OrderDto failedAssemblyOrder(@RequestBody UUID orderId) {
        log.info("Received request to failed assembly order ID: {}", orderId);
        return service.failedAssemblyOrder(orderId);
    }
}
