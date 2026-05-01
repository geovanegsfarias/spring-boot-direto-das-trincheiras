package academy.devdojo.repository;

import academy.devdojo.commons.UserUtils;
import academy.devdojo.domain.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserHardCodedRepositoryTest {
    private List<User> userList;
    @Mock
    private UserData userData;
    @InjectMocks
    private UserHardCodedRepository repository;
    @InjectMocks
    private UserUtils userUtils; // usar @injectmocks apenas na classe a ser testada (não repetir isso)

    @BeforeEach
    void init() {
        userList = userUtils.newUserList();
    }

    @Test
    @DisplayName("findAll returns a list with all users")
    @Order(1)
    void findAll_ReturnsAllUsers_WhenSuccessful() {
        BDDMockito.when(userData.getUsers()).thenReturn(userList);

        var users = repository.findAll();

        Assertions.assertThat(users).isNotNull().hasSameElementsAs(userList);
    }

    @Test
    @DisplayName("findById returns a user with given id")
    @Order(2)
    void findById_ReturnsAUserById_WhenSuccessful() {
        BDDMockito.when(userData.getUsers()).thenReturn(userList);

        var expectedUser = userList.getFirst();
        var users = repository.findById(1L);

        Assertions.assertThat(users).isPresent().contains(expectedUser);
    }

    @Test
    @DisplayName("findByName returns empty list when name is null")
    @Order(3)
    void findByName_ReturnsEmptyList_WhenNameIsNull() {
        BDDMockito.when(userData.getUsers()).thenReturn(userList);

        var users = repository.findByFirstName(null);

        Assertions.assertThat(users).isNotNull().isEmpty();
    }

    @Test
    @DisplayName("findByName returns a list with found object when name exists")
    @Order(4)
    void findByName_ReturnsFoundUsersInList_WhenNameIsFound() {
        BDDMockito.when(userData.getUsers()).thenReturn(userList);

        var expectedUser = userList.getFirst();
        var users = repository.findByFirstName(expectedUser.getFirstName());

        Assertions.assertThat(users).isNotNull().hasSize(1).contains(expectedUser);
    }

    @Test
    @DisplayName("save creates a user")
    @Order(5)
    void save_CreatesUser_WhenSuccessfully() {
        BDDMockito.when(userData.getUsers()).thenReturn(userList);

        var userToSave = userUtils.newUserToSave();
        var user = repository.save(userToSave);

        Assertions.assertThat(user).isEqualTo(userToSave).hasNoNullFieldsOrProperties();

        var userSavedOptional = repository.findById(userToSave.getId());
        Assertions.assertThat(userSavedOptional).isPresent().contains(userToSave);
    }

    @Test
    @DisplayName("delete removes a user")
    @Order(6)
    void delete_RemoveUser_WhenSuccessful() {
        BDDMockito.when(userData.getUsers()).thenReturn(userList);

        var userToDelete = userList.getFirst();
        repository.delete(userToDelete);

        var users = repository.findAll();

        Assertions.assertThat(users).isNotEmpty().doesNotContain(userToDelete);
    }

    @Test
    @DisplayName("update updates a user")
    @Order(7)
    void update_Updates_WhenSuccessful() {
        BDDMockito.when(userData.getUsers()).thenReturn(userList);
        var userToUpdate = this.userList.getFirst();
        userToUpdate.setFirstName("Colt");

        repository.update(userToUpdate);

        Assertions.assertThat(this.userList).contains(userToUpdate);

        var userUpdatedOptional = repository.findById(userToUpdate.getId());

        Assertions.assertThat(userUpdatedOptional).isPresent();
        Assertions.assertThat(userUpdatedOptional.get().getFirstName()).isEqualTo(userToUpdate.getFirstName());
    }
}