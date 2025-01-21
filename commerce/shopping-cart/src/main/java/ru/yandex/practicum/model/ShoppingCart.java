package ru.yandex.practicum.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.UuidGenerator;

import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "carts")
@Getter
@Setter
@ToString
public class ShoppingCart {

    @Id
    @UuidGenerator
    private UUID id;

    private String username;

    @Enumerated
    private ShoppingCartState state;

    @ElementCollection
    @CollectionTable(name="products",
            joinColumns=@JoinColumn(name="cart_id"))
    @MapKeyColumn(name="product_id")
    @Column(name="quantity")
    private Map<UUID, Integer> products;


    @Override
    public boolean equals(Object object) {
        if (object == null || getClass() != object.getClass()) return false;
        ShoppingCart that = (ShoppingCart) object;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
