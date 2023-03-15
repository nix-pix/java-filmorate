package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FriendDaoTest {
    private final FriendDao friendDao;
    private final UserDbStorage userDbStorage;

    @BeforeAll
    public void beforeAll() {
        userDbStorage.add(User.builder()
                .email("test10@email.ru")
                .login("test10_login")
                .name("test10_name")
                .birthday(LocalDate.of(1980, 10, 20))
                .build());
        userDbStorage.add(User.builder()
                .email("test11@email.ru")
                .login("test11_login")
                .name("test11_name")
                .birthday(LocalDate.of(1981, 11, 21))
                .build());
        userDbStorage.add(User.builder()
                .email("test12@email.ru")
                .login("test12_login")
                .name("test12_name")
                .birthday(LocalDate.of(1982, 12, 22))
                .build());
        userDbStorage.add(User.builder()
                .email("test13@email.ru")
                .login("test13_login")
                .name("test13_name")
                .birthday(LocalDate.of(1983, 3, 23))
                .build());
    }

    @Test
    public void addFriendTest() {
        friendDao.addFriend(1, 2);
        List<User> userList = friendDao.getFriends(1);
        User actualFriend = userList.get(0);

        assertThat(actualFriend).hasFieldOrPropertyWithValue("login", "test11_login");
    }

    @Test
    public void deleteFriendTest() {
        friendDao.addFriend(1, 2);
        friendDao.addFriend(1, 3);
        List<User> userListBefore = friendDao.getFriends(1);
        assertThat(userListBefore).hasSize(2);

        friendDao.deleteFriend(1, 3);
        List<User> userListAfter = friendDao.getFriends(1);
        assertThat(userListAfter).hasSize(2);
    }

    @Test
    public void findCommonFriendsTest() {
        friendDao.addFriend(1, 3);
        friendDao.addFriend(2, 3);

        List<User> commonList = friendDao.findCommonFriends(1, 2);
        User commonFriend = commonList.get(0);

        assertThat(commonList).hasSize(1);
        assertThat(commonFriend).hasFieldOrPropertyWithValue("login", "test12_login");
    }
}
