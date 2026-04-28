package academy.devdojo.repository;

import academy.devdojo.domain.User;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserData {
    private final List<User> users = new ArrayList<>();

    {
        users.addAll(
                List.of(
                        User.builder().id(1L).firstName("William").lastName("Suane").email("devdojo@gmail.com").build(),
                        User.builder().id(2L).firstName("Geovane").lastName("Gomes").email("geovanegsf02@gmail.com").build(),
                        User.builder().id(3L).firstName("Darryl").lastName("Brawler").email("darryl@gmail.com").build()
                )
        );
    }

    public List<User> getUsers() {
        return users;
    }
}