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
import pl.com.chrzanowski.sma.common.security.config.ResourceInitializer;
import pl.com.chrzanowski.sma.company.dto.CompanyDTO;
import pl.com.chrzanowski.sma.company.mapper.CompanyDTOMapper;
import pl.com.chrzanowski.sma.company.model.Company;
import pl.com.chrzanowski.sma.company.repository.CompanyRepository;
import pl.com.chrzanowski.sma.contact.dto.ContactDTO;
import pl.com.chrzanowski.sma.contact.repository.ContactRepository;
import pl.com.chrzanowski.sma.integrationTests.helper.UserHelper;

import java.time.Duration;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
public class ContactControllerIntegrationTest extends AbstractTestContainers {

    public static final String CONTACT_API_PATH = "/api/contacts";
    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private Flyway flyway;

    @Autowired
    private UserHelper userHelper;

    private String jwtToken;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private CompanyDTOMapper companyDTOMapper;

    @Autowired
    private ResourceInitializer resourceInitializer;

    private CompanyDTO companyDTO;
    private Company company;

    @BeforeEach
    void setUp() {
        this.webTestClient = this.webTestClient.mutate()
                .responseTimeout(Duration.ofSeconds(60))
                .build();

        flyway.clean();
        flyway.migrate();

        resourceInitializer.initializeResources();

        LoginRequest firstUserLogin = userHelper.registerFirstUser(webTestClient);
        this.jwtToken = userHelper.authenticateUser(firstUserLogin, webTestClient);

        companyRepository.deleteAll();
        company = Company.builder().name("TestCompany").additionalInfo("TestInfo").build();
        company = companyRepository.saveAndFlush(company);
        companyDTO = companyDTOMapper.toDto(company);
    }

    /**
     * Helper – tworzy przykładowy obiekt ContactDTO.
     */
    private ContactDTO createSampleContact() {

        return ContactDTO.builder()
                .firstName("John")
                .lastName("Doe")
                .phoneNumber("123456789")
                .email("john.doe@example.com")
                .additionalInfo("Test contact")
                .company(companyDTO)
                .build();
    }

    @Test
    void shouldAddContactSuccessfully() {
        ContactDTO contactBaseDTO = createSampleContact();

        ContactDTO savedContact = webTestClient.post()
                .uri(CONTACT_API_PATH)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(contactBaseDTO)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(ContactDTO.class)
                .returnResult().getResponseBody();

        assertThat(savedContact).isNotNull();
        assertThat(savedContact.getId()).isNotNull();
        assertThat(savedContact.getFirstName()).isEqualTo(contactBaseDTO.getFirstName());
        assertThat(savedContact.getLastName()).isEqualTo(contactBaseDTO.getLastName());
        assertThat(savedContact.getEmail()).isEqualTo(contactBaseDTO.getEmail());
    }

    @Test
    void shouldUpdateContactSuccessfully() {
        // Dodajemy kontakt
        ContactDTO contactBaseDTO = createSampleContact();
        ContactDTO savedContact = webTestClient.post()
                .uri(CONTACT_API_PATH)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(contactBaseDTO)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(ContactDTO.class)
                .returnResult().getResponseBody();

        // Modyfikujemy np. numer telefonu
        ContactDTO updatedContactBaseDTO = ContactDTO.builder()
                .id(savedContact.getId())
                .firstName(savedContact.getFirstName())
                .lastName(savedContact.getLastName())
                .phoneNumber("987654321") // nowy numer telefonu
                .email(savedContact.getEmail())
                .additionalInfo(savedContact.getAdditionalInfo())
                .company(savedContact.getCompany())
                .build();

        ContactDTO updatedContact = webTestClient.put()
                .uri(CONTACT_API_PATH + "/" + savedContact.getId())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(updatedContactBaseDTO)
                .exchange()
                .expectStatus().isOk()
                .expectBody(ContactDTO.class)
                .returnResult().getResponseBody();

        assertThat(updatedContact).isNotNull();
        assertThat(updatedContact.getId()).isEqualTo(savedContact.getId());
        assertThat(updatedContact.getPhoneNumber()).isEqualTo("987654321");
    }

