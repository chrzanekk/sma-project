package pl.com.chrzanowski.sma.integrationTests;

import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.test.web.reactive.server.WebTestClient;
import pl.com.chrzanowski.sma.AbstractTestContainers;
import pl.com.chrzanowski.sma.auth.dto.request.LoginRequest;
import pl.com.chrzanowski.sma.auth.dto.response.MessageResponse;
import pl.com.chrzanowski.sma.company.mapper.CompanyBaseMapper;
import pl.com.chrzanowski.sma.company.model.Company;
import pl.com.chrzanowski.sma.company.repository.CompanyRepository;
import pl.com.chrzanowski.sma.email.service.SendEmailService;
import pl.com.chrzanowski.sma.employee.dto.EmployeeDTO;
import pl.com.chrzanowski.sma.employee.mapper.EmployeeDTOMapper;
import pl.com.chrzanowski.sma.employee.repository.EmployeeRepository;
import pl.com.chrzanowski.sma.integrationTests.helper.UserHelper;
import pl.com.chrzanowski.sma.position.mapper.PositionBaseMapper;
import pl.com.chrzanowski.sma.position.model.Position;
import pl.com.chrzanowski.sma.position.repository.PositionRepository;
import pl.com.chrzanowski.sma.user.mapper.UserMapper;
import pl.com.chrzanowski.sma.user.model.User;
import pl.com.chrzanowski.sma.user.service.UserService;

import java.time.Duration;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@AutoConfigureWebTestClient
@Import(ContractorControllerIntegrationTest.TestConfig.class)
public class EmployeeControllerIntegrationTest extends AbstractTestContainers {

    public static final String EMPLOYEE_API_PATH = "/api/employees";

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private Flyway flyway;

    @Autowired
    private SendEmailService sendEmailService;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private UserHelper userHelper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private EmployeeDTOMapper employeeDTOMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private CompanyBaseMapper companyBaseMapper;

    @Autowired
    private PositionRepository positionRepository;

    @Autowired
    private PositionBaseMapper positionBaseMapper;

    private String jwtToken;

    private EmployeeDTO firstEmployee;
    private EmployeeDTO secondEmployee;

    private Company company;
    private Position firstPosition;
    private Position secondPosition;

    @TestConfiguration
    static class TestConfig{
        @Bean
        @Primary
        public SendEmailService sendEmailService() {
            return Mockito.mock(SendEmailService.class);
        }
    }

    @BeforeEach
    void setUp() {
        this.webTestClient = this.webTestClient
                .mutate()
                .baseUrl(EMPLOYEE_API_PATH)
                .responseTimeout(Duration.ofSeconds(60))
                .build();

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
    }

}
