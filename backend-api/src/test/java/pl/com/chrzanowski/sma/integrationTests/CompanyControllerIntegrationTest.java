package pl.com.chrzanowski.sma.integrationTests;

import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import pl.com.chrzanowski.sma.AbstractTestContainers;
import pl.com.chrzanowski.sma.auth.dto.request.LoginRequest;
import pl.com.chrzanowski.sma.company.dto.CompanyBaseDTO;
import pl.com.chrzanowski.sma.company.mapper.CompanyBaseMapper;
import pl.com.chrzanowski.sma.company.model.Company;
import pl.com.chrzanowski.sma.company.repository.CompanyRepository;
import pl.com.chrzanowski.sma.company.service.CompanyService;
import pl.com.chrzanowski.sma.email.service.SendEmailService;
import pl.com.chrzanowski.sma.integrationTests.helper.UserHelper;
import pl.com.chrzanowski.sma.user.mapper.UserMapper;
import pl.com.chrzanowski.sma.user.model.User;
import pl.com.chrzanowski.sma.user.service.UserService;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@AutoConfigureWebTestClient
@Import(CompanyControllerIntegrationTest.TestConfig.class)
public class CompanyControllerIntegrationTest extends AbstractTestContainers {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private Flyway flyway;

    @Autowired
    private SendEmailService sendEmailService;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private CompanyBaseMapper companyBaseMapper;

    @Autowired
    private UserHelper userHelper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserService userService;

    private String jwtToken;

    private CompanyBaseDTO firstCompany;

    @TestConfiguration
    static class TestConfig {
        @Bean
        @Primary
        public SendEmailService sendEmailService() {
            // Jeśli w Company nie wykorzystujemy wysyłki mailowej, można zamockować tę zależność
            return org.mockito.Mockito.mock(SendEmailService.class);
        }
    }

    @BeforeEach
    void setUp() {
        this.webTestClient = this.webTestClient.mutate()
                .responseTimeout(Duration.ofSeconds(60))
                .build();

        flyway.clean();
        flyway.migrate();

        reset(sendEmailService);

        // Jeśli wysyłka maili jest wywoływana przy rejestracji lub innych operacjach
        when(sendEmailService.sendAfterRegistration(any(), any()))
                .thenReturn(null);
        when(sendEmailService.sendAfterEmailConfirmation(any(), any()))
                .thenReturn(null);
        when(sendEmailService.sendPasswordResetMail(any(), any()))
                .thenReturn(null);
        when(sendEmailService.sendAfterPasswordChange(any(), any()))
                .thenReturn(null);

        // Rejestrujemy dwóch użytkowników i autoryzujemy pierwszego
        LoginRequest firstUser = userHelper.registerFirstUser(webTestClient);
        LoginRequest secondUser = userHelper.registerSecondUser(webTestClient);
        this.jwtToken = userHelper.authenticateUser(firstUser, webTestClient);

        User firstRegisteredUser = userMapper.toEntity(userService.getUserByLogin(firstUser.getLogin()));
        User secondRegisteredUser = userMapper.toEntity(userService.getUserByLogin(secondUser.getLogin()));

        // Tworzymy dwie firmy
        firstCompany = createCompany("First Company", firstRegisteredUser);

        createCompany("Second Company", secondRegisteredUser);
    }

    private CompanyBaseDTO createCompany(String name, User user) {
        Company company = Company.builder()
                .name(name)
                .createdDatetime(Instant.now())
                .lastModifiedDatetime(Instant.now())
                .createdBy(user)
                .build();
        Company savedCompany = companyRepository.save(company);
        return companyBaseMapper.toDto(savedCompany);
    }

    @Test
    void shouldAddCompanySuccessfully() {
        CompanyBaseDTO newCompany = CompanyBaseDTO.builder()
                .name("New Company")
                .createdDatetime(Instant.now())
                .lastModifiedDatetime(Instant.now())
                .build();

        List<Company> companiesBefore = companyRepository.findAll();

        CompanyBaseDTO savedCompany = webTestClient.post()
                .uri("/api/companies/add")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(newCompany)
                .exchange()
                .expectStatus().isOk()
                .expectBody(CompanyBaseDTO.class)
                .returnResult().getResponseBody();

        List<Company> companiesAfter = companyRepository.findAll();

        assertThat(companiesAfter.size()).isEqualTo(companiesBefore.size() + 1);
        assertThat(savedCompany).isNotNull();
        assertThat(savedCompany.getName()).isEqualTo("New Company");
    }

    @Test
    void shouldGetCompanyByIdSuccessfully() {
        Long id = firstCompany.getId();
        CompanyBaseDTO companyDTO = webTestClient.get()
                .uri("/api/companies/getById/" + id)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .exchange()
                .expectStatus().isOk()
                .expectBody(CompanyBaseDTO.class)
                .returnResult().getResponseBody();

        assertThat(companyDTO).isNotNull();
        assertThat(companyDTO.getId()).isEqualTo(firstCompany.getId());
        assertThat(companyDTO.getName()).isEqualTo("First Company");
    }

    @Test
    void shouldFilterCompaniesByAllFieldsSuccessfully() {
        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("nameStartsWith", "First");
        queryParams.add("taxNumberStartsWith", "1111");
        queryParams.add("streetStartsWith", "Main");
        queryParams.add("cityStartsWith", "Warsaw");

        List<CompanyBaseDTO> companies = webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path("/api/companies/find")
                        .queryParams(queryParams)
                        .build())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(CompanyBaseDTO.class)
                .returnResult().getResponseBody();

        assertThat(companies).hasSize(1);
        assertThat(companies.getFirst().getName()).isEqualTo("First Company");
    }

    @Test
    void shouldFilterCompaniesByAllFieldsSuccessfullyReturnZeroElements() {
        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("nameStartsWith", "Third");

        List<CompanyBaseDTO> companies = webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path("/api/companies/find")
                        .queryParams(queryParams)
                        .build())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(CompanyBaseDTO.class)
                .returnResult().getResponseBody();

        assertThat(companies).hasSize(0);
    }

    @Test
    void shouldUpdateCompanySuccessfully() {
        CompanyBaseDTO updateCompany = CompanyBaseDTO.builder()
                .id(firstCompany.getId())
                .name("Updated Company")
                .createdDatetime(Instant.now())
                .lastModifiedDatetime(Instant.now())
                .build();

        CompanyBaseDTO updatedCompany = webTestClient.put()
                .uri("/api/companies/update")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(updateCompany)
                .exchange()
                .expectStatus().isOk()
                .expectBody(CompanyBaseDTO.class)
                .returnResult().getResponseBody();

        assertThat(updatedCompany).isNotNull();
        assertThat(updatedCompany.getName()).isEqualTo("Updated Company");
    }

    @Test
    void shouldFailToUpdateCompanyWithoutAuthorization() {
        CompanyBaseDTO updateCompany = CompanyBaseDTO.builder()
                .id(firstCompany.getId())
                .name("Unauthorized Update")
                .build();

        webTestClient.put()
                .uri("/api/companies/update")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(updateCompany)
                .exchange()
                .expectStatus().isUnauthorized();
    }

    @Test
    void shouldDeleteCompanySuccessfully() {
        Long id = firstCompany.getId();
        webTestClient.delete()
                .uri("/api/companies/delete/" + id)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .exchange()
                .expectStatus().isNoContent();

        List<CompanyBaseDTO> companies = webTestClient.get()
                .uri("/api/companies/all")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(CompanyBaseDTO.class)
                .returnResult().getResponseBody();

        // Zakładamy, że po usunięciu pozostaną tylko 1 firma
        assertThat(companies).hasSize(1);
    }

    @Test
    void shouldFailToDeleteCompanyWithoutAuthorization() {
        webTestClient.delete()
                .uri("/api/companies/delete/" + 66L)
                .exchange()
                .expectStatus().isUnauthorized();
    }

    @Test
    void shouldGetAllCompaniesWithPaginationSuccessfully() {
        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("page", "0");
        queryParams.add("size", "1");

        List<CompanyBaseDTO> companies = webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path("/api/companies/page")
                        .queryParams(queryParams)
                        .build())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(CompanyBaseDTO.class)
                .returnResult().getResponseBody();

        // Zakładamy, że przy pageSize=1 pobieramy 1 element (ale możemy mieć dodatkowe nagłówki z paginacją)
        assertThat(companies).hasSize(1);
    }

    @Test
    void shouldReturnEmptyListWhenPageIsOutOfBounds() {
        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("page", "10");
        queryParams.add("pageSize", "10");

        List<CompanyBaseDTO> companies = webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path("/api/companies/page")
                        .queryParams(queryParams)
                        .build())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(CompanyBaseDTO.class)
                .returnResult().getResponseBody();

        assertThat(companies).isEmpty();
    }

    @Test
    void shouldFailPaginationWithoutAuthorization() {
        webTestClient.get()
                .uri("/api/companies/page?page=0&pageSize=10")
                .exchange()
                .expectStatus().isUnauthorized();
    }

    @Test
    void shouldFailToAddCompanyWithInvalidData() {
        CompanyBaseDTO invalidCompany = CompanyBaseDTO.builder()
                .name("") // pole wymagane, brak nazwy
                .createdDatetime(Instant.now())
                .lastModifiedDatetime(Instant.now())
                .build();

        webTestClient.post()
                .uri("/api/companies/add")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(invalidCompany)
                .exchange()
                .expectStatus().is4xxClientError();
    }

    @Test
    void shouldFailToUpdateNonExistentCompany() {
        CompanyBaseDTO nonExistentCompany = CompanyBaseDTO.builder()
                .id(9999L) // zakładamy, że taki ID nie istnieje
                .name("Nonexistent Company")
                .createdDatetime(Instant.now())
                .lastModifiedDatetime(Instant.now())
                .build();

        webTestClient.put()
                .uri("/api/companies/update")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(nonExistentCompany)
                .exchange()
                .expectStatus().is4xxClientError();
    }

    @Test
    void shouldFailToGetCompanyByNonexistentId() {
        webTestClient.get()
                .uri("/api/companies/getById/9999")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .exchange()
                .expectStatus().is4xxClientError();
    }

    @Test
    void shouldFailToGetCompaniesWithInvalidPaginationParams() {
        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("page", "1");
        queryParams.add("pageSize", "1");
        queryParams.add("nameStartsWith", "Third");

        List<CompanyBaseDTO> companies = webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path("/api/companies/page")
                        .queryParams(queryParams)
                        .build())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(CompanyBaseDTO.class)
                .returnResult().getResponseBody();

        assertThat(companies).isEmpty();
    }
}
