package pl.com.chrzanowski.scma.controller;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.json.JSONArray;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import pl.com.chrzanowski.scma.ScaffoldingCompanyManagementAppApplication;
import pl.com.chrzanowski.scma.domain.*;
import pl.com.chrzanowski.scma.repository.*;
import pl.com.chrzanowski.scma.service.ServiceActionService;
import pl.com.chrzanowski.scma.service.SummaryValueServiceActionService;
import pl.com.chrzanowski.scma.service.WorkshopService;
import pl.com.chrzanowski.scma.service.dto.ServiceActionDTO;
import pl.com.chrzanowski.scma.service.mapper.ServiceActionMapper;
import pl.com.chrzanowski.scma.service.mapper.ServiceActionTypeMapper;
import pl.com.chrzanowski.scma.service.mapper.WorkshopMapper;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = ScaffoldingCompanyManagementAppApplication.class)
@AutoConfigureMockMvc
@WithMockUser
public class ServiceActionControllerIT {

    private static final String API_PATH = "/api/serviceActions";

    private static final Instant DEFAULT_CREATE_DATE = Instant.ofEpochMilli(0L);
    private static final Instant DEFAULT_MODIFY_DATE = Instant.ofEpochMilli(36000L);
    private static final Integer FIRST_CAR_MILEAGE = 111111;
    private static final Integer SECOND_CAR_MILEAGE = 222222;
    private static final String FIRST_INVOICE_NUMBER = "1/01/2022";
    private static final String SECOND_INVOICE_NUMBER = "365/12/2022";
    private static final BigDecimal FIRST_GROSS_VALUE = new BigDecimal(123);
    private static final BigDecimal SECOND_GROSS_VALUE = new BigDecimal(220);
    private static final BigDecimal SUMMARY_GROSS_VALUE = new BigDecimal("343");
    private static final BigDecimal FIRST_NET_VALUE = new BigDecimal(100);
    private static final BigDecimal SECOND_NET_VALUE = new BigDecimal(200);
    private static final BigDecimal SUMMARY_NET_VALUE = new BigDecimal(300);
    private static final BigDecimal FIRST_TAX_VALUE = new BigDecimal(23);
    private static final BigDecimal SECOND_TAX_VALUE = new BigDecimal(20);
    private static final BigDecimal SUMMARY_TAX_VALUE = new BigDecimal(43);
    private static final BigDecimal FIRST_TAX_RATE = new BigDecimal("0.23");
    private static final BigDecimal SECOND_TAX_RATE = new BigDecimal("0.10");
    private static final LocalDate FIRST_SERVICE_DATE = LocalDate.of(2022, 1, 2);
    private static final LocalDate SECOND_SERVICE_DATE = LocalDate.of(2022, 12, 1);
    private static final String FIRST_DESCRIPTION = "FIRST_DESCRIPTION";
    private static final String SECOND_DESCRIPTION = "SECOND_DESCRIPTION";

    @Autowired
    MockMvc restServiceActionMvc;
    @Autowired
    EntityManager em;
    @Autowired
    ServiceActionRepository serviceActionRepository;
    @Autowired
    ServiceActionService serviceActionService;
    @Autowired
    ServiceActionMapper serviceActionMapper;
    @Autowired
    ServiceActionTypeRepository serviceActionTypeRepository;
    @Autowired
    ServiceActionTypeMapper serviceActionTypeMapper;
    @Autowired
    WorkshopMapper workshopMapper;
    @Autowired
    WorkshopRepository workshopRepository;
    @Autowired
    WorkshopService workshopService;
    @Autowired
    private VehicleRepository vehicleRepository;
    @Autowired
    private FuelTypeRepository fuelTypeRepository;
    @Autowired
    private VehicleTypeRepository vehicleTypeRepository;
    @Autowired
    private VehicleModelRepository vehicleModelRepository;
    @Autowired
    private VehicleBrandRepository vehicleBrandRepository;
    @Autowired
    private SummaryValueServiceActionService summaryValueServiceActionService;

