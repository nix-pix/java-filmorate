package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Objects;

@Component
@Slf4j
@Primary
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User add(User user) {
        String sqlQuery = "INSERT INTO users(email, user_login, user_name, birthday) VALUES (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement statement = con.prepareStatement(sqlQuery, new String[]{"user_id"});
            statement.setString(1, user.getEmail());
            statement.setString(2, user.getLogin());
            statement.setString(3, user.getName());
            statement.setString(4, String.valueOf(user.getBirthday()));
            return statement;
        }, keyHolder);
        user.setId(Objects.requireNonNull(keyHolder.getKey()).intValue());
        log.info("Внесен в БД новый пользователь с ID {}", user.getId());
        return user;
    }

    @Override
    public User update(User user) {
        String sqlQuery = "UPDATE users SET email = ?, user_login = ?, user_name = ?, birthday = ? WHERE user_id = ?";
        int recordsAffected = jdbcTemplate.update(sqlQuery,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday(),
                user.getId());
        if (recordsAffected != 1) {
            log.warn("Не найден пользователь с ID " + user.getId());
            throw new UserNotFoundException("Пользователь с ID " + user.getId() + " не найден!");
        }
        log.info("Обновлен пользователь в БД с ID {}", user.getId());
        return user;
    }

    @Override
    public User get(long id) {
        String sqlQuery = "SELECT * FROM users WHERE user_id = ?";
        try {
            log.info("Запрошен пользователь с ID {} из БД", id);
            return jdbcTemplate.queryForObject(sqlQuery, this::mapToRowUser, id);
        } catch (DataAccessException e) {
            log.warn("Не найден пользователь с ID " + id);
            throw new UserNotFoundException("Пользователь с ID " + id + " не найден!");
        }
    }

    @Override
    public Collection<User> getAll() {
        String sqlQuery = "SELECT * FROM users";
        log.info("Запрошен список пользователей из БД");
        return jdbcTemplate.query(sqlQuery, this::mapToRowUser);
    }

    @Override
    public void delete(long id) {
    }

    @Override
    public void deleteAll() {
    }

    private User mapToRowUser(ResultSet rs, int rowNum) throws SQLException {
        return User.builder()
                .id(rs.getLong("user_id"))
                .email(rs.getString("email"))
                .login(rs.getString("user_login"))
                .name(rs.getString("user_name"))
                .birthday(rs.getDate("birthday").toLocalDate())
                .build();
    }
}
