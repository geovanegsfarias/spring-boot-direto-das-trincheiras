package academy.devdojo.controllers;

import academy.devdojo.domain.Producer;
import academy.devdojo.mapper.ProducerMapperImpl;
import academy.devdojo.repository.ProducerData;
import academy.devdojo.repository.ProducerHardCodedRepository;
import academy.devdojo.service.ProducerService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ResourceLoader;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

// @SpringBootTest: usar quando você quer fazer um teste de integração
@WebMvcTest(controllers = ProducerController.class) // slice test: starta apenas o que é necessário para fazer o teste da camada web, como parâmetro qual controller queremos carregar no contexto(mock)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Import({ProducerMapperImpl.class, ProducerService.class, ProducerHardCodedRepository.class, ProducerData.class}) // import de tudo que o teste precisa pra funcionar (por padrão não carrega anotações de criação de beans)
class ProducerControllerTest {
    @Autowired
    private MockMvc mockMvc; // classe que permite testar endpoints
    @MockBean
    private ProducerData producerData;
    private List<Producer> producerList;
    @Autowired
    private ResourceLoader resourceLoader;

    @BeforeEach
    void init() {
        var dateTime = "2026-04-26T10:44:52.3883943";
        var formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSSS");
        var localDateTime = LocalDateTime.parse(dateTime, formatter);

        var ufotable = Producer.builder().id(1L).name("Ufotable").createdAt(localDateTime).build();
        var witStudio = Producer.builder().id(2L).name("Wit Studio").createdAt(localDateTime).build();
        var studioGhibli = Producer.builder().id(3L).name("Studio Ghibli").createdAt(localDateTime).build();
        var toeiAnimation = Producer.builder().id(4L).name("Toei Animation").createdAt(localDateTime).build();
        producerList = new ArrayList<>(List.of(ufotable, witStudio, studioGhibli, toeiAnimation));
    }

    @Test
    @DisplayName("GET v1/producers returns a list with all producers when argument is null")
    @Order(1)
    void findAll_ReturnsAllProducers_WhenArgumentIsNull() throws Exception {
        var response = readResourceFile("producer/get-producer-null-name-200.json");

        BDDMockito.when(producerData.getProducers()).thenReturn(producerList);

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/producers"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("GET v1/producers?name=Ufotable returns a list with found object when name exists")
    @Order(2)
    void findAll_ReturnsFoundProducersInList_WhenNameIsFound() throws Exception {
        BDDMockito.when(producerData.getProducers()).thenReturn(producerList);
        var response = readResourceFile("producer/get-producer-ufotable-name-200.json");
        var name = "Ufotable";

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/producers").param("name", name))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("GET v1/producers?name=x returns empty list when name is not found")
    @Order(3)
    void findAll_ReturnsEmptyList_WhenNameIsNull() throws Exception {
        BDDMockito.when(producerData.getProducers()).thenReturn(producerList);
        var response = readResourceFile("producer/get-producer-x-name-200.json");
        var name = "x";

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/producers").param("name", name))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("GET v1/producer/1 returns a producer with given id")
    @Order(4)
    void findById_ReturnsAProducerById_WhenSuccessful() throws Exception {
        BDDMockito.when(producerData.getProducers()).thenReturn(producerList);
        var response = readResourceFile("producer/get-producer-by-id-1-200.json");
        var id = 1L;

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/producers/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("GET v1/producer/99 throws ResponseStatusException 404 when producer is not found")
    @Order(5)
    void findById_ThrowsResponseStatusException_WhenProducerIsNotFound() throws Exception { // o contexto(mock) não retorna um response body, então buscamos outras formas:
        BDDMockito.when(producerData.getProducers()).thenReturn(producerList);
        var id = 99L;

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/producers/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.status().reason("Producer not Found"));
    }

    private String readResourceFile(String fileName) throws IOException {
        var file = resourceLoader.getResource("classpath:%s".formatted(fileName)).getFile();
        return new String(Files.readAllBytes(file.toPath()));
    }
}

