package pl.com.chrzanowski.sma.integrationTests;

import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import pl.com.chrzanowski.sma.AbstractTestContainers;
import pl.com.chrzanowski.sma.auth.dto.request.LoginRequest;
import pl.com.chrzanowski.sma.auth.dto.request.RegisterRequest;
import pl.com.chrzanowski.sma.auth.dto.response.MessageResponse;
import pl.com.chrzanowski.sma.common.enumeration.Country;
import pl.com.chrzanowski.sma.contractor.dto.ContractorDTO;
import pl.com.chrzanowski.sma.contractor.mapper.ContractorMapper;
import pl.com.chrzanowski.sma.contractor.model.Contractor;
import pl.com.chrzanowski.sma.contractor.repository.ContractorRepository;
import pl.com.chrzanowski.sma.contractor.service.filter.ContractorFilter;
import pl.com.chrzanowski.sma.email.service.SendEmailService;
import pl.com.chrzanowski.sma.integrationTests.helper.UserHelper;
import pl.com.chrzanowski.sma.role.model.Role;
import pl.com.chrzanowski.sma.role.repository.RoleRepository;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@AutoConfigureWebTestClient
public class ContractorIntegrationTest extends AbstractTestContainers {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private Flyway flyway;

    @MockBean
    private SendEmailService sendEmailService;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private ContractorRepository contractorRepository;

    private String jwtToken;
    @Autowired
    private ContractorMapper contractorMapper;

    @BeforeEach
    void setUp() {
        this.webTestClient = this.webTestClient.mutate()
                .responseTimeout(Duration.ofSeconds(60)).build();

        flyway.clean();
        flyway.migrate();

        reset(sendEmailService);

        when(sendEmailService.sendAfterRegistration(any(), any()))
                .thenReturn(new MessageResponse("Register successful"));
        when(sendEmailService.sendAfterEmailConfirmation(any(), any()))
                .thenReturn(new MessageResponse("Confirmed successful"));
        when(sendEmailService.sendPasswordResetMail(any(), any()))
                .thenReturn(new MessageResponse("Password reset token sent"));
        when(sendEmailService.sendAfterPasswordChange(any(), any()))
                .thenReturn(new MessageResponse("Password changed successfully"));

        Role adminRole = roleRepository.findByName("ROLE_ADMIN")
                .orElseThrow(() -> new RuntimeException("Role not found"));
        Role userRole = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new RuntimeException("Role not found"));

        RegisterRequest existingUser = RegisterRequest.builder()
                .username("username")
                .password("password")
                .email("username@test.com")
                .role(Set.of(adminRole.getName()))
                .build();

        UserHelper.registerUser(existingUser, webTestClient);
        RegisterRequest secondUser = RegisterRequest.builder()
                .username("second")
                .password("password")
                .email("second@test.com")
                .role(Set.of(userRole.getName()))
                .build();
        UserHelper.registerUser(secondUser, webTestClient);

        LoginRequest loginRequest = LoginRequest.builder()
                .username(existingUser.getUsername())
                .password(existingUser.getPassword())
                .rememberMe(false).build();

        this.jwtToken = UserHelper.authenticateUser(loginRequest, webTestClient);

        createContractor(null, "First Contractor", "1234567890", "Main Street", "10", "1A",
                "00-001", "Warsaw", Country.POLAND);

