package ru.yandex.practicum;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import ru.yandex.practicum.dto.OrderDto;
import ru.yandex.practicum.dto.PaymentDto;
import ru.yandex.practicum.model.Payment;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface PaymentMapper {

    @Mapping(source = "id", target = "paymentId")
    @Mapping(expression = "java(payment.getTotalPayment() - payment.getDeliveryTotal() - payment.getProductTotal())",
            target = "feeTotal")
    PaymentDto map(Payment payment);

    @Mapping(target = "state", ignore = true)
    @Mapping(target = "totalPayment", ignore = true)
    @Mapping(source = "deliveryPrice", target = "deliveryTotal")
    @Mapping(target = "productTotal", ignore = true)
    Payment map(OrderDto orderDto);
}
