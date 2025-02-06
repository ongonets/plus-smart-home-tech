package ru.yandex.practicum.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Map;
import java.util.UUID;

@Entity
@Table(name = "order_booking")
@Setter
@Getter
@ToString
public class OrderBooking {

    @Id
    @Column(name = "order_id")
    private UUID orderId;

    @Column(name = "delivery_id")
    private UUID deliveryId;

    @ElementCollection
    @CollectionTable(name="product_booking",
            joinColumns=@JoinColumn(name="order_id"))
    @MapKeyColumn(name="product_id")
    @Column(name="quantity")
    private Map<UUID, Integer> products;
}
