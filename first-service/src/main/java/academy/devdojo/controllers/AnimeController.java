package academy.devdojo.controllers;

import academy.devdojo.domain.Anime;
import academy.devdojo.mapper.AnimeMapper;
import academy.devdojo.request.AnimePostRequest;
import academy.devdojo.request.AnimePutRequest;
import academy.devdojo.response.AnimeGetResponse;
import academy.devdojo.response.AnimePostResponse;
import academy.devdojo.service.AnimeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("v1/animes")
@Slf4j
public class AnimeController {
    private static final AnimeMapper MAPPER = AnimeMapper.INSTANCE;
    private AnimeService service;

    public AnimeController() {
        this.service = new AnimeService();
    }

    @GetMapping
    public ResponseEntity<List<AnimeGetResponse>> listAll(@RequestParam(required = false) String name) {
        log.debug("Request received to list all animes, params name: '{}'", name);

        var animes = service.findAll(name);

        var animeGetResponse = MAPPER.toAnimeGetResponseList(animes);

        return ResponseEntity.status(HttpStatus.OK).body(animeGetResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AnimeGetResponse> findById(@PathVariable Long id) {
        log.debug("Request to find anime by id: '{}'", id);

        var anime = service.findByIdOrThrowNotFound(id);

        var animeGetResponse = MAPPER.toAnimeGetResponse(anime);

        return ResponseEntity.status(HttpStatus.OK).body(animeGetResponse);
    }

    @PostMapping
    public ResponseEntity<AnimePostResponse> save(@RequestBody AnimePostRequest request) {
        log.debug("Request to save name: {}", request.getName());

        var anime = MAPPER.toAnime(request);

        var animeSaved = service.save(anime);

        var animePostResponse = MAPPER.toAnimePostResponse(animeSaved);

        return ResponseEntity.status(HttpStatus.CREATED).body(animePostResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        log.debug("Request to delete anime by id: {}", id);

        service.delete(id);

        return ResponseEntity.noContent().build();
    }

    @PutMapping
    public ResponseEntity<Void> update(@RequestBody AnimePutRequest request) {
        log.debug("Request to update anime: {}", request);

        var animeToUpdate = MAPPER.toAnime(request);

        service.update(animeToUpdate);

        return ResponseEntity.noContent().build();
    }

}