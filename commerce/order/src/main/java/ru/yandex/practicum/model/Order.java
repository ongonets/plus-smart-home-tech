package ru.yandex.practicum.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.UuidGenerator;
import ru.yandex.practicum.dto.OrderState;

import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "orders")
@Getter
@Setter
@ToString
public class Order {

    @Id
    @UuidGenerator
    private UUID id;

    private String username;

    @Enumerated
    private OrderState state;

    @ElementCollection
    @CollectionTable(name="products",
            joinColumns=@JoinColumn(name="order_id"))
    @MapKeyColumn(name="product_id")
    @Column(name="quantity")
    private Map<UUID, Integer> products;

    @Column(name = "shopping_cart_id")
    private UUID shoppingCartId;

    @Column(name = "payment_id")
    private UUID paymentId;

    @Column(name = "delivery_id")
    private UUID deliveryId;

    @Column(name = "delivery_volume")
    private double deliveryVolume;

    @Column(name = "delivery_weight")
    private double deliveryWeight;

    private boolean fragile;

    @Column(name = "total_price")
    private double totalPrice;

    @Column(name = "delivery_price")
    private double deliveryPrice;

    @Column(name = "product_price")
    private double productPrice;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id", referencedColumnName = "id")
    private DeliveryAddress address;

    @Override
    public boolean equals(Object object) {
        if (object == null || getClass() != object.getClass()) return false;
        Order order = (Order) object;
        return Objects.equals(id, order.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
