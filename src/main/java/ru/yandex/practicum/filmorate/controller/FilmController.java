package ru.yandex.practicum.filmorate.controller;

import lombok.Getter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.validator.FilmValidator;

import javax.validation.Valid;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/films")
@Slf4j
@Getter
public class FilmController {
    private int idSequence = 0;
    private final Map<Integer, Film> films = new HashMap<>();

    private int generateId() {
        idSequence++;
        return idSequence;
    }

    @PostMapping
    public Film add(@Valid @RequestBody Film film) {
        FilmValidator.validate(film);
        film.setId(generateId());
        films.put(film.getId(), film);
        log.info("Добавлен фильм: {}", film);
        return film;
    }

    @PutMapping
    public Film update(@Valid @RequestBody @NonNull Film film) {
        if (isCorrectId(film.getId())) {
            FilmValidator.validate(film);
            films.put(film.getId(), film);
            log.info("Обновлен фильм: {}", film);
        }
        return film;
    }

    @GetMapping
    public Collection<Film> getAll() {
        log.info("Получены все фильмы");
        return films.values();
    }

    public boolean isCorrectId(int id) {
        if (films.size() > 0 && films.containsKey(id)) {
            return true;
        } else {
            throw new FilmNotFoundException("Получен некорректный id = " + id);
        }
    }
}