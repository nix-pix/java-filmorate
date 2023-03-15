package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DuplicateKeyException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import java.time.LocalDate;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@SpringBootTest
@AutoConfigureTestDatabase
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FilmLikesDaoTest {
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
                .name("Фильм11")
                .description("Описание фильма11")
                .releaseDate(LocalDate.of(2020, 2, 1))
                .duration(100)
                .mpa(mpaRatingDao.getMpaById(1))
                .build());
        filmDbStorage.add(Film.builder()
                .name("Фильм12")
                .description("Описание фильма12")
                .releaseDate(LocalDate.of(2010, 2, 2))
                .duration(120)
                .mpa(mpaRatingDao.getMpaById(2))
                .build());
        filmDbStorage.add(Film.builder()
                .name("Фильм13")
                .description("Описание фильма13")
                .releaseDate(LocalDate.of(2013, 3, 3))
                .duration(130)
                .mpa(mpaRatingDao.getMpaById(3))
                .build());
        filmDbStorage.add(Film.builder()
                .name("Фильм14")
                .description("Описание фильма14")
                .releaseDate(LocalDate.of(2014, 4, 4))
                .duration(140)
                .mpa(mpaRatingDao.getMpaById(4))
                .build());
    }

    @Test
    public void addLikeTest() {
        filmLikesDao.addLike(1, 1);
        assertThat(filmDbStorage.get(1).getLikes().size()).isEqualTo(1);
    }

    @Test
    public void deleteLikeTest() {
        filmLikesDao.addLike(2, 2);
        filmLikesDao.deleteLike(2, 2);

        assertThat(filmDbStorage.get(2).getLikes().isEmpty()).isTrue();
    }

    @Test
    public void findLikesOfFilmTest() {
        filmLikesDao.addLike(4, 1);
        filmLikesDao.addLike(4, 2);
        Set<Long> userSet = filmLikesDao.findLikesOfFilm(4);

        assertThat(userSet.size()).isEqualTo(2);
    }

    @Test
    public void canNotAddAnotherLikeToSameFilmTest() {
        filmLikesDao.addLike(3, 2);
        assertThatExceptionOfType(DuplicateKeyException.class).isThrownBy(() -> filmLikesDao.addLike(3, 2));
    }
}
