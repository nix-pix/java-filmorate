package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
public class InMemoryUserStorage implements UserStorage {
    private final Map<Long, User> users = new HashMap<>();

    private boolean isCorrectId(long id) {
        if (users.size() > 0 && users.containsKey(id)) {
            return true;
        } else {
            throw new UserNotFoundException("Получен некорректный id = " + id);
        }
    }

    @Override
    public User add(User user) {
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User update(User user) {
        if (isCorrectId(user.getId())) {
            users.put(user.getId(), user);
        }
        return user;
    }

    @Override
    public User get(long id) {
        boolean check = isCorrectId(id);
        return users.get(id);
    }

    @Override
    public Collection<User> getAll() {
        return users.values();
    }

    @Override
    public void delete(long id) {
        if (isCorrectId(id)) {
            users.remove(id);
        }
    }

    @Override
    public void deleteAll() {
        users.clear();
    }
}
