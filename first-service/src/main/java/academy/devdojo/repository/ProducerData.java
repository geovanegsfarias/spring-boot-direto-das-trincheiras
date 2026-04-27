package academy.devdojo.repository;

import academy.devdojo.domain.Producer;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class ProducerData {
    private final List<Producer> producers = new ArrayList<>();

    {
        producers.addAll(List.of(
                Producer.builder().id(1L).name("Mappa").createdAt(LocalDateTime.now()).build(),
                Producer.builder().id(2L).name("Kyoto Animation").createdAt(LocalDateTime.now()).build(),
                Producer.builder().id(3L).name("Madhouse").createdAt(LocalDateTime.now()).build(),
                Producer.builder().id(4L).name("Toei Animation").createdAt(LocalDateTime.now()).build()));
    }

    public List<Producer> getProducers() {
        return producers;
    }

}
