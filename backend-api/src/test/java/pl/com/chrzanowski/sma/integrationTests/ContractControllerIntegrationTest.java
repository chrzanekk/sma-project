package pl.com.chrzanowski.sma.integrationTests;

import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import pl.com.chrzanowski.sma.AbstractTestContainers;
import pl.com.chrzanowski.sma.auth.dto.request.LoginRequest;
import pl.com.chrzanowski.sma.company.dto.CompanyDTO;
import pl.com.chrzanowski.sma.company.mapper.CompanyDTOMapper;
import pl.com.chrzanowski.sma.company.model.Company;
import pl.com.chrzanowski.sma.company.repository.CompanyRepository;
import pl.com.chrzanowski.sma.contracts.dto.ContractDTO;
import pl.com.chrzanowski.sma.contracts.mapper.ContractDTOMapper;
import pl.com.chrzanowski.sma.contracts.repository.ContractRepository;
import pl.com.chrzanowski.sma.integrationTests.helper.UserHelper;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAmount;
import java.util.Currency;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.InstanceOfAssertFactories.TEMPORAL;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
public class ContractControllerIntegrationTest extends AbstractTestContainers {

    public static final String CONTRACT_API_PATH = "/api/contracts";
    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private Flyway flyway;

    @Autowired
    private UserHelper userHelper;

    private String jwtToken;

    @Autowired
    private ContractRepository contractRepository;

    @Autowired
    private ContractDTOMapper contractDTOMapper;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private CompanyDTOMapper companyDTOMapper;

    private Company company;
    private CompanyDTO companyDTO;

    @BeforeEach
    void setUp() {
        this.webTestClient = this.webTestClient.mutate()
                .responseTimeout(Duration.ofSeconds(60))
                .build();

        flyway.clean();
        flyway.migrate();

        LoginRequest firstUserLogin = userHelper.registerFirstUser(webTestClient);
        this.jwtToken = userHelper.authenticateUser(firstUserLogin, webTestClient);

        companyRepository.deleteAll();
        company = Company.builder().name("TestCompany").additionalInfo("TestInfo").build();
        company = companyRepository.saveAndFlush(company);
        companyDTO = companyDTOMapper.toDto(company);
    }

    private ContractDTO createSampleContract() {
        return ContractDTO.builder()
                .company(companyDTO)
                .number("CON2025-001")
                .value(BigDecimal.valueOf(200000L))
                .currency(Currency.getInstance("PLN"))
                .startDate(Instant.now())
                .endDate(Instant.now().atZone(ZoneOffset.UTC).plusMonths(2).toInstant())
                .description("Sample contract for tests")
                .build();
    }

    @Test
    void shouldAddContractSuccessfully() {
        ContractDTO contractBaseDTO = createSampleContract();

        ContractDTO savedContract = webTestClient.post()
                .uri(CONTRACT_API_PATH)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(contractBaseDTO)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(ContractDTO.class)
                .returnResult().getResponseBody();

        assertThat(savedContract).isNotNull();
        assertThat(savedContract.getId()).isNotNull();
        assertThat(savedContract.getNumber()).isEqualTo(contractBaseDTO.getNumber());
        assertThat(savedContract.getDescription()).isEqualTo(contractBaseDTO.getDescription());
    }

    @Test
    void shouldUpdateContractSuccessfully() {
        ContractDTO contractBaseDTO = createSampleContract();
        ContractDTO savedContract = webTestClient.post()
                .uri(CONTRACT_API_PATH)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(contractBaseDTO)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(ContractDTO.class)
                .returnResult().getResponseBody();

        ContractDTO updateContract = ContractDTO.builder()
                .id(savedContract.getId())
                .company(savedContract.getCompany())
                .number("CON2025-002")
                .value(savedContract.getValue())
                .currency(Currency.getInstance("PLN"))
                .startDate(savedContract.getStartDate())
                .endDate(Instant.now().atZone(ZoneOffset.UTC).plusMonths(1).toInstant())
                .description("Updated contract description")
                .build();

        ContractDTO updatedContract = webTestClient.put()
                .uri(CONTRACT_API_PATH + "/" + savedContract.getId())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(updateContract)
                .exchange()
                .expectStatus().isOk()
                .expectBody(ContractDTO.class)
                .returnResult().getResponseBody();

        assertThat(updatedContract).isNotNull();
        assertThat(updatedContract.getId()).isEqualTo(savedContract.getId());
        assertThat(updatedContract.getNumber()).isEqualTo("CON2025-002");
        assertThat(updatedContract.getDescription()).isEqualTo("Updated contract description");
    }

