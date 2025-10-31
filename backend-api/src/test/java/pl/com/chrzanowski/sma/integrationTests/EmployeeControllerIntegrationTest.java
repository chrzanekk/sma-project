//package pl.com.chrzanowski.sma.integrationTests;
//
//import org.flywaydb.core.Flyway;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mockito;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.actuate.endpoint.ApiVersion;
//import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.context.TestConfiguration;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Import;
//import org.springframework.context.annotation.Primary;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.reactive.server.WebTestClient;
//import pl.com.chrzanowski.sma.AbstractTestContainers;
//import pl.com.chrzanowski.sma.auth.dto.request.LoginRequest;
//import pl.com.chrzanowski.sma.auth.dto.response.MessageResponse;
//import pl.com.chrzanowski.sma.common.security.enums.ApiPath;
//import pl.com.chrzanowski.sma.common.security.resource.model.Resource;
//import pl.com.chrzanowski.sma.common.security.resource.repository.ResourceRepository;
//import pl.com.chrzanowski.sma.company.dto.CompanyDTO;
//import pl.com.chrzanowski.sma.company.mapper.CompanyBaseMapper;
//import pl.com.chrzanowski.sma.company.mapper.CompanyDTOMapper;
//import pl.com.chrzanowski.sma.company.model.Company;
//import pl.com.chrzanowski.sma.company.repository.CompanyRepository;
//import pl.com.chrzanowski.sma.email.service.SendEmailService;
//import pl.com.chrzanowski.sma.employee.dto.EmployeeDTO;
//import pl.com.chrzanowski.sma.employee.mapper.EmployeeDTOMapper;
//import pl.com.chrzanowski.sma.employee.repository.EmployeeRepository;
//import pl.com.chrzanowski.sma.integrationTests.helper.UserHelper;
//import pl.com.chrzanowski.sma.position.dto.PositionDTO;
//import pl.com.chrzanowski.sma.position.mapper.PositionBaseMapper;
//import pl.com.chrzanowski.sma.position.mapper.PositionDTOMapper;
//import pl.com.chrzanowski.sma.position.model.Position;
//import pl.com.chrzanowski.sma.position.repository.PositionRepository;
//import pl.com.chrzanowski.sma.user.mapper.UserMapper;
//import pl.com.chrzanowski.sma.user.model.User;
//import pl.com.chrzanowski.sma.user.service.UserService;
//
//import java.math.BigDecimal;
//import java.time.Duration;
//import java.time.Instant;
//import java.util.List;
//
//import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
//
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@AutoConfigureWebTestClient
//public class EmployeeControllerIntegrationTest extends AbstractTestContainers {
//
//    public static final String EMPLOYEE_API_PATH = ApiPath.EMPLOYEE;
//
//    @Autowired
//    private WebTestClient webTestClient;
//
//    @Autowired
//    private Flyway flyway;
//
//    @Autowired
//    private EmployeeRepository employeeRepository;
//
//    @Autowired
//    private UserHelper userHelper;
//
//    @Autowired
//    private UserMapper userMapper;
//
//    @Autowired
//    private EmployeeDTOMapper employeeDTOMapper;
//
//    @Autowired
//    private UserService userService;
//
//    @Autowired
//    private CompanyRepository companyRepository;
//
//    @Autowired
//    private CompanyBaseMapper companyBaseMapper;
//
//    @Autowired
//    private PositionRepository positionRepository;
//
//    @Autowired
//    private PositionBaseMapper positionBaseMapper;
//
//    @Autowired
//    private CompanyDTOMapper companyDTOMapper;
//
//    @Autowired
//    private PositionDTOMapper positionDTOMapper;
//
//    @Autowired
//    private pl.com.chrzanowski.sma.common.security.config.ResourceInitializer resourceInitializer;
//
//    private String jwtToken;
//
//    private EmployeeDTO firstEmployee;
//    private EmployeeDTO secondEmployee;
//
//    private Company company;
//    private CompanyDTO companyDTO;
//
//    private Position firstPosition;
//    private Position secondPosition;
//    private PositionDTO firstPositionDTO;
//    private PositionDTO secondPositionDTO;
//
//    @Autowired
//    private ResourceRepository resourceRepository;
//
//    @BeforeEach
//    void setUp() {
//        this.webTestClient = this.webTestClient
//                .mutate()
//                .responseTimeout(Duration.ofSeconds(60))
//                .build();
//
//        flyway.clean();
//        flyway.migrate();
//
//        resourceInitializer.initializeResources();
//
//        LoginRequest firstUser = userHelper.registerFirstUser(webTestClient);
//        LoginRequest secondUser = userHelper.registerSecondUser(webTestClient);
//        this.jwtToken = userHelper.authenticateUser(firstUser, webTestClient);
//
//        User firstRegisteredUser = userMapper.toEntity(userService.getUserByLogin(firstUser.getLogin()));
//        User secondRegisteredUser = userMapper.toEntity(userService.getUserByLogin(secondUser.getLogin()));
//
//        companyRepository.deleteAll();
//        company = Company.builder().name("TestCompany").additionalInfo("TestInfo").build();
//        company = companyRepository.saveAndFlush(company);
//        companyDTO = companyDTOMapper.toDto(company);
//
//        positionRepository.deleteAll();
//        firstPosition = positionRepository.saveAndFlush(Position.builder()
//                .name("FirstPosition")
//                .description("Description of first position")
//                .company(company)
//                .createdBy(firstRegisteredUser)
//                .createdDatetime(Instant.now())
//                .modifiedBy(firstRegisteredUser)
//                .lastModifiedDatetime(Instant.now())
//                .build());
//        secondPosition = positionRepository.saveAndFlush(Position.builder()
//                .name("SecondPosition")
//                .description("Description of second position")
//                .company(company)
//                .createdBy(firstRegisteredUser)
//                .createdDatetime(Instant.now())
//                .modifiedBy(firstRegisteredUser)
//                .lastModifiedDatetime(Instant.now())
//                .build());
//        firstPositionDTO = positionDTOMapper.toDto(firstPosition);
//        secondPositionDTO = positionDTOMapper.toDto(secondPosition);
//    }
//
//    private EmployeeDTO createSampleEmployee() {
//        return EmployeeDTO.builder()
//                .firstName("FirstName")
//                .lastName("LastName")
//                .hourRate(new BigDecimal("20"))
//                .company(companyDTO)
//                .position(firstPositionDTO)
//                .build();
//    }
//
//    private EmployeeDTO createSecondSampleEmployee() {
//        return EmployeeDTO.builder()
//                .firstName("SecondName")
//                .lastName("SecondLastName")
//                .hourRate(new BigDecimal("25"))
//                .company(companyDTO)
//                .position(secondPositionDTO)
//                .build();
//    }
//
//    @Test
//    void shouldAddEmployeeSuccessfully() {
//        EmployeeDTO employeeDTO = createSampleEmployee();
//        List<Resource> resource = resourceRepository.findAll();
//        EmployeeDTO savedEmployee = webTestClient.post()
//                .uri(EMPLOYEE_API_PATH)
//                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
//                .contentType(MediaType.APPLICATION_JSON)
//                .bodyValue(employeeDTO)
//                .exchange()
//                .expectStatus().isCreated()
//                .expectBody(EmployeeDTO.class)
//                .returnResult().getResponseBody();
//
//        assertThat(savedEmployee).isNotNull();
//        assertThat(savedEmployee.getId()).isNotNull();
//        assertThat(savedEmployee.getFirstName()).isEqualTo(employeeDTO.getFirstName());
//        assertThat(savedEmployee.getLastName()).isEqualTo(employeeDTO.getLastName());
//        assertThat(savedEmployee.getHourRate()).isEqualTo(employeeDTO.getHourRate());
//
//    }
//}
package pl.com.chrzanowski.sma.integrationTests;

