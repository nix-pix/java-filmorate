package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class MpaRating {
    private final int id;

    @NotBlank(message = "Название не должно быть пустым")
    @NotNull(message = "Требуется указать название")
    @Size(max = 50)
    private final String name;

    @Size(max = 200)
    private String description;
}
