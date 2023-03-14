package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class Genre {
    private final int id;

    @NotBlank(message = "Название не должно быть пустым")
    @Size(max = 50)
    private final String name;
}
