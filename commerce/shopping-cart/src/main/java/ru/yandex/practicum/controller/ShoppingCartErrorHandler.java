package ru.yandex.practicum.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.dto.ExceptionMessage;
import ru.yandex.practicum.exception.NotAuthorizedUserException;
import ru.yandex.practicum.exception.ProductInShoppingCartNotInWarehouse;

@RestControllerAdvice
@Slf4j
public class ShoppingCartErrorHandler extends BaseErrorHandler{

    @ExceptionHandler
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ExceptionMessage handleNotAuthorizedUserException(NotAuthorizedUserException e) {
        return getErrorDto(e, "User not authorized", HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionMessage handleNotAuthorizedUserException(ProductInShoppingCartNotInWarehouse e) {
        return getErrorDto(e, "Product not in warehouse", HttpStatus.BAD_REQUEST);

    }


    

}
