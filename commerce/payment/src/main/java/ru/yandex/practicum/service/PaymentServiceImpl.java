package ru.yandex.practicum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.PaymentMapper;
import ru.yandex.practicum.controller.OrderOperations;
import ru.yandex.practicum.controller.ShoppingStoreOperations;
import ru.yandex.practicum.dto.OrderDto;
import ru.yandex.practicum.dto.PaymentDto;
import ru.yandex.practicum.dto.PaymentState;
import ru.yandex.practicum.dto.ProductDto;
import ru.yandex.practicum.exception.NoPaymentFoundException;
import ru.yandex.practicum.exception.NotEnoughInfoInOrderToCalculateException;
import ru.yandex.practicum.model.Payment;
import ru.yandex.practicum.repository.PaymentRepository;

import java.util.Map;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository repository;
    private final OrderOperations orderClient;
    private final ShoppingStoreOperations storeClient;
    private final PaymentMapper mapper;

    @Override
    public PaymentDto createPayment(OrderDto order) {
        if (order.getOrderId() == null || order.getProducts().isEmpty()|| order.getDeliveryPrice() <= 0.0) {
            throw new NotEnoughInfoInOrderToCalculateException("Not enough information in order");
        }
        Payment payment = mapper.map(order);
        Double productCost = calculateProductCost(order.getProducts());
        payment.setProductTotal(productCost);
        Double totalCost = calculateTotalCost(productCost, order.getDeliveryPrice());
        payment.setTotalPayment(totalCost);
        payment.setState(PaymentState.PENDING);
        repository.save(payment);
        log.info("Create payment for order ID: {}", payment.getOrderId());
        return mapper.map(payment);
    }

    @Override
    public Double calculateTotalCost(OrderDto order) {
        if (order.getOrderId() == null || order.getProducts().isEmpty()|| order.getDeliveryPrice() <= 0.0) {
            throw new NotEnoughInfoInOrderToCalculateException("Not enough information in order");
        }
        Double productCost = calculateProductCost(order.getProducts());
        Double totalCost = calculateTotalCost(productCost, order.getDeliveryPrice());
        log.info("Calculated total cost for order ID {}, total cost = {}", order.getOrderId(), totalCost);
        return totalCost;
    }


    @Override
    public void refundPayment(UUID orderId) {
        Payment payment = getPayment(orderId);
        payment.setState(PaymentState.SUCCESSFUL);
        repository.save(payment);
        orderClient.paymentOrder(orderId);
        log.info("Order ID: {} is paid", orderId);
    }

    @Override
    public Double calculateProductCost(OrderDto order) {
        if (order.getOrderId() == null || order.getProducts().isEmpty()) {
            throw new NotEnoughInfoInOrderToCalculateException("Not enough information in order");
        }
        Double productCost = calculateProductCost(order.getProducts());
        log.info("Calculated product cost for order ID {}, product cost = {}", order.getOrderId(), productCost);
        return productCost;
    }

    @Override
    public void failedPayment(UUID orderId) {
        Payment payment = getPayment(orderId);
        payment.setState(PaymentState.FAILED);
        repository.save(payment);
        orderClient.failedPaymentOrder(orderId);
        log.info("Order ID: {} payment failed", orderId);
    }

    private Payment getPayment(UUID orderId) {
        return repository.findByOrderId(orderId)
                .orElseThrow(() ->
                        {
                            log.error("Not found payment for order ID: {} ", orderId);
                            return new NoPaymentFoundException(String.format("Not found payment for ID:  %s", orderId));
                        }
                );
    }

    private Double calculateProductCost(Map<UUID, Integer> products) {
        return  products.keySet().stream()
                .map(storeClient::findProduct)
                .map(product -> product.getPrice() * products.get(product.getProductId()))
                .reduce(0.0, Double::sum);
    }

    private Double calculateTotalCost(double productPrice, double deliveryPrice) {
        return productPrice * 1.1 + deliveryPrice;
    }
}
