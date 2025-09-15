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
import pl.com.chrzanowski.sma.common.enumeration.Country;
import pl.com.chrzanowski.sma.company.dto.CompanyDTO;
import pl.com.chrzanowski.sma.company.mapper.CompanyDTOMapper;
import pl.com.chrzanowski.sma.company.model.Company;
import pl.com.chrzanowski.sma.company.repository.CompanyRepository;
import pl.com.chrzanowski.sma.constructionsite.dto.ConstructionSiteAuditableDTO;
import pl.com.chrzanowski.sma.constructionsite.dto.ConstructionSiteDTO;
import pl.com.chrzanowski.sma.integrationTests.helper.UserHelper;

import java.time.Duration;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
class ConstructionSiteControllerIntegrationTest extends AbstractTestContainers {

    public static final String API_CONSTRUCTION_SITES = "/api/construction-sites";
    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private Flyway flyway;

    @Autowired
    private UserHelper userHelper;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private CompanyDTOMapper companyDTOMapper;

    private String jwtToken;
    private Company company;
    private CompanyDTO companyDTO;

    @BeforeEach
    void setUp() {
        this.webTestClient = this.webTestClient
                .mutate()
                .responseTimeout(Duration.ofSeconds(60))
                .build();

        // reset DB
        flyway.clean();
        flyway.migrate();

        // register & authenticate
        LoginRequest login = userHelper.registerFirstUser(webTestClient);
        jwtToken = userHelper.authenticateUser(login, webTestClient);

        // prepare a company
        companyRepository.deleteAll();
        company = Company.builder()
                .name("TestCompany")
                .additionalInfo("Info")
                .build();
        company = companyRepository.saveAndFlush(company);
        companyDTO = companyDTOMapper.toDto(company);
    }

    private ConstructionSiteDTO createSampleSite(String name) {
        return ConstructionSiteDTO.builder()
                .name(name)
                .address("123 Main St")
                .country(Country.POLAND)
                .shortName(name.substring(0, 1))
                .code(name.substring(0, 1))
                .company(companyDTO)
                .build();
    }

    @Test
    void shouldAddConstructionSiteSuccessfully() {
        ConstructionSiteDTO dto = createSampleSite("AlphaSite");

        ConstructionSiteDTO saved = webTestClient.post()
                .uri(API_CONSTRUCTION_SITES)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(dto)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(ConstructionSiteDTO.class)
                .returnResult()
                .getResponseBody();

        assertNotNull(saved);
        assertNotNull(saved.getId());
        assertEquals("AlphaSite", saved.getName());
        assertEquals("123 Main St", saved.getAddress());
    }

    @Test
    void shouldGetConstructionSiteByIdSuccessfully() {
        // create
        ConstructionSiteDTO dto = createSampleSite("BravoSite");
        ConstructionSiteDTO saved = webTestClient.post()
                .uri(API_CONSTRUCTION_SITES)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(dto)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(ConstructionSiteDTO.class)
                .returnResult()
                .getResponseBody();

        // fetch
        ConstructionSiteDTO fetched = webTestClient.get()
                .uri(API_CONSTRUCTION_SITES + "/" + saved.getId())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .exchange()
                .expectStatus().isOk()
                .expectBody(ConstructionSiteDTO.class)
                .returnResult()
                .getResponseBody();

        assertNotNull(fetched);
        assertEquals(saved.getId(), fetched.getId());
        assertEquals("BravoSite", fetched.getName());
    }

