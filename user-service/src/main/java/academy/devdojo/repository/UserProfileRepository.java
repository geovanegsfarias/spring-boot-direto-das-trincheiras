package academy.devdojo.repository;

import academy.devdojo.domain.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {

    @Query("SELECT up FROM UserProfile up join fetch up.user u join fetch up.profile p")
    List<UserProfile> retrieveAll();
}

// esse retrieveAll faz queries com JPQL (aula 97)
// usando jpql, podemos otimizar as queries que usamos no projeto, o findAll() faz a mesma coisa que o retrieveAll, porém usa 5 queries para isso.