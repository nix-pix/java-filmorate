package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import ru.yandex.practicum.filmorate.validator.UserValidator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserStorage userStorage;
    private int idSequence = 0;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    private int generateId() {
        idSequence++;
        return idSequence;
    }

    public User addUser(User user) {
        UserValidator.validate(user);
        user.setId(generateId());
        return userStorage.add(user);
    }

    public User updateUser(User user) {
        UserValidator.validate(user);
        return userStorage.update(user);
    }

    public User getUser(int id) {
        return userStorage.get(id);
    }

    public Collection<User> getAllUsers() {
        return userStorage.getAll();
    }

    public void addFriend(int userId, int friendId) {
        User user = userStorage.get(userId);
        User friend = userStorage.get(friendId);
        user.getFriends().add(friendId);
        friend.getFriends().add(userId);
    }

    public void deleteFriend(int userId, int friendId) {
        userStorage.get(userId).getFriends().remove(friendId);
        userStorage.get(friendId).getFriends().remove(userId);
    }

    public List<User> getCommonFriends(int firstUserId, int secondUserId) {
        List<User> commonFriends = new ArrayList<>();
        User firstUser = userStorage.get(firstUserId);
        User secondUser = userStorage.get(secondUserId);
        for (Integer id : firstUser.getFriends()) {
            if (secondUser.getFriends().contains(id)) {
                commonFriends.add(userStorage.get(id));
            }
        }
        return commonFriends;
    }

    public List<User> getAllFriends(int id) {
        return userStorage.get(id).getFriends()
                .stream()
                .map(friendId -> userStorage.get(friendId))
                .collect(Collectors.toList());
//        List<User> friends = new ArrayList<>();
//        User user = userStorage.get(id);
//        if (user.getFriends() != null) {
//            for (int userId : user.getFriends()) {
//                friends.add(userStorage.get(userId));
//            }
//        }
//        return friends;
    }
}
