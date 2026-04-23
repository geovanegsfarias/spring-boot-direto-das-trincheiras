package academy.devdojo.repository;

import academy.devdojo.domain.Anime;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AnimeHardCodedRepository {
    private static final List<Anime> ANIMES = new ArrayList<>();

    static {
        ANIMES.addAll(List.of(
                Anime.builder().id(1L).name("Jujutsu Kaisen").build(),
                Anime.builder().id(2L).name("Naruto Shippuden").build(),
                Anime.builder().id(3L).name("Dragon Ball Super").build(),
                Anime.builder().id(4L).name("Hunter X Hunter").build(),
                Anime.builder().id(5L).name("JoJo's Bizarre Adventure").build()));
    }

    public List<Anime> findAll() {
        return ANIMES;
    }

    public Optional<Anime> findById(Long id) {
        return ANIMES.stream().filter(producer -> producer.getId().equals(id)).findFirst();
    }

    public List<Anime> findByName(String name) {
        return ANIMES.stream().filter(producer -> producer.getName().equalsIgnoreCase(name)).toList();
    }

    public Anime save(Anime anime) {
        ANIMES.add(anime);
        return anime;
    }

    public void delete(Anime anime) {
        ANIMES.remove(anime);
    }

    public void update(Anime anime) {
        delete(anime);
        save(anime);
    }

}
