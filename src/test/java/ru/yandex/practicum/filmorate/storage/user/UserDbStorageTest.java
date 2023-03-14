package ru.yandex.practicum.filmorate.storage.user;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserDbStorageTest {
    private final UserDbStorage userDbStorage;

    @BeforeAll
    public void beforeAll() {
        userDbStorage.add(User.builder()
                .email("test@email.ru")
                .login("test_login")
                .name("test_name")
                .birthday(LocalDate.of(1980, 10, 20))
                .build());
    }

    @Test
    public void findUserById() {
        Optional<User> userOptional = Optional.ofNullable(userDbStorage.get(1));

        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", 1L)
                );
    }

    @Test
    public void updateUser() {
        User updatedUser = User.builder()
                .id(1)
                .email("test1@email.ru")
                .login("test1_login")
                .name("test1_name")
                .birthday(LocalDate.of(1980, 11, 21))
                .build();

        Optional<User> userOptional = Optional.ofNullable(userDbStorage.update(updatedUser));

        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("email", "test1@email.ru")
                );
    }

    @Test
    public void getUsers() {
        userDbStorage.add(User.builder()
                .email("test2@email.ru")
                .login("test2_login")
                .name("test2_name")
                .birthday(LocalDate.of(1980, 12, 22))
                .build());
        Collection<User> userList = userDbStorage.getAll();
        assertThat(userList.size()).isEqualTo(2);
    }

    @Test
    public void throwUserNotFoundException() {
        assertThatExceptionOfType(UserNotFoundException.class).isThrownBy(() -> userDbStorage.get(5));
    }
}
