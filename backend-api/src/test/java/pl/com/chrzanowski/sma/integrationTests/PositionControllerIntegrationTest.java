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
import pl.com.chrzanowski.sma.integrationTests.helper.UserHelper;
import pl.com.chrzanowski.sma.position.dto.PositionDTO;
import pl.com.chrzanowski.sma.position.repository.PositionRepository;

import java.time.Duration;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
public class PositionControllerIntegrationTest extends AbstractTestContainers {

    public static final String POSITION_API_PATH = "/api/positions";

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private Flyway flyway;

    @Autowired
    private UserHelper userHelper;

    private String jwtToken;

    @Autowired
    private PositionRepository positionRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private CompanyDTOMapper companyDTOMapper;

    private CompanyDTO companyDTO;
    private Company company;

    @BeforeEach
    void setUp() {
        // Ustawienie timeoutu dla WebTestClient
        this.webTestClient = this.webTestClient.mutate()
                .responseTimeout(Duration.ofSeconds(60))
                .build();

        // Czyszczenie i migracja bazy danych przed każdym testem
        flyway.clean();
        flyway.migrate();

        // Rejestracja i autoryzacja pierwszego użytkownika
        LoginRequest firstUserLogin = userHelper.registerFirstUser(webTestClient);
        this.jwtToken = userHelper.authenticateUser(firstUserLogin, webTestClient);

        // Przygotowanie Company dla testów
        positionRepository.deleteAll();
        companyRepository.deleteAll();
        company = Company.builder().name("TestCompany").additionalInfo("TestInfo").build();
        company = companyRepository.saveAndFlush(company);
        companyDTO = companyDTOMapper.toDto(company);
    }

    /**
     * Helper – tworzy przykładowy obiekt PositionDTO.
     */
    private PositionDTO createSamplePosition() {
        return PositionDTO.builder()
                .name("Manager")
                .company(companyDTO)
                .build();
    }

    @Test
    void shouldAddPositionSuccessfully() {
        PositionDTO positionDTO = createSamplePosition();

        PositionDTO savedPosition = webTestClient.post()
                .uri(POSITION_API_PATH)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(positionDTO)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(PositionDTO.class)
                .returnResult().getResponseBody();

        assertThat(savedPosition).isNotNull();
        assertThat(savedPosition.getId()).isNotNull();
        assertThat(savedPosition.getName()).isEqualTo(positionDTO.getName());
        assertThat(savedPosition.getCompany()).isNotNull();
        assertThat(savedPosition.getCompany().getId()).isEqualTo(companyDTO.getId());
    }

    @Test
    void shouldUpdatePositionSuccessfully() {
        // Dodajemy pozycję
        PositionDTO positionDTO = createSamplePosition();
        PositionDTO savedPosition = webTestClient.post()
                .uri(POSITION_API_PATH)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(positionDTO)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(PositionDTO.class)
                .returnResult().getResponseBody();

        // Modyfikujemy nazwę pozycji
        PositionDTO updatedPositionDTO = PositionDTO.builder()
                .id(savedPosition.getId())
                .name("Senior Manager") // nowa nazwa
                .company(savedPosition.getCompany())
                .build();

        PositionDTO updatedPosition = webTestClient.put()
                .uri(POSITION_API_PATH + "/" + savedPosition.getId())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(updatedPositionDTO)
                .exchange()
                .expectStatus().isOk()
                .expectBody(PositionDTO.class)
                .returnResult().getResponseBody();

        assertThat(updatedPosition).isNotNull();
        assertThat(updatedPosition.getId()).isEqualTo(savedPosition.getId());
        assertThat(updatedPosition.getName()).isEqualTo("Senior Manager");
        assertThat(updatedPosition.getCompany()).isNotNull();
    }

    @Test
    void shouldGetPositionByIdSuccessfully() {
        // Dodajemy pozycję
        PositionDTO positionDTO = createSamplePosition();
        PositionDTO savedPosition = webTestClient.post()
                .uri(POSITION_API_PATH)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(positionDTO)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(PositionDTO.class)
                .returnResult().getResponseBody();

        // Pobieramy pozycję po ID
        PositionDTO retrievedPosition = webTestClient.get()
                .uri(POSITION_API_PATH + "/" + savedPosition.getId())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .exchange()
                .expectStatus().isOk()
                .expectBody(PositionDTO.class)
                .returnResult().getResponseBody();

        assertThat(retrievedPosition).isNotNull();
        assertThat(retrievedPosition.getId()).isEqualTo(savedPosition.getId());
        assertThat(retrievedPosition.getName()).isEqualTo(savedPosition.getName());
        assertThat(retrievedPosition.getCompany()).isNotNull();
    }

