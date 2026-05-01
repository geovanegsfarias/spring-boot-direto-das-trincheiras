package academy.devdojo.service;

import academy.devdojo.domain.User;
import academy.devdojo.exception.NotFoundException;
import academy.devdojo.repository.UserHardCodedRepository;
import academy.devdojo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserHardCodedRepository repository;
    private final UserRepository userRepository;

    public List<User> findAll(String firstName) {
        return (firstName == null) ? userRepository.findAll() : repository.findByFirstName(firstName);
    }

    public User findByIdOrThrowException(Long id) {
        return repository.findById(id).orElseThrow(() -> new NotFoundException("User not found"));
    }

    public User save(User user) {
        return repository.save(user);
    }

    public void update(User userToUpdate) {
        findByIdOrThrowException(userToUpdate.getId());
        repository.update(userToUpdate);
    }

    public void delete(Long id) {
        var userToDelete = findByIdOrThrowException(id);
        repository.delete(userToDelete);
    }


}
