package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserStorage userStorage;

    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public void addFriend(int userId, int friendId) {
        userStorage.get(userId).getFriends().add(userStorage.get(friendId).getId());
        userStorage.get(friendId).getFriends().add(userStorage.get(userId).getId());
    }

    public void deleteFriend(int userId, int friendId) {
        userStorage.get(userId).getFriends().remove(userStorage.get(friendId).getId());
        userStorage.get(friendId).getFriends().remove(userStorage.get(userId).getId());
    }

    public List<User> getMutualFriends(int firstUserId, int secondUserId) {
        Set<Integer> mutualFriendsSet = userStorage.get(firstUserId).getFriends();
        mutualFriendsSet.retainAll(userStorage.get(secondUserId).getFriends());
        return mutualFriendsSet
                .stream()
                .map(friendId -> userStorage.get(friendId))
                .collect(Collectors.toList());
    }

    public List<User> getAllFriends(int id) {
        return userStorage.get(id).getFriends()
                .stream()
                .map(friendId -> userStorage.get(friendId))
                .collect(Collectors.toList());
    }
}
