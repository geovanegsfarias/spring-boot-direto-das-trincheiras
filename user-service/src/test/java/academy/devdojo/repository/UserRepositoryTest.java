package academy.devdojo.repository;

import academy.devdojo.commons.UserUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;

// slice test para o banco de dados
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
// o @DataJpaTest configura um banco de dados em memoria para os testes, para usar o seu próprio banco, você precisa desabilitar essa configuração
@Import(UserUtils.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
//@Transactional(propagation = Propagation.NOT_SUPPORTED) // os testes de repositorio rodam em transações, o que acontece em um teste é desfeito após o seu fim, para desabilitar isso, use essa anotação (oq o teste 1 fez, vai ser mantido para o teste 2), isso aumenta o acoplamento dos testes, mas aumenta a velocidade com q eles são executados, não é recomendado usar
class UserRepositoryTest {
    @Autowired
    private UserRepository repository;
    @Autowired
    private UserUtils userUtils;

    @Test
    @DisplayName("save creates an user")
    @Order(1)
    void save_CreatesUser_WhenSuccessful() {
        var userToSave = userUtils.newUserToSave();
        var savedUser = repository.save(userToSave);

        Assertions.assertThat(savedUser).hasNoNullFieldsOrProperties(); // se todos os campos foram inseridos
        Assertions.assertThat(savedUser.getId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("findAll returns a list with all users")
    @Order(2)
    @Sql("/sql/init_one_user.sql")
        // antes de executar o metodo, execute esse sql
    void findAll_ReturnsAllUsers_WhenSuccessful() {
        var users = repository.findAll();
        Assertions.assertThat(users).isNotEmpty();
    }

}

// se você estiver querys nativas, complexas que geralmente são dificeis de fzr pelo controller, você pode fzr esse tipo de teste de banco de dados, caso contrário é perda tempo
// é perda de tempo por que o banco de dados já vai ser testado de uma forma melhor nos testes de integração, e é necessário muitas configurações no h2 pelo application.yaml para fazer tudo funcionar já que o h2 é cheio de frescuras.