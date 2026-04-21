package academy.devdojo.controllers;

import academy.devdojo.domain.Anime;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("v1/animes")
public class AnimeController {
    private Anime animeObject = new Anime();

    @GetMapping
    public List<Anime> listAll(@RequestParam(required = false) String name) {
        var animes = animeObject.listAnimes();

        if (name == null) {
            return animes;
        }

        return animes.stream().filter(anime -> anime.getName().equalsIgnoreCase(name)).toList();
    }

    @GetMapping("/{id}")
    public Anime findById(@PathVariable Long id) {
        var animes = animeObject.listAnimes();
        return animes.stream().filter(anime -> anime.getId().equals(id)).findFirst().orElse(null);
    }

}