    private Workshop firstWorkshop;
    private Workshop secondWorkshop;
    private ServiceActionType firstServiceActionType;
    private ServiceActionType secondServiceActionType;
    private VehicleBrand firstVehicleBrand;
    private VehicleBrand secondVehicleBrand;
    private VehicleModel firstVehicleModel;
    private VehicleModel secondVehicleModel;
    private VehicleType firstVehicleType;
    private VehicleType secondVehicleType;
    private FuelType firstFuelType;
    private FuelType secondFuelType;
    private Vehicle firstVehicle;
    private Vehicle secondVehicle;
    private ServiceAction firstServiceAction;
    private ServiceAction secondServiceAction;
    private Set<ServiceActionType> firstSetOfActionTypes;
    private Set<ServiceActionType> secondSetOfActionTypes;

    public static ServiceAction createEntity(EntityManager em) {
        return new ServiceAction()
                .setCarMileage(FIRST_CAR_MILEAGE)
                .setInvoiceNumber(FIRST_INVOICE_NUMBER)
                .setGrossValue(FIRST_GROSS_VALUE)
                .setTaxValue(FIRST_TAX_VALUE)
                .setTaxRate(FIRST_TAX_RATE)
                .setNetValue(FIRST_NET_VALUE)
                .setServiceDate(FIRST_SERVICE_DATE)
                .setDescription(FIRST_DESCRIPTION)
                .setCreateDate(DEFAULT_CREATE_DATE);
    }

    public static ServiceAction createSecondEntity(EntityManager em) {
        return new ServiceAction()
                .setCarMileage(SECOND_CAR_MILEAGE)
                .setInvoiceNumber(SECOND_INVOICE_NUMBER)
                .setGrossValue(SECOND_GROSS_VALUE)
                .setTaxValue(SECOND_TAX_VALUE)
                .setTaxRate(SECOND_TAX_RATE)
                .setNetValue(SECOND_NET_VALUE)
                .setServiceDate(SECOND_SERVICE_DATE)
                .setDescription(SECOND_DESCRIPTION)
                .setCreateDate(DEFAULT_CREATE_DATE);
    }

    @BeforeEach
    public void initTest() {
        deleteAllExistingData();
        createServiceActions();
    }


    @Test
    @Transactional
    void createServiceAction() throws Exception {

        int size = serviceActionService.findAll().size();

        ServiceActionDTO serviceActionDTO = serviceActionMapper.toDto(firstServiceAction);
        restServiceActionMvc.perform(post(API_PATH + "/add")
                        .contentType(TestUtil.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(serviceActionDTO)))
                .andExpect(status().isOk());

        int sizeAfter = serviceActionService.findAll().size();
        assertThat(sizeAfter).isEqualTo(size + 1);
    }

    @Test
    @Transactional
    void updateServiceAction() throws Exception {
        serviceActionRepository.saveAndFlush(firstServiceAction);
        int size = serviceActionService.findAll().size();

        ServiceActionDTO serviceActionDTO = serviceActionMapper.toDto(firstServiceAction);
        ServiceActionDTO serviceActionDTOtoUpdate = ServiceActionDTO.builder(serviceActionDTO)
                .carMileage(SECOND_CAR_MILEAGE).build();
        restServiceActionMvc.perform(put(API_PATH + "/update")
                        .contentType(TestUtil.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(serviceActionDTOtoUpdate)))
                .andExpect(status().isOk());

        List<ServiceAction> serviceActionListAfterUpdate = serviceActionRepository.findAll();
        int sizeAfter = serviceActionListAfterUpdate.size();
        ServiceAction firstServiceActionAfterUpdate = serviceActionListAfterUpdate.get(0);
        assertThat(sizeAfter).isEqualTo(size);
        assertThat(firstServiceActionAfterUpdate.getCarMileage()).isEqualTo(SECOND_CAR_MILEAGE);
    }