    @Test
    void shouldGetConstructionSiteByIdNotFound() {
        webTestClient.get()
                .uri(API_CONSTRUCTION_SITES + "/9999")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void shouldUpdateConstructionSiteSuccessfully() {
        // create
        ConstructionSiteDTO dto = createSampleSite("CharlieSite");
        ConstructionSiteDTO saved = webTestClient.post()
                .uri(API_CONSTRUCTION_SITES)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(dto)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(ConstructionSiteDTO.class)
                .returnResult()
                .getResponseBody();

        // update name
        ConstructionSiteDTO toUpdate = ConstructionSiteDTO.builder()
                .id(saved.getId())
                .name("CharlieUpdated")
                .address(saved.getAddress())
                .country(saved.getCountry())
                .shortName(saved.getShortName())
                .code(saved.getCode())
                .company(companyDTO)
                .build();

        ConstructionSiteDTO updated = webTestClient.put()
                .uri(API_CONSTRUCTION_SITES + "/" + toUpdate.getId())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(toUpdate)
                .exchange()
                .expectStatus().isOk()
                .expectBody(ConstructionSiteDTO.class)
                .returnResult()
                .getResponseBody();

        assertNotNull(updated);
        assertEquals("CharlieUpdated", updated.getName());
        assertEquals(saved.getId(), updated.getId());
    }

    @Test
    void shouldFailUpdateConstructionSiteWithInvalidId() {
        ConstructionSiteDTO dto = createSampleSite("CharlieSite");
        ConstructionSiteDTO saved = webTestClient.post()
                .uri(API_CONSTRUCTION_SITES)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(dto)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(ConstructionSiteDTO.class)
                .returnResult()
                .getResponseBody();

        ConstructionSiteDTO toUpdate = ConstructionSiteDTO.builder()
                .id(88888L)
                .name("CharlieUpdated")
                .address(saved.getAddress())
                .country(saved.getCountry())
                .shortName(saved.getShortName())
                .code(saved.getCode())
                .company(companyDTO)
                .build();

        webTestClient.put()
                .uri(API_CONSTRUCTION_SITES + "/" + toUpdate.getId())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(toUpdate)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void shouldDeleteBaseConstructionSiteSuccessfully() {
        // create
        ConstructionSiteDTO dto = createSampleSite("EchoSite");
        ConstructionSiteDTO saved = webTestClient.post()
                .uri(API_CONSTRUCTION_SITES)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(dto)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(ConstructionSiteDTO.class)
                .returnResult()
                .getResponseBody();

        // delete
        webTestClient.delete()
                .uri(API_CONSTRUCTION_SITES + "/" + saved.getId())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .exchange()
                .expectStatus().isNoContent();

        // verify gone
        webTestClient.get()
                .uri(API_CONSTRUCTION_SITES + "/" + saved.getId())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void shouldGetConstructionSitesByFilterSuccessfully() {
        // create two sites
        webTestClient.post().uri(API_CONSTRUCTION_SITES)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(createSampleSite("FilterA"))
                .exchange().expectStatus().isCreated();

        webTestClient.post().uri(API_CONSTRUCTION_SITES)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(createSampleSite("FilterB"))
                .exchange().expectStatus().isCreated();

        List<ConstructionSiteAuditableDTO> filtered = webTestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(API_CONSTRUCTION_SITES)
                        .queryParam("nameStartsWith", "FilterA")
                        .build())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(ConstructionSiteAuditableDTO.class)
                .returnResult()
                .getResponseBody();

        assertThat(filtered).isNotEmpty();
        assertThat(filtered).allMatch(d -> d.getBase().getName().startsWith("FilterA"));
    }

    @Test
    void shouldGetConstructionSitesByFilterAndPageSuccessfully() {
        // create two sites with same prefix
        webTestClient.post().uri(API_CONSTRUCTION_SITES)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(createSampleSite("PageX"))
                .exchange().expectStatus().isCreated();

        webTestClient.post().uri(API_CONSTRUCTION_SITES)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(createSampleSite("PageY"))
                .exchange().expectStatus().isCreated();

        List<ConstructionSiteAuditableDTO> pageContent = webTestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(API_CONSTRUCTION_SITES)
                        .queryParam("nameStartsWith", "PageX")
                        .queryParam("country", "")
                        .queryParam("page", "0")
                        .queryParam("size", "1")
                        .build())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().exists("X-Total-Count")
                .expectBodyList(ConstructionSiteAuditableDTO.class)
                .returnResult()
                .getResponseBody();

        assertThat(pageContent).hasSize(1);
    }
}