    @Test
    void shouldGetContractByIdSuccessfully() {
        ContractDTO contractBaseDTO = createSampleContract();
        ContractDTO savedContract = webTestClient.post()
                .uri(CONTRACT_API_PATH)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(contractBaseDTO)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(ContractDTO.class)
                .returnResult().getResponseBody();

        ContractDTO retrievedContract = webTestClient.get()
                .uri(CONTRACT_API_PATH + "/" + savedContract.getId())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .exchange()
                .expectStatus().isOk()
                .expectBody(ContractDTO.class)
                .returnResult().getResponseBody();

        assertThat(retrievedContract).isNotNull();
        assertThat(retrievedContract.getId()).isEqualTo(savedContract.getId());
    }

    @Test
    void shouldDeleteContractSuccessfully() {
        ContractDTO contractBaseDTO = createSampleContract();
        ContractDTO savedContract = webTestClient.post()
                .uri(CONTRACT_API_PATH)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(contractBaseDTO)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(ContractDTO.class)
                .returnResult().getResponseBody();

        webTestClient.delete()
                .uri(CONTRACT_API_PATH + "/" + savedContract.getId())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .exchange()
                .expectStatus().isNoContent();

        webTestClient.get()
                .uri(CONTRACT_API_PATH + "/" + savedContract.getId())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .exchange()
                .expectStatus().is4xxClientError();
    }

    @Test
    void shouldGetContractsByFilterSuccessfully() {
        ContractDTO contractA = ContractDTO.builder()
                .company(companyDTO)
                .number("CON-A")
                .description("A contract")
                .value(BigDecimal.valueOf(1))
                .currency(Currency.getInstance("PLN"))
                .startDate(Instant.now())
                .endDate(Instant.now().atZone(ZoneOffset.UTC).plusMonths(1).toInstant())
                .build();
        ContractDTO contractB = ContractDTO.builder()
                .company(companyDTO)
                .number("CON-B")
                .description("B contract")
                .value(BigDecimal.valueOf(2))
                .currency(Currency.getInstance("PLN"))
                .startDate(Instant.now())
                .endDate(Instant.now().atZone(ZoneOffset.UTC).plusMonths(2).toInstant())
                .build();

        webTestClient.post()
                .uri(CONTRACT_API_PATH)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(contractA)
                .exchange()
                .expectStatus().isCreated();

        webTestClient.post()
                .uri(CONTRACT_API_PATH)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(contractB)
                .exchange()
                .expectStatus().isCreated();

        List<ContractDTO> filteredContracts = webTestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(CONTRACT_API_PATH)
                        .queryParam("numberStartsWith", "CON-A")
                        .build())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(ContractDTO.class)
                .returnResult().getResponseBody();

