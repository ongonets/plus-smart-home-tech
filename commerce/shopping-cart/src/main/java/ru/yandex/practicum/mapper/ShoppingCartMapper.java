package ru.yandex.practicum.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.yandex.practicum.dto.ShoppingCartDto;
import ru.yandex.practicum.model.ShoppingCart;

@Mapper(componentModel = "spring")
public interface ShoppingCartMapper {

    @Mapping(source = "id", target = "shoppingCartId")
    ShoppingCartDto map(ShoppingCart shoppingCart);
}
