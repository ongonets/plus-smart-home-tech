package ru.yandex.practicum.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Entity
@Table(name = "addresses")
@Getter
@Setter
@ToString
public class DeliveryAddress {

    @Id
    @UuidGenerator
    private UUID id;

    private String country;

    private String city;

    private String street;

    private String house;

    private String flat;
}
