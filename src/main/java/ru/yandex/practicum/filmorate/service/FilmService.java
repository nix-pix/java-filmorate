package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import ru.yandex.practicum.filmorate.validator.FilmValidator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private long idSequence = 0;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    private long generateId() {
        idSequence++;
        return idSequence;
    }

    public Film addFilm(Film film) {
        FilmValidator.validate(film);
        film.setId(generateId());
        return filmStorage.add(film);
    }

    public Film updateFilm(Film film) {
        FilmValidator.validate(film);
        return filmStorage.update(film);
    }

    public Film getFilm(long id) {
        return filmStorage.get(id);
    }

    public Collection<Film> getAllFilms() {
        return filmStorage.getAll();
    }

    public void putLike(long filmId, long userId) {
        filmStorage.get(filmId).getLikes().add(userStorage.get(userId).getId());
    }

    public void deleteLike(long filmId, long userId) {
        filmStorage.get(filmId).getLikes().remove(userStorage.get(userId).getId());
    }

    public List<Film> getPopular(int count) {
        List<Film> films = new ArrayList<>(filmStorage.getAll());
        Comparator<Film> comparator = (c1, c2) -> c2.getLikes().size() - c1.getLikes().size();
        films.sort(comparator);
        List<Film> result = new ArrayList<>();
        if (films.size() > count) {
            return films.subList(0, count);
        } else {
            return films;
        }
    }
}
