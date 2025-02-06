package ru.yandex.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.model.OrderBooking;

import java.util.UUID;

public interface WarehouseBookingRepository  extends JpaRepository<OrderBooking, UUID> {
}
