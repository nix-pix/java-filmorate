package ru.yandex.practicum.filmorate.controller;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/films")
@Slf4j
@Getter
public class FilmController {
    private final FilmStorage filmStorage;
    private final FilmService filmService;

    @Autowired
    public FilmController(FilmStorage filmStorage, FilmService filmService) {
        this.filmStorage = filmStorage;
        this.filmService = filmService;
    }

    @PostMapping
    public Film add(@Valid @RequestBody Film film) {
        log.info("Добавлен фильм: {}", film);
        return filmStorage.add(film);
    }

    @PutMapping
    public Film update(@Valid @RequestBody @NotNull Film film) {
        log.info("Обновлен фильм: {}", film);
        return filmStorage.update(film);
    }

    @PutMapping("/{id}/like/{userId}")
    public void putLike(@PathVariable int filmId, @PathVariable int userId) {
        log.info("Пользователь с id: {} поставил лайк фильму с id: {}", userId, filmId);
        filmService.putLike(filmId, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLike(@PathVariable int filmId, @PathVariable int userId) {
        log.info("Пользователь с id: {} удалил лайк с фильма с id: {}", userId, filmId);
        filmService.deleteLike(filmId, userId);
    }

    @GetMapping
    public Collection<Film> getAll() {
        log.info("Получены все фильмы");
        return filmStorage.getAll();
    }

    @GetMapping("/{id}")
    public Film getById(@PathVariable int id) {
        log.info("Получен фильм с id: {}", id);
        return filmStorage.get(id);
    }

    @GetMapping("/popular")
    public List<Film> getPopular(@RequestParam(defaultValue = "10") int count) {
        log.info("Получен список {} популярных фильмов", count);
        return filmService.getPopular(count);
    }
}
