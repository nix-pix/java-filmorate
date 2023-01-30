package ru.yandex.practicum.filmorate.controller;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validator.UserValidatior;

import javax.validation.Valid;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
@Slf4j
@Getter
public class UserController {
    private Map<Integer, User> users = new HashMap<>();
    UserValidatior userValidatior;

    @PostMapping
    public User add(@Valid @RequestBody User user) {
        UserValidatior.validate(user);
        log.info("Добавлен пользователь: {}", user);
        users.put(user.getId(), user);
        return user;
    }

    @PutMapping
    public User update(@Valid @RequestBody User user) {
        userValidatior.validate(user);
        log.info("Обновлен пользователь: {}", user);
        users.put(user.getId(), user);
        return user;
    }

    @GetMapping
    public Collection<User> getAll() {
        log.info("Получены все пользователи");
        return users.values();
    }
}