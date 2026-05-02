package academy.devdojo.producer;

import academy.devdojo.domain.Producer;
import academy.devdojo.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProducerService {
    private final ProducerRepository repository;

    @Autowired
    public ProducerService(ProducerRepository repository) {
        this.repository = repository;
    }

    public List<Producer> findAll(String name) {
        return name == null ? repository.findAll() : repository.findByName(name);
    }

    public Producer findByIdOrThrowNotFound(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Producer not found"));
    }

    public Producer save(Producer producer) {
        return repository.save(producer);
    }

    public void delete(Long id) {
        var producer = findByIdOrThrowNotFound(id);
        repository.delete(producer);
    }

    public void update(Producer producerToUpdate) {
        assertThatProducerExists(producerToUpdate.getId());
        repository.save(producerToUpdate);
    }

    public void assertThatProducerExists(Long id) {
        findByIdOrThrowNotFound(id);
    }

}