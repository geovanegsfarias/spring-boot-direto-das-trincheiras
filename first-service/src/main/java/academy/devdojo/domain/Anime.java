package academy.devdojo.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
public class Anime {
    private Long id;
    private String name;
    private static List<Anime> animes = new ArrayList<>(List.of(
            new Anime(1L, "Jujutsu Kaisen"),
            new Anime(2L, "Naruto Shippuden"),
            new Anime(3L, "Dragon Ball Super"),
            new Anime(4L, "Hunter X Hunter"),
            new Anime(5L, "JoJo's Bizarre Adventure")
    ));

    public static List<Anime> getAnimes() {
        return animes;
    }
}