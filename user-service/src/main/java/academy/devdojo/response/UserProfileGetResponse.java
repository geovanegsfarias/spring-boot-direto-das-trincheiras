package academy.devdojo.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserProfileGetResponse {
    // isso é um record aninhado numa classe, não é um atributo
    public record User(Long id, String firstName) {}
    public record Profile(Long id, String name) {}

    private Long id;
    private User user; // atributo do record lá de cima
    private Profile profile;
}

// não fazer vinculos entre DTOS, ou seja, não usar um dto dentro de um dto