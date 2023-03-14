package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Long, Film> films = new HashMap<>();

    private boolean isCorrectId(long id) {
        if (films.size() > 0 && films.containsKey(id)) {
            return true;
        } else {
            throw new FilmNotFoundException("Получен некорректный id = " + id);
        }
    }

    @Override
    public Film add(Film film) {
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Film update(Film film) {
        if (isCorrectId(film.getId())) {
            films.put(film.getId(), film);
        }
        return film;
    }

    @Override
    public Film get(long id) {
        boolean check = isCorrectId(id);
        return films.get(id);
    }

    @Override
    public Collection<Film> getAll() {
        return films.values();
    }

    @Override
    public List<Film> getTopRatedFilms(Integer count) {
        Collection<Film> films = getAll();
        return films.stream()
                .sorted(Collections.reverseOrder(Film.COMPARE_BY_LIKES))
                .limit(count)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(long id) {
        if (isCorrectId(id)) {
            films.remove(id);
        }
    }

    @Override
    public void deleteAll() {
        films.clear();
    }
}
