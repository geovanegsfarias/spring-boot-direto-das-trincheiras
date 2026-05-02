package academy.devdojo.producer;

import academy.devdojo.commons.FileUtils;
import academy.devdojo.commons.ProducerUtils;
import academy.devdojo.domain.Producer;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

// @SpringBootTest: usar quando você quer fazer um teste de integração
@WebMvcTest(controllers = ProducerController.class)
// slice test: starta apenas o que é necessário para fazer o teste da camada web, como parâmetro qual controller queremos carregar no contexto(mock)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ComponentScan(basePackages = {"academy.devdojo.producer", "academy.devdojo.commons"}) // package by feature
// import de tudo que o teste precisa pra funcionar (por padrão não carrega anotações de criação de beans)
// alternativa ao @Import que talvez não faça tanto sentido: @ComponentScan(basePackages = {"academy.devdojo"}) (dessa vez estamos usando package by feature no component scan, não é muito comum mas interessante para projetos grandes)
//@ActiveProfiles("test")
class ProducerControllerTest {
    private static final String URL = "/v1/producers";
    private List<Producer> producerList;
    @Autowired
    private MockMvc mockMvc; // classe que permite testar endpoints
    @MockBean
    private ProducerRepository repository;
    @Autowired
    private FileUtils fileUtils;
    @Autowired
    private ProducerUtils producerUtils;

    private static Stream<Arguments> putProducerBadRequestSource() {
        var allRequiredErrors = allRequiredErrors();
        allRequiredErrors.add("The field 'id' cannot be null");

        return Stream.of(
                Arguments.of("put-request-producer-empty-fields-400.json", allRequiredErrors),
                Arguments.of("put-request-producer-blank-fields-400.json", allRequiredErrors)
        );
    }

    private static Stream<Arguments> postProducerBadRequestSource() {
        var allRequiredErrors = allRequiredErrors();

        return Stream.of(
                Arguments.of("post-request-producer-empty-fields-400.json", allRequiredErrors),
                Arguments.of("post-request-producer-blank-fields-400.json", allRequiredErrors)
        );
    }

    private static List<String> allRequiredErrors() {
        var nameRequiredError = "The field 'name' is required";

        return new ArrayList<>(List.of(nameRequiredError));
    }

    @BeforeEach
    void init() {
        producerList = producerUtils.newProducerList();
    }

