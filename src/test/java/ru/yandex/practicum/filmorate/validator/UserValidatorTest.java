package ru.yandex.practicum.filmorate.validator;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserValidatorTest {

    @Test
    void validateNameTest() {
        User user = new User(
                1,
                "user@email.com",
                "login",
                null,
                LocalDate.of(2000, 12, 10));
        UserValidator.validate(user);
        assertEquals("login", user.getName());
    }
}