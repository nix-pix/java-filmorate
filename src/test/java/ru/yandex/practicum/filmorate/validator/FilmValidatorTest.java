package ru.yandex.practicum.filmorate.validator;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertThrows;

class FilmValidatorTest {

    @Test
    void validateDateTest() {
        Film film = new Film(
                1,
                "Name",
                "Description",
                LocalDate.of(1894, 12, 28),
                100);
        assertThrows(ValidationException.class,
                () -> {
                    FilmValidator.validate(film);
                });
    }
}