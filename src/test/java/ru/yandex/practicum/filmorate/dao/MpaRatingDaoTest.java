package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exception.MpaNotFoundException;
import ru.yandex.practicum.filmorate.model.MpaRating;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@SpringBootTest
@AutoConfigureTestDatabase
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class MpaRatingDaoTest {
    private final MpaRatingDao mpaRatingDao;

    @Test
    public void getAllMpaTest() {
        List<MpaRating> mpaList = mpaRatingDao.getAllMpa();

        assertThat(mpaList.size()).isEqualTo(5);
    }

    @Test
    public void getMpaByIdTest() {
        MpaRating mpa = mpaRatingDao.getMpaById(1);

        assertThat(mpa).hasFieldOrPropertyWithValue("name", "G");
    }

    @Test
    public void notMpsFoundTest() {
        assertThatExceptionOfType(MpaNotFoundException.class).isThrownBy(() -> mpaRatingDao.getMpaById(10));
    }
}
