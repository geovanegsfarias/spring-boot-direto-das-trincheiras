package academy.devdojo.anime;

import academy.devdojo.domain.Anime;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("v1/animes")
@Slf4j
public class AnimeController {

    private final AnimeMapper mapper;
    private final AnimeService service;

    @Autowired
    public AnimeController(AnimeService service, AnimeMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @GetMapping
    public ResponseEntity<List<AnimeGetResponse>> listAll(@RequestParam(required = false) String name) {
        log.debug("Request received to list all animes, params name: '{}'", name);

        var animes = service.findAll(name);

        var animeGetResponse = mapper.toAnimeGetResponseList(animes);

        return ResponseEntity.status(HttpStatus.OK).body(animeGetResponse);
    }

    @GetMapping("/paginated")
    public ResponseEntity<Page<AnimeGetResponse>> listAllPaginated(Pageable pageable) {
        log.debug("Request received to list all animes paginated");

        var pageAnimeGetResponse = service.findAllPaginated(pageable).map(mapper::toAnimeGetResponse); // .map(page -> mapper.toAnimeGetResponse(page))
        return ResponseEntity.ok(pageAnimeGetResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AnimeGetResponse> findById(@PathVariable Long id) {
        log.debug("Request to find anime by id: '{}'", id);

        var anime = service.findByIdOrThrowNotFound(id);

        var animeGetResponse = mapper.toAnimeGetResponse(anime);

        return ResponseEntity.status(HttpStatus.OK).body(animeGetResponse);
    }

    @PostMapping
    public ResponseEntity<AnimePostResponse> save(@RequestBody @Valid AnimePostRequest request) {
        log.debug("Request to save name: {}", request.getName());

        var anime = mapper.toAnime(request);

        var animeSaved = service.save(anime);

        var animePostResponse = mapper.toAnimePostResponse(animeSaved);

        return ResponseEntity.status(HttpStatus.CREATED).body(animePostResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        log.debug("Request to delete anime by id: {}", id);

        service.delete(id);

        return ResponseEntity.noContent().build();
    }

    @PutMapping
    public ResponseEntity<Void> update(@RequestBody @Valid AnimePutRequest request) {
        log.debug("Request to update anime: {}", request);

        var animeToUpdate = mapper.toAnime(request);

        service.update(animeToUpdate);

        return ResponseEntity.noContent().build();
    }

}