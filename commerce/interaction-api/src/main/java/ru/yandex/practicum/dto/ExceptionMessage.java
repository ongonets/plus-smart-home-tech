package ru.yandex.practicum.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
public class ExceptionMessage {
    private Throwable cause;
    private StackTraceElement[] stackTrace;
    private HttpStatus httpStatus;
    private String userMessage;
    private String message;
    private Throwable[] suppressed;
    private String localizedMessage;
}