import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.Assertions;
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
import pl.com.chrzanowski.sma.common.security.config.ResourceInitializer;
import pl.com.chrzanowski.sma.common.security.enums.ApiPath;
import pl.com.chrzanowski.sma.company.dto.CompanyDTO;
import pl.com.chrzanowski.sma.company.mapper.CompanyDTOMapper;
import pl.com.chrzanowski.sma.company.model.Company;
import pl.com.chrzanowski.sma.company.repository.CompanyRepository;
import pl.com.chrzanowski.sma.employee.dto.EmployeeDTO;
import pl.com.chrzanowski.sma.employee.repository.EmployeeRepository;
import pl.com.chrzanowski.sma.integrationTests.helper.UserHelper;
import pl.com.chrzanowski.sma.position.dto.PositionDTO;
import pl.com.chrzanowski.sma.position.mapper.PositionDTOMapper;
import pl.com.chrzanowski.sma.position.model.Position;
import pl.com.chrzanowski.sma.position.repository.PositionRepository;
import pl.com.chrzanowski.sma.user.mapper.UserMapper;
import pl.com.chrzanowski.sma.user.model.User;
import pl.com.chrzanowski.sma.user.service.UserService;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
public class EmployeeControllerIntegrationTest extends AbstractTestContainers {

    public static final String EMPLOYEE_API_PATH = ApiPath.EMPLOYEE;

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private Flyway flyway;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private UserHelper userHelper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private PositionRepository positionRepository;

    @Autowired
    private CompanyDTOMapper companyDTOMapper;

    @Autowired
    private PositionDTOMapper positionDTOMapper;

    @Autowired
    private ResourceInitializer resourceInitializer;

    private String jwtToken;

    private Company company;
    private CompanyDTO companyDTO;

    private Position firstPosition;
    private Position secondPosition;
    private PositionDTO firstPositionDTO;
    private PositionDTO secondPositionDTO;

    @BeforeEach
    void setUp() {
        this.webTestClient = this.webTestClient
                .mutate()
                .responseTimeout(Duration.ofSeconds(60))
                .build();

        flyway.clean();
        flyway.migrate();

        // Synchronizacja resources po migracji
        resourceInitializer.initializeResources();

        LoginRequest firstUser = userHelper.registerFirstUser(webTestClient);
        this.jwtToken = userHelper.authenticateUser(firstUser, webTestClient);

        User firstRegisteredUser = userMapper.toEntity(userService.getUserByLogin(firstUser.getLogin()));

        employeeRepository.deleteAll();
        companyRepository.deleteAll();
        company = Company.builder().name("TestCompany").additionalInfo("TestInfo").build();
        company = companyRepository.saveAndFlush(company);
        companyDTO = companyDTOMapper.toDto(company);

        positionRepository.deleteAll();
        firstPosition = positionRepository.saveAndFlush(Position.builder()
                .name("FirstPosition")
                .description("Description of first position")
                .company(company)
                .createdBy(firstRegisteredUser)
                .createdDatetime(Instant.now())
                .modifiedBy(firstRegisteredUser)
                .lastModifiedDatetime(Instant.now())
                .build());
        secondPosition = positionRepository.saveAndFlush(Position.builder()
                .name("SecondPosition")
                .description("Description of second position")
                .company(company)
                .createdBy(firstRegisteredUser)
                .createdDatetime(Instant.now())
                .modifiedBy(firstRegisteredUser)
                .lastModifiedDatetime(Instant.now())
                .build());
        firstPositionDTO = positionDTOMapper.toDto(firstPosition);
        secondPositionDTO = positionDTOMapper.toDto(secondPosition);
    }

    /**
     * Helper – tworzy przykładowy obiekt EmployeeDTO.
     */
    private EmployeeDTO createSampleEmployee() {
        return EmployeeDTO.builder()
                .firstName("John")
                .lastName("Doe")
                .hourRate(new BigDecimal("20.00"))
                .company(companyDTO)
                .position(firstPositionDTO)
                .build();
    }

    /**
     * Helper – tworzy drugi przykładowy obiekt EmployeeDTO.
     */
    private EmployeeDTO createSecondSampleEmployee() {
        return EmployeeDTO.builder()
                .firstName("Jane")
                .lastName("Smith")
                .hourRate(new BigDecimal("25.00"))
                .company(companyDTO)
                .position(secondPositionDTO)
                .build();
    }

    // ========== POZYTYWNE SCENARIUSZE ==========

    @Test
    void shouldAddEmployeeSuccessfully() {
        EmployeeDTO employeeDTO = createSampleEmployee();

        EmployeeDTO savedEmployee = webTestClient.post()
                .uri(EMPLOYEE_API_PATH)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(employeeDTO)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(EmployeeDTO.class)
                .returnResult().getResponseBody();

        assertThat(savedEmployee).isNotNull();
        assertThat(savedEmployee.getId()).isNotNull();
        assertThat(savedEmployee.getFirstName()).isEqualTo(employeeDTO.getFirstName());
        assertThat(savedEmployee.getLastName()).isEqualTo(employeeDTO.getLastName());
        assertThat(savedEmployee.getHourRate()).isEqualTo(employeeDTO.getHourRate());
        assertThat(savedEmployee.getCompany()).isNotNull();
        assertThat(savedEmployee.getCompany().getId()).isEqualTo(companyDTO.getId());
        assertThat(savedEmployee.getPosition()).isNotNull();
        assertThat(savedEmployee.getPosition().getId()).isEqualTo(firstPositionDTO.getId());
    }

    @Test
    void shouldUpdateEmployeeSuccessfully() {
        // Dodajemy pracownika
        EmployeeDTO employeeDTO = createSampleEmployee();
        EmployeeDTO savedEmployee = webTestClient.post()
                .uri(EMPLOYEE_API_PATH)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(employeeDTO)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(EmployeeDTO.class)
                .returnResult().getResponseBody();

        // Modyfikujemy dane pracownika
        Assertions.assertNotNull(savedEmployee);
        EmployeeDTO updatedEmployeeDTO = EmployeeDTO.builder()
                .id(savedEmployee.getId())
                .firstName("UpdatedFirstName")
                .lastName("UpdatedLastName")
                .hourRate(new BigDecimal("30.00"))
                .company(savedEmployee.getCompany())
                .position(secondPositionDTO) // zmiana pozycji
                .build();

        EmployeeDTO updatedEmployee = webTestClient.put()
                .uri(EMPLOYEE_API_PATH + "/" + savedEmployee.getId())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(updatedEmployeeDTO)
                .exchange()
                .expectStatus().isOk()
                .expectBody(EmployeeDTO.class)
                .returnResult().getResponseBody();

        assertThat(updatedEmployee).isNotNull();
        assertThat(updatedEmployee.getId()).isEqualTo(savedEmployee.getId());
        assertThat(updatedEmployee.getFirstName()).isEqualTo("UpdatedFirstName");
        assertThat(updatedEmployee.getLastName()).isEqualTo("UpdatedLastName");
        assertThat(updatedEmployee.getHourRate()).isEqualTo(new BigDecimal("30.00"));
        assertThat(updatedEmployee.getPosition().getId()).isEqualTo(secondPositionDTO.getId());
    }

    @Test
    void shouldGetEmployeeByIdSuccessfully() {
        // Dodajemy pracownika
        EmployeeDTO employeeDTO = createSampleEmployee();
        EmployeeDTO savedEmployee = webTestClient.post()
                .uri(EMPLOYEE_API_PATH)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(employeeDTO)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(EmployeeDTO.class)
                .returnResult().getResponseBody();

        // Pobieramy pracownika po ID
        Assertions.assertNotNull(savedEmployee);
        EmployeeDTO retrievedEmployee = webTestClient.get()
                .uri(EMPLOYEE_API_PATH + "/" + savedEmployee.getId())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .exchange()
                .expectStatus().isOk()
                .expectBody(EmployeeDTO.class)
                .returnResult().getResponseBody();

        assertThat(retrievedEmployee).isNotNull();
        assertThat(retrievedEmployee.getId()).isEqualTo(savedEmployee.getId());
        assertThat(retrievedEmployee.getFirstName()).isEqualTo(savedEmployee.getFirstName());
        assertThat(retrievedEmployee.getLastName()).isEqualTo(savedEmployee.getLastName());
        assertThat(retrievedEmployee.getCompany()).isNotNull();
        assertThat(retrievedEmployee.getPosition()).isNotNull();
    }

    @Test
    void shouldDeleteEmployeeSuccessfully() {
        // Dodajemy pracownika
        EmployeeDTO employeeDTO = createSampleEmployee();
        EmployeeDTO savedEmployee = webTestClient.post()
                .uri(EMPLOYEE_API_PATH)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(employeeDTO)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(EmployeeDTO.class)
                .returnResult().getResponseBody();

        // Usuwamy pracownika
        Assertions.assertNotNull(savedEmployee);
        webTestClient.delete()
                .uri(EMPLOYEE_API_PATH + "/" + savedEmployee.getId())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .exchange()
                .expectStatus().isNoContent();

        // Sprawdzamy, czy pobranie usuniętego pracownika zwróci błąd (404)
        webTestClient.get()
                .uri(EMPLOYEE_API_PATH + "/" + savedEmployee.getId())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .exchange()
                .expectStatus().is4xxClientError();
    }

    @Test
    void shouldGetEmployeesByFilterSuccessfully() {
        // Dodajemy dwóch pracowników o różnych nazwiskach
        EmployeeDTO employee1 = EmployeeDTO.builder()
                .firstName("John")
                .lastName("Developer")
                .hourRate(new BigDecimal("20.00"))
                .company(companyDTO)
                .position(firstPositionDTO)
                .build();

        EmployeeDTO employee2 = EmployeeDTO.builder()
                .firstName("Jane")
                .lastName("Manager")
                .hourRate(new BigDecimal("25.00"))
                .company(companyDTO)
                .position(secondPositionDTO)
                .build();

        webTestClient.post()
                .uri(EMPLOYEE_API_PATH)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(employee1)
                .exchange()
                .expectStatus().isCreated();

        webTestClient.post()
                .uri(EMPLOYEE_API_PATH)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(employee2)
                .exchange()
                .expectStatus().isCreated();

        // Filtrujemy po nazwisku zawierającym "Developer"
        List<EmployeeDTO> filteredEmployees = webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path(EMPLOYEE_API_PATH)
                        .queryParam("lastNameContains", "Developer")
                        .build())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(EmployeeDTO.class)
                .returnResult().getResponseBody();

