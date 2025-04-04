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
import pl.com.chrzanowski.sma.company.dto.CompanyBaseDTO;
import pl.com.chrzanowski.sma.company.mapper.CompanyMapper;
import pl.com.chrzanowski.sma.company.model.Company;
import pl.com.chrzanowski.sma.company.repository.CompanyRepository;
import pl.com.chrzanowski.sma.contact.dto.ContactBaseDTO;
import pl.com.chrzanowski.sma.contact.model.Contact;
import pl.com.chrzanowski.sma.contact.repository.ContactRepository;
import pl.com.chrzanowski.sma.integrationTests.helper.UserHelper;

import java.time.Duration;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
public class ContactControllerIntegrationTest extends AbstractTestContainers {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private Flyway flyway;

    @Autowired
    private UserHelper userHelper;

    private String jwtToken;

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private CompanyMapper companyMapper;

    private CompanyBaseDTO companyBaseDTO;
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

        companyRepository.deleteAll();
        company = Company.builder().name("TestCompany").additionalInfo("TestInfo").build();
        company = companyRepository.saveAndFlush(company);
        companyBaseDTO = companyMapper.toDto(company);
    }

    /**
     * Helper – tworzy przykładowy obiekt ContactDTO.
     */
    private ContactBaseDTO createSampleContact() {

        return ContactBaseDTO.builder()
                .firstName("John")
                .lastName("Doe")
                .phoneNumber("123456789")
                .email("john.doe@example.com")
                .additionalInfo("Test contact")
                .company(companyBaseDTO)
                .build();
    }

    @Test
    void shouldAddContactSuccessfully() {
        ContactBaseDTO contactBaseDTO = createSampleContact();

        ContactBaseDTO savedContact = webTestClient.post()
                .uri("/api/contacts/add")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(contactBaseDTO)
                .exchange()
                .expectStatus().isOk()
                .expectBody(ContactBaseDTO.class)
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
        ContactBaseDTO contactBaseDTO = createSampleContact();
        ContactBaseDTO savedContact = webTestClient.post()
                .uri("/api/contacts/add")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(contactBaseDTO)
                .exchange()
                .expectStatus().isOk()
                .expectBody(ContactBaseDTO.class)
                .returnResult().getResponseBody();

        List<Contact> contacts = contactRepository.findAll();

        // Modyfikujemy np. numer telefonu
        ContactBaseDTO updatedContactBaseDTO = ContactBaseDTO.builder()
                .id(savedContact.getId())
                .firstName(savedContact.getFirstName())
                .lastName(savedContact.getLastName())
                .phoneNumber("987654321") // nowy numer telefonu
                .email(savedContact.getEmail())
                .additionalInfo(savedContact.getAdditionalInfo())
                .company(savedContact.getCompany())
                .companyId(savedContact.getCompany().getId())
                .build();

        ContactBaseDTO updatedContact = webTestClient.put()
                .uri("/api/contacts/update")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(updatedContactBaseDTO)
                .exchange()
                .expectStatus().isOk()
                .expectBody(ContactBaseDTO.class)
                .returnResult().getResponseBody();

        assertThat(updatedContact).isNotNull();
        assertThat(updatedContact.getId()).isEqualTo(savedContact.getId());
        assertThat(updatedContact.getPhoneNumber()).isEqualTo("987654321");
    }

    @Test
    void shouldGetContactByIdSuccessfully() {
        // Dodajemy kontakt
        ContactBaseDTO contactBaseDTO = createSampleContact();
        ContactBaseDTO savedContact = webTestClient.post()
                .uri("/api/contacts/add")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(contactBaseDTO)
                .exchange()
                .expectStatus().isOk()
                .expectBody(ContactBaseDTO.class)
                .returnResult().getResponseBody();

        // Pobieramy kontakt po ID
        ContactBaseDTO retrievedContact = webTestClient.get()
                .uri("/api/contacts/getById/" + savedContact.getId())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .exchange()
                .expectStatus().isOk()
                .expectBody(ContactBaseDTO.class)
                .returnResult().getResponseBody();

        assertThat(retrievedContact).isNotNull();
        assertThat(retrievedContact.getId()).isEqualTo(savedContact.getId());
        assertThat(retrievedContact.getFirstName()).isEqualTo(savedContact.getFirstName());
    }

    @Test
    void shouldGetAllContactsSuccessfully() {
        // Dodajemy kontakt
        ContactBaseDTO contactBaseDTO = createSampleContact();
        ContactBaseDTO savedContact = webTestClient.post()
                .uri("/api/contacts/add")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(contactBaseDTO)
                .exchange()
                .expectStatus().isOk()
                .expectBody(ContactBaseDTO.class)
                .returnResult().getResponseBody();

        // Pobieramy wszystkie kontakty
        List<ContactBaseDTO> contacts = webTestClient.get()
                .uri("/api/contacts/all")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(ContactBaseDTO.class)
                .returnResult().getResponseBody();

        assertThat(contacts).isNotEmpty();
        assertThat(contacts)
                .anyMatch(contact -> contact.getId().equals(savedContact.getId()));
    }

    @Test
    void shouldDeleteContactSuccessfully() {
        // Dodajemy kontakt
        ContactBaseDTO contactBaseDTO = createSampleContact();
        ContactBaseDTO savedContact = webTestClient.post()
                .uri("/api/contacts/add")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(contactBaseDTO)
                .exchange()
                .expectStatus().isOk()
                .expectBody(ContactBaseDTO.class)
                .returnResult().getResponseBody();

        // Usuwamy kontakt
        webTestClient.delete()
                .uri("/api/contacts/delete/" + savedContact.getId())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .exchange()
                .expectStatus().isOk();

        // Opcjonalnie: można sprawdzić, czy pobranie usuniętego kontaktu zwróci błąd (np. 404)
        webTestClient.get()
                .uri("/api/contacts/getById/" + savedContact.getId())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .exchange()
                .expectStatus().is4xxClientError();
    }

    @Test
    void shouldGetContactsByFilterSuccessfully() {
        // Dodajemy dwa kontakty o różnych imionach
        ContactBaseDTO contactAlice = ContactBaseDTO.builder()
                .firstName("Alice")
                .lastName("Smith")
                .phoneNumber("111111111")
                .email("alice@example.com")
                .additionalInfo("Kontakt Alice")
                .build();
        ContactBaseDTO contactBob = ContactBaseDTO.builder()
                .firstName("Bob")
                .lastName("Jones")
                .phoneNumber("222222222")
                .email("bob@example.com")
                .additionalInfo("Kontakt Bob")
                .build();

        webTestClient.post()
                .uri("/api/contacts/add")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(contactAlice)
                .exchange()
                .expectStatus().isOk();

        webTestClient.post()
                .uri("/api/contacts/add")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(contactBob)
                .exchange()
                .expectStatus().isOk();

        // Filtrowanie – zakładamy, że ContactFilter mapuje parametr "firstName"
        List<ContactBaseDTO> filteredContacts = webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path("/api/contacts/find")
                        .queryParam("firstNameStartsWith", "Alice")
                        .build())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(ContactBaseDTO.class)
                .returnResult().getResponseBody();

        assertThat(filteredContacts).isNotEmpty();
        assertThat(filteredContacts)
                .allMatch(contact -> "Alice".equals(contact.getFirstName()));
    }

    @Test
    void shouldGetContactsByFilterAndPageSuccessfully() {
        // Dodajemy dwa kontakty o tym samym imieniu
        ContactBaseDTO contact1 = ContactBaseDTO.builder()
                .firstName("Charlie")
                .lastName("Brown")
                .phoneNumber("333333333")
                .email("charlie1@example.com")
                .additionalInfo("Kontakt Charlie1")
                .build();
        ContactBaseDTO contact2 = ContactBaseDTO.builder()
                .firstName("John")
                .lastName("Black")
                .phoneNumber("444444444")
                .email("charlie2@example.com")
                .additionalInfo("Kontakt Charlie2")
                .build();

        webTestClient.post()
                .uri("/api/contacts/add")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(contact1)
                .exchange()
                .expectStatus().isOk();

        webTestClient.post()
                .uri("/api/contacts/add")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(contact2)
                .exchange()
                .expectStatus().isOk();

        // Pobieramy kontakty z filtrem i paginacją – przekazujemy parametr "firstName" oraz parametry stronicowania ("page" i "size")
        List<ContactBaseDTO> pagedContacts = webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path("/api/contacts/page")
                        .queryParam("firstNameStartsWith", "Charlie")
                        .queryParam("page", "0")
                        .queryParam("size", "1")
                        .build())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(ContactBaseDTO.class)
                .returnResult().getResponseBody();

        assertThat(pagedContacts).isNotEmpty();
        assertThat(pagedContacts.size()).isEqualTo(1);
    }

    @Test
    void shouldFailAddContactWithoutAuthentication() {
        ContactBaseDTO contactBaseDTO = createSampleContact();

        webTestClient.post()
                .uri("/api/contacts/add")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(contactBaseDTO)
                .exchange()
                .expectStatus().isUnauthorized();
    }

    @Test
    void shouldFailGetAllContactsWithoutAuthentication() {
        webTestClient.get()
                .uri("/api/contacts/all")
                .exchange()
                .expectStatus().isUnauthorized();
    }

    // Negatywne scenariusze

    @Test
    void shouldFailUpdateNonexistentContact() {
        // Próba aktualizacji kontaktu, który nie istnieje
        ContactBaseDTO nonExistentContact = ContactBaseDTO.builder()
                .id(999L) // zakładamy, że taki ID nie istnieje
                .firstName("Non")
                .lastName("Existent")
                .phoneNumber("000000000")
                .email("non.existent@example.com")
                .additionalInfo("Brak kontaktu")
                .build();

        webTestClient.put()
                .uri("/api/contacts/update")
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
                .uri("/api/contacts/getById/999")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .exchange()
                .expectStatus().is4xxClientError();
    }

    @Test
    void shouldFailAddContactWithInvalidData() {
        // Próba dodania kontaktu z nieprawidłowymi danymi (np. pusty firstName, niepoprawny format e-mail)
        ContactBaseDTO invalidContact = ContactBaseDTO.builder()
                .firstName("") // brak imienia
                .lastName("Doe")
                .phoneNumber("123456789")
                .email("invalid-email-format") // niepoprawny format e-mail
                .additionalInfo("Niepoprawne dane kontaktu")
                .build();

        webTestClient.post()
                .uri("/api/contacts/add")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(invalidContact)
                .exchange()
                .expectStatus().is4xxClientError();
    }

    @Test
    void shouldFailUpdateContactWithInvalidData() {
        // Dodajemy poprawny kontakt, a następnie próbujemy zaktualizować z niepoprawnymi danymi
        ContactBaseDTO validContact = createSampleContact();
        ContactBaseDTO savedContact = webTestClient.post()
                .uri("/api/contacts/add")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(validContact)
                .exchange()
                .expectStatus().isOk()
                .expectBody(ContactBaseDTO.class)
                .returnResult().getResponseBody();

        // Aktualizacja z pustym lastName (przyjmujemy, że pole to jest obowiązkowe)
        ContactBaseDTO invalidUpdate = ContactBaseDTO.builder()
                .id(savedContact.getId())
                .firstName(savedContact.getFirstName())
                .lastName("") // nieprawidłowa wartość
                .phoneNumber(savedContact.getPhoneNumber())
                .email(savedContact.getEmail())
                .additionalInfo(savedContact.getAdditionalInfo())
                .build();

        webTestClient.put()
                .uri("/api/contacts/update")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(invalidUpdate)
                .exchange()
                .expectStatus().is4xxClientError();
    }

    @Test
    void getContactsByFilterShouldReturnEmptyList() {
        // Dodanie poprawnego kontaktu (jeśli wymagane)
        ContactBaseDTO validContact = createSampleContact();
        webTestClient.post()
                .uri("/api/contacts/add")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(validContact)
                .exchange()
                .expectStatus().isOk();

        // Próba pobrania kontaktów z ujemnymi parametrami stronicowania
        List<ContactBaseDTO> contacts = webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path("/api/contacts/page")
                        .queryParam("firstNameStartsWith", "Johnny")
                        .queryParam("page", "1")
                        .queryParam("size", "1")
                        .build())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(ContactBaseDTO.class)
                .returnResult()
                .getResponseBody();

        assertThat(contacts).isEmpty();
    }
}
