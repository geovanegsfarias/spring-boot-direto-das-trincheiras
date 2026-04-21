package academy.devdojo.controllers;

import academy.devdojo.domain.Producer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@RestController
@RequestMapping("v1/producers")
@Slf4j
public class ProducerController {

    @GetMapping
    public List<Producer> listAll(@RequestParam(required = false) String name) {
        var Producers = Producer.getProducers();

        if (name == null) {
            return Producers;
        }

        return Producers.stream().filter(producer -> producer.getName().equalsIgnoreCase(name)).toList();
    }

    @GetMapping("/{id}")
    public Producer findById(@PathVariable Long id) {
        return Producer.getProducers().stream().filter(producer -> producer.getId().equals(id)).findFirst().orElse(null);
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE,
    headers = "x-api-key")
    public ResponseEntity<Producer> saveProducer(@RequestBody Producer producer, @RequestHeader HttpHeaders headers) {
        log.info("Headers: '{}'", headers);
        producer.setId(ThreadLocalRandom.current().nextLong(6, 1000));
        Producer.getProducers().add(producer);
        return ResponseEntity.status(HttpStatus.CREATED).body(producer);
    }

}