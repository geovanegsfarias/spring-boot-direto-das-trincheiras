package academy.devdojo.repository;

import academy.devdojo.domain.UserProfile;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {

    @Query("SELECT up FROM UserProfile up join fetch up.user u join fetch up.profile p")
    List<UserProfile> retrieveAll();

//  @EntityGraph(attributePaths = "{user, profile}") // quando fizer essa query, considere esses atributos na hora de retornar
    @EntityGraph(value = "UserProfile.fullDetails")
    List<UserProfile> findAll();
}

// esse retrieveAll faz queries com JPQL (aula 97)
// usando jpql, podemos otimizar as queries que usamos no projeto, o findAll() faz a mesma coisa que o retrieveAll, porém usa 5 queries para isso.

/*
EntityGraph Resolve o problema de N+1 queries forçando o JPA a buscar os relacionamentos com a entidade principal numa única query com JOIN, mesmo que estejam marcados como LAZY.

Forma 1: direto no metodo, você cita os atributos de relacionamento diretamente na anotação @EntityGraph(attributePaths = "{user, profile}")

Forma 2: usando @NamedEntityGraph na entidade e referenciando com @EntityGraph(value = "?") no metodo


 */