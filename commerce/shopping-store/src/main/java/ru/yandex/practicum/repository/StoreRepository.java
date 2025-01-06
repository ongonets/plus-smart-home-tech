package ru.yandex.practicum.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.dto.ProductCategory;
import ru.yandex.practicum.model.Product;

import java.util.List;

public interface StoreRepository extends JpaRepository<Product, String> {

    List<Product> findByCategory(ProductCategory category, Pageable pageable);

}
