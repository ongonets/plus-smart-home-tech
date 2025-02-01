package ru.yandex.practicum.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.yandex.practicum.dto.AddressDto;
import ru.yandex.practicum.dto.DeliveryDto;
import ru.yandex.practicum.model.Address;
import ru.yandex.practicum.model.Delivery;

@Mapper(componentModel = "spring")
public interface DeliveryMapper {

    @Mapping(target = "id", ignore = true)
    Delivery map(DeliveryDto dto);

    @Mapping(target = "id", ignore = true)
    Address map(AddressDto dto);

    DeliveryDto map(Delivery delivery);

    AddressDto map(Address address);
}
