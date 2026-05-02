package academy.devdojo.service;

import academy.devdojo.domain.User;
import academy.devdojo.exception.EmailAlreadyExistsException;
import academy.devdojo.exception.NotFoundException;
import academy.devdojo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository repository;

    public List<User> findAll(String firstName) {
        return (firstName == null) ? repository.findAll() : repository.findByFirstNameIgnoreCase(firstName);
    }

    public User findByIdOrThrowException(Long id) {
        return repository.findById(id).orElseThrow(() -> new NotFoundException("User not found"));
    }

    public User save(User user) {
        assertEmailDoesNotExist(user.getEmail());
        return repository.save(user);
    }

    public void update(User userToUpdate) {
        findByIdOrThrowException(userToUpdate.getId());
        assertEmailDoesNotExist(userToUpdate.getEmail(), userToUpdate.getId());
        repository.save(userToUpdate);
    }

    public void delete(Long id) {
        var userToDelete = findByIdOrThrowException(id);
        repository.delete(userToDelete);
    }

    public void assertEmailDoesNotExist(String email) {
        repository.findByEmail(email).ifPresent(this::throwEmailExistsException); // = u -> throwEmailExistsException(u)
    }

    public void assertEmailDoesNotExist(String email, Long id) {
        repository.findByEmailAndIdNot(email, id).ifPresent(this::throwEmailExistsException);
    }

    private void throwEmailExistsException(User user) {
        throw new EmailAlreadyExistsException("E-mail %s already exists".formatted(user.getEmail()));
    }


}
