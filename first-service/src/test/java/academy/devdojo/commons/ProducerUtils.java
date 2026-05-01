package academy.devdojo.commons;

import academy.devdojo.domain.Producer;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Component
public class ProducerUtils {
    public List<Producer> newProducerList() {
        var dateTime = "2026-04-26T10:44:52.3883943";
        var formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSSS");
        var localDateTime = LocalDateTime.parse(dateTime, formatter);

        var ufotable = Producer.builder().id(1L).name("Ufotable").createdAt(localDateTime).build();
        var witStudio = Producer.builder().id(2L).name("Wit Studio").createdAt(localDateTime).build();
        var studioGhibli = Producer.builder().id(3L).name("Studio Ghibli").createdAt(localDateTime).build();
        var toeiAnimation = Producer.builder().id(4L).name("Toei Animation").createdAt(localDateTime).build();

        return new ArrayList<>(List.of(ufotable, witStudio, studioGhibli, toeiAnimation));
    }

    public Producer newProducerToSave() {
        return Producer.builder().id(99L).name("Aniplex").createdAt(LocalDateTime.now()).build();
    }
}