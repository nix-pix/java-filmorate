-- Инициализация значений в справочнике Genre
merge into genres (genre_name) key (genre_name)
    values('Комедия'), ('Драма'), ('Мультфильм'), ('Триллер'), ('Документальный'), ('Боевик');

-- Инициализация значений в справочнике RankMPA
merge into mpa_ratings (mpa_rating_name) key (mpa_rating_name)
    values ('G'), ('PG'), ('PG-13'), ('R'), ('NC-17');
