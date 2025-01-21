package ru.yandex.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.model.WarehouseProduct;

public interface WarehouseRepository extends JpaRepository<WarehouseProduct, String> {
}
