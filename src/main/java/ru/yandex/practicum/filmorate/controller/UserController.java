package ru.yandex.practicum.filmorate.controller;

import lombok.Getter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validator.UserValidator;

import javax.validation.Valid;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
@Slf4j
@Getter
public class UserController {
    private int idSequence = 0;
    private final Map<Integer, User> users = new HashMap<>();

    private int generateId() {
        idSequence++;
        return idSequence;
    }

    @PostMapping
    public User add(@Valid @RequestBody User user) {
        user.setId(generateId());
        UserValidator.validate(user);
        users.put(user.getId(), user);
        log.info("Добавлен пользователь: {}", user);
        return user;
    }

    @PutMapping
    public User update(@Valid @RequestBody @NonNull User user) {
        if (isCorrectId(user.getId())) {
            UserValidator.validate(user);
            users.put(user.getId(), user);
            log.info("Обновлен пользователь: {}", user);
        }
        return user;
    }

    @GetMapping
    public Collection<User> getAll() {
        log.info("Получены все пользователи");
        return users.values();
    }

    public boolean isCorrectId(int id) {
        if (users.size() > 0 && users.containsKey(id)) {
            return true;
        } else {
            throw new UserNotFoundException("Получен некорректный id = " + id);
        }
    }
}