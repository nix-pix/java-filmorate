package ru.yandex.practicum.filmorate.validator;

import lombok.experimental.UtilityClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.yandex.practicum.filmorate.model.User;

@UtilityClass
public class UserValidator {
    private static final Logger log = LoggerFactory.getLogger(UserValidator.class);

    public void validate(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
            log.info("Пустое имя пользователя было заменено на логин");
        }
    }
}
