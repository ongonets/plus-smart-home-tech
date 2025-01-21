package ru.yandex.practicum.mapper;

import org.mapstruct.*;
import ru.yandex.practicum.dto.ProductDto;
import ru.yandex.practicum.model.Product;

import java.util.List;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface StoreMapper {

    @Mapping(source = "id", target = "productId")
    @Mapping(source = "name", target = "productName")
    @Mapping(source = "quantity", target = "quantityState")
    @Mapping(source = "category", target = "productCategory")
    ProductDto map(Product product);

    List<ProductDto> map(List<Product> products);

    @Mapping(source = "productId", target = "id")
    @Mapping(source = "productName", target = "name")
    @Mapping(source = "quantityState", target = "quantity")
    @Mapping(source = "productCategory", target = "category")
    Product map(ProductDto dto);

    @Mapping(source = "productId", target = "id")
    @Mapping(source = "productName", target = "name")
    @Mapping(source = "quantityState", target = "quantity")
    @Mapping(source = "productCategory", target = "category")
    void update(@MappingTarget Product product, ProductDto dto);
}