    @Test
    @Transactional
    void getServiceActionById() throws Exception {
        saveServiceActionToDB();

        List<ServiceAction> serviceActionList = serviceActionRepository.findAll();
        ServiceAction serviceAction = serviceActionList.get(0);
        Long serviceActionId = serviceAction.getId();

        restServiceActionMvc.perform(get(API_PATH + "/getById/{id}", serviceActionId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id").value(serviceAction.getId().intValue()))
                .andExpect(jsonPath("$.carMileage").value(serviceAction.getCarMileage()))
                .andExpect(jsonPath("$.invoiceNumber").value(serviceAction.getInvoiceNumber()))
                .andExpect(jsonPath("$.grossValue").value(serviceAction.getGrossValue().toString()))
                .andExpect(jsonPath("$.taxValue").value(serviceAction.getTaxValue().toString()))
                .andExpect(jsonPath("$.netValue").value(serviceAction.getNetValue().toString()))
                .andExpect(jsonPath("$.taxRate").value(serviceAction.getTaxRate().toString()))
                .andExpect(jsonPath("$.serviceDate").value(serviceAction.getServiceDate().toString()))
                .andExpect(jsonPath("$.description").value(serviceAction.getDescription()))
                .andExpect(jsonPath("$.workshopId").value(serviceAction.getWorkshop().getId().intValue()))
                .andExpect(jsonPath("$.workshopName").value(serviceAction.getWorkshop().getName()))
                .andExpect(jsonPath("$.vehicleId").value(serviceAction.getVehicle().getId().intValue()))
                .andExpect(jsonPath("$.vehicleRegistrationNumber").value(serviceAction.getVehicle()
                        .getRegistrationNumber()));

    }

    @Test
    @Transactional
    void getServiceActionByIdAndShouldNotBeFound() throws Exception {
        restServiceActionMvc.perform(get(API_PATH + "/getById/{id}", 123L))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    void getAllServiceActions() throws Exception {
        saveServiceActionToDB();

        int sizeBeforeTest = serviceActionRepository.findAll().size();

        MvcResult result = restServiceActionMvc.perform(get(API_PATH + "/all"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isNotEmpty())
                .andReturn();

        String list = result.getResponse().getContentAsString();
        JSONArray jsonArray = new JSONArray(list);
        int jsonSize = jsonArray.length();
        assertThat(jsonSize).isEqualTo(sizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllServiceActionsHasSummaryValuesByFilter() throws Exception {
        saveServiceActionToDB();

        int sizeBeforeTest = serviceActionRepository.findAll().size();

        MvcResult result = restServiceActionMvc.perform(get(API_PATH + "/?sort=id,desc&"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.[0].summaryGrossValue").value(SUMMARY_GROSS_VALUE))
                .andExpect(jsonPath("$.[0].summaryTaxValue").value(SUMMARY_TAX_VALUE))
                .andExpect(jsonPath("$.[0].summaryNetValue").value(SUMMARY_NET_VALUE)).andReturn();

        String list = result.getResponse().getContentAsString();
        JSONArray jsonArray = new JSONArray(list);
        int jsonSize = jsonArray.length();
        assertThat(jsonSize).isEqualTo(sizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllServiceActionsHasSummaryValues() throws Exception {
        saveServiceActionToDB();

        restServiceActionMvc.perform(get(API_PATH + "/getSummaryValues"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.summaryGrossValue").value(SUMMARY_GROSS_VALUE.doubleValue()))
                .andExpect(jsonPath("$.summaryTaxValue").value(SUMMARY_TAX_VALUE.doubleValue()))
                .andExpect(jsonPath("$.summaryNetValue").value(SUMMARY_NET_VALUE.doubleValue()));
    }

    @Test
    @Transactional
    void deleteServiceActionById() throws Exception {
        saveServiceActionToDB();
        List<ServiceAction> allServiceActions = serviceActionRepository.findAll();
        int sizeBeforeTest = allServiceActions.size();
        Long id = allServiceActions.get(0).getId();

        restServiceActionMvc.perform(delete(API_PATH + "/delete/{id}", id))
                .andExpect(status().isOk());
        int sizeAfterTest = serviceActionRepository.findAll().size();
        assertThat(sizeAfterTest).isEqualTo(sizeBeforeTest - 1);
    }


    @Test
    @Transactional
    void findServiceActionByCarMileage() throws Exception {
        saveServiceActionToDB();
        defaultServiceActionShouldBeFound("carMileageStartsWith=" + FIRST_CAR_MILEAGE + "&carMileageEndWith=" + 222220);
        defaultServiceActionShouldNotBeFound("carMileageStartsWith=" + 333333);
    }

    @Test
    @Transactional
    void findServiceActionByInvoiceNumber() throws Exception {
        saveServiceActionToDB();
        defaultServiceActionShouldBeFound("invoiceNumber=" + FIRST_INVOICE_NUMBER);
        defaultServiceActionShouldNotBeFound("invoiceNumber=" + "badInvoiceNumber");
    }

    @Test
    @Transactional
    void findServiceActionByGrossValue() throws Exception {
        saveServiceActionToDB();
        defaultServiceActionShouldBeFound("grossValueStartsWith=" + FIRST_GROSS_VALUE + "&grossValueEndWith=" + 125);
        defaultServiceActionShouldNotBeFound("grossValueStartsWith=" + 666);
    }

    @Test
    @Transactional
    void findServiceActionByNetValue() throws Exception {
        saveServiceActionToDB();
        defaultServiceActionShouldBeFound("netValueStartsWith=" + FIRST_NET_VALUE + "&netValueEndWith=" + 101);
        defaultServiceActionShouldNotBeFound("netValueStartsWith=" + 666);
    }

    @Test
    @Transactional
    void findServiceActionByTaxValue() throws Exception {
        saveServiceActionToDB();
        defaultServiceActionShouldBeFound("taxValueStartsWith=" + FIRST_TAX_VALUE + "&taxValueEndWith=" + 24);
        defaultServiceActionShouldNotBeFound("taxValueStartsWith=" + 666);
    }

    @Test
    @Transactional
    void findServiceActionByTaxRate() throws Exception {
        saveServiceActionToDB();
        defaultServiceActionShouldBeFound("taxRateStartsWith=" + FIRST_TAX_RATE + "&taxRateEndWith=" + new BigDecimal(
                "0.24"));
        defaultServiceActionShouldNotBeFound("taxRateStartsWith=" + 666);
    }

    @Test
    @Transactional
    void findServiceActionByServiceDate() throws Exception {
        saveServiceActionToDB();
        defaultServiceActionShouldBeFound("serviceDateStartsWith=" + FIRST_SERVICE_DATE + "&serviceDateEndWith=" + FIRST_SERVICE_DATE.plusDays(1));
        defaultServiceActionShouldNotBeFound("serviceDateStartsWith=" + LocalDate.now());
    }

    @Test
    @Transactional
    void findServiceActionByWorkshopId() throws Exception {
        saveServiceActionToDB();
        defaultServiceActionShouldBeFound("workshopId=" + firstServiceAction.getWorkshop().getId());
        defaultServiceActionShouldNotBeFound("workshopId=" + 144L);
    }

    @Test
    @Transactional
    void findServiceActionByWorkshopName() throws Exception {
        saveServiceActionToDB();
        defaultServiceActionShouldBeFound("workshopName=" + firstServiceAction.getWorkshop().getName().substring(0, 3));
        defaultServiceActionShouldNotBeFound("workshopName=" + "badWorkshopName");
    }

    @Test
    @Transactional
    void findServiceActionByVehicleId() throws Exception {
        saveServiceActionToDB();
        defaultServiceActionShouldBeFound("vehicleId=" + firstServiceAction.getVehicle().getId());
        defaultServiceActionShouldNotBeFound("vehicleId=" + 144L);
    }

    @Test
    @Transactional
    void findServiceActionByVehicleRegistrationNumber() throws Exception {
        saveServiceActionToDB();
        defaultServiceActionShouldBeFound("vehicleRegistrationNumber=" + firstServiceAction.getVehicle()
                .getRegistrationNumber().substring(0, 3));
        defaultServiceActionShouldNotBeFound("vehicleRegistrationNumber=" + "badRegNumber");
    }


    private void defaultServiceActionShouldBeFound(String filter) throws Exception {
        restServiceActionMvc.perform(get(API_PATH + "/?sort=id,desc&" + filter))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(firstServiceAction.getId().intValue())))
                .andExpect(jsonPath("$.[*].carMileage").value(hasItem(firstServiceAction.getCarMileage())))
                .andExpect(jsonPath("$.[*].invoiceNumber").value(hasItem(firstServiceAction.getInvoiceNumber())))
                .andExpect(jsonPath("$.[*].grossValue").value(hasItem(firstServiceAction.getGrossValue().intValue())))
                .andExpect(jsonPath("$.[*].taxValue").value(hasItem(firstServiceAction.getTaxValue().intValue())))
                .andExpect(jsonPath("$.[*].netValue").value(hasItem(firstServiceAction.getNetValue().intValue())))
                .andExpect(jsonPath("$.[*].taxRate").value(hasItem(firstServiceAction.getTaxRate().doubleValue())))
                .andExpect(jsonPath("$.[*].serviceDate").value(hasItem(firstServiceAction.getServiceDate().toString())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(firstServiceAction.getDescription())))
                .andExpect(jsonPath("$.[*].workshopId").value(hasItem(firstServiceAction.getWorkshop().getId()
                        .intValue())))
                .andExpect(jsonPath("$.[*].workshopName").value(hasItem(firstServiceAction.getWorkshop().getName())))
                .andExpect(jsonPath("$.[*].vehicleId").value(hasItem(firstServiceAction.getVehicle().getId()
                        .intValue())))
                .andExpect(jsonPath("$.[*].vehicleRegistrationNumber").value(hasItem(firstServiceAction.getVehicle()
                        .getRegistrationNumber())));
    }

    private void defaultServiceActionShouldNotBeFound(String filter) throws Exception {
        restServiceActionMvc.perform(get(API_PATH + "/?sort=id,desc&" + filter))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }


    private void deleteAllExistingData() {
        fuelTypeRepository.deleteAll();
        vehicleTypeRepository.deleteAll();
        vehicleBrandRepository.deleteAll();
        vehicleModelRepository.deleteAll();
        vehicleRepository.deleteAll();
        serviceActionTypeRepository.deleteAll();
        workshopRepository.deleteAll();
        serviceActionRepository.deleteAll();
    }

    private void saveServiceActionToDB() {
        serviceActionRepository.saveAndFlush(firstServiceAction);
        serviceActionRepository.saveAndFlush(secondServiceAction);
    }

    private void createServiceActions() {
        createAllDefaultData();
        firstServiceAction = createEntity(em);
        firstServiceAction.setVehicle(firstVehicle);
        firstServiceAction.setServiceActionTypes(firstSetOfActionTypes);
        firstServiceAction.setWorkshop(firstWorkshop);

        secondServiceAction = createSecondEntity(em);
        secondServiceAction.setVehicle(secondVehicle);
        secondServiceAction.setServiceActionTypes(secondSetOfActionTypes);
        secondServiceAction.setWorkshop(secondWorkshop);
    }

    private void createAllDefaultData() {
        createGlobalVehicleBrands();
        createGlobalVehicleTypes();
        createGlobalFuelTypes();
        createGlobalVehicleModels();
        createGlobalVehicles();
        createGlobalActionTypes();
        createSetOfTwoActionTypes();
        createSetOfOneActionType();
        createGlobalWorkshopInDB();
        createSecondGlobalWorkshopInDB();
    }

    private void createGlobalActionTypes() {
        firstServiceActionType = ServiceActionTypeControllerIT.createEntity(em);
        em.persist(firstServiceActionType);
        em.flush();
        secondServiceActionType = ServiceActionTypeControllerIT.createSecondEntity(em);
        em.persist(secondServiceActionType);
        em.flush();
    }

    private void createGlobalWorkshopInDB() {
        firstWorkshop = WorkshopControllerIT.createEntity(em);
        firstWorkshop.setServiceActionTypes(firstSetOfActionTypes);
        em.persist(firstWorkshop);
        em.flush();
    }

    private void createSetOfTwoActionTypes() {
        List<ServiceActionType> serviceActionTypes = serviceActionTypeRepository.findAll();
        firstSetOfActionTypes = new HashSet<>(serviceActionTypes);
    }

    private void createSetOfOneActionType() {
        List<ServiceActionType> serviceActionTypes = new ArrayList<>();
        serviceActionTypes.add(firstServiceActionType);
        secondSetOfActionTypes = new HashSet<>(serviceActionTypes);
    }


    private void createSecondGlobalWorkshopInDB() {
        secondWorkshop = WorkshopControllerIT.createSecondEntity(em);
        secondWorkshop.setServiceActionTypes(secondSetOfActionTypes);
        em.persist(secondWorkshop);
        em.flush();
    }

    private void createGlobalFuelTypes() {
        firstFuelType = FuelTypeControllerIT.createEntity(em);
        fuelTypeRepository.saveAndFlush(firstFuelType);

        secondFuelType = FuelTypeControllerIT.createSecondEntity(em);
        fuelTypeRepository.saveAndFlush(secondFuelType);
    }

    private void createGlobalVehicleTypes() {
        firstVehicleType = VehicleTypeControllerIT.createEntity(em);
        vehicleTypeRepository.saveAndFlush(firstVehicleType);
        secondVehicleType = VehicleTypeControllerIT.createSecondEntity(em);
        vehicleTypeRepository.saveAndFlush(secondVehicleType);
    }

    private void createGlobalVehicleModels() {
        firstVehicleModel = VehicleModelControllerIT.createEntity(em);
        firstVehicleModel.setVehicleBrand(firstVehicleBrand);
        em.persist(firstVehicleModel);
        em.flush();
        secondVehicleModel = VehicleModelControllerIT.createSecondEntity(em);
        secondVehicleModel.setVehicleBrand(secondVehicleBrand);
        em.persist(secondVehicleModel);
        em.flush();
    }

    private void createGlobalVehicleBrands() {
        firstVehicleBrand = VehicleBrandControllerIT.createEntity(em);
        em.persist(firstVehicleBrand);
        em.flush();
        secondVehicleBrand = VehicleBrandControllerIT.createSecondEntity(em);
        em.persist(secondVehicleBrand);
        em.flush();
    }

    private void createGlobalVehicles() {
        firstVehicle = VehicleControllerIT.createEntity(em);
        firstVehicle.setFuelType(firstFuelType);
        firstVehicle.setVehicleType(firstVehicleType);
        firstVehicle.setVehicleModel(firstVehicleModel);
        vehicleRepository.saveAndFlush(firstVehicle);
        secondVehicle = VehicleControllerIT.createSecondEntity(em);
        secondVehicle.setFuelType(secondFuelType);
        secondVehicle.setVehicleType(secondVehicleType);
        secondVehicle.setVehicleModel(secondVehicleModel);
        vehicleRepository.saveAndFlush(secondVehicle);
    }
}
