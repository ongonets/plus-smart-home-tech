package ru.yandex.practicum.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class Pageable {
    private int page;
    private int size;
    private List<String> sort;
}
