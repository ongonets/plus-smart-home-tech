package ru.yandex.practicum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.dto.*;
import ru.yandex.practicum.exception.NotFoundException;
import ru.yandex.practicum.mapper.StoreMapper;
import ru.yandex.practicum.model.Product;
import ru.yandex.practicum.repository.StoreRepository;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class StoreServiceImpl implements StoreService {

    private final StoreRepository repository;
    private final StoreMapper mapper;

    @Override
    public List<ProductDto> findProduct(String category, PageableDto dto) {
        Pageable pageable = PageRequest.of(dto.getPage(), dto.getSize(), getSort(dto.getSort()));
        List<Product> products = repository.findByCategory(ProductCategory.valueOf(category), pageable);
        return mapper.map(products);
    }

    @Override
    public ProductDto findProduct(String productId) {
        Product product = getProduct(productId);
        return mapper.map(product);
    }

    @Override
    public ProductDto createProduct(ProductDto productDto) {
        Product product = mapper.map(productDto);
        repository.save(product);
        log.info("Saved product ID: {}", product.getId());
        return mapper.map(product);
    }

    @Override
    public ProductDto updateProduct(ProductDto productDto) {
        Product product = getProduct(productDto.getProductId());
        mapper.update(product, productDto);
        repository.save(product);
        log.info("Updated product ID: {}", product.getId());
        return mapper.map(product);
    }

    @Override
    public void removeProduct(String productId) {
        Product product = getProduct(productId);
        product.setProductState(ProductState.DEACTIVATE);
        repository.save(product);
        log.info("Deactivated product ID: {}", productId);
    }

    @Override
    public void updateQuantity(SetProductQuantityStateRequest request) {
        String productId = request.getProductId();
        Product product = getProduct(productId);
        product.setQuantity(request.getQuantityState());
        repository.save(product);
        log.info("Update quantity of product ID: {}", productId);
    }

    private Product getProduct(String productId) {
        UUID uuid = UUID.fromString(productId);
        return repository.findById(uuid)
                .orElseThrow(() ->
                        {
                            log.error("Not found product ID: {} ", productId);
                            return new NotFoundException(String.format("Not found product ID:  %s", productId));
                        }
                );
    }

    private Sort getSort(List<String> sortOrders) {
        Sort sort = Sort.unsorted();
        for (String sortOrder : sortOrders) {
            switch (ProductSortOrders.valueOf(sortOrder.toUpperCase())) {
                case PRODUCTID -> sort.and(Sort.by("id"));
                case PRODUCTNAME -> sort.and(Sort.by("name"));
                case DESCRIPTION -> sort.and(Sort.by("description"));
                case QUANTITYSTATE -> sort.and(Sort.by("quantity"));
                case PRODUCTSTATE -> sort.and(Sort.by("productState"));
                case RATING -> sort.and(Sort.by("rating"));
                case PRICE -> sort.and(Sort.by("price"));
            }
        }
        return sort;
    }
}
