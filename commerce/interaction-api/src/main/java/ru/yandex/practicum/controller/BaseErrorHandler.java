package ru.yandex.practicum.controller;

import org.springframework.http.HttpStatus;
import ru.yandex.practicum.dto.ExceptionMessage;

abstract class BaseErrorHandler {
    
     public ExceptionMessage getErrorDto(Exception e, String userMessage, HttpStatus status) {
        return new ExceptionMessage(e.getCause(),
                e.getStackTrace(),
                status,
                userMessage,
                e.getMessage(),
                e.getSuppressed(),
                e.getLocalizedMessage()
        );
    }
}
