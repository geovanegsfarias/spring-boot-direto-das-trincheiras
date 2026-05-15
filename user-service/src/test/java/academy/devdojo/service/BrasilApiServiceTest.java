package academy.devdojo.service;

import academy.devdojo.commons.CepUtils;
import academy.devdojo.config.BrasilApiConfigurationProperties;
import academy.devdojo.config.RestClientConfiguration;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.match.MockRestRequestMatchers;
import org.springframework.test.web.client.response.MockRestResponseCreators;
import org.springframework.web.client.RestClient;
import org.testcontainers.shaded.com.fasterxml.jackson.core.JsonProcessingException;

@RestClientTest({RestClientConfiguration.class, BrasilApiService.class, BrasilApiConfigurationProperties.class, ObjectMapper.class, CepUtils.class})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class BrasilApiServiceTest {
    @Autowired
    private RestClient.Builder brasilApiClientBuilder;
    @Autowired
    private BrasilApiService service;
    @Autowired
    private MockRestServiceServer server;
    @Autowired
    private BrasilApiConfigurationProperties properties;
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private CepUtils cepUtils;

    // quando estamos trabalhando com servidores no geral, onde você precisa fazer o mock da requisicao e resposta, sempre é bom você usar essa anotação
    @AfterEach
    void reset() {
        server.reset();
    }

    @Order(1)
    @Test
    @DisplayName("findCep returns CepGetResponse when successful")
    void findCep_ReturnsCepGetResponse_WhenSuccessful() throws Exception {
        server = MockRestServiceServer.bindTo(brasilApiClientBuilder).build();
        var cep = "00000000";
        var cepGetResponse = cepUtils.newCepGetResponse();
        var jsonResponse = mapper.writeValueAsString(cepGetResponse);
        var requestTo = MockRestRequestMatchers.requestToUriTemplate(properties.baseUrl() + properties.cepUri(), cep);
        var withSuccess = MockRestResponseCreators.withSuccess(jsonResponse, MediaType.APPLICATION_JSON);
        server.expect(requestTo).andRespond(withSuccess);

        Assertions.assertThat(service.findCep(cep)).isNotNull().isEqualTo(cepGetResponse);
    }
}