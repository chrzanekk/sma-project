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
import pl.com.chrzanowski.sma.contact.dto.ContactDTO;
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
                .build();
    }

    @Test
    void shouldAddContactSuccessfully() {
        ContactDTO contactDTO = createSampleContact();

        ContactDTO savedContact = webTestClient.post()
                .uri("/api/contacts/add")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(contactDTO)
                .exchange()
                .expectStatus().isOk()
                .expectBody(ContactDTO.class)
                .returnResult().getResponseBody();

        assertThat(savedContact).isNotNull();
        assertThat(savedContact.getId()).isNotNull();
        assertThat(savedContact.getFirstName()).isEqualTo(contactDTO.getFirstName());
        assertThat(savedContact.getLastName()).isEqualTo(contactDTO.getLastName());
        assertThat(savedContact.getEmail()).isEqualTo(contactDTO.getEmail());
    }

    @Test
    void shouldUpdateContactSuccessfully() {
        // Dodajemy kontakt
        ContactDTO contactDTO = createSampleContact();
        ContactDTO savedContact = webTestClient.post()
                .uri("/api/contacts/add")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(contactDTO)
                .exchange()
                .expectStatus().isOk()
                .expectBody(ContactDTO.class)
                .returnResult().getResponseBody();

        // Modyfikujemy np. numer telefonu
        ContactDTO updatedContactDTO = ContactDTO.builder()
                .id(savedContact.getId())
                .firstName(savedContact.getFirstName())
                .lastName(savedContact.getLastName())
                .phoneNumber("987654321") // nowy numer telefonu
                .email(savedContact.getEmail())
                .additionalInfo(savedContact.getAdditionalInfo())
                .build();

        ContactDTO updatedContact = webTestClient.put()
                .uri("/api/contacts/update")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(updatedContactDTO)
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
        ContactDTO contactDTO = createSampleContact();
        ContactDTO savedContact = webTestClient.post()
                .uri("/api/contacts/add")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(contactDTO)
                .exchange()
                .expectStatus().isOk()
                .expectBody(ContactDTO.class)
                .returnResult().getResponseBody();

        // Pobieramy kontakt po ID
        ContactDTO retrievedContact = webTestClient.get()
                .uri("/api/contacts/getById/" + savedContact.getId())
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
    void shouldGetAllContactsSuccessfully() {
        // Dodajemy kontakt
        ContactDTO contactDTO = createSampleContact();
        ContactDTO savedContact = webTestClient.post()
                .uri("/api/contacts/add")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(contactDTO)
                .exchange()
                .expectStatus().isOk()
                .expectBody(ContactDTO.class)
                .returnResult().getResponseBody();

        // Pobieramy wszystkie kontakty
        List<ContactDTO> contacts = webTestClient.get()
                .uri("/api/contacts/all")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(ContactDTO.class)
                .returnResult().getResponseBody();

        assertThat(contacts).isNotEmpty();
        assertThat(contacts)
                .anyMatch(contact -> contact.getId().equals(savedContact.getId()));
    }

    @Test
    void shouldDeleteContactSuccessfully() {
        // Dodajemy kontakt
        ContactDTO contactDTO = createSampleContact();
        ContactDTO savedContact = webTestClient.post()
                .uri("/api/contacts/add")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(contactDTO)
                .exchange()
                .expectStatus().isOk()
                .expectBody(ContactDTO.class)
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
        List<ContactDTO> filteredContacts = webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path("/api/contacts/find")
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
        List<ContactDTO> pagedContacts = webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path("/api/contacts/page")
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
        ContactDTO contactDTO = createSampleContact();

        webTestClient.post()
                .uri("/api/contacts/add")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(contactDTO)
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
        ContactDTO nonExistentContact = ContactDTO.builder()
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
        ContactDTO invalidContact = ContactDTO.builder()
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
        ContactDTO validContact = createSampleContact();
        ContactDTO savedContact = webTestClient.post()
                .uri("/api/contacts/add")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(validContact)
                .exchange()
                .expectStatus().isOk()
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
        ContactDTO validContact = createSampleContact();
        webTestClient.post()
                .uri("/api/contacts/add")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(validContact)
                .exchange()
                .expectStatus().isOk();

        // Próba pobrania kontaktów z ujemnymi parametrami stronicowania
        List<ContactDTO> contacts = webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path("/api/contacts/page")
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
