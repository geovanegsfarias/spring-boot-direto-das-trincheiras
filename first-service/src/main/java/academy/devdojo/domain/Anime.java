package academy.devdojo.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Anime {
    private Long id;
    private String name;

    public List<Anime> listAnimes() {
        return List.of(
                new Anime(1L, "Jujutsu Kaisen"),
                new Anime(2L, "Naruto Shippuden"),
                new Anime(3L, "Dragon Ball Super"),
                new Anime(4L, "Hunter X Hunter"),
                new Anime(5L, "JoJo's Bizarre Adventure")
        );
    }
}