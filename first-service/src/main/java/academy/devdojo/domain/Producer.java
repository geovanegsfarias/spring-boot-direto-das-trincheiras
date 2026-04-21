package academy.devdojo.domain;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Producer {
    @EqualsAndHashCode.Include
    private Long id;
    private String name;
    private LocalDateTime createdAt;
    private static List<Producer> producers = new ArrayList<>(List.of(
            Producer.builder().id(1L).name("Mappa").createdAt(LocalDateTime.now()).build(),
            Producer.builder().id(2L).name("Kyoto Animation").createdAt(LocalDateTime.now()).build(),
            Producer.builder().id(3L).name("Madhouse").createdAt(LocalDateTime.now()).build(),
            Producer.builder().id(4L).name("Toei Animation").createdAt(LocalDateTime.now()).build()
    ));

    public static List<Producer> getProducers() {
        return producers;
    }
}