    @Test
    void shouldGetContactByIdSuccessfully() {
        // Dodajemy kontakt
        ContactDTO contactBaseDTO = createSampleContact();
        ContactDTO savedContact = webTestClient.post()
                .uri(CONTACT_API_PATH)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(contactBaseDTO)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(ContactDTO.class)
                .returnResult().getResponseBody();

        // Pobieramy kontakt po ID
        ContactDTO retrievedContact = webTestClient.get()
                .uri(CONTACT_API_PATH + "/" + savedContact.getId())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .exchange()
                .expectStatus().isOk()
                .expectBody(ContactDTO.class)
                .returnResult().getResponseBody();

        assertThat(retrievedContact).isNotNull();
        assertThat(retrievedContact.getId()).isEqualTo(savedContact.getId());
        assertThat(retrievedContact.getFirstName()).isEqualTo(savedContact.getFirstName());
    }

    @Test
    void shouldDeleteContactSuccessfully() {
        // Dodajemy kontakt
        ContactDTO contactBaseDTO = createSampleContact();
        ContactDTO savedContact = webTestClient.post()
                .uri(CONTACT_API_PATH)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(contactBaseDTO)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(ContactDTO.class)
                .returnResult().getResponseBody();

        // Usuwamy kontakt
        webTestClient.delete()
                .uri(CONTACT_API_PATH + "/" + savedContact.getId())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .exchange()
                .expectStatus().isNoContent();

        // Opcjonalnie: można sprawdzić, czy pobranie usuniętego kontaktu zwróci błąd (np. 404)
        webTestClient.get()
                .uri(CONTACT_API_PATH + savedContact.getId())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .exchange()
                .expectStatus().is4xxClientError();
    }

    @Test
    void shouldGetContactsByFilterSuccessfully() {
        // Dodajemy dwa kontakty o różnych imionach
        ContactDTO contactAlice = ContactDTO.builder()
                .firstName("Alice")
                .lastName("Smith")
                .phoneNumber("111111111")
                .email("alice@example.com")
                .additionalInfo("Kontakt Alice")
                .build();
        ContactDTO contactBob = ContactDTO.builder()
                .firstName("Bob")
                .lastName("Jones")
                .phoneNumber("222222222")
                .email("bob@example.com")
                .additionalInfo("Kontakt Bob")
                .build();

        webTestClient.post()
                .uri(CONTACT_API_PATH)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(contactAlice)
                .exchange()
                .expectStatus().isCreated();

        webTestClient.post()
                .uri(CONTACT_API_PATH)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(contactBob)
                .exchange()
                .expectStatus().isCreated();

        // Filtrowanie – zakładamy, że ContactFilter mapuje parametr "firstName"
        List<ContactDTO> filteredContacts = webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path(CONTACT_API_PATH)
                        .queryParam("firstNameStartsWith", "Alice")
                        .build())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(ContactDTO.class)
                .returnResult().getResponseBody();