        assertThat(filteredEmployees).isNotEmpty();
        assertThat(filteredEmployees)
                .allMatch(employee -> employee.getLastName().contains("Developer"));
    }

    @Test
    void shouldGetEmployeesByFilterAndPageSuccessfully() {
        // Dodajemy trzech pracowników
        EmployeeDTO employee1 = EmployeeDTO.builder()
                .firstName("John")
                .lastName("Smith")
                .hourRate(new BigDecimal("20.00"))
                .company(companyDTO)
                .position(firstPositionDTO)
                .build();

        EmployeeDTO employee2 = EmployeeDTO.builder()
                .firstName("Jane")
                .lastName("Smith")
                .hourRate(new BigDecimal("25.00"))
                .company(companyDTO)
                .position(secondPositionDTO)
                .build();

        EmployeeDTO employee3 = EmployeeDTO.builder()
                .firstName("Bob")
                .lastName("Smith")
                .hourRate(new BigDecimal("22.00"))
                .company(companyDTO)
                .position(firstPositionDTO)
                .build();

        webTestClient.post()
                .uri(EMPLOYEE_API_PATH)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(employee1)
                .exchange()
                .expectStatus().isCreated();

        webTestClient.post()
                .uri(EMPLOYEE_API_PATH)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(employee2)
                .exchange()
                .expectStatus().isCreated();

        webTestClient.post()
                .uri(EMPLOYEE_API_PATH)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(employee3)
                .exchange()
                .expectStatus().isCreated();

        // Pobieramy pracowników z filtrem i paginacją
        List<EmployeeDTO> pagedEmployees = webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path(EMPLOYEE_API_PATH)
                        .queryParam("lastNameContains", "Smith")
                        .queryParam("page", "0")
                        .queryParam("size", "2")
                        .build())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(EmployeeDTO.class)
                .returnResult().getResponseBody();

        assertThat(pagedEmployees).isNotEmpty();
        assertThat(pagedEmployees.size()).isLessThanOrEqualTo(2);
    }

    @Test
    void shouldGetAllEmployeesSuccessfully() {
        // Dodajemy kilku pracowników
        EmployeeDTO employee1 = createSampleEmployee();
        EmployeeDTO employee2 = createSecondSampleEmployee();

        webTestClient.post()
                .uri(EMPLOYEE_API_PATH)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(employee1)
                .exchange()
                .expectStatus().isCreated();

        webTestClient.post()
                .uri(EMPLOYEE_API_PATH)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(employee2)
                .exchange()
                .expectStatus().isCreated();

        // Pobieramy wszystkich pracowników
        List<EmployeeDTO> allEmployees = webTestClient.get()
                .uri(EMPLOYEE_API_PATH)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(EmployeeDTO.class)
                .returnResult().getResponseBody();

        assertThat(allEmployees).isNotEmpty();
        assertThat(allEmployees.size()).isGreaterThanOrEqualTo(2);
    }

    // ========== NEGATYWNE SCENARIUSZE ==========

    @Test
    void shouldFailAddEmployeeWithoutAuthentication() {
        EmployeeDTO employeeDTO = createSampleEmployee();

        webTestClient.post()
                .uri(EMPLOYEE_API_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(employeeDTO)
                .exchange()
                .expectStatus().isUnauthorized();
    }

    @Test
    void shouldFailGetAllEmployeesWithoutAuthentication() {
        webTestClient.get()
                .uri(EMPLOYEE_API_PATH)
                .exchange()
                .expectStatus().isUnauthorized();
    }

    @Test
    void shouldFailUpdateNonexistentEmployee() {
        // Próba aktualizacji pracownika, który nie istnieje
        EmployeeDTO nonExistentEmployee = EmployeeDTO.builder()
                .id(999L) // zakładamy, że taki ID nie istnieje
                .firstName("NonExistent")
                .lastName("Employee")
                .hourRate(new BigDecimal("20.00"))
                .company(companyDTO)
                .position(firstPositionDTO)
                .build();

        webTestClient.put()
                .uri(EMPLOYEE_API_PATH + "/" + nonExistentEmployee.getId())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(nonExistentEmployee)
                .exchange()
                .expectStatus().is4xxClientError();
    }

    @Test
    void shouldFailGetEmployeeByIdNonexistent() {
        // Próba pobrania pracownika o nieistniejącym ID
        webTestClient.get()
                .uri(EMPLOYEE_API_PATH + "/999")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .exchange()
                .expectStatus().is4xxClientError();
    }

    @Test
    void shouldFailAddEmployeeWithInvalidData() {
        // Próba dodania pracownika z nieprawidłowymi danymi (np. puste imię)
        EmployeeDTO invalidEmployee = EmployeeDTO.builder()
                .firstName("") // puste imię
                .lastName("Doe")
                .hourRate(new BigDecimal("20.00"))
                .company(companyDTO)
                .position(firstPositionDTO)
                .build();

        webTestClient.post()
                .uri(EMPLOYEE_API_PATH)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(invalidEmployee)
                .exchange()
                .expectStatus().is4xxClientError();
    }

    @Test
    void shouldFailAddEmployeeWithNullFirstName() {
        // Próba dodania pracownika z null jako imieniem
        EmployeeDTO invalidEmployee = EmployeeDTO.builder()
                .firstName(null) // null imię
                .lastName("Doe")
                .hourRate(new BigDecimal("20.00"))
                .company(companyDTO)
                .position(firstPositionDTO)
                .build();

        webTestClient.post()
                .uri(EMPLOYEE_API_PATH)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(invalidEmployee)
                .exchange()
                .expectStatus().is4xxClientError();
    }

    @Test
    void shouldFailAddEmployeeWithNullLastName() {
        // Próba dodania pracownika z null jako nazwiskiem
        EmployeeDTO invalidEmployee = EmployeeDTO.builder()
                .firstName("John")
                .lastName(null) // null nazwisko
                .hourRate(new BigDecimal("20.00"))
                .company(companyDTO)
                .position(firstPositionDTO)
                .build();

        webTestClient.post()
                .uri(EMPLOYEE_API_PATH)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(invalidEmployee)
                .exchange()
                .expectStatus().is4xxClientError();
    }

    @Test
    void shouldFailAddEmployeeWithNullCompany() {
        // Próba dodania pracownika z null jako company
        EmployeeDTO invalidEmployee = EmployeeDTO.builder()
                .firstName("John")
                .lastName("Doe")
                .hourRate(new BigDecimal("20.00"))
                .company(null) // null company
                .position(firstPositionDTO)
                .build();

        webTestClient.post()
                .uri(EMPLOYEE_API_PATH)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(invalidEmployee)
                .exchange()
                .expectStatus().is4xxClientError();
    }

    @Test
    void shouldFailAddEmployeeWithNullPosition() {
        // Próba dodania pracownika z null jako position
        EmployeeDTO invalidEmployee = EmployeeDTO.builder()
                .firstName("John")
                .lastName("Doe")
                .hourRate(new BigDecimal("20.00"))
                .company(companyDTO)
                .position(null) // null position
                .build();

        webTestClient.post()
                .uri(EMPLOYEE_API_PATH)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(invalidEmployee)
                .exchange()
                .expectStatus().is4xxClientError();
    }

    @Test
    void shouldFailAddEmployeeWithNegativeHourRate() {
        // Próba dodania pracownika z ujemną stawką godzinową
        EmployeeDTO invalidEmployee = EmployeeDTO.builder()
                .firstName("John")
                .lastName("Doe")
                .hourRate(new BigDecimal("-10.00")) // ujemna stawka
                .company(companyDTO)
                .position(firstPositionDTO)
                .build();

        webTestClient.post()
                .uri(EMPLOYEE_API_PATH)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(invalidEmployee)
                .exchange()
                .expectStatus().is4xxClientError();
    }

    @Test
    void shouldFailUpdateEmployeeWithInvalidData() {
        // Dodajemy poprawnego pracownika, a następnie próbujemy zaktualizować z niepoprawnymi danymi
        EmployeeDTO validEmployee = createSampleEmployee();
        EmployeeDTO savedEmployee = webTestClient.post()
                .uri(EMPLOYEE_API_PATH)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(validEmployee)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(EmployeeDTO.class)
                .returnResult().getResponseBody();

        // Aktualizacja z pustym imieniem
        Assertions.assertNotNull(savedEmployee);
        EmployeeDTO invalidUpdate = EmployeeDTO.builder()
                .id(savedEmployee.getId())
                .firstName("") // nieprawidłowa wartość
                .lastName(savedEmployee.getLastName())
                .hourRate(savedEmployee.getHourRate())
                .company(savedEmployee.getCompany())
                .position(savedEmployee.getPosition())
                .build();

        webTestClient.put()
                .uri(EMPLOYEE_API_PATH + "/" + invalidUpdate.getId())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(invalidUpdate)
                .exchange()
                .expectStatus().is4xxClientError();
    }

    @Test
    void getEmployeesByFilterShouldReturnEmptyList() {
        // Dodanie poprawnego pracownika
        EmployeeDTO validEmployee = createSampleEmployee();
        webTestClient.post()
                .uri(EMPLOYEE_API_PATH)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(validEmployee)
                .exchange()
                .expectStatus().isCreated();

        // Próba pobrania pracowników z filtrem, który nie pasuje do żadnego pracownika
        List<EmployeeDTO> employees = webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path(EMPLOYEE_API_PATH)
                        .queryParam("lastNameContains", "NonExistentLastName")
                        .queryParam("page", "0")
                        .queryParam("size", "10")
                        .build())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(EmployeeDTO.class)
                .returnResult()
                .getResponseBody();

        assertThat(employees).isEmpty();
    }

    @Test
    void shouldFailDeleteNonexistentEmployee() {
        // Próba usunięcia pracownika, który nie istnieje
        webTestClient.delete()
                .uri(EMPLOYEE_API_PATH + "/999")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .exchange()
                .expectStatus().is4xxClientError();
    }
}
