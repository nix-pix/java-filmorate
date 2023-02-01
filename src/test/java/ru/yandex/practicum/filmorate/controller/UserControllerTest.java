package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertThrows;

class UserControllerTest {
    UserController userController = new UserController();

    @Test
    void isCorrectIdTest() {
        User user = new User(
                1,
                "user@email.com",
                "login",
                "name",
                LocalDate.of(2000, 12, 10));
        userController.add(user);
        assertThrows(UserNotFoundException.class,
                () -> {
                    userController.isCorrectId(6);
                });
    }
}