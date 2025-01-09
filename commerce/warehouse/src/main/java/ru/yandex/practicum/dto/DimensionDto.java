package ru.yandex.practicum.dto;

import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class DimensionDto {

    @Min(1)
    private double width;

    @Min(1)
    private double height;

    @Min(1)
    private double depth;
}
