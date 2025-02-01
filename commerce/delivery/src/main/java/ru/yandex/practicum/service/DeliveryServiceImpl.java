package ru.yandex.practicum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.controller.OrderOperations;
import ru.yandex.practicum.controller.WarehouseOperations;
import ru.yandex.practicum.dto.*;
import ru.yandex.practicum.exception.NoDeliveryFoundException;
import ru.yandex.practicum.mapper.DeliveryMapper;
import ru.yandex.practicum.model.Delivery;
import ru.yandex.practicum.repository.DeliveryRepository;

import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class DeliveryServiceImpl implements DeliveryService {

    private static final double BASIC_COST = 5.0;

    private final DeliveryRepository repository;
    private final DeliveryMapper mapper;
    private final OrderOperations orderClient;
    private final WarehouseOperations warehouseClient;

    @Override
    public DeliveryDto createDelivery(DeliveryDto deliveryDto) {
        Delivery delivery = mapper.map(deliveryDto);
        delivery.setState(DeliveryState.CREATED);
        repository.save(delivery);
        log.info("Create delivery for order ID: {}", delivery.getOrderId());
        return mapper.map(delivery);
    }

    @Override
    public void successfulDelivery(UUID orderId) {
        Delivery delivery = getDelivery(orderId);
        delivery.setState(DeliveryState.DELIVERED);
        repository.save(delivery);
        orderClient.completedOrder(orderId);
        log.info("Order ID: {} is delivered", orderId);
    }

    @Override
    public void pickedDelivery(UUID orderId) {
        Delivery delivery = getDelivery(orderId);
        delivery.setState(DeliveryState.IN_PROGRESS);
        repository.save(delivery);
        orderClient.assemblyOrder(orderId);
        warehouseClient.shippedProductToDelivery(new ShippedToDeliveryRequest(orderId, delivery.getId()));
        log.info("Order ID: {} is picked for delivery", orderId);

    }

    @Override
    public void failedDelivery(UUID orderId) {
        Delivery delivery = getDelivery(orderId);
        delivery.setState(DeliveryState.FAILED);
        repository.save(delivery);
        orderClient.failedDeliveryOrder(orderId);
        log.info("Order ID: {} delivery failed", orderId);
    }

    @Override
    public Double calculateDeliveryCost(DeliveryDto deliveryDto) {
        double deliveryCost = BASIC_COST;
        String warehouseStreet = deliveryDto.getFromAddress().getStreet();
        if (warehouseStreet.equals("ADDRESS_2")) {
            deliveryCost = deliveryCost * 2 + BASIC_COST;
        }
        if (deliveryDto.isFragile()) {
            deliveryCost = deliveryCost + deliveryCost * 0.2;
        }
        deliveryCost = deliveryCost + deliveryDto.getDeliveryWeight() * 0.3;
        deliveryCost = deliveryCost + deliveryDto.getDeliveryVolume() * 0.2;
        if (deliveryDto.getToAddress().getStreet().equals(warehouseStreet)) {
            deliveryCost = deliveryCost + deliveryCost * 0.2;
        }
        log.info("Calculate delivery cost for order ID: {}, delivery cost = {}",
                deliveryDto.getOrderId(), deliveryCost);
        return deliveryCost;
    }

    private Delivery getDelivery(UUID orderId) {
        return repository.findByOrderId(orderId)
                .orElseThrow(() ->
                        {
                            log.error("Not found payment for order ID: {} ", orderId);
                            return new NoDeliveryFoundException(
                                    String.format("Not found payment for ID:  %s", orderId)
                            );
                        }
                );
    }
}
