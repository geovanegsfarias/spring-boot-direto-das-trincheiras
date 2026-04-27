package academy.devdojo.repository;

import academy.devdojo.domain.Anime;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class AnimeData {
    private final List<Anime> animes = new ArrayList<>();

    {
        animes.addAll(List.of(
                Anime.builder().id(1L).name("Jujutsu Kaisen").build(),
                Anime.builder().id(2L).name("Naruto Shippuden").build(),
                Anime.builder().id(3L).name("Dragon Ball Super").build(),
                Anime.builder().id(4L).name("Hunter X Hunter").build(),
                Anime.builder().id(5L).name("JoJo's Bizarre Adventure").build()));
    }

    public List<Anime> getAnimes() {
        return animes;
    }
}
