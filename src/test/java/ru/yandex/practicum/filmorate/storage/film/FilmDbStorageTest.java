package ru.yandex.practicum.filmorate.storage.film;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.dao.FilmLikesDao;
import ru.yandex.practicum.filmorate.dao.MpaRatingDao;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class FilmDbStorageTest {
    private final FilmDbStorage filmDbStorage;
    private final MpaRatingDao mpaRatingDao;
    private final UserDbStorage userDbStorage;
    private final FilmLikesDao filmLikesDao;

    @BeforeAll
    public void beforeAll() {
        userDbStorage.add(User.builder()
                .email("test@email.ru")
                .login("test_login")
                .name("test_name")
                .birthday(LocalDate.of(1980, 10, 20))
                .build());
        userDbStorage.add(User.builder()
                .email("test1@email.ru")
                .login("test1_login")
                .name("test1_name")
                .birthday(LocalDate.of(1980, 11, 21))
                .build());
        filmDbStorage.add(Film.builder()
                .name("Фильм1")
                .description("Описание фильма1")
                .releaseDate(LocalDate.of(2020, 2, 1))
                .duration(100)
                .mpa(mpaRatingDao.getMpaById(1))
                .build());
        filmDbStorage.add(Film.builder()
                .name("Фильм2")
                .description("Описание фильма2")
                .releaseDate(LocalDate.of(2010, 2, 2))
                .duration(120)
                .mpa(mpaRatingDao.getMpaById(2))
                .build());
    }

    @Test
    public void findFilmById() {
        Optional<Film> filmOptional = Optional.ofNullable(filmDbStorage.get(1));

        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("id", 1L)
                );
    }

    @Test
    public void updateFilm() {
        Film updatedFilm = Film.builder()
                .id(1)
                .name("Фильм10")
                .description("Описание фильма10")
                .releaseDate(LocalDate.of(2010, 2, 5))
                .duration(120)
                .mpa(mpaRatingDao.getMpaById(1))
                .build();

        Optional<Film> filmOptional = Optional.ofNullable(filmDbStorage.update(updatedFilm));

        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("duration", 120L)
                );
        assertThat(filmDbStorage.get(1).getDuration()).isEqualTo(120);
    }

    @Test
    public void getFilms() {
        Collection<Film> filmList = filmDbStorage.getAll();
        assertThat(filmList.size()).isEqualTo(2);
    }

    @Test
    public void returnTopRatedFilm() {
        filmLikesDao.addLike(2, 1);

        List<Film> popularFilms = filmDbStorage.getTopRatedFilms(10);
        Optional<Film> filmOptional = Optional.ofNullable(popularFilms.get(0));

        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(film -> assertThat(film).hasFieldOrPropertyWithValue("name", "Фильм2"));
    }

    @Test
    public void throwFilmNotFoundException() {
        assertThatExceptionOfType(FilmNotFoundException.class).isThrownBy(() -> filmDbStorage.get(5));
    }
}
