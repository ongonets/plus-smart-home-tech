package ru.yandex.practicum.exception;

public class NoOrderBookingInWarehouseException extends RuntimeException {
    public NoOrderBookingInWarehouseException(String message) {
        super(message);
    }
}
