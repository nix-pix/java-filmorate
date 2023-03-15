package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.FriendDao;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import ru.yandex.practicum.filmorate.validator.UserValidator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class UserService {
    private final UserStorage userStorage;
    private final FriendDao friendDao;
    private long idSequence = 0;

    @Autowired
    public UserService(UserStorage userStorage, FriendDao friendDao) {
        this.userStorage = userStorage;
        this.friendDao = friendDao;
    }

    private long generateId() {
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

    public User getUser(long id) {
        return userStorage.get(id);
    }

    public Collection<User> getAllUsers() {
        return userStorage.getAll();
    }

    public void addFriend(long userId, long friendId) {
//        User user = userStorage.get(userId);
//        User friend = userStorage.get(friendId);
//        user.getFriends().add(friendId);
//        friend.getFriends().add(userId);
        userStorage.get(userId);
        userStorage.get(friendId);

        friendDao.addFriend(userId, friendId);
    }

    public void deleteFriend(long userId, long friendId) {
//        userStorage.get(userId).getFriends().remove(friendId);
//        userStorage.get(friendId).getFriends().remove(userId);
        userStorage.get(userId);
        userStorage.get(friendId);

        friendDao.deleteFriend(userId, friendId);
    }

    public List<User> getCommonFriends(long firstUserId, long secondUserId) {
        List<User> commonFriends = new ArrayList<>();
        User firstUser = userStorage.get(firstUserId);
        User secondUser = userStorage.get(secondUserId);
        if (firstUser.getFriends() != null && secondUser.getFriends() != null) {
            for (Long id : firstUser.getFriends()) {
                if (secondUser.getFriends().contains(id)) {
                    commonFriends.add(userStorage.get(id));
                }
            }
        }
//        return commonFriends;
        return friendDao.findCommonFriends(firstUserId, secondUserId);
    }

    public List<User> getAllFriends(long id) {
        userStorage.get(id);
        return friendDao.getFriends(id);
//        return userStorage.get(id).getFriends()
//                .stream()
//                .map(friendId -> userStorage.get(friendId))
//                .collect(Collectors.toList());
    }
}
