package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public User addUser(@Valid @RequestBody User user) {
        log.info("Добавлен пользователь: {}", user);
        return userService.addUser(user);
    }

    @PutMapping
    public User updateUser(@Valid @NotNull @RequestBody User user) {
        log.info("Обновлен пользователь: {}", user);
        return userService.updateUser(user);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable long id, @PathVariable long friendId) {
        log.info("Пользователь с id: {} добавил в друзья пользователя с id: {}", id, friendId);
        userService.addFriend(id, friendId);
    }

    @DeleteMapping("{id}/friends/{friendId}")
    public void deleteFriends(@PathVariable long id, @PathVariable long friendId) {
        log.info("Пользователь с id: {} удалил из друзей пользователя с id: {}", id, friendId);
        userService.deleteFriend(id, friendId);
    }

    @GetMapping
    public Collection<User> getAllUsers() {
        log.info("Получены все пользователи");
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable long id) {
        log.info("Получен пользователь с id: {}", id);
        return userService.getUser(id);
    }

    @GetMapping("/{id}/friends")
    public List<User> getAllFriends(@PathVariable long id) {
        log.info("Получен список друзей пользователя с id: {}", id);
        return userService.getAllFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getCommonFriends(@PathVariable long id, @PathVariable long otherId) {
        log.info("Получен список общих друзей пользователей с id: {} и {}", id, otherId);
        return userService.getCommonFriends(id, otherId);
    }
}
