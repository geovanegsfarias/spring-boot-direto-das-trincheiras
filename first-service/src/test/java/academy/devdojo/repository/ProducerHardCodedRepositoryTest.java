package academy.devdojo.repository;

import academy.devdojo.domain.Producer;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ProducerHardCodedRepositoryTest {
    @Mock
    private ProducerData producerData;
    private final List<Producer> producerList = new ArrayList<>();
    @InjectMocks
    private ProducerHardCodedRepository repository;

    @BeforeEach
    void init() {
        var ufotable = Producer.builder().id(1L).name("Ufotable").createdAt(LocalDateTime.now()).build();
        var witStudio = Producer.builder().id(2L).name("Wit Studio").createdAt(LocalDateTime.now()).build();
        var studioGhibli = Producer.builder().id(3L).name("Studio Ghibli").createdAt(LocalDateTime.now()).build();
        var toeiAnimation = Producer.builder().id(4L).name("Toei Animation").createdAt(LocalDateTime.now()).build();
        producerList.addAll(List.of(ufotable, witStudio, studioGhibli));
    }

    @Test
    @DisplayName("findAll returns a list with all producers")
    @Order(1)
    void findAll_ReturnsAllProducers_WhenSuccessful() {
        BDDMockito.when(producerData.getProducers()).thenReturn(producerList);

        var producers = repository.findAll();

        Assertions.assertThat(producers).isNotNull().hasSameElementsAs(producerList);
    }

    @Test
    @DisplayName("findById returns a producer with given id")
    @Order(2)
    void findById_ReturnsAProducerById_WhenSuccessful() {
        BDDMockito.when(producerData.getProducers()).thenReturn(producerList);

        var expectedProducer = producerList.getFirst();
        var producers = repository.findById(1L);

        Assertions.assertThat(producers).isPresent().contains(expectedProducer);
    }

    @Test
    @DisplayName("findByName returns empty list when name is null")
    @Order(3)
    void findByName_ReturnsEmptyList_WhenNameIsNull() {
        BDDMockito.when(producerData.getProducers()).thenReturn(producerList);

        var producers = repository.findByName(null);

        Assertions.assertThat(producers).isNotNull().isEmpty();
    }

    @Test
    @DisplayName("findByName returns a list with found object when name exists")
    @Order(4)
    void findByName_ReturnsFoundProducersInList_WhenNameIsFound() {
        BDDMockito.when(producerData.getProducers()).thenReturn(producerList);

        var expectedProducer = producerList.getFirst();
        var producers = repository.findByName(expectedProducer.getName());

        Assertions.assertThat(producers).isNotNull().contains(expectedProducer);
    }

    @Test
    @DisplayName("save creates a producer")
    @Order(5)
    void save_CreatesProducer_WhenSuccessfully() {
        BDDMockito.when(producerData.getProducers()).thenReturn(producerList);

        var producerToSave = Producer.builder().id(99L).name("MAPPA").createdAt(LocalDateTime.now()).build();
        var producer = repository.save(producerToSave);

        Assertions.assertThat(producer).isEqualTo(producerToSave).hasNoNullFieldsOrProperties();

        var producerSavedOptional = repository.findById(producerToSave.getId());
        Assertions.assertThat(producerSavedOptional).isPresent().contains(producerToSave);
    }

    @Test
    @DisplayName("delete removes a producer")
    @Order(6)
    void delete_RemoveProducer_WhenSuccessful() {
        BDDMockito.when(producerData.getProducers()).thenReturn(producerList);

        var producerToDelete = producerList.getFirst();
        repository.delete(producerToDelete);

        var producers = repository.findAll();

        Assertions.assertThat(producers).isNotEmpty().doesNotContain(producerToDelete);
    }

    @Test
    @DisplayName("update updates a producer")
    @Order(7)
    void update_Updates_WhenSuccessful() {
        BDDMockito.when(producerData.getProducers()).thenReturn(producerList);
        var producerToUpdate = this.producerList.getFirst();
        producerToUpdate.setName("Aniplex");

        repository.update(producerToUpdate);

        Assertions.assertThat(this.producerList).contains(producerToUpdate);

        var producerUpdatedOptional = repository.findById(producerToUpdate.getId());

        Assertions.assertThat(producerUpdatedOptional).isPresent();
        Assertions.assertThat(producerUpdatedOptional.get().getName()).isEqualTo(producerToUpdate.getName());
    }
}

// Cobertura de testes é importante, mas a quantidade de cenários diferentes testados é mais importante (testando tanto pelo lado positivo, quanto para o negativo).