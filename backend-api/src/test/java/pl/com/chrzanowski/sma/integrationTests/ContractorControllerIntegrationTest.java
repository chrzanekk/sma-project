package pl.com.chrzanowski.sma.integrationTests;

import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
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
import pl.com.chrzanowski.sma.auth.dto.response.MessageResponse;
import pl.com.chrzanowski.sma.common.enumeration.Country;
import pl.com.chrzanowski.sma.company.mapper.CompanyBaseMapper;
import pl.com.chrzanowski.sma.company.model.Company;
import pl.com.chrzanowski.sma.company.repository.CompanyRepository;
import pl.com.chrzanowski.sma.contact.dto.ContactBaseDTO;
import pl.com.chrzanowski.sma.contact.model.Contact;
import pl.com.chrzanowski.sma.contractor.dto.ContractorBaseDTO;
import pl.com.chrzanowski.sma.contractor.dto.ContractorDTO;
import pl.com.chrzanowski.sma.contractor.mapper.ContractorDTOMapper;
import pl.com.chrzanowski.sma.contractor.model.Contractor;
import pl.com.chrzanowski.sma.contractor.repository.ContractorRepository;
import pl.com.chrzanowski.sma.email.service.SendEmailService;
import pl.com.chrzanowski.sma.integrationTests.helper.UserHelper;
import pl.com.chrzanowski.sma.user.mapper.UserMapper;
import pl.com.chrzanowski.sma.user.model.User;
import pl.com.chrzanowski.sma.user.service.UserService;

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
@Import(ContractorControllerIntegrationTest.TestConfig.class)
public class ContractorControllerIntegrationTest extends AbstractTestContainers {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private Flyway flyway;

    @Autowired
    private SendEmailService sendEmailService;

    @Autowired
    private ContractorRepository contractorRepository;

    private String jwtToken;

    @Autowired
    private UserHelper userHelper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ContractorDTOMapper contractorMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private CompanyBaseMapper companyBaseMapper;

    private ContractorDTO firstContractor;

    private Company company;

    @TestConfiguration
    static class TestConfig {
        @Bean
        @Primary
        public SendEmailService sendEmailService() {
            return Mockito.mock(SendEmailService.class);
        }
    }


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

        LoginRequest firstUser = userHelper.registerFirstUser(webTestClient);
        LoginRequest secondUser = userHelper.registerSecondUser(webTestClient);
        this.jwtToken = userHelper.authenticateUser(firstUser, webTestClient);

        User firstRegisteredUser = userMapper.toEntity(userService.getUserByLogin(firstUser.getLogin()));
        User secondRegisteredUser = userMapper.toEntity(userService.getUserByLogin(secondUser.getLogin()));

        firstContractor = createContractor("First Contractor", "1234567890", "Main Street", "10", "1A",
                "00-001", "Warsaw", Country.POLAND, firstRegisteredUser);

        createContractor("Second Contractor", "0987654321", "Second Street", "20", "2B",
                "00-002", "Krakow", Country.ENGLAND, secondRegisteredUser);
        companyRepository.deleteAll();
        company = Company.builder().name("TestCompany").additionalInfo("TestInfo").build();
        company = companyRepository.saveAndFlush(company);

    }

    private ContractorDTO createContractor(String name, String taxNumber, String street, String buildingNo,
                                           String apartmentNo, String postalCode, String city, Country country, User user) {
        Contact contactBaseDTO = Contact.builder()
                .firstName("test name")
                .lastName("lastName")
                .email("email@email.com")
                .phoneNumber("771238181")
                .additionalInfo("information").build();
        Contractor contractor = Contractor.builder()
                .name(name)
                .taxNumber(taxNumber)
                .street(street)
                .buildingNo(buildingNo)
                .apartmentNo(apartmentNo)
                .postalCode(postalCode)
                .city(city)
                .country(country)
                .customer(true)
                .supplier(true)
                .scaffoldingUser(true)
                .contacts(Set.of(contactBaseDTO))
                .createdDatetime(Instant.now())
                .createdBy(user)
                .lastModifiedDatetime(null)
                .company(company)
                .build();
        Contractor savedContractor = contractorRepository.save(contractor);
        return contractorMapper.toDto(savedContractor);
    }

    @Test
    void shouldAddContractorSuccessfully() {
        ContactBaseDTO contactBaseDTO = ContactBaseDTO.builder()
                .firstName("firstname")
                .lastName("lastName")
                .email("email@email.com")
                .phoneNumber("777818181")
                .additionalInfo("testtt").build();
        ContractorDTO newContractor = ContractorDTO.builder()
                .name("New Contractor")
                .taxNumber("5555555555")
                .street("New Street")
                .buildingNo("30")
                .apartmentNo("3C")
                .postalCode("00-003")
                .city("Gdansk")
                .country(Country.POLAND)
                .customer(true)
                .supplier(true)
                .scaffoldingUser(true)
                .contacts(Set.of(contactBaseDTO))
                .company(companyBaseMapper.toDto(company))
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
        Long id = firstContractor.getId();
        ContractorBaseDTO contractor = webTestClient.get()
                .uri("/api/contractors/getById/" + id)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .exchange()
                .expectStatus().isOk()
                .expectBody(ContractorBaseDTO.class)
                .returnResult().getResponseBody();

        assertThat(contractor).isNotNull();
        assertThat(contractor.getId()).isEqualTo(firstContractor.getId());
        assertThat(contractor.getName()).isEqualTo("First Contractor");
        assertThat(contractor.getCity()).isEqualTo("Warsaw");
    }

    @Test
    void shouldFilterContractorsByAllFieldsSuccessfully() {
        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("nameStartsWith", "First");
        queryParams.add("taxNumberStartsWith", "1234567890");
        queryParams.add("streetStartsWith", "Main Street");
        queryParams.add("cityStartsWith", "Warsaw");
        queryParams.add("buildingNoStartsWith", "10");
        queryParams.add("apartmentNoStartsWith", "1A");
        queryParams.add("postalCodeStartsWith", "00-001");
        queryParams.add("country", Country.POLAND.name());
        queryParams.add("page", "0");
        queryParams.add("size", "10");

        List<ContractorBaseDTO> contractors = webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path("/api/contractors/find")
                        .queryParams(queryParams)
                        .build())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(ContractorBaseDTO.class)
                .returnResult().getResponseBody();

        assertThat(contractors).hasSize(1);
        assertThat(contractors.getFirst().getName()).isEqualTo("First Contractor");
    }

    @Test
    void shouldFilterContractorsByAllFieldsSuccessfullyReturnZeroElements() {
        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("nameStartsWith", "Second");
        queryParams.add("taxNumberStartsWith", "1234567890");
        queryParams.add("streetStartsWith", "Second Street");
        queryParams.add("cityStartsWith", "Warsaw");
        queryParams.add("buildingNoStartsWith", "10");
        queryParams.add("apartmentNoStartsWith", "1A");
        queryParams.add("postalCodeStartsWith", "00-001");
        queryParams.add("country", Country.POLAND.name());
        queryParams.add("page", "0");
        queryParams.add("size", "10");

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
                .id(firstContractor.getId())
                .name("Updated Contractor")
                .taxNumber("1111111111")
                .street("Updated Street")
                .buildingNo("40")
                .apartmentNo("4D")
                .postalCode("00-004")
                .city("Poznan")
                .country(Country.POLAND)
                .customer(true)
                .supplier(true)
                .scaffoldingUser(true)
                .contacts(firstContractor.getContacts())
                .company(companyBaseMapper.toDto(company))
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
        ContractorBaseDTO updateContractor = ContractorBaseDTO.builder()
                .id(1L)
                .name("Unauthorized Update")
                .build();

        webTestClient.put()
                .uri("/api/contractors/update")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(updateContractor)
                .exchange()
                .expectStatus().isUnauthorized();
    }

    @Test
    void shouldDeleteContractorSuccessfully() {
        Long id = firstContractor.getId();
        webTestClient.delete()
                .uri("/api/contractors/delete/" + id)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .exchange()
                .expectStatus().isOk();

        List<Contractor> contractors = contractorRepository.findAll();

        assertThat(contractors).hasSize(1);
    }

    @Test
    void shouldFailToDeleteContractorWithoutAuthorization() {

        webTestClient.delete()
                .uri("/api/contractors/delete/" + 66L)
                .exchange()
                .expectStatus().isUnauthorized();
    }

    @Test
    void shouldGetAllContractorsWithPaginationSuccessfully() {
        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("page", "0");
        queryParams.add("size", "1");

        List<ContractorBaseDTO> contractors = webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path("/api/contractors/page")
                        .queryParams(queryParams)
                        .build())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(ContractorBaseDTO.class)
                .returnResult().getResponseBody();

        assertThat(contractors).hasSize(1);
    }

    @Test
    void shouldReturnEmptyListWhenPageIsOutOfBounds() {
        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("page", "10");
        queryParams.add("size", "10");

        List<ContractorBaseDTO> contractors = webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path("/api/contractors/page")
                        .queryParams(queryParams)
                        .build())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(ContractorBaseDTO.class)
                .returnResult().getResponseBody();

        assertThat(contractors).isEmpty();
    }

    @Test
    void shouldFailPaginationWithoutAuthorization() {
        webTestClient.get()
                .uri("/api/contractors/page?page=0&pageSize=10")
                .exchange()
                .expectStatus().isUnauthorized();
    }


    @Test
    void shouldFailToAddContractorWithInvalidData() {
        ContractorBaseDTO invalidContractor = ContractorBaseDTO.builder()
                .name("") // brak nazwy - pole wymagane
                .taxNumber("5555555555")
                .street("Invalid Street")
                .buildingNo("30")
                .apartmentNo("3C")
                .postalCode("00-003")
                .city("Gdansk")
                .country(Country.POLAND)
                .customer(true)
                .supplier(true)
                .scaffoldingUser(true)
                .build();

        webTestClient.post()
                .uri("/api/contractors/add")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(invalidContractor)
                .exchange()
                .expectStatus().is4xxClientError();
    }

    @Test
    void shouldFailToUpdateNonExistentContractor() {
        ContractorBaseDTO nonExistentContractor = ContractorBaseDTO.builder()
                .id(9999L) // zakładamy, że taki ID nie istnieje
                .name("Nonexistent Contractor")
                .taxNumber("0000000000")
                .street("Unknown Street")
                .buildingNo("0")
                .apartmentNo("0")
                .postalCode("00-000")
                .city("Nowhere")
                .country(Country.POLAND)
                .customer(true)
                .supplier(true)
                .scaffoldingUser(true)
                .build();

        webTestClient.put()
                .uri("/api/contractors/update")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(nonExistentContractor)
                .exchange()
                .expectStatus().is4xxClientError();
    }

    @Test
    void shouldFailToGetContractorByNonexistentId() {
        webTestClient.get()
                .uri("/api/contractors/getById/9999")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .exchange()
                .expectStatus().is4xxClientError();
    }

    @Test
    void shouldFailToGetContractorsWithInvalidPaginationParams() {
        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("page", "1");
        queryParams.add("size", "1");
        queryParams.add("nameStartsWith", "Third");

        List<ContractorBaseDTO> contractors = webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path("/api/contractors/page")
                        .queryParams(queryParams)
                        .build())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(ContractorBaseDTO.class)
                .returnResult().getResponseBody();

        assertThat(contractors).isEmpty();
    }

}