    @Test
    @DisplayName("GET v1/producers returns a list with all producers when argument is null")
    @Order(1)
    void findAll_ReturnsAllProducers_WhenArgumentIsNull() throws Exception {
        BDDMockito.when(repository.findAll()).thenReturn(producerList);
        var response = fileUtils.readResourceFile("producer/get-producer-null-name-200.json");

        mockMvc.perform(MockMvcRequestBuilders.get(URL))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("GET v1/producers?name=Ufotable returns a list with found object when name exists")
    @Order(2)
    void findAll_ReturnsFoundProducersInList_WhenNameIsFound() throws Exception {
        var response = fileUtils.readResourceFile("producer/get-producer-ufotable-name-200.json");
        var name = "Ufotable";
        var foundProducer = producerList.stream().filter(producer -> producer.getName().equals(name)).findFirst().orElse(null);

        BDDMockito.when(repository.findByName(name)).thenReturn(Collections.singletonList(foundProducer));

        mockMvc.perform(MockMvcRequestBuilders.get(URL).param("name", name))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("GET v1/producers?name=x returns empty list when name is not found")
    @Order(3)
    void findAll_ReturnsEmptyList_WhenNameIsNull() throws Exception {
        var response = fileUtils.readResourceFile("producer/get-producer-x-name-200.json");
        var name = "x";

        mockMvc.perform(MockMvcRequestBuilders.get(URL).param("name", name))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("GET v1/producers/1 returns a producer with given id")
    @Order(4)
    void findById_ReturnsAProducerById_WhenSuccessful() throws Exception {
        var response = fileUtils.readResourceFile("producer/get-producer-by-id-1-200.json");
        var id = 1L;
        var foundProducer = producerList.stream().filter(producer -> producer.getId().equals(id)).findFirst();
        BDDMockito.when(repository.findById(id)).thenReturn(foundProducer);

        mockMvc.perform(MockMvcRequestBuilders.get(URL + "/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("GET v1/producers/99 throws NotFound 404 when producer is not found")
    @Order(5)
    void findById_ThrowsNotFound_WhenProducerIsNotFound() throws Exception { // o contexto(mock) não retorna um response body, então buscamos outras formas:
        var response = fileUtils.readResourceFile("producer/get-response-producer-by-id-404.json");
        var id = 99L;

        mockMvc.perform(MockMvcRequestBuilders.get(URL + "/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("POST v1/producers creates a producer")
    @Order(6)
    void save_CreatesProducer_WhenSuccessfully() throws Exception {
        var request = fileUtils.readResourceFile("producer/post-request-producer-200.json");
        var response = fileUtils.readResourceFile("producer/post-response-producer-201.json");
        var producerToSave = producerUtils.newProducerToSave();

        BDDMockito.when(repository.save(ArgumentMatchers.any())).thenReturn(producerToSave);

        mockMvc.perform(MockMvcRequestBuilders
                        .post(URL)
                        .content(request)
                        .header("x-api-key", "123")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("DELETE v1/producers/1 removes a producer")
    @Order(7)
    void delete_RemoveProducer_WhenSuccessful() throws Exception {
        var id = producerList.getFirst().getId();
        var foundProducer = producerList.stream().filter(producer -> producer.getId().equals(id)).findFirst();
        BDDMockito.when(repository.findById(id)).thenReturn(foundProducer);

        mockMvc.perform(MockMvcRequestBuilders.delete(URL + "/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    @DisplayName("DELETE v1/producers/99 throws NotFound when producer is not found")
    @Order(8)
    void delete_ThrowsNotFound_WhenProducerIsNotFound() throws Exception {
        var response = fileUtils.readResourceFile("producer/delete-response-producer-404.json");
        var id = 99L;

        mockMvc.perform(MockMvcRequestBuilders.delete(URL + "/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("PUT v1/producers updates a producer")
    @Order(9)
    void update_Updates_WhenSuccessful() throws Exception {
        var request = fileUtils.readResourceFile("producer/put-request-producer-200.json");
        var id = 1L;
        var foundProducer = producerList.stream().filter(producer -> producer.getId().equals(id)).findFirst();
        BDDMockito.when(repository.findById(id)).thenReturn(foundProducer);

        mockMvc.perform(MockMvcRequestBuilders
                        .put(URL)
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    @DisplayName("PUT v1/producers throws NotFound when producer is not found")
    @Order(10)
    void update_ThrowsNotFound_WhenProducerIsNotFound() throws Exception {
        var request = fileUtils.readResourceFile("producer/put-request-producer-404.json");
        var response = fileUtils.readResourceFile("producer/put-response-producer-404.json");

        mockMvc.perform(MockMvcRequestBuilders
                        .put(URL)
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @ParameterizedTest
    @MethodSource("postProducerBadRequestSource")
    @DisplayName("POST v1/producers returns bad request when fields are not valid")
    @Order(11)
    void save_ReturnsBadRequest_WhenFieldsAreNotValid(String fileName, List<String> errors) throws Exception {
        var request = fileUtils.readResourceFile("producer/%s".formatted(fileName));

        var mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .post(URL)
                        .content(request)
                        .header("x-api-key", "123")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn();

        var resolvedException = mvcResult.getResolvedException();

        Assertions.assertThat(resolvedException).isNotNull();

        Assertions.assertThat(resolvedException.getMessage()).contains(errors);
    }

    @ParameterizedTest
    @MethodSource("putProducerBadRequestSource")
    @DisplayName("PUT v1/producers returns bad request when fields are not valid")
    @Order(12)
    void update_ReturnsBadRequest_WhenFieldsAreNotValid(String fileName, List<String> errors) throws Exception {
        var request = fileUtils.readResourceFile("producer/%s".formatted(fileName));

        var mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .put(URL)
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn();

        var resolvedException = mvcResult.getResolvedException();

        Assertions.assertThat(resolvedException).isNotNull();

        Assertions.assertThat(resolvedException.getMessage()).contains(errors);
    }

}

// Importante: entender a diferença entre @Mock, @MockBean e @SpyBean
// Importante: entender o funcionamento geral do mockito
// Importante: entender o funcionamento geral do mockito do spring
// Sobre @SpyBean: tem situações que você vai precisar mockar uma determinada chamada de um metodo, porém vc quer que o restante do funcionamento do objeto funcione normalmente.