package ru.yandex.practicum.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.UuidGenerator;
import ru.yandex.practicum.dto.DeliveryState;

import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "delivery")
@Getter
@Setter
@ToString
public class Delivery {

    @Id
    @UuidGenerator
    private UUID id;

    @Column(name = "order_id")
    private UUID orderId;

    @Enumerated
    private DeliveryState state;

    @Column(name = "volume")
    private double deliveryVolume;

    @Column(name = "weight")
    private double deliveryWeight;

    private boolean fragile;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "from_address_id", referencedColumnName = "id")
    private Address fromAddress;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "to_address_id", referencedColumnName = "id")
    private Address toAddress;

    @Override
    public boolean equals(Object object) {
        if (object == null || getClass() != object.getClass()) return false;
        Delivery delivery = (Delivery) object;
        return Objects.equals(id, delivery.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
