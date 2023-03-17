package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private long id;

    @NotBlank(message = "Укажите электронную почту")
    @Email(message = "Не корректный адрес электронной почты")
    @Size(max = 200)
    private String email;

    @NotBlank(message = "Логин не может быть пустым")
    @Pattern(regexp = "^\\S*$", message = "Логин не может содержать пробелы") // не содержит пробелов
    @Size(max = 50)
    private String login;

    @Size(max = 50)
    private String name;

    @Past(message = "Дата рождения должна быть в прошлом")
    private LocalDate birthday;
    private Set<Long> friends = new HashSet<>();
}
