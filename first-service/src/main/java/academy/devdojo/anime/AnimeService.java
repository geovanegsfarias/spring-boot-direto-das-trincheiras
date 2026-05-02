package academy.devdojo.anime;

import academy.devdojo.domain.Anime;
import academy.devdojo.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnimeService {

    private final AnimeRepository repository;

    @Autowired
    public AnimeService(AnimeRepository repository) {
        this.repository = repository;
    }

    public List<Anime> findAll(String name) {
        return name == null ? repository.findAll() : repository.findByName(name);
    }

    public Anime findByIdOrThrowNotFound(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Anime not found"));
    }

    public Anime save(Anime anime) {
        return repository.save(anime);
    }

    public void delete(Long id) {
        var anime = findByIdOrThrowNotFound(id);
        repository.delete(anime);
    }

    public void update(Anime animeToUpdate) {
        assertThatAnimeExists(animeToUpdate.getId());
        repository.save(animeToUpdate);
    }

    public void assertThatAnimeExists(Long id) {
        findByIdOrThrowNotFound(id);
    }
}