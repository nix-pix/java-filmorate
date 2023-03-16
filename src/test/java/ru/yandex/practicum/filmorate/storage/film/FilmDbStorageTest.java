package ru.yandex.practicum.filmorate.storage.film;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.dao.FilmLikesDao;
import ru.yandex.practicum.filmorate.dao.MpaRatingDao;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@SpringBootTest
@AutoConfigureTestDatabase
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class FilmDbStorageTest {
    private final FilmDbStorage filmDbStorage;
    private final MpaRatingDao mpaRatingDao;
    private final UserDbStorage userDbStorage;
    private final FilmLikesDao filmLikesDao;

    @BeforeAll
    public void beforeAll() {
        userDbStorage.add(User.builder()
                .email("test101@email.ru")
                .login("test101_login")
                .name("test101_name")
                .birthday(LocalDate.of(1980, 11, 21))
                .build());
        userDbStorage.add(User.builder()
                .email("test102@email.ru")
                .login("test102_login")
                .name("test102_name")
                .birthday(LocalDate.of(1980, 12, 22))
                .build());
        filmDbStorage.add(Film.builder()
                .name("Фильм101")
                .description("Описание фильма101")
                .releaseDate(LocalDate.of(2010, 1, 1))
                .duration(110)
                .mpa(mpaRatingDao.getMpaById(1))
                .build());
        filmDbStorage.add(Film.builder()
                .name("Фильм102")
                .description("Описание фильма102")
                .releaseDate(LocalDate.of(2020, 2, 2))
                .duration(120)
                .mpa(mpaRatingDao.getMpaById(2))
                .build());
    }

    @Test
    public void getFilmByIdTest() {
        Optional<Film> filmOptional = Optional.ofNullable(filmDbStorage.get(1));
        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("id", 1L)
                );
    }

    @Test
    public void updateFilmTest() {
        Film updatedFilm = Film.builder()
                .id(1)
                .name("Фильм100")
                .description("Описание фильма100")
                .releaseDate(LocalDate.of(2000, 10, 5))
                .duration(100)
                .mpa(mpaRatingDao.getMpaById(3))
                .build();

        Optional<Film> filmOptional = Optional.ofNullable(filmDbStorage.update(updatedFilm));

        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("duration", 100L)
                );
        assertThat(filmDbStorage.get(1).getDuration()).isEqualTo(100);
    }

    @Test
    public void throwFilmNotFoundExceptionTest() {
        assertThatExceptionOfType(FilmNotFoundException.class).isThrownBy(() -> filmDbStorage.get(10));
    }
}
