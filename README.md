# java-filmorate
### Бэкенд для сервиса, который работает с фильмами и оценками пользователей, а также возвращать топ-5 фильмов, рекомендованных к просмотру.

## Возможности приложения:
* создание, обновление фильмов;
* возможность отметки понравившихся фильмов;
* вывод лучших фильмов (в зависимости от количества лайков);
* создание и обновление пользователей.

## java-filmorate database schema
#### Важные моменты:
* Каждый столбец таблицы должен содержать только одно значение. Хранить массивы значений или вложенные записи нельзя.
* Все неключевые атрибуты должны однозначно определяться ключом.
* Все неключевые атрибуты должны зависеть только от первичного ключа, а не от других неключевых атрибутов.
* База данных должна поддерживать бизнес-логику, предусмотренную в приложении. Подумайте о том, как будет происходить получение всех фильмов, пользователей. А как — топ N наиболее популярных фильмов. Или список общих друзей с другим пользователем.

### ER-диаграмма
![ER-diagram](extras\ER-diagram.png)

https://dbdiagram.io/d/64025a28296d97641d8551ef

Для сохранения жанров вынес их в отдельную таблицу **films_genres** и использовать внешний ключ для связи с таблицей **films**.

В таблице **friends** информация о дружбе между пользователями. Столбцы **user_1_id** и **user_2_id** будут содержать ID пользователей, между которыми существует дружба. Столбец **friendship_status** будет хранить информацию о статусе дружбы.

Система рейтингов вынесена в отдельную таблицу **mpa_ratings**.

### Примеры запросов

#### UserController methods:

```sql
--getAllUsers
SELECT * 
FROM users;
```

```sql
--getUserById(long id)
SELECT *
FROM users
WHERE user_id = id;
```

```sql
--getAllFriends(long id)
SELECT *
FROM friends
WHERE user_1_id = id;
```

```sql
--Получить друзей юзера user1_id
SELECT *
FROM user
WHERE id IN (SELECT user_id
             FROM friends
             WHERE friend_id = user1_id);
```

```sql
--getCommonFriends(long id, long otherId)
SELECT user_2_id
FROM (SELECT *
      FROM friends AS f1
      WHERE user_1_id = id)
OUTER JOIN friends AS f2 ON f1.user_2_id = f2.user_2_id
WHERE user_1_id = id;
```

```sql
--Получить общих друзей iduser1 и iduser2
SELECT *
FROM user
WHERE id IN (SELECT user_id
             FROM friends
             WHERE friend_id = iduser1)
             AND id IN (SELECT user_id
                        FROM friends
                        WHERE friend_id = iduser2);
```

#### FilmController methods:

```sql
--getAll
SELECT *
FROM films
```

```sql
--getById(long id)
SELECT *
FROM films
WHERE film_id = id;
```

```sql
--getPopular(int count)
SELECT film_id,
       COUNT(user_id)
FROM likes
GROUP BY film_id
ORDER BY COUNT(user_id) DESC
LIMIT count;
```

```sql
--Выборка ТОП-5 наиболее популярных фильмов по лайкам
SELECT film_name,
       COUNT(*) AS films_count
FROM films
JOIN likes ON films.film_id = likes.film_id
GROUP BY films.film_id
ORDER BY films_count DESC
LIMIT 5;
```