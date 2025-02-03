package ru.yandex.practicum.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValueMappingStrategy;
import ru.yandex.practicum.dto.AddressDto;
import ru.yandex.practicum.dto.CreateNewOrderRequest;
import ru.yandex.practicum.dto.DeliveryDto;
import ru.yandex.practicum.dto.OrderDto;
import ru.yandex.practicum.model.DeliveryAddress;
import ru.yandex.practicum.model.Order;

import java.util.List;

@Mapper(componentModel = "spring", nullValueMappingStrategy = NullValueMappingStrategy.RETURN_NULL)
public interface OrderMapper {

    @Mapping(target = "shoppingCartId", expression = "java(request.getShoppingCart().getShoppingCartId())")
    @Mapping(target = "products", expression = "java(request.getShoppingCart().getProducts())")
    @Mapping(target = "address", expression = "java(map(request.getDeliveryAddress()))")
    @Mapping(target = "username", source = "username")
    Order map(String username, CreateNewOrderRequest request);

    DeliveryAddress map(AddressDto addressDto);

    @Mapping(target = "orderId", source = "id")
    OrderDto map(Order order);

    List<OrderDto> map(List<Order> orders);

    @Mapping(target = "orderId", source = "order.id")
    @Mapping(target = "toAddress", source = "order.address")
    @Mapping(target = "deliveryId", ignore = true)
    DeliveryDto mapToDelivery(AddressDto fromAddress, Order order);


}