    @Test
    void shouldDeletePositionSuccessfully() {
        // Dodajemy pozycję
        PositionDTO positionDTO = createSamplePosition();
        PositionDTO savedPosition = webTestClient.post()
                .uri(POSITION_API_PATH)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(positionDTO)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(PositionDTO.class)
                .returnResult().getResponseBody();

        // Usuwamy pozycję
        webTestClient.delete()
                .uri(POSITION_API_PATH + "/" + savedPosition.getId())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .exchange()
                .expectStatus().isNoContent();

        // Sprawdzamy, czy pobranie usuniętej pozycji zwróci błąd (404)
        webTestClient.get()
                .uri(POSITION_API_PATH + "/" + savedPosition.getId())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .exchange()
                .expectStatus().is4xxClientError();
    }

    @Test
    void shouldGetPositionsByFilterSuccessfully() {
        // Dodajemy dwie pozycje o różnych nazwach
        PositionDTO positionManager = PositionDTO.builder()
                .name("Manager")
                .company(companyDTO)
                .build();
        PositionDTO positionDeveloper = PositionDTO.builder()
                .name("Developer")
                .company(companyDTO)
                .build();

        webTestClient.post()
                .uri(POSITION_API_PATH)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(positionManager)
                .exchange()
                .expectStatus().isCreated();

        webTestClient.post()
                .uri(POSITION_API_PATH)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(positionDeveloper)
                .exchange()
                .expectStatus().isCreated();


        List<PositionDTO> filteredPositions = webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path(POSITION_API_PATH)
                        .queryParam("nameContains", "Manager")
                        .build())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(PositionDTO.class)
                .returnResult().getResponseBody();

