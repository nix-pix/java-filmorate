package ru.yandex.practicum.filmorate.dao.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.FriendDao;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Friend;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
@Slf4j
public class FriendDaoImpl implements FriendDao {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public FriendDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void addFriend(long id, long friendId) {
        String sqlQueryForFriendsFind = "SELECT friendship_status FROM friends WHERE user_1_id = ? AND user_2_id = ? OR user_2_id = ? AND user_1_id = ?";
        List<Boolean> statusList = jdbcTemplate.queryForList(sqlQueryForFriendsFind, Boolean.class, id, friendId, id, friendId);

        if (!statusList.isEmpty()) {
            Boolean status = statusList.get(0);

            if (status != null && !status) {
                String sqlQueryUpdate = "UPDATE friends SET friendship_status = true WHERE user_1_id = ? AND user_2_id = ? OR user_2_id =? AND user_1_id = ?";
                jdbcTemplate.update(sqlQueryUpdate, id, friendId, id, friendId);
                log.info("Пользователь с ID {} подтвердил дружбу с пользователем с ID {}", friendId, id);
            } else {
                String sqlQueryInsert = "INSERT INTO friends (user_1_id, user_2_id, friendship_status) VALUES (?, ?, false)";
                jdbcTemplate.update(sqlQueryInsert, id, friendId);
                log.info("Пользователь с ID {} добавил в друзья пользователя с ID {}", id, friendId);
            }
        } else {
            String sqlQueryInsert = "INSERT INTO friends (user_1_id, user_2_id, friendship_status) VALUES (?, ?, false)";
            jdbcTemplate.update(sqlQueryInsert, id, friendId);
            log.info("Пользователь с ID {} добавил в друзья пользователя с ID {}", id, friendId);
        }
    }

    @Override
    public void deleteFriend(long id, long friendId) {
        String sqlQueryFind = "SELECT friendship_status FROM friends WHERE user_1_id = ? AND user_2_id = ? OR user_2_id = ? AND user_1_id = ?";
        List<Boolean> statusList = jdbcTemplate.queryForList(sqlQueryFind, Boolean.class, id, friendId, id, friendId);

        if(!statusList.isEmpty()) {
            Boolean status = statusList.get(0);

            if (!status) {
                String sqlQuery = "DELETE FROM friends WHERE user_1_id = ? AND user_2_id = ?";
                log.info("Удалена строка о дружбе с ID1 {} и ID2 {}", id, friendId);
                jdbcTemplate.update(sqlQuery, id, friendId);
            } else {
                String sqlQuery = "UPDATE friends SET friendship_status = false WHERE user_1_id = ? AND user_2_id = ? OR user_2_id = ? AND user_1_id = ?";
                log.info("Установлен статус false в строке дружбы между ID1 {} и ID2 {}", id, friendId);
                jdbcTemplate.update(sqlQuery, id, friendId, id, friendId);
            }

        } else {
            log.warn("Пользователи с ID {} и {} не являются друзьями", id, friendId);
            throw new UserNotFoundException("Неверный запрос поиска дружбы между пользователями");
        }
    }

    @Override
    public List<User> getFriends(long id) {
        String sqlQuery = "SELECT * FROM users u JOIN friends f on u.user_id = f.user_2_id WHERE f.user_1_id = ?" +
                "UNION SELECT * FROM users u JOIN friends f on u.user_id = f.user_2_id WHERE f.user_2_id = ? AND friendship_status = true";
        log.info("Запрошен список друзей пользователя с ID {} из БД", id);
        return jdbcTemplate.query(sqlQuery, this::mapToRowUser, id, id);
    }

    @Override
    public List<User> findCommonFriends(long id, long friendId) {
        String sqlQuery = "SELECT * FROM users WHERE user_id IN (SELECT CASE " +
                "WHEN (user_1_id = ? AND user_2_id != ?) THEN user_2_id WHEN (user_1_id != ? AND user_2_id = ?) THEN user_1_id " +
                "END FROM friends INTERSECT SELECT CASE WHEN (user_1_id = ? AND user_2_id != ?) THEN user_2_id " +
                "WHEN (user_2_id != ? AND user_2_id = ?) THEN user_1_id END FROM friends)";
        log.info("Запрошен список общих друзей ID {} и ID {} из БД", id, friendId);
        return jdbcTemplate.query(sqlQuery, this::mapToRowUser, id, friendId, friendId, id, friendId, id, id, friendId);
    }

    private Friend mapToRowFriend(ResultSet rs, int rowNum) throws SQLException {
        return new Friend(rs.getInt("user_1_id"),
                rs.getInt("user_2_id"),
                rs.getBoolean("friendship_status"));
    }

    private User mapToRowUser (ResultSet rs, int rowNum) throws SQLException {
        return User.builder()
                .id(rs.getInt("user_id"))
                .email(rs.getString("email"))
                .login(rs.getString("user_login"))
                .name(rs.getString("user_name"))
                .birthday(rs.getDate("birthday").toLocalDate())
                .build();
    }
}
