package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Film {
    private long id;

    @NotBlank(message = "Название не может быть пустым")
    @NotNull(message = "Требуется указать название")
    @Size(max = 200)
    private String name;

    @Size(max = 200)
    private String description;
    private LocalDate releaseDate;

    @Positive(message = "Продолжительность фильма должна быть положительной!")
    private long duration;
    private MpaRating mpa;
    private List<Genre> genres;
    private Set<Long> likes;

    public static final Comparator<Film> COMPARE_BY_LIKES = new Comparator<Film>() {
        @Override
        public int compare(Film o1, Film o2) {
            return o1.getLikes().size() - o2.getLikes().size();
        }
    };
}
