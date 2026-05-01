package academy.devdojo.commons;

import academy.devdojo.domain.Anime;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class AnimeUtils {

    public List<Anime> newAnimeList() {
        var jujutsuKaisen = Anime.builder().id(1L).name("Jujutsu Kaisen").build();
        var narutoShippuden = Anime.builder().id(2L).name("Naruto Shippuden").build();
        var dragonBallSuper = Anime.builder().id(3L).name("Dragon Ball Super").build();
        var hunterXHunter = Anime.builder().id(4L).name("Hunter X Hunter").build();
        var jojo = Anime.builder().id(5L).name("JoJo's Bizarre Adventure").build();

        return new ArrayList<>(List.of(jujutsuKaisen, narutoShippuden, dragonBallSuper, hunterXHunter, jojo));
    }

    public Anime newAnimeToSave() {
        return Anime.builder().id(99L).name("One Piece").build();
    }
}