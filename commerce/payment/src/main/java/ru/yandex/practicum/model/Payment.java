package ru.yandex.practicum.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.UuidGenerator;
import ru.yandex.practicum.dto.PaymentState;
import ru.yandex.practicum.service.PaymentService;

import java.util.UUID;

@Entity
@Table(name = "payments")
@Getter
@Setter
@ToString
public class Payment {

    @Id
    @UuidGenerator
    private UUID id;

    @Column(name = "order_id")
    private UUID orderId;

    private PaymentState state;

    @Column(name = "total_payment")
    private double totalPayment;

    @Column(name = "delivery_total")
    private double deliveryTotal;

    @Column(name = "product_total")
    private double productTotal;
}