        createContractor(null, "Second Contractor", "0987654321", "Second Street", "20", "2B",
                "00-002", "Krakow", Country.POLAND);

    }

    private ContractorDTO createContractor(Long id, String name, String taxNumber, String street, String buildingNo,
                                           String apartmentNo, String postalCode, String city, Country country) {
        ContractorDTO contractor = ContractorDTO.builder()
                .id(id)
                .name(name)
                .taxNumber(taxNumber)
                .street(street)
                .buildingNo(buildingNo)
                .apartmentNo(apartmentNo)
                .postalCode(postalCode)
                .city(city)
                .country(country)
                .createdDatetime(Instant.now())
                .lastModifiedDatetime(Instant.now())
                .build();

        return webTestClient.post()
                .uri("/api/contractors/add")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(contractor)
                .exchange()
                .expectStatus().isOk()
                .expectBody(ContractorDTO.class)
                .returnResult().getResponseBody();
    }

    @Test
    void shouldAddContractorSuccessfully() {
        ContractorDTO newContractor = ContractorDTO.builder()
                .name("New Contractor")
                .taxNumber("5555555555")
                .street("New Street")
                .buildingNo("30")
                .apartmentNo("3C")
                .postalCode("00-003")
                .city("Gdansk")
                .country(Country.POLAND)
                .createdDatetime(Instant.now())
                .lastModifiedDatetime(Instant.now())
                .build();

        List<Contractor> contractorListBefore = contractorRepository.findAll();

        ContractorDTO savedContractor = webTestClient.post()
                .uri("/api/contractors/add")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(newContractor)
                .exchange()
                .expectStatus().isOk()
                .expectBody(ContractorDTO.class)
                .returnResult().getResponseBody();

        List<Contractor> contractorListAfter = contractorRepository.findAll();

        assertThat(contractorListAfter.size()).isEqualTo(contractorListBefore.size() + 1);

        assertThat(savedContractor).isNotNull();
        assertThat(savedContractor.getName()).isEqualTo("New Contractor");
        assertThat(savedContractor.getTaxNumber()).isEqualTo("5555555555");
    }

    @Test
    void shouldGetContractorByIdSuccessfully() {
        ContractorDTO contractor = webTestClient.get()
                .uri("/api/contractors/getById/1")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .exchange()
                .expectStatus().isOk()
                .expectBody(ContractorDTO.class)
                .returnResult().getResponseBody();

        assertThat(contractor).isNotNull();
        assertThat(contractor.getId()).isEqualTo(1L);
        assertThat(contractor.getName()).isEqualTo("First Contractor");
        assertThat(contractor.getCity()).isEqualTo("Warsaw");
    }

    @Test
    void shouldFilterContractorsByAllFieldsSuccessfully() {
        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("name", "First");
        queryParams.add("taxNumber", "1234567890");
        queryParams.add("street", "Main Street");
        queryParams.add("city", "Warsaw");
        queryParams.add("buildingNo", "10");
        queryParams.add("apartmentNo", "1A");
        queryParams.add("postalCode", "00-001");
        queryParams.add("country", Country.POLAND.name());
        queryParams.add("page", "0");
        queryParams.add("pageSize", "10");

        List<ContractorDTO> contractors = webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path("/api/contractors/find")
                        .queryParams(queryParams)
                        .build())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(ContractorDTO.class)
                .returnResult().getResponseBody();

        assertThat(contractors).hasSize(1);
        assertThat(contractors.get(0).getName()).isEqualTo("First Contractor");
    }

    @Test
    void shouldFilterContractorsByAllFieldsSuccessfullyReturnZeroElements() {
        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("name", "Second");
        queryParams.add("taxNumber", "1234567890");
        queryParams.add("street", "Second Street");
        queryParams.add("city", "Warsaw");
        queryParams.add("buildingNo", "10");
        queryParams.add("apartmentNo", "1A");
        queryParams.add("postalCode", "00-001");
        queryParams.add("country", Country.POLAND.name());
        queryParams.add("page", "0");
        queryParams.add("pageSize", "10");

        List<ContractorDTO> contractors = webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path("/api/contractors/find")
                        .queryParams(queryParams)
                        .build())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(ContractorDTO.class)
                .returnResult().getResponseBody();

        assertThat(contractors).hasSize(0);
    }

    @Test
    void shouldUpdateContractorSuccessfully() {
        ContractorDTO updateContractor = ContractorDTO.builder()
                .id(1L)
                .name("Updated Contractor")
                .taxNumber("1111111111")
                .street("Updated Street")
                .buildingNo("40")
                .apartmentNo("4D")
                .postalCode("00-004")
                .city("Poznan")
                .country(Country.POLAND)
                .createdDatetime(Instant.now())
                .lastModifiedDatetime(Instant.now())
                .build();

        ContractorDTO updatedContractor = webTestClient.put()
                .uri("/api/contractors/update")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(updateContractor)
                .exchange()
                .expectStatus().isOk()
                .expectBody(ContractorDTO.class)
                .returnResult().getResponseBody();

        assertThat(updatedContractor).isNotNull();
        assertThat(updatedContractor.getName()).isEqualTo("Updated Contractor");
        assertThat(updatedContractor.getCity()).isEqualTo("Poznan");
    }

    @Test
    void shouldFailToUpdateContractorWithoutAuthorization() {
        ContractorDTO updateContractor = ContractorDTO.builder()
                .id(1L)
                .name("Unauthorized Update")
                .build();

        webTestClient.put()
                .uri("/api/contractors/update")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(updateContractor)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void shouldDeleteContractorSuccessfully() {
        webTestClient.delete()
                .uri("/api/contractors/delete/1")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .exchange()
                .expectStatus().isOk();

        List<ContractorDTO> contractors = webTestClient.get()
                .uri("/api/contractors/all")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(ContractorDTO.class)
                .returnResult().getResponseBody();

        assertThat(contractors).hasSize(1); // Powinien zostać tylko drugi kontrahent
    }

    @Test
    void shouldFailToDeleteContractorWithoutAuthorization() {
        webTestClient.delete()
                .uri("/api/contractors/delete/1")
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void shouldGetAllContractorsWithPaginationSuccessfully() {
        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("page", "0");
        queryParams.add("pageSize", "1");

        List<ContractorDTO> contractors = webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path("/api/contractors/page")
                        .queryParams(queryParams)
                        .build())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(ContractorDTO.class)
                .returnResult().getResponseBody();

        assertThat(contractors).hasSize(2);
    }

    @Test
    void shouldReturnEmptyListWhenPageIsOutOfBounds() {
        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("page", "10");
        queryParams.add("pageSize", "10");

        List<ContractorDTO> contractors = webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path("/api/contractors/page")
                        .queryParams(queryParams)
                        .build())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(ContractorDTO.class)
                .returnResult().getResponseBody();

        assertThat(contractors).isEmpty();
    }

    @Test
    void shouldFailPaginationWithoutAuthorization() {
        webTestClient.get()
                .uri("/api/contractors/page?page=0&pageSize=10")
                .exchange()
                .expectStatus().isBadRequest();
    }

}
