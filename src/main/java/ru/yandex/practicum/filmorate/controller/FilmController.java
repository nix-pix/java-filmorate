package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @PostMapping
    public Film addFilm(@Valid @RequestBody Film film) {
        log.info("Добавлен фильм: {}", film);
        return filmService.addFilm(film);
    }

    @PutMapping
    public Film updateFilm(@Valid @NotNull @RequestBody Film film) {
        log.info("Обновлен фильм: {}", film);
        return filmService.updateFilm(film);
    }

    @PutMapping("/{id}/like/{userId}")
    public void putLike(@PathVariable long id, @PathVariable long userId) {
        log.info("Пользователь с id: {} поставил лайк фильму с id: {}", userId, id);
        filmService.putLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLike(@PathVariable long id, @PathVariable long userId) {
        log.info("Пользователь с id: {} удалил лайк с фильма с id: {}", userId, id);
        filmService.deleteLike(id, userId);
    }

    @GetMapping
    public Collection<Film> getAll() {
        log.info("Получены все фильмы");
        return filmService.getAllFilms();
    }

    @GetMapping("/{id}")
    public Film getById(@PathVariable long id) {
        log.info("Получен фильм с id: {}", id);
        return filmService.getFilm(id);
    }

    @GetMapping("/popular")
    public List<Film> getPopular(@RequestParam(defaultValue = "10") int count) {
        log.info("Получен список {} популярных фильмов", count);
        return filmService.getPopular(count);
    }
}
