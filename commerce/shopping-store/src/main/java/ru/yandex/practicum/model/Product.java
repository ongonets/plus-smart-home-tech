package ru.yandex.practicum.model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.UuidGenerator;
import ru.yandex.practicum.dto.ProductCategory;
import ru.yandex.practicum.dto.ProductState;
import ru.yandex.practicum.dto.QuantityState;

import java.util.Objects;
import java.util.UUID;


@Entity
@Table(name="products")
@Getter
@Setter
@ToString
public class Product {

    @Id
    @UuidGenerator
    private UUID id;

    private String name;

    private String description;

    @Column(name = "src")
    private String imageSrc;

    private QuantityState quantity;

    @Column(name = "state")
    private ProductState productState;

    private int rating;

    private ProductCategory category;

    private double price;

    @Override
    public boolean equals(Object object) {
        if (object == null || getClass() != object.getClass()) return false;
        Product product = (Product) object;
        return Objects.equals(id, product.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
