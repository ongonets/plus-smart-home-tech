package ru.yandex.practicum.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.dto.ExceptionMessage;
import ru.yandex.practicum.exception.ProductNotFoundException;

@RestControllerAdvice
@Slf4j
public class ShoppingStoreErrorHandler extends BaseErrorHandler{

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionMessage handleProductNotFoundException(ProductNotFoundException e) {
        return getErrorDto(e, "Product not found", HttpStatus.BAD_REQUEST);
    }
}
