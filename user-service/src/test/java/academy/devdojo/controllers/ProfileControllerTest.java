package academy.devdojo.controllers;

import academy.devdojo.commons.FileUtils;
import academy.devdojo.commons.ProfileUtils;
import academy.devdojo.domain.Profile;
import academy.devdojo.mapper.ProfileMapperImpl;
import academy.devdojo.repository.ProfileRepository;
import academy.devdojo.service.ProfileService;
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
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

@WebMvcTest(controllers = ProfileController.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Import({ProfileMapperImpl.class, ProfileService.class, ProfileRepository.class, FileUtils.class, ProfileUtils.class})
class ProfileControllerTest {
    private static final String URL = "/v1/profiles";
    private List<Profile> profileList;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ProfileRepository repository;
    @Autowired
    private FileUtils fileUtils;
    @Autowired
    private ProfileUtils profileUtils;
    
    @BeforeEach
    void init() {
        profileList = profileUtils.newProfileList();
    }

    @Test
    @DisplayName("GET v1/profiles returns a list with all profiles")
    @Order(1)
    void findAll_ReturnsAllProfiles_WhenSuccessful() throws Exception {
        BDDMockito.when(repository.findAll()).thenReturn(profileList);
        var response = fileUtils.readResourceFile("profile/get-profiles-200.json");

        mockMvc.perform(MockMvcRequestBuilders.get(URL))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("GET v1/profiles returns empty list when nothing is found")
    @Order(2)
    void findAll_ReturnsEmptyList_WhenNothingIsFound() throws Exception {
        var response = fileUtils.readResourceFile("profile/get-profiles-empty-list-200.json");

        mockMvc.perform(MockMvcRequestBuilders.get(URL))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("POST v1/profiles creates a profile")
    @Order(3)
    void save_CreatesProfile_WhenSuccessfully() throws Exception {
        var request = fileUtils.readResourceFile("profile/post-request-profile-200.json");
        var response = fileUtils.readResourceFile("profile/post-response-profile-201.json");
        var profileSaved = profileUtils.newProfileSaved();

        BDDMockito.when(repository.save(ArgumentMatchers.any())).thenReturn(profileSaved);

        mockMvc.perform(MockMvcRequestBuilders
                        .post(URL)
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @ParameterizedTest
    @MethodSource("postProfileBadRequestSource")
    @DisplayName("POST v1/profiles returns bad request when fields are not valid")
    @Order(4)
    void save_ReturnsBadRequest_WhenFieldsAreNotValid(String fileName, List<String> errors) throws Exception { // testando bean validation, nesse caso estamos testando todos os campos de uma vez, mas em projetos reais buscar testar de modo unitário (cada campo por vez)
        var request = fileUtils.readResourceFile("profile/%s".formatted(fileName));

        var mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .post(URL)
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

    private static Stream<Arguments> postProfileBadRequestSource() {
        var nameRequiredError = "The field 'name' is required";
        var descriptionRequiredError = "The field 'description' is required";

        var allErrors = List.of(nameRequiredError, descriptionRequiredError);

        return Stream.of(
                Arguments.of("post-request-profile-empty-fields-400.json", allErrors),
                Arguments.of("post-request-profile-blank-fields-400.json", allErrors)
        );
    }

}