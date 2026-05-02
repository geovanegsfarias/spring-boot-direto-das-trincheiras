package academy.devdojo.commons;

import academy.devdojo.domain.User;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserUtils {

    public List<User> newUserList() {

        var william = User.builder().id(1L).firstName("William").lastName("Suane").email("devdojo@gmail.com").build();
        var geovane = User.builder().id(2L).firstName("Geovane").lastName("Gomes").email("geovanegsf02@gmail.com").build();
        var darryl = User.builder().id(3L).firstName("Darryl").lastName("Brawler").email("darryl@gmail.com").build();

        return new ArrayList<>(List.of(william, geovane, darryl));
    }

    public User newUserToSave() {
        return User.builder()
                .id(99L)
                .firstName("Colt")
                .lastName("Brawler")
                .email("colt@gmail.com")
                .build();
    }
}
