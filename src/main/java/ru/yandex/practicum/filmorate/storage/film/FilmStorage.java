package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

public interface FilmStorage {

    Film add(Film film);

    Film update(Film film);

    Film get(int id);

    Collection<Film> getAll();

    void delete(int id);

    void deleteAll();

}
