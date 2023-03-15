package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exception.GenreNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@SpringBootTest
@AutoConfigureTestDatabase
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class GenreDaoTest {
    public final GenreDao genreDao;
    public final FilmDbStorage filmDbStorage;
    public final MpaRatingDao mpaRatingDao;
    public final FilmGenreDao filmGenreDao;

    @BeforeAll
    public void beforeAll() {
        filmDbStorage.add(Film.builder()
                .name("Фильм11")
                .description("Описание фильма11")
                .releaseDate(LocalDate.of(2020, 2, 1))
                .duration(100)
                .mpa(mpaRatingDao.getMpaById(1))
                .build());
        filmGenreDao.addGenre(1, 2);
        filmGenreDao.addGenre(1, 4);
    }

    @Test
    public void getGenreByIdTest() {
        Genre genre = genreDao.getGenreById(1);

        assertThat(genre).hasFieldOrPropertyWithValue("name", "Комедия");
    }

    @Test
    public void getAllGenresTest() {
        List<Genre> genreList = genreDao.getAllGenres();

        assertThat(genreList.size()).isEqualTo(6);
    }

    @Test
    public void getGenresByFilmTest() {
        List<Genre> genreList = genreDao.getGenresByFilm(1);

        assertThat(genreList.size()).isEqualTo(2);
        assertThat(genreList.get(0)).hasFieldOrPropertyWithValue("name", "Драма");
    }

    @Test
    public void deleteGenreFromFilmTest() {
        filmGenreDao.deleteGenre(1);

        List<Genre> genreList = genreDao.getGenresByFilm(1);

        assertThat(genreList.size()).isEqualTo(0);
    }

    @Test
    public void noGenreFoundTest() {
        assertThatExceptionOfType(GenreNotFoundException.class).isThrownBy(() -> genreDao.getGenreById(111));
    }
}
