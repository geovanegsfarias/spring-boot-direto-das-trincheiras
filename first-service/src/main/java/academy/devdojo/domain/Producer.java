package academy.devdojo.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
public class Producer {
    private Long id;
    private String name;
    private static List<Producer> producers = new ArrayList<>(List.of(
            new Producer(1L, "Mappa"),
            new Producer(2L, "Kyoto Animation"),
            new Producer(3L, "Madhouse")
    ));

    public static List<Producer> getProducers() {
        return producers;
    }
}