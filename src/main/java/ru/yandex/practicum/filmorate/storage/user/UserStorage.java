package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

public interface UserStorage {

    User add(User user);

    User update(User user);

    User get(long id);

    Collection<User> getAll();

    void delete(long id);

    void deleteAll();
}
