package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.validator.FilmValidator;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
public class InMemoryFilmStorage implements FilmStorage {
    private int idSequence = 0;
    private final Map<Integer, Film> films = new HashMap<>();

    private int generateId() {
        idSequence++;
        return idSequence;
    }

    private boolean isCorrectId(int id) {
        if (films.size() > 0 && films.containsKey(id)) {
            return true;
        } else {
            throw new FilmNotFoundException("Получен некорректный id = " + id);
        }
    }

    @Override
    public Film add(Film film) {
        FilmValidator.validate(film);
        film.setId(generateId());
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Film update(Film film) {
        if (isCorrectId(film.getId())) {
            FilmValidator.validate(film);
            films.put(film.getId(), film);
        }
        return film;
    }

    @Override
    public Film get(int id) {
//        boolean flag = isCorrectId(id);
//        return films.get(id);
        if (isCorrectId(id)) {
            return films.get(id);
        }
        return null;
    }

    @Override
    public Collection<Film> getAll() {
        return films.values();
    }

    @Override
    public void delete(int id) {
        if (isCorrectId(id)) {
            films.remove(id);
        }
    }

    @Override
    public void deleteAll() {
        films.clear();
    }
}
