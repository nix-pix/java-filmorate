package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.List;

public interface FilmStorage {

    Film add(Film film);

    Film update(Film film);

    Film get(long id);

    Collection<Film> getAll();

    List<Film> getTopRatedFilms(Integer count);

    void delete(long id);

    void deleteAll();
}
