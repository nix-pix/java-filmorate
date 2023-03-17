package ru.yandex.practicum.filmorate.dao;

public interface FilmGenreDao {

    void addGenre(long filmId, long genreId);

    void deleteGenre(long filmId);
}
