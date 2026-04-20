package academy.devdojo.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "v1/heroes")
public class HeroController {
    private static final List<String> HEROES = List.of("Luffy", "Goku", "Naruto");

    @GetMapping
    public List<String> listAllHeroes() {
        return HEROES;
    }

    @GetMapping("/filter")
    public List<String> listAllHeroesParam(@RequestParam(required = true) String name) {
        return HEROES.stream().filter(hero -> hero.equalsIgnoreCase(name)).toList();
    }

    @GetMapping("/filterList")
    public List<String> listAllHeroesParamList(@RequestParam List<String> names) {
        return HEROES.stream().filter(hero -> names.contains(hero)).toList();
    }
}
