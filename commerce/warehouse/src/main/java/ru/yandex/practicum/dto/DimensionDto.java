package ru.yandex.practicum.dto;

import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class DimensionDto {

    @Min(1)
    private int width;

    @Min(1)
    private int height;

    @Min(1)
    private int depth;
}
