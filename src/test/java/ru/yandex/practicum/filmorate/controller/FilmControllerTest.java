package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertThrows;

class FilmControllerTest {
    FilmController filmController = new FilmController();

    @Test
    void isCorrectIdTest() {
        Film film = new Film(
                1,
                "Name",
                "Description",
                LocalDate.of(2000, 12, 28),
                100);
        filmController.add(film);
        assertThrows(FilmNotFoundException.class,
                () -> {
                    filmController.isCorrectId(2);
                });
    }
}