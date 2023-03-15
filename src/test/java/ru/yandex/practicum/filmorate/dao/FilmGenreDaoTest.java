package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FilmGenreDaoTest {
    private final FilmDbStorage filmDbStorage;
    private final MpaRatingDao mpaRatingDao;
    private final FilmGenreDao filmGenreDao;

    @BeforeAll
    public void beforeAll() {
        filmDbStorage.add(Film.builder()
                .name("Фильм1")
                .description("Описание фильма1")
                .releaseDate(LocalDate.of(2011, 1, 1))
                .duration(110)
                .mpa(mpaRatingDao.getMpaById(1))
                .build());
        filmDbStorage.add(Film.builder()
                .name("Фильм2")
                .description("Описание фильма2")
                .releaseDate(LocalDate.of(2012, 2, 2))
                .duration(120)
                .mpa(mpaRatingDao.getMpaById(2))
                .build());
    }

    @Test
    public void addGenreTest() {
        filmGenreDao.addGenre(1, 1);
        assertThat(filmDbStorage.get(1).getGenres().size()).isEqualTo(1);
    }

    @Test
    public void deleteGenreTest() {
        filmGenreDao.addGenre(2, 2);
        filmGenreDao.deleteGenre(2);

        assertThat(filmDbStorage.get(2).getGenres().isEmpty()).isTrue();
    }
}
