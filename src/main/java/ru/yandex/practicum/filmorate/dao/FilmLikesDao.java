package ru.yandex.practicum.filmorate.dao;

import java.util.Set;

public interface FilmLikesDao {

    void addLike(long filmId, long userId);

    void deleteLike(long filmId, long userId);

    Set<Long> findLikesOfFilm(long filmId);
}