        assertThat(filteredContacts).isNotEmpty();
        assertThat(filteredContacts)
                .allMatch(contact -> "Alice".equals(contact.getFirstName()));
    }

    @Test
    void shouldGetContactsByFilterAndPageSuccessfully() {
        // Dodajemy dwa kontakty o tym samym imieniu
        ContactDTO contact1 = ContactDTO.builder()
                .firstName("Charlie")
                .lastName("Brown")
                .phoneNumber("333333333")
                .email("charlie1@example.com")
                .additionalInfo("Kontakt Charlie1")
                .build();
        ContactDTO contact2 = ContactDTO.builder()
                .firstName("John")
                .lastName("Black")
                .phoneNumber("444444444")
                .email("charlie2@example.com")
                .additionalInfo("Kontakt Charlie2")
                .build();

        webTestClient.post()
                .uri(CONTACT_API_PATH)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(contact1)
                .exchange()
                .expectStatus().isCreated();

        webTestClient.post()
                .uri(CONTACT_API_PATH)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(contact2)
                .exchange()
                .expectStatus().isCreated();

        // Pobieramy kontakty z filtrem i paginacją – przekazujemy parametr "firstName" oraz parametry stronicowania ("page" i "size")
        List<ContactDTO> pagedContacts = webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path(CONTACT_API_PATH)
                        .queryParam("firstNameStartsWith", "Charlie")
                        .queryParam("page", "0")
                        .queryParam("size", "1")
                        .build())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(ContactDTO.class)
                .returnResult().getResponseBody();

        assertThat(pagedContacts).isNotEmpty();
        assertThat(pagedContacts.size()).isEqualTo(1);
    }

    @Test
    void shouldFailAddContactWithoutAuthentication() {
        ContactDTO contactBaseDTO = createSampleContact();

        webTestClient.post()
                .uri(CONTACT_API_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(contactBaseDTO)
                .exchange()
                .expectStatus().isUnauthorized();
    }

    @Test
    void shouldFailGetAllContactsWithoutAuthentication() {
        webTestClient.get()
                .uri(CONTACT_API_PATH)
                .exchange()
                .expectStatus().isUnauthorized();
    }

    // Negatywne scenariusze

    @Test
    void shouldFailUpdateNonexistentContact() {
        // Próba aktualizacji kontaktu, który nie istnieje
        ContactDTO nonExistentContact = ContactDTO.builder()
                .id(999L) // zakładamy, że taki ID nie istnieje
                .firstName("Non")
                .lastName("Existent")
                .phoneNumber("000000000")
                .email("non.existent@example.com")
                .additionalInfo("Brak kontaktu")
                .build();

        webTestClient.put()
                .uri(CONTACT_API_PATH + "/" + nonExistentContact.getId())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(nonExistentContact)
                .exchange()
                .expectStatus().is4xxClientError();
    }

    @Test
    void shouldFailGetContactByIdNonexistent() {
        // Próba pobrania kontaktu o nieistniejącym ID
        webTestClient.get()
                .uri(CONTACT_API_PATH + "/999")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .exchange()
                .expectStatus().is4xxClientError();
    }

    @Test
    void shouldFailAddContactWithInvalidData() {
        // Próba dodania kontaktu z nieprawidłowymi danymi (np. pusty firstName, niepoprawny format e-mail)
        ContactDTO invalidContact = ContactDTO.builder()
                .firstName("") // brak imienia
                .lastName("Doe")
                .phoneNumber("123456789")
                .email("invalid-email-format") // niepoprawny format e-mail
                .additionalInfo("Niepoprawne dane kontaktu")
                .build();

        webTestClient.post()
                .uri(CONTACT_API_PATH)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(invalidContact)
                .exchange()
                .expectStatus().is4xxClientError();
    }

    @Test
    void shouldFailUpdateContactWithInvalidData() {
        // Dodajemy poprawny kontakt, a następnie próbujemy zaktualizować z niepoprawnymi danymi
        ContactDTO validContact = createSampleContact();
        ContactDTO savedContact = webTestClient.post()
                .uri(CONTACT_API_PATH)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(validContact)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(ContactDTO.class)
                .returnResult().getResponseBody();

        // Aktualizacja z pustym lastName (przyjmujemy, że pole to jest obowiązkowe)
        ContactDTO invalidUpdate = ContactDTO.builder()
                .id(savedContact.getId())
                .firstName(savedContact.getFirstName())
                .lastName("") // nieprawidłowa wartość
                .phoneNumber(savedContact.getPhoneNumber())
                .email(savedContact.getEmail())
                .additionalInfo(savedContact.getAdditionalInfo())
                .build();

        webTestClient.put()
                .uri(CONTACT_API_PATH + "/" + invalidUpdate.getId())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(invalidUpdate)
                .exchange()
                .expectStatus().is4xxClientError();
    }

    @Test
    void getContactsByFilterShouldReturnEmptyList() {
        // Dodanie poprawnego kontaktu (jeśli wymagane)
        ContactDTO validContact = createSampleContact();
        webTestClient.post()
                .uri(CONTACT_API_PATH)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(validContact)
                .exchange()
                .expectStatus().isCreated();

        // Próba pobrania kontaktów z ujemnymi parametrami stronicowania
        List<ContactDTO> contacts = webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path(CONTACT_API_PATH)
                        .queryParam("firstNameStartsWith", "Johnny")
                        .queryParam("page", "1")
                        .queryParam("size", "1")
                        .build())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(ContactDTO.class)
                .returnResult()
                .getResponseBody();

        assertThat(contacts).isEmpty();
    }
}