        assertThat(filteredContracts)
                .isNotEmpty()
                .allMatch(c -> "CON-A".equals(c.getNumber()));
    }

    @Test
    void shouldGetContractsByFilterAndPageSuccessfully() {
        ContractDTO contract1 = ContractDTO.builder()
                .company(companyDTO)
                .number("PAG-1")
                .description("Paged contract 1")
                .value(BigDecimal.valueOf(1))
                .currency(Currency.getInstance("PLN"))
                .startDate(Instant.now())
                .endDate(Instant.now().atZone(ZoneOffset.UTC).plusMonths(1).toInstant())
                .build();
        ContractDTO contract2 = ContractDTO.builder()
                .company(companyDTO)
                .number("PAG-1")
                .description("Paged contract 2")
                .value(BigDecimal.valueOf(2))
                .currency(Currency.getInstance("PLN"))
                .startDate(Instant.now())
                .endDate(Instant.now().atZone(ZoneOffset.UTC).plusMonths(2).toInstant())
                .build();

        webTestClient.post()
                .uri(CONTRACT_API_PATH)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(contract1)
                .exchange()
                .expectStatus().isCreated();

        webTestClient.post()
                .uri(CONTRACT_API_PATH)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(contract2)
                .exchange()
                .expectStatus().isCreated();

        List<ContractDTO> pagedContracts = webTestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(CONTRACT_API_PATH)
                        .queryParam("contractNumber", "PAG-1")
                        .queryParam("page", "0")
                        .queryParam("size", "1")
                        .build())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(ContractDTO.class)
                .returnResult().getResponseBody();

        assertThat(pagedContracts).isNotEmpty();
        assertThat(pagedContracts.size()).isEqualTo(1);
    }

    @Test
    void shouldFailAddContractWithoutAuthentication() {
        ContractDTO contractBaseDTO = createSampleContract();

        webTestClient.post()
                .uri(CONTRACT_API_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(contractBaseDTO)
                .exchange()
                .expectStatus().isUnauthorized();
    }

    @Test
    void shouldFailGetAllContractsWithoutAuthentication() {
        webTestClient.get()
                .uri(CONTRACT_API_PATH)
                .exchange()
                .expectStatus().isUnauthorized();
    }

    @Test
    void shouldFailUpdateNonexistentContract() {
        ContractDTO nonExistent = ContractDTO.builder()
                .id(999L)
                .company(companyDTO)
                .number("DOES-NOT-EXIST")
                .value(BigDecimal.valueOf(1))
                .description("Should fail")
                .currency(Currency.getInstance("PLN"))
                .startDate(Instant.now())
                .endDate(Instant.now().atZone(ZoneOffset.UTC).plusMonths(2).toInstant())
                .build();

        webTestClient.put()
                .uri(CONTRACT_API_PATH + "/" + nonExistent.getId())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(nonExistent)
                .exchange()
                .expectStatus().is4xxClientError();
    }

    @Test
    void shouldFailGetContractByIdNonexistent() {
        webTestClient.get()
                .uri(CONTRACT_API_PATH + "/999")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .exchange()
                .expectStatus().is4xxClientError();
    }

    @Test
    void shouldFailAddContractWithInvalidData() {
        ContractDTO invalidContract = ContractDTO.builder()
                .company(companyDTO)
                .number("") // Puste pole wymagane
                .startDate(null) // Brak daty
                .description("Błąd danych")
                .build();

        webTestClient.post()
                .uri(CONTRACT_API_PATH)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(invalidContract)
                .exchange()
                .expectStatus().is4xxClientError();
    }

    @Test
    void shouldFailUpdateContractWithInvalidData() {
        ContractDTO validContract = createSampleContract();
        ContractDTO savedContract = webTestClient.post()
                .uri(CONTRACT_API_PATH)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(validContract)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(ContractDTO.class)
                .returnResult().getResponseBody();

        ContractDTO invalidUpdate = ContractDTO.builder()
                .id(savedContract.getId())
                .company(savedContract.getCompany())
                .number("") // niepoprawne dane
                .currency(Currency.getInstance("PLN"))
                .description(savedContract.getDescription())
                .startDate(null)
                .endDate(savedContract.getEndDate())
                .build();

        webTestClient.put()
                .uri(CONTRACT_API_PATH + "/" + invalidUpdate.getId())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(invalidUpdate)
                .exchange()
                .expectStatus().is4xxClientError();
    }

    @Test
    void getContractsByFilterShouldReturnEmptyList() {
        ContractDTO validContract = createSampleContract();
        webTestClient.post()
                .uri(CONTRACT_API_PATH)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(validContract)
                .exchange()
                .expectStatus().isCreated();

        List<ContractDTO> contracts = webTestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(CONTRACT_API_PATH)
                        .queryParam("contractNumber", "NO-SUCH-NUMBER")
                        .queryParam("page", "1")
                        .queryParam("size", "1")
                        .build())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(ContractDTO.class)
                .returnResult().getResponseBody();

        assertThat(contracts).isEmpty();
    }
}
