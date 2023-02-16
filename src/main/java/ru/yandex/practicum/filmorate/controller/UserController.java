package ru.yandex.practicum.filmorate.controller;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/users")
@Slf4j
@Getter
public class UserController {
    private final UserStorage userStorage;
    private final UserService userService;

    @Autowired
    public UserController(UserStorage userStorage, UserService userService) {
        this.userStorage = userStorage;
        this.userService = userService;
    }

    @PostMapping
    public User add(@Valid @RequestBody User user) {
        log.info("Добавлен пользователь: {}", user);
        return userStorage.add(user);
    }

    @PutMapping
    public User update(@Valid @RequestBody @NotNull User user) {
        log.info("Обновлен пользователь: {}", user);
        return userStorage.update(user);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable int userId, @PathVariable int friendId) {
        log.info("Пользователь с id: {} добавил в друзья пользователя с id: {}", userId, friendId);
        userService.addFriend(userId, friendId);
    }

    @DeleteMapping("{id}/friends/{friendId}")
    public void deleteFriends(@PathVariable int userId, @PathVariable int friendId) {
        log.info("Пользователь с id: {} удалил из друзей пользователя с id: {}", userId, friendId);
        userService.deleteFriend(userId, friendId);
    }

    @GetMapping
    public Collection<User> getAll() {
        log.info("Получены все пользователи");
        return userStorage.getAll();
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable int id) {
        log.info("Получен пользователь с id: {}", id);
        return userStorage.get(id);
    }

    @GetMapping("/{id}/friends")
    public List<User> getAllFriends(@PathVariable int id) {
        log.info("Получен список друзей пользователя с id: {}", id);
        return userService.getAllFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getMutualFriends(@PathVariable int firstUserId, @PathVariable int secondUserId) {
        log.info("Получен список общих друзей пользователей с id: {} и {}", firstUserId, secondUserId);
        return userService.getMutualFriends(firstUserId, secondUserId);
    }
}
