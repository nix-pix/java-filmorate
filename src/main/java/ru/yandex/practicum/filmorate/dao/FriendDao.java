package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface FriendDao {

    void addFriend(long id, long friendId);

    void deleteFriend(long id, long friendId);

    List<User> getFriends(long id);

    List<User> findCommonFriends(long id, long friendId);
}