        assertThat(filteredPositions).isNotEmpty();
        assertThat(filteredPositions)
                .allMatch(position -> position.getName().contains("Manager"));
    }

    @Test
    void shouldGetPositionsByFilterAndPageSuccessfully() {
        // Dodajemy trzy pozycje
        PositionDTO position1 = PositionDTO.builder()
                .name("Senior Developer")
                .company(companyDTO)
                .build();
        PositionDTO position2 = PositionDTO.builder()
                .name("Junior Developer")
                .company(companyDTO)
                .build();
        PositionDTO position3 = PositionDTO.builder()
                .name("Mid Developer")
                .company(companyDTO)
                .build();

        webTestClient.post()
                .uri(POSITION_API_PATH)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(position1)
                .exchange()
                .expectStatus().isCreated();

        webTestClient.post()
                .uri(POSITION_API_PATH)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(position2)
                .exchange()
                .expectStatus().isCreated();

        webTestClient.post()
                .uri(POSITION_API_PATH)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(position3)
                .exchange()
                .expectStatus().isCreated();

        // Pobieramy pozycje z filtrem i paginacją
        List<PositionDTO> pagedPositions = webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path(POSITION_API_PATH)
                        .queryParam("nameContains", "Developer")
                        .queryParam("page", "0")
                        .queryParam("size", "2")
                        .build())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(PositionDTO.class)
                .returnResult().getResponseBody();

        assertThat(pagedPositions).isNotEmpty();
        assertThat(pagedPositions.size()).isLessThanOrEqualTo(2);
    }

    @Test
    void shouldFailAddPositionWithoutAuthentication() {
        PositionDTO positionDTO = createSamplePosition();

        webTestClient.post()
                .uri(POSITION_API_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(positionDTO)
                .exchange()
                .expectStatus().isUnauthorized();
    }

    @Test
    void shouldFailGetAllPositionsWithoutAuthentication() {
        webTestClient.get()
                .uri(POSITION_API_PATH)
                .exchange()
                .expectStatus().isUnauthorized();
    }

    // Negatywne scenariusze

    @Test
    void shouldFailUpdateNonexistentPosition() {
        // Próba aktualizacji pozycji, która nie istnieje
        PositionDTO nonExistentPosition = PositionDTO.builder()
                .id(999L) // zakładamy, że taki ID nie istnieje
                .name("Non Existent Position")
                .company(companyDTO)
                .build();

        webTestClient.put()
                .uri(POSITION_API_PATH + "/" + nonExistentPosition.getId())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(nonExistentPosition)
                .exchange()
                .expectStatus().is4xxClientError();
    }

    @Test
    void shouldFailGetPositionByIdNonexistent() {
        // Próba pobrania pozycji o nieistniejącym ID
        webTestClient.get()
                .uri(POSITION_API_PATH + "/999")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .exchange()
                .expectStatus().is4xxClientError();
    }

    @Test
    void shouldFailAddPositionWithInvalidData() {
        // Próba dodania pozycji z nieprawidłowymi danymi (np. pusta nazwa)
        PositionDTO invalidPosition = PositionDTO.builder()
                .name("") // brak nazwy
                .company(companyDTO)
                .build();

        webTestClient.post()
                .uri(POSITION_API_PATH)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(invalidPosition)
                .exchange()
                .expectStatus().is4xxClientError();
    }

    @Test
    void shouldFailAddPositionWithNullName() {
        // Próba dodania pozycji z null jako nazwą
        PositionDTO invalidPosition = PositionDTO.builder()
                .name(null) // null nazwa
                .company(companyDTO)
                .build();

        webTestClient.post()
                .uri(POSITION_API_PATH)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(invalidPosition)
                .exchange()
                .expectStatus().is4xxClientError();
    }

    @Test
    void shouldFailAddPositionWithNullCompany() {
        // Próba dodania pozycji z null jako company
        PositionDTO invalidPosition = PositionDTO.builder()
                .name("Manager")
                .company(null) // null company
                .build();

        webTestClient.post()
                .uri(POSITION_API_PATH)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(invalidPosition)
                .exchange()
                .expectStatus().is4xxClientError();
    }

    @Test
    void shouldFailUpdatePositionWithInvalidData() {
        // Dodajemy poprawną pozycję, a następnie próbujemy zaktualizować z niepoprawnymi danymi
        PositionDTO validPosition = createSamplePosition();
        PositionDTO savedPosition = webTestClient.post()
                .uri(POSITION_API_PATH)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(validPosition)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(PositionDTO.class)
                .returnResult().getResponseBody();

        // Aktualizacja z pustą nazwą
        PositionDTO invalidUpdate = PositionDTO.builder()
                .id(savedPosition.getId())
                .name("") // nieprawidłowa wartość
                .company(savedPosition.getCompany())
                .build();

        webTestClient.put()
                .uri(POSITION_API_PATH + "/" + invalidUpdate.getId())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(invalidUpdate)
                .exchange()
                .expectStatus().is4xxClientError();
    }

    @Test
    void getPositionsByFilterShouldReturnEmptyList() {
        // Dodanie poprawnej pozycji
        PositionDTO validPosition = createSamplePosition();
        webTestClient.post()
                .uri(POSITION_API_PATH)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(validPosition)
                .exchange()
                .expectStatus().isCreated();

        // Próba pobrania pozycji z filtrem, który nie pasuje do żadnej pozycji
        List<PositionDTO> positions = webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path(POSITION_API_PATH)
                        .queryParam("nameContains", "NonExistentPosition")
                        .queryParam("page", "0")
                        .queryParam("size", "10")
                        .build())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(PositionDTO.class)
                .returnResult()
                .getResponseBody();

        assertThat(positions).isEmpty();
    }

    @Test
    void shouldFailDeleteNonexistentPosition() {
        // Próba usunięcia pozycji, która nie istnieje
        webTestClient.delete()
                .uri(POSITION_API_PATH + "/999")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .exchange()
                .expectStatus().is4xxClientError();
    }

    @Test
    void shouldGetAllPositionsSuccessfully() {
        // Dodajemy kilka pozycji
        PositionDTO position1 = PositionDTO.builder()
                .name("Manager")
                .company(companyDTO)
                .build();
        PositionDTO position2 = PositionDTO.builder()
                .name("Developer")
                .company(companyDTO)
                .build();

        webTestClient.post()
                .uri(POSITION_API_PATH)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(position1)
                .exchange()
                .expectStatus().isCreated();

        webTestClient.post()
                .uri(POSITION_API_PATH)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(position2)
                .exchange()
                .expectStatus().isCreated();

        // Pobieramy wszystkie pozycje
        List<PositionDTO> allPositions = webTestClient.get()
                .uri(POSITION_API_PATH)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(PositionDTO.class)
                .returnResult().getResponseBody();

        assertThat(allPositions).isNotEmpty();
        assertThat(allPositions.size()).isGreaterThanOrEqualTo(2);
    }
}
