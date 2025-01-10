package ru.yandex.practicum.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.yandex.practicum.dto.NewProductInWarehouseRequest;
import ru.yandex.practicum.model.WarehouseProduct;

@Mapper(componentModel = "spring")
public interface WarehouseMapper {


    @Mapping(source = "productId", target = "id")
    @Mapping(expression = "java(request.getDimension().getWidth())", target = "width")
    @Mapping(expression = "java(request.getDimension().getHeight())", target = "height")
    @Mapping(expression = "java(request.getDimension().getDepth())", target = "depth")
    @Mapping(target = "quantity", ignore = true)
    WarehouseProduct map(NewProductInWarehouseRequest request);


}
