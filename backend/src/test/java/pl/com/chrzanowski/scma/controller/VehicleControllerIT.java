package pl.com.chrzanowski.scma.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import pl.com.chrzanowski.scma.ScaffoldingCompanyManagementAppApplication;
import pl.com.chrzanowski.scma.domain.*;
import pl.com.chrzanowski.scma.repository.*;
import pl.com.chrzanowski.scma.service.VehicleBrandService;
import pl.com.chrzanowski.scma.service.VehicleModelService;
import pl.com.chrzanowski.scma.service.VehicleService;
import pl.com.chrzanowski.scma.service.dto.VehicleDTO;
import pl.com.chrzanowski.scma.service.mapper.VehicleBrandMapper;
import pl.com.chrzanowski.scma.service.mapper.VehicleMapper;
import pl.com.chrzanowski.scma.service.mapper.VehicleModelMapper;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = ScaffoldingCompanyManagementAppApplication.class)
@AutoConfigureMockMvc
@WithMockUser
public class VehicleControllerIT {

    private static final String API_PATH = "/api/vehicles";

    private static final LocalDate FIRST_FIRST_REGISTRATION_DATE = LocalDate.of(2002, 1, 1);
    private static final LocalDate SECOND_FIRST_REGISTRATION_DATE = LocalDate.of(2012, 1, 1);
    private static final LocalDate UPDATED_FIRST_REGISTRATION_DATE = LocalDate.of(2022, 1, 1);

    private static final String FIST_REGISTRATION_NUMBER = "AAA-1111";
    private static final String SECOND_REGISTRATION_NUMBER = "BBB-2222";
    private static final String UPDATED_REGISTRATION_NUMBER = "CCC-3333";

    private static final String FIRST_VIN = "111111111111111";
    private static final String SECOND_VIN = "222222222222222";
    private static final String UPDATED_VIN = "333333333333333";
    private static final Short FIRST_PRODUCTION_YEAR = 2021;
    private static final Short SECOND_PRODUCTION_YEAR = 2012;
    private static final Short UPDATED_PRODUCTION_YEAR = 2022;
    private static final Short FIRST_FREE_PLACES_FOR_TECH_INSPECTION = 3;
    private static final Short SECOND_FREE_PLACES_FOR_TECH_INSPECTION = 2;
    private static final Short UPDATED_FREE_PLACES_FOR_TECH_INSPECTION = 1;

    private static final BigDecimal FIRST_LENGTH = new BigDecimal("2.2");
    private static final BigDecimal SECOND_LENGTH = new BigDecimal("2.3");
    private static final BigDecimal UPDATED_LENGTH = new BigDecimal("2.4");
    private static final BigDecimal FIRST_WIDTH = new BigDecimal("2.0");
    private static final BigDecimal SECOND_WIDTH = new BigDecimal("2.1");
    private static final BigDecimal UPDATED_WIDTH = new BigDecimal("2.2");
    private static final BigDecimal FIRST_HEIGHT = new BigDecimal("1.8");
    private static final BigDecimal SECOND_HEIGHT = new BigDecimal("1.9");
    private static final BigDecimal UPDATED_HEIGHT = new BigDecimal("2.0");

    private static final Instant DEFAULT_CREATE_DATE = Instant.ofEpochMilli(0L);
    private static final Instant DEFAULT_MODIFY_DATE = Instant.ofEpochMilli(36000L);

    @Autowired
    private MockMvc restVehicleMvc;
    @Autowired
    private EntityManager em;
    @Autowired
    private VehicleRepository vehicleRepository;
    @Autowired
    private VehicleService vehicleService;
    @Autowired
    private VehicleMapper vehicleMapper;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private FuelTypeRepository fuelTypeRepository;
    @Autowired
    private VehicleTypeRepository vehicleTypeRepository;
    @Autowired
    private VehicleModelRepository vehicleModelRepository;
    @Autowired
    private VehicleBrandRepository vehicleBrandRepository;
    @Autowired
    private VehicleModelService vehicleModelService;
    @Autowired
    private VehicleModelMapper vehicleModelMapper;
    @Autowired
    private VehicleBrandService vehicleBrandService;
    @Autowired
    private VehicleBrandMapper vehicleBrandMapper;


    private VehicleBrand firstVehicleBrand;
    private VehicleBrand secondVehicleBrand;
    private VehicleBrand updatedVehicleBrand;
    private VehicleModel firstVehicleModel;
    private VehicleModel secondVehicleModel;
    private VehicleModel updatedVehicleModel;
    private VehicleType firstVehicleType;
    private VehicleType secondVehicleType;
    private VehicleType updatedVehicleType;
    private FuelType firstFuelType;
    private FuelType secondFuelType;
    private FuelType updatedFuelType;

    private Vehicle firstVehicle;
    private Vehicle secondVehicle;
    private Vehicle updatedVehicle;

    public static Vehicle createEntity(EntityManager em) {
        return createFirstBasicVehicle();
    }

    public static Vehicle createSecondEntity(EntityManager em) {
        return createSecondBasicVehicle();
    }

    public static Vehicle createUpdatedEntity(EntityManager em) {
        return createUpdatedBasicVehicle();
    }


    public static Vehicle createFirstBasicVehicle() {
        return new Vehicle()
                .setRegistrationNumber(FIST_REGISTRATION_NUMBER)
                .setVin(FIRST_VIN)
                .setProductionYear(FIRST_PRODUCTION_YEAR)
                .setFirstRegistrationDate(FIRST_FIRST_REGISTRATION_DATE)
                .setFreePlacesForTechInspection(FIRST_FREE_PLACES_FOR_TECH_INSPECTION)
                .setLength(FIRST_LENGTH)
                .setWidth(FIRST_WIDTH)
                .setHeight(FIRST_HEIGHT)
                .setCreateDate(DEFAULT_CREATE_DATE);
    }

    public static Vehicle createSecondBasicVehicle() {
        return new Vehicle()
                .setRegistrationNumber(SECOND_REGISTRATION_NUMBER)
                .setVin(SECOND_VIN)
                .setProductionYear(SECOND_PRODUCTION_YEAR)
                .setFirstRegistrationDate(SECOND_FIRST_REGISTRATION_DATE)
                .setFreePlacesForTechInspection(SECOND_FREE_PLACES_FOR_TECH_INSPECTION)
                .setLength(SECOND_LENGTH)
                .setWidth(SECOND_WIDTH)
                .setHeight(SECOND_HEIGHT)
                .setCreateDate(DEFAULT_CREATE_DATE);
    }

    public static Vehicle createUpdatedBasicVehicle() {
        return new Vehicle()
                .setRegistrationNumber(UPDATED_REGISTRATION_NUMBER)
                .setVin(UPDATED_VIN)
                .setProductionYear(UPDATED_PRODUCTION_YEAR)
                .setFirstRegistrationDate(UPDATED_FIRST_REGISTRATION_DATE)
                .setFreePlacesForTechInspection(UPDATED_FREE_PLACES_FOR_TECH_INSPECTION)
                .setLength(UPDATED_LENGTH)
                .setWidth(UPDATED_WIDTH)
                .setHeight(UPDATED_HEIGHT)
                .setCreateDate(DEFAULT_CREATE_DATE)
                .setModifyDate(DEFAULT_MODIFY_DATE);
    }

    @BeforeEach
    public void initTest() {
        vehicleModelRepository.deleteAll();
        vehicleBrandRepository.deleteAll();
        fuelTypeRepository.deleteAll();
        vehicleTypeRepository.deleteAll();
        createAllDefaultData();
        createGlobalVehicles();

    }

    @Test
    @Transactional
    public void createVehicle() throws Exception {

        List<Vehicle> allVehicles = vehicleRepository.findAll();
        List<FuelType> allFuelTypes = fuelTypeRepository.findAll();
        List<VehicleModel> allVehicleModels = vehicleModelRepository.findAll();
        List<VehicleType> allVehicleTypes = vehicleTypeRepository.findAll();

        FuelType fuelType = allFuelTypes.get(0);
        VehicleModel vehicleModel = allVehicleModels.get(0);
        VehicleType vehicleType = allVehicleTypes.get(0);

        int sizeBeforeTest = allVehicles.size();

        VehicleDTO vehicleDTO = vehicleMapper.toDto(firstVehicle);

        restVehicleMvc.perform(post(API_PATH + "/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(vehicleDTO)))
                .andExpect(status().isOk());

        List<Vehicle> listAfterTest = vehicleRepository.findAll();
        int sizeAfterTest = listAfterTest.size();
        Vehicle savedVehicle = listAfterTest.get(0);
        assertThat(sizeAfterTest).isEqualTo(sizeBeforeTest + 1);
        assertThat(savedVehicle.getVehicleModel().getName()).isEqualTo(vehicleModel.getName());
        assertThat(savedVehicle.getVehicleModel().getVehicleBrand().getName()).isEqualTo(vehicleModel.getVehicleBrand()
                .getName());
        assertThat(savedVehicle.getFuelType().getName()).isEqualTo(fuelType.getName());
        assertThat(savedVehicle.getVehicleType().getName()).isEqualTo(vehicleType.getName());
        assertThat(savedVehicle.getFirstRegistrationDate()).isEqualTo(FIRST_FIRST_REGISTRATION_DATE);
        assertThat(savedVehicle.getFreePlacesForTechInspection()).isEqualTo(FIRST_FREE_PLACES_FOR_TECH_INSPECTION);
        assertThat(savedVehicle.getRegistrationNumber()).isEqualTo(FIST_REGISTRATION_NUMBER);
        assertThat(savedVehicle.getProductionYear()).isEqualTo(FIRST_PRODUCTION_YEAR);
        assertThat(savedVehicle.getHeight()).isEqualTo(FIRST_HEIGHT);
        assertThat(savedVehicle.getVin()).isEqualTo(FIRST_VIN);
        assertThat(savedVehicle.getWidth()).isEqualTo(FIRST_WIDTH);
        assertThat(savedVehicle.getLength()).isEqualTo(FIRST_LENGTH);
    }

    @Test
    @Transactional
    public void createVehicleShouldThrowExceptionForMissingBrandId() throws Exception {

        List<Vehicle> allVehicles = vehicleRepository.findAll();
        List<FuelType> allFuelTypes = fuelTypeRepository.findAll();
        List<VehicleModel> allVehicleModels = vehicleModelRepository.findAll();
        List<VehicleType> allVehicleTypes = vehicleTypeRepository.findAll();

        FuelType fuelType = allFuelTypes.get(0);
        VehicleModel vehicleModel = allVehicleModels.get(0);
        VehicleType vehicleType = allVehicleTypes.get(0);

        int sizeBeforeTest = allVehicles.size();

        VehicleDTO vehicleDTO = vehicleMapper.toDto(firstVehicle);
        VehicleDTO vehicleDTOtoSave = VehicleDTO.builder()
                .brandId(null)
                .brandName(vehicleModel.getVehicleBrand().getName())
                .modelId(vehicleModel.getId())
                .modelName(vehicleModel.getName())
                .fuelTypeId(fuelType.getId())
                .fuelTypeName(fuelType.getName())
                .vehicleTypeId(vehicleType.getId())
                .vehicleTypeName(vehicleType.getName())
                .registrationNumber(FIST_REGISTRATION_NUMBER)
                .vin(FIRST_VIN)
                .productionYear(FIRST_PRODUCTION_YEAR)
                .firstRegistrationDate(FIRST_FIRST_REGISTRATION_DATE)
                .freePlacesForTechInspection(FIRST_FREE_PLACES_FOR_TECH_INSPECTION)
                .length(FIRST_LENGTH)
                .width(FIRST_WIDTH)
                .height(FIRST_HEIGHT)
                .createDate(DEFAULT_CREATE_DATE).build();

        restVehicleMvc.perform(post(API_PATH + "/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(vehicleDTOtoSave)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    public void createVehicleShouldThrowExceptionForMissingBrandName() throws Exception {

        List<Vehicle> allVehicles = vehicleRepository.findAll();
        List<FuelType> allFuelTypes = fuelTypeRepository.findAll();
        List<VehicleModel> allVehicleModels = vehicleModelRepository.findAll();
        List<VehicleType> allVehicleTypes = vehicleTypeRepository.findAll();

        FuelType fuelType = allFuelTypes.get(0);
        VehicleModel vehicleModel = allVehicleModels.get(0);
        VehicleType vehicleType = allVehicleTypes.get(0);

        int sizeBeforeTest = allVehicles.size();

        VehicleDTO vehicleDTO = vehicleMapper.toDto(firstVehicle);
        VehicleDTO vehicleDTOtoSave = VehicleDTO.builder()
                .brandId(vehicleModel.getVehicleBrand().getId())
                .brandName(null)
                .modelId(vehicleModel.getId())
                .modelName(vehicleModel.getName())
                .fuelTypeId(fuelType.getId())
                .fuelTypeName(fuelType.getName())
                .vehicleTypeId(vehicleType.getId())
                .vehicleTypeName(vehicleType.getName())
                .registrationNumber(FIST_REGISTRATION_NUMBER)
                .vin(FIRST_VIN)
                .productionYear(FIRST_PRODUCTION_YEAR)
                .firstRegistrationDate(FIRST_FIRST_REGISTRATION_DATE)
                .freePlacesForTechInspection(FIRST_FREE_PLACES_FOR_TECH_INSPECTION)
                .length(FIRST_LENGTH)
                .width(FIRST_WIDTH)
                .height(FIRST_HEIGHT)
                .createDate(DEFAULT_CREATE_DATE).build();

        restVehicleMvc.perform(post(API_PATH + "/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(vehicleDTOtoSave)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    public void createVehicleShouldThrowExceptionForMissingModelId() throws Exception {

        List<Vehicle> allVehicles = vehicleRepository.findAll();
        List<FuelType> allFuelTypes = fuelTypeRepository.findAll();
        List<VehicleModel> allVehicleModels = vehicleModelRepository.findAll();
        List<VehicleType> allVehicleTypes = vehicleTypeRepository.findAll();

        FuelType fuelType = allFuelTypes.get(0);
        VehicleModel vehicleModel = allVehicleModels.get(0);
        VehicleType vehicleType = allVehicleTypes.get(0);

        int sizeBeforeTest = allVehicles.size();

        VehicleDTO vehicleDTO = vehicleMapper.toDto(firstVehicle);
        VehicleDTO vehicleDTOtoSave = VehicleDTO.builder()
                .brandId(vehicleModel.getVehicleBrand().getId())
                .brandName(vehicleModel.getVehicleBrand().getName())
                .modelId(null)
                .modelName(vehicleModel.getName())
                .fuelTypeId(fuelType.getId())
                .fuelTypeName(fuelType.getName())
                .vehicleTypeId(vehicleType.getId())
                .vehicleTypeName(vehicleType.getName())
                .registrationNumber(FIST_REGISTRATION_NUMBER)
                .vin(FIRST_VIN)
                .productionYear(FIRST_PRODUCTION_YEAR)
                .firstRegistrationDate(FIRST_FIRST_REGISTRATION_DATE)
                .freePlacesForTechInspection(FIRST_FREE_PLACES_FOR_TECH_INSPECTION)
                .length(FIRST_LENGTH)
                .width(FIRST_WIDTH)
                .height(FIRST_HEIGHT)
                .createDate(DEFAULT_CREATE_DATE).build();

        restVehicleMvc.perform(post(API_PATH + "/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(vehicleDTOtoSave)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    public void createVehicleShouldThrowExceptionForMissingModelName() throws Exception {

        List<Vehicle> allVehicles = vehicleRepository.findAll();
        List<FuelType> allFuelTypes = fuelTypeRepository.findAll();
        List<VehicleModel> allVehicleModels = vehicleModelRepository.findAll();
        List<VehicleType> allVehicleTypes = vehicleTypeRepository.findAll();

        FuelType fuelType = allFuelTypes.get(0);
        VehicleModel vehicleModel = allVehicleModels.get(0);
        VehicleType vehicleType = allVehicleTypes.get(0);

        int sizeBeforeTest = allVehicles.size();

        VehicleDTO vehicleDTO = vehicleMapper.toDto(firstVehicle);
        VehicleDTO vehicleDTOtoSave = VehicleDTO.builder()
                .brandId(vehicleModel.getVehicleBrand().getId())
                .brandName(vehicleModel.getVehicleBrand().getName())
                .modelId(vehicleModel.getId())
                .modelName(null)
                .fuelTypeId(fuelType.getId())
                .fuelTypeName(fuelType.getName())
                .vehicleTypeId(vehicleType.getId())
                .vehicleTypeName(vehicleType.getName())
                .registrationNumber(FIST_REGISTRATION_NUMBER)
                .vin(FIRST_VIN)
                .productionYear(FIRST_PRODUCTION_YEAR)
                .firstRegistrationDate(FIRST_FIRST_REGISTRATION_DATE)
                .freePlacesForTechInspection(FIRST_FREE_PLACES_FOR_TECH_INSPECTION)
                .length(FIRST_LENGTH)
                .width(FIRST_WIDTH)
                .height(FIRST_HEIGHT)
                .createDate(DEFAULT_CREATE_DATE).build();

        restVehicleMvc.perform(post(API_PATH + "/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(vehicleDTOtoSave)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    public void createVehicleShouldThrowExceptionForMissingFuelTypeId() throws Exception {

        List<Vehicle> allVehicles = vehicleRepository.findAll();
        List<FuelType> allFuelTypes = fuelTypeRepository.findAll();
        List<VehicleModel> allVehicleModels = vehicleModelRepository.findAll();
        List<VehicleType> allVehicleTypes = vehicleTypeRepository.findAll();

        FuelType fuelType = allFuelTypes.get(0);
        VehicleModel vehicleModel = allVehicleModels.get(0);
        VehicleType vehicleType = allVehicleTypes.get(0);

        int sizeBeforeTest = allVehicles.size();

        VehicleDTO vehicleDTO = vehicleMapper.toDto(firstVehicle);
        VehicleDTO vehicleDTOtoSave = VehicleDTO.builder()
                .brandId(vehicleModel.getVehicleBrand().getId())
                .brandName(vehicleModel.getVehicleBrand().getName())
                .modelId(vehicleModel.getId())
                .modelName(vehicleModel.getName())
                .fuelTypeId(null)
                .fuelTypeName(fuelType.getName())
                .vehicleTypeId(vehicleType.getId())
                .vehicleTypeName(vehicleType.getName())
                .registrationNumber(FIST_REGISTRATION_NUMBER)
                .vin(FIRST_VIN)
                .productionYear(FIRST_PRODUCTION_YEAR)
                .firstRegistrationDate(FIRST_FIRST_REGISTRATION_DATE)
                .freePlacesForTechInspection(FIRST_FREE_PLACES_FOR_TECH_INSPECTION)
                .length(FIRST_LENGTH)
                .width(FIRST_WIDTH)
                .height(FIRST_HEIGHT)
                .createDate(DEFAULT_CREATE_DATE).build();

        restVehicleMvc.perform(post(API_PATH + "/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(vehicleDTOtoSave)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    public void createVehicleShouldThrowExceptionForMissingFuelTypeName() throws Exception {

        List<Vehicle> allVehicles = vehicleRepository.findAll();
        List<FuelType> allFuelTypes = fuelTypeRepository.findAll();
        List<VehicleModel> allVehicleModels = vehicleModelRepository.findAll();
        List<VehicleType> allVehicleTypes = vehicleTypeRepository.findAll();

        FuelType fuelType = allFuelTypes.get(0);
        VehicleModel vehicleModel = allVehicleModels.get(0);
        VehicleType vehicleType = allVehicleTypes.get(0);

        int sizeBeforeTest = allVehicles.size();

        VehicleDTO vehicleDTO = vehicleMapper.toDto(firstVehicle);
        VehicleDTO vehicleDTOtoSave = VehicleDTO.builder()
                .brandId(vehicleModel.getVehicleBrand().getId())
                .brandName(vehicleModel.getVehicleBrand().getName())
                .modelId(vehicleModel.getId())
                .modelName(vehicleModel.getName())
                .fuelTypeId(fuelType.getId())
                .fuelTypeName(null)
                .vehicleTypeId(vehicleType.getId())
                .vehicleTypeName(vehicleType.getName())
                .registrationNumber(FIST_REGISTRATION_NUMBER)
                .vin(FIRST_VIN)
                .productionYear(FIRST_PRODUCTION_YEAR)
                .firstRegistrationDate(FIRST_FIRST_REGISTRATION_DATE)
                .freePlacesForTechInspection(FIRST_FREE_PLACES_FOR_TECH_INSPECTION)
                .length(FIRST_LENGTH)
                .width(FIRST_WIDTH)
                .height(FIRST_HEIGHT)
                .createDate(DEFAULT_CREATE_DATE).build();

        restVehicleMvc.perform(post(API_PATH + "/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(vehicleDTOtoSave)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    public void createVehicleShouldThrowExceptionForMissingVehicleTypeId() throws Exception {

        List<Vehicle> allVehicles = vehicleRepository.findAll();
        List<FuelType> allFuelTypes = fuelTypeRepository.findAll();
        List<VehicleModel> allVehicleModels = vehicleModelRepository.findAll();
        List<VehicleType> allVehicleTypes = vehicleTypeRepository.findAll();

        FuelType fuelType = allFuelTypes.get(0);
        VehicleModel vehicleModel = allVehicleModels.get(0);
        VehicleType vehicleType = allVehicleTypes.get(0);

        int sizeBeforeTest = allVehicles.size();

        VehicleDTO vehicleDTO = vehicleMapper.toDto(firstVehicle);
        VehicleDTO vehicleDTOtoSave = VehicleDTO.builder()
                .brandId(vehicleModel.getVehicleBrand().getId())
                .brandName(vehicleModel.getVehicleBrand().getName())
                .modelId(vehicleModel.getId())
                .modelName(vehicleModel.getName())
                .fuelTypeId(fuelType.getId())
                .fuelTypeName(fuelType.getName())
                .vehicleTypeId(null)
                .vehicleTypeName(vehicleType.getName())
                .registrationNumber(FIST_REGISTRATION_NUMBER)
                .vin(FIRST_VIN)
                .productionYear(FIRST_PRODUCTION_YEAR)
                .firstRegistrationDate(FIRST_FIRST_REGISTRATION_DATE)
                .freePlacesForTechInspection(FIRST_FREE_PLACES_FOR_TECH_INSPECTION)
                .length(FIRST_LENGTH)
                .width(FIRST_WIDTH)
                .height(FIRST_HEIGHT)
                .createDate(DEFAULT_CREATE_DATE).build();

        restVehicleMvc.perform(post(API_PATH + "/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(vehicleDTOtoSave)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    public void createVehicleShouldThrowExceptionForMissingVehicleTypeName() throws Exception {

        List<Vehicle> allVehicles = vehicleRepository.findAll();
        List<FuelType> allFuelTypes = fuelTypeRepository.findAll();
        List<VehicleModel> allVehicleModels = vehicleModelRepository.findAll();
        List<VehicleType> allVehicleTypes = vehicleTypeRepository.findAll();

        FuelType fuelType = allFuelTypes.get(0);
        VehicleModel vehicleModel = allVehicleModels.get(0);
        VehicleType vehicleType = allVehicleTypes.get(0);

        int sizeBeforeTest = allVehicles.size();

        VehicleDTO vehicleDTO = vehicleMapper.toDto(firstVehicle);
        VehicleDTO vehicleDTOtoSave = VehicleDTO.builder()
                .brandId(vehicleModel.getVehicleBrand().getId())
                .brandName(vehicleModel.getVehicleBrand().getName())
                .modelId(vehicleModel.getId())
                .modelName(vehicleModel.getName())
                .fuelTypeId(fuelType.getId())
                .fuelTypeName(fuelType.getName())
                .vehicleTypeId(vehicleType.getId())
                .vehicleTypeName(null)
                .registrationNumber(FIST_REGISTRATION_NUMBER)
                .vin(FIRST_VIN)
                .productionYear(FIRST_PRODUCTION_YEAR)
                .firstRegistrationDate(FIRST_FIRST_REGISTRATION_DATE)
                .freePlacesForTechInspection(FIRST_FREE_PLACES_FOR_TECH_INSPECTION)
                .length(FIRST_LENGTH)
                .width(FIRST_WIDTH)
                .height(FIRST_HEIGHT)
                .createDate(DEFAULT_CREATE_DATE).build();

        restVehicleMvc.perform(post(API_PATH + "/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(vehicleDTOtoSave)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    public void createVehicleShouldThrowExceptionForMissingRegistrationNumber() throws Exception {

        List<Vehicle> allVehicles = vehicleRepository.findAll();
        List<FuelType> allFuelTypes = fuelTypeRepository.findAll();
        List<VehicleModel> allVehicleModels = vehicleModelRepository.findAll();
        List<VehicleType> allVehicleTypes = vehicleTypeRepository.findAll();

        FuelType fuelType = allFuelTypes.get(0);
        VehicleModel vehicleModel = allVehicleModels.get(0);
        VehicleType vehicleType = allVehicleTypes.get(0);

        int sizeBeforeTest = allVehicles.size();

        VehicleDTO vehicleDTO = vehicleMapper.toDto(firstVehicle);
        VehicleDTO vehicleDTOtoSave = VehicleDTO.builder()
                .brandId(vehicleModel.getVehicleBrand().getId())
                .brandName(vehicleModel.getVehicleBrand().getName())
                .modelId(vehicleModel.getId())
                .modelName(vehicleModel.getName())
                .fuelTypeId(fuelType.getId())
                .fuelTypeName(fuelType.getName())
                .vehicleTypeId(vehicleType.getId())
                .vehicleTypeName(vehicleType.getName())
                .registrationNumber(null)
                .vin(FIRST_VIN)
                .productionYear(FIRST_PRODUCTION_YEAR)
                .firstRegistrationDate(FIRST_FIRST_REGISTRATION_DATE)
                .freePlacesForTechInspection(FIRST_FREE_PLACES_FOR_TECH_INSPECTION)
                .length(FIRST_LENGTH)
                .width(FIRST_WIDTH)
                .height(FIRST_HEIGHT)
                .createDate(DEFAULT_CREATE_DATE).build();

        restVehicleMvc.perform(post(API_PATH + "/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(vehicleDTOtoSave)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    public void createVehicleShouldThrowExceptionForMissingVIN() throws Exception {

        List<Vehicle> allVehicles = vehicleRepository.findAll();
        List<FuelType> allFuelTypes = fuelTypeRepository.findAll();
        List<VehicleModel> allVehicleModels = vehicleModelRepository.findAll();
        List<VehicleType> allVehicleTypes = vehicleTypeRepository.findAll();

        FuelType fuelType = allFuelTypes.get(0);
        VehicleModel vehicleModel = allVehicleModels.get(0);
        VehicleType vehicleType = allVehicleTypes.get(0);

        int sizeBeforeTest = allVehicles.size();

        VehicleDTO vehicleDTO = vehicleMapper.toDto(firstVehicle);
        VehicleDTO vehicleDTOtoSave = VehicleDTO.builder()
                .brandId(vehicleModel.getVehicleBrand().getId())
                .brandName(vehicleModel.getVehicleBrand().getName())
                .modelId(vehicleModel.getId())
                .modelName(vehicleModel.getName())
                .fuelTypeId(fuelType.getId())
                .fuelTypeName(fuelType.getName())
                .vehicleTypeId(vehicleType.getId())
                .vehicleTypeName(vehicleType.getName())
                .registrationNumber(FIST_REGISTRATION_NUMBER)
                .vin(null)
                .productionYear(FIRST_PRODUCTION_YEAR)
                .firstRegistrationDate(FIRST_FIRST_REGISTRATION_DATE)
                .freePlacesForTechInspection(FIRST_FREE_PLACES_FOR_TECH_INSPECTION)
                .length(FIRST_LENGTH)
                .width(FIRST_WIDTH)
                .height(FIRST_HEIGHT)
                .createDate(DEFAULT_CREATE_DATE).build();

        restVehicleMvc.perform(post(API_PATH + "/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(vehicleDTOtoSave)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    public void createVehicleShouldThrowExceptionForMissingProductionYear() throws Exception {

        List<Vehicle> allVehicles = vehicleRepository.findAll();
        List<FuelType> allFuelTypes = fuelTypeRepository.findAll();
        List<VehicleModel> allVehicleModels = vehicleModelRepository.findAll();
        List<VehicleType> allVehicleTypes = vehicleTypeRepository.findAll();

        FuelType fuelType = allFuelTypes.get(0);
        VehicleModel vehicleModel = allVehicleModels.get(0);
        VehicleType vehicleType = allVehicleTypes.get(0);

        int sizeBeforeTest = allVehicles.size();

        VehicleDTO vehicleDTO = vehicleMapper.toDto(firstVehicle);
        VehicleDTO vehicleDTOtoSave = VehicleDTO.builder()
                .brandId(vehicleModel.getVehicleBrand().getId())
                .brandName(vehicleModel.getVehicleBrand().getName())
                .modelId(vehicleModel.getId())
                .modelName(vehicleModel.getName())
                .fuelTypeId(fuelType.getId())
                .fuelTypeName(fuelType.getName())
                .vehicleTypeId(vehicleType.getId())
                .vehicleTypeName(vehicleType.getName())
                .registrationNumber(FIST_REGISTRATION_NUMBER)
                .vin(FIRST_VIN)
                .productionYear(null)
                .firstRegistrationDate(FIRST_FIRST_REGISTRATION_DATE)
                .freePlacesForTechInspection(FIRST_FREE_PLACES_FOR_TECH_INSPECTION)
                .length(FIRST_LENGTH)
                .width(FIRST_WIDTH)
                .height(FIRST_HEIGHT)
                .createDate(DEFAULT_CREATE_DATE).build();

        restVehicleMvc.perform(post(API_PATH + "/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(vehicleDTOtoSave)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    public void createVehicleShouldThrowExceptionForMissingRegistrationDate() throws Exception {

        List<Vehicle> allVehicles = vehicleRepository.findAll();
        List<FuelType> allFuelTypes = fuelTypeRepository.findAll();
        List<VehicleModel> allVehicleModels = vehicleModelRepository.findAll();
        List<VehicleType> allVehicleTypes = vehicleTypeRepository.findAll();

        FuelType fuelType = allFuelTypes.get(0);
        VehicleModel vehicleModel = allVehicleModels.get(0);
        VehicleType vehicleType = allVehicleTypes.get(0);

        int sizeBeforeTest = allVehicles.size();

        VehicleDTO vehicleDTO = vehicleMapper.toDto(firstVehicle);
        VehicleDTO vehicleDTOtoSave = VehicleDTO.builder()
                .brandId(vehicleModel.getVehicleBrand().getId())
                .brandName(vehicleModel.getVehicleBrand().getName())
                .modelId(vehicleModel.getId())
                .modelName(vehicleModel.getName())
                .fuelTypeId(fuelType.getId())
                .fuelTypeName(fuelType.getName())
                .vehicleTypeId(vehicleType.getId())
                .vehicleTypeName(vehicleType.getName())
                .registrationNumber(FIST_REGISTRATION_NUMBER)
                .vin(FIRST_VIN)
                .productionYear(FIRST_PRODUCTION_YEAR)
                .firstRegistrationDate(null)
                .freePlacesForTechInspection(FIRST_FREE_PLACES_FOR_TECH_INSPECTION)
                .length(FIRST_LENGTH)
                .width(FIRST_WIDTH)
                .height(FIRST_HEIGHT)
                .createDate(DEFAULT_CREATE_DATE).build();

        restVehicleMvc.perform(post(API_PATH + "/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(vehicleDTOtoSave)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    public void createVehicleShouldThrowExceptionForMissingFreePlacecForTechInspection() throws Exception {

        List<Vehicle> allVehicles = vehicleRepository.findAll();
        List<FuelType> allFuelTypes = fuelTypeRepository.findAll();
        List<VehicleModel> allVehicleModels = vehicleModelRepository.findAll();
        List<VehicleType> allVehicleTypes = vehicleTypeRepository.findAll();

        FuelType fuelType = allFuelTypes.get(0);
        VehicleModel vehicleModel = allVehicleModels.get(0);
        VehicleType vehicleType = allVehicleTypes.get(0);

        int sizeBeforeTest = allVehicles.size();

        VehicleDTO vehicleDTO = vehicleMapper.toDto(firstVehicle);
        VehicleDTO vehicleDTOtoSave = VehicleDTO.builder()
                .brandId(vehicleModel.getVehicleBrand().getId())
                .brandName(vehicleModel.getVehicleBrand().getName())
                .modelId(vehicleModel.getId())
                .modelName(vehicleModel.getName())
                .fuelTypeId(fuelType.getId())
                .fuelTypeName(fuelType.getName())
                .vehicleTypeId(vehicleType.getId())
                .vehicleTypeName(vehicleType.getName())
                .registrationNumber(FIST_REGISTRATION_NUMBER)
                .vin(FIRST_VIN)
                .productionYear(FIRST_PRODUCTION_YEAR)
                .firstRegistrationDate(FIRST_FIRST_REGISTRATION_DATE)
                .freePlacesForTechInspection(null)
                .length(FIRST_LENGTH)
                .width(FIRST_WIDTH)
                .height(FIRST_HEIGHT)
                .createDate(DEFAULT_CREATE_DATE).build();

        restVehicleMvc.perform(post(API_PATH + "/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(vehicleDTOtoSave)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    public void createVehicleShouldThrowExceptionForMissingLength() throws Exception {

        List<Vehicle> allVehicles = vehicleRepository.findAll();
        List<FuelType> allFuelTypes = fuelTypeRepository.findAll();
        List<VehicleModel> allVehicleModels = vehicleModelRepository.findAll();
        List<VehicleType> allVehicleTypes = vehicleTypeRepository.findAll();

        FuelType fuelType = allFuelTypes.get(0);
        VehicleModel vehicleModel = allVehicleModels.get(0);
        VehicleType vehicleType = allVehicleTypes.get(0);

        int sizeBeforeTest = allVehicles.size();

        VehicleDTO vehicleDTO = vehicleMapper.toDto(firstVehicle);
        VehicleDTO vehicleDTOtoSave = VehicleDTO.builder()
                .brandId(vehicleModel.getVehicleBrand().getId())
                .brandName(vehicleModel.getVehicleBrand().getName())
                .modelId(vehicleModel.getId())
                .modelName(vehicleModel.getName())
                .fuelTypeId(fuelType.getId())
                .fuelTypeName(fuelType.getName())
                .vehicleTypeId(vehicleType.getId())
                .vehicleTypeName(vehicleType.getName())
                .registrationNumber(FIST_REGISTRATION_NUMBER)
                .vin(FIRST_VIN)
                .productionYear(FIRST_PRODUCTION_YEAR)
                .firstRegistrationDate(FIRST_FIRST_REGISTRATION_DATE)
                .freePlacesForTechInspection(FIRST_FREE_PLACES_FOR_TECH_INSPECTION)
                .length(null)
                .width(FIRST_WIDTH)
                .height(FIRST_HEIGHT)
                .createDate(DEFAULT_CREATE_DATE).build();

        restVehicleMvc.perform(post(API_PATH + "/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(vehicleDTOtoSave)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    public void createVehicleShouldThrowExceptionForMissingHeight() throws Exception {

        List<Vehicle> allVehicles = vehicleRepository.findAll();
        List<FuelType> allFuelTypes = fuelTypeRepository.findAll();
        List<VehicleModel> allVehicleModels = vehicleModelRepository.findAll();
        List<VehicleType> allVehicleTypes = vehicleTypeRepository.findAll();

        FuelType fuelType = allFuelTypes.get(0);
        VehicleModel vehicleModel = allVehicleModels.get(0);
        VehicleType vehicleType = allVehicleTypes.get(0);

        int sizeBeforeTest = allVehicles.size();

        VehicleDTO vehicleDTO = vehicleMapper.toDto(firstVehicle);
        VehicleDTO vehicleDTOtoSave = VehicleDTO.builder()
                .brandId(vehicleModel.getVehicleBrand().getId())
                .brandName(vehicleModel.getVehicleBrand().getName())
                .modelId(vehicleModel.getId())
                .modelName(vehicleModel.getName())
                .fuelTypeId(fuelType.getId())
                .fuelTypeName(fuelType.getName())
                .vehicleTypeId(vehicleType.getId())
                .vehicleTypeName(vehicleType.getName())
                .registrationNumber(FIST_REGISTRATION_NUMBER)
                .vin(FIRST_VIN)
                .productionYear(FIRST_PRODUCTION_YEAR)
                .firstRegistrationDate(FIRST_FIRST_REGISTRATION_DATE)
                .freePlacesForTechInspection(FIRST_FREE_PLACES_FOR_TECH_INSPECTION)
                .length(FIRST_LENGTH)
                .width(FIRST_WIDTH)
                .height(null)
                .createDate(DEFAULT_CREATE_DATE).build();

        restVehicleMvc.perform(post(API_PATH + "/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(vehicleDTOtoSave)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    public void createVehicleShouldThrowExceptionForMissingWidth() throws Exception {

        List<Vehicle> allVehicles = vehicleRepository.findAll();
        List<FuelType> allFuelTypes = fuelTypeRepository.findAll();
        List<VehicleModel> allVehicleModels = vehicleModelRepository.findAll();
        List<VehicleType> allVehicleTypes = vehicleTypeRepository.findAll();

        FuelType fuelType = allFuelTypes.get(0);
        VehicleModel vehicleModel = allVehicleModels.get(0);
        VehicleType vehicleType = allVehicleTypes.get(0);

        int sizeBeforeTest = allVehicles.size();

        VehicleDTO vehicleDTO = vehicleMapper.toDto(firstVehicle);
        VehicleDTO vehicleDTOtoSave = VehicleDTO.builder()
                .brandId(vehicleModel.getVehicleBrand().getId())
                .brandName(vehicleModel.getVehicleBrand().getName())
                .modelId(vehicleModel.getId())
                .modelName(vehicleModel.getName())
                .fuelTypeId(fuelType.getId())
                .fuelTypeName(fuelType.getName())
                .vehicleTypeId(vehicleType.getId())
                .vehicleTypeName(vehicleType.getName())
                .registrationNumber(FIST_REGISTRATION_NUMBER)
                .vin(FIRST_VIN)
                .productionYear(FIRST_PRODUCTION_YEAR)
                .firstRegistrationDate(FIRST_FIRST_REGISTRATION_DATE)
                .freePlacesForTechInspection(FIRST_FREE_PLACES_FOR_TECH_INSPECTION)
                .length(FIRST_LENGTH)
                .width(null)
                .height(FIRST_HEIGHT)
                .createDate(DEFAULT_CREATE_DATE).build();

        restVehicleMvc.perform(post(API_PATH + "/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(vehicleDTOtoSave)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    public void createVehicleShouldThrowBadRequestForNull() throws Exception {
        restVehicleMvc.perform(post(API_PATH + "/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(null))).andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    public void updateVehicle() throws Exception {

        List<FuelType> allFuelTypes = fuelTypeRepository.findAll();
        List<VehicleModel> allVehicleModels = vehicleModelRepository.findAll();
        List<VehicleType> allVehicleTypes = vehicleTypeRepository.findAll();
        List<VehicleBrand> allBrands = vehicleBrandRepository.findAll();

        FuelType fuelType = allFuelTypes.get(0);
        VehicleModel vehicleModel = allVehicleModels.get(0);
        VehicleType vehicleType = allVehicleTypes.get(0);


        VehicleDTO vehicleDTO = vehicleMapper.toDto(firstVehicle);
        VehicleDTO vehicleDTOtoSave = VehicleDTO.builder()
                .brandId(vehicleModel.getVehicleBrand().getId())
                .brandName(vehicleModel.getVehicleBrand().getName())
                .modelId(vehicleModel.getId())
                .modelName(vehicleModel.getName())
                .fuelTypeId(fuelType.getId())
                .fuelTypeName(fuelType.getName())
                .vehicleTypeId(vehicleType.getId())
                .vehicleTypeName(vehicleType.getName())
                .registrationNumber(FIST_REGISTRATION_NUMBER)
                .vin(FIRST_VIN)
                .productionYear(FIRST_PRODUCTION_YEAR)
                .firstRegistrationDate(FIRST_FIRST_REGISTRATION_DATE)
                .freePlacesForTechInspection(FIRST_FREE_PLACES_FOR_TECH_INSPECTION)
                .length(FIRST_LENGTH)
                .width(FIRST_WIDTH)
                .height(FIRST_HEIGHT)
                .createDate(DEFAULT_CREATE_DATE).build();
        VehicleDTO savedVehicle = vehicleService.save(vehicleDTOtoSave);

        List<Vehicle> allVehicles = vehicleRepository.findAll();
        int sizeBeforeTest = allVehicles.size();
        VehicleDTO vehicleDTOtoUpdate = VehicleDTO.builder()
                .id(savedVehicle.getId())
                .brandId(updatedVehicle.getVehicleModel().getVehicleBrand().getId())
                .brandName(updatedVehicle.getVehicleModel().getVehicleBrand().getName())
                .modelId(updatedVehicle.getVehicleModel().getId())
                .modelName(updatedVehicle.getVehicleModel().getName())
                .fuelTypeId(updatedVehicle.getFuelType().getId())
                .fuelTypeName(updatedVehicle.getFuelType().getName())
                .vehicleTypeId(updatedVehicle.getVehicleType().getId())
                .vehicleTypeName(updatedVehicle.getVehicleType().getName())
                .registrationNumber(updatedVehicle.getRegistrationNumber())
                .vin(updatedVehicle.getVin())
                .productionYear(updatedVehicle.getProductionYear())
                .firstRegistrationDate(updatedVehicle.getFirstRegistrationDate())
                .freePlacesForTechInspection(updatedVehicle.getFreePlacesForTechInspection())
                .length(updatedVehicle.getLength())
                .width(updatedVehicle.getWidth())
                .height(updatedVehicle.getHeight())
                .createDate(updatedVehicle.getCreateDate())
                .modifyDate(updatedVehicle.getModifyDate()).build();


        restVehicleMvc.perform(put(API_PATH + "/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(vehicleDTOtoUpdate)))
                .andExpect(status().isOk());

        List<Vehicle> listAfterTest = vehicleRepository.findAll();
        int sizeAfterTest = listAfterTest.size();
        Vehicle savedVehicleInDB = listAfterTest.get(0);
        assertThat(sizeAfterTest).isEqualTo(sizeBeforeTest);
        assertThat(savedVehicleInDB.getVehicleModel().getName()).isEqualTo(updatedVehicleModel.getName());
        assertThat(savedVehicleInDB.getVehicleModel().getVehicleBrand()
                .getName()).isEqualTo(updatedVehicleModel.getVehicleBrand()
                .getName());
        assertThat(savedVehicleInDB.getFuelType().getName()).isEqualTo(updatedFuelType.getName());
        assertThat(savedVehicleInDB.getVehicleType().getName()).isEqualTo(updatedVehicleType.getName());
        assertThat(savedVehicleInDB.getFirstRegistrationDate()).isEqualTo(UPDATED_FIRST_REGISTRATION_DATE);
        assertThat(savedVehicleInDB.getFreePlacesForTechInspection()).isEqualTo(UPDATED_FREE_PLACES_FOR_TECH_INSPECTION);
        assertThat(savedVehicleInDB.getRegistrationNumber()).isEqualTo(UPDATED_REGISTRATION_NUMBER);
        assertThat(savedVehicleInDB.getProductionYear()).isEqualTo(UPDATED_PRODUCTION_YEAR);
        assertThat(savedVehicleInDB.getHeight()).isEqualTo(UPDATED_HEIGHT);
        assertThat(savedVehicleInDB.getVin()).isEqualTo(UPDATED_VIN);
        assertThat(savedVehicleInDB.getWidth()).isEqualTo(UPDATED_WIDTH);
        assertThat(savedVehicleInDB.getLength()).isEqualTo(UPDATED_LENGTH);
    }

    @Test
    @Transactional
    public void updateVehicleShouldThrowBadRequestForMissingId() throws Exception {

        List<FuelType> allFuelTypes = fuelTypeRepository.findAll();
        List<VehicleModel> allVehicleModels = vehicleModelRepository.findAll();
        List<VehicleType> allVehicleTypes = vehicleTypeRepository.findAll();
        List<VehicleBrand> allBrands = vehicleBrandRepository.findAll();

        FuelType fuelType = allFuelTypes.get(0);
        VehicleModel vehicleModel = allVehicleModels.get(0);
        VehicleType vehicleType = allVehicleTypes.get(0);


        VehicleDTO vehicleDTO = vehicleMapper.toDto(firstVehicle);
        VehicleDTO vehicleDTOtoSave = VehicleDTO.builder()
                .brandId(vehicleModel.getVehicleBrand().getId())
                .brandName(vehicleModel.getVehicleBrand().getName())
                .modelId(vehicleModel.getId())
                .modelName(vehicleModel.getName())
                .fuelTypeId(fuelType.getId())
                .fuelTypeName(fuelType.getName())
                .vehicleTypeId(vehicleType.getId())
                .vehicleTypeName(vehicleType.getName())
                .registrationNumber(FIST_REGISTRATION_NUMBER)
                .vin(FIRST_VIN)
                .productionYear(FIRST_PRODUCTION_YEAR)
                .firstRegistrationDate(FIRST_FIRST_REGISTRATION_DATE)
                .freePlacesForTechInspection(FIRST_FREE_PLACES_FOR_TECH_INSPECTION)
                .length(FIRST_LENGTH)
                .width(FIRST_WIDTH)
                .height(FIRST_HEIGHT)
                .createDate(DEFAULT_CREATE_DATE).build();
        VehicleDTO savedVehicle = vehicleService.save(vehicleDTOtoSave);

        List<Vehicle> allVehicles = vehicleRepository.findAll();
        int sizeBeforeTest = allVehicles.size();
        VehicleDTO vehicleDTOtoUpdate = VehicleDTO.builder()
//                .id(savedVehicle.getId())
                .brandId(updatedVehicle.getVehicleModel().getVehicleBrand().getId())
                .brandName(updatedVehicle.getVehicleModel().getVehicleBrand().getName())
                .modelId(updatedVehicle.getVehicleModel().getId())
                .modelName(updatedVehicle.getVehicleModel().getName())
                .fuelTypeId(updatedVehicle.getFuelType().getId())
                .fuelTypeName(updatedVehicle.getFuelType().getName())
                .vehicleTypeId(updatedVehicle.getVehicleType().getId())
                .vehicleTypeName(updatedVehicle.getVehicleType().getName())
                .registrationNumber(updatedVehicle.getRegistrationNumber())
                .vin(updatedVehicle.getVin())
                .productionYear(updatedVehicle.getProductionYear())
                .firstRegistrationDate(updatedVehicle.getFirstRegistrationDate())
                .freePlacesForTechInspection(updatedVehicle.getFreePlacesForTechInspection())
                .length(updatedVehicle.getLength())
                .width(updatedVehicle.getWidth())
                .height(updatedVehicle.getHeight())
                .createDate(updatedVehicle.getCreateDate())
                .modifyDate(updatedVehicle.getModifyDate()).build();


        restVehicleMvc.perform(put(API_PATH + "/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(vehicleDTOtoUpdate)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    public void updateVehicleShouldThrowBadRequestForMissingBrandId() throws Exception {

        List<FuelType> allFuelTypes = fuelTypeRepository.findAll();
        List<VehicleModel> allVehicleModels = vehicleModelRepository.findAll();
        List<VehicleType> allVehicleTypes = vehicleTypeRepository.findAll();
        List<VehicleBrand> allBrands = vehicleBrandRepository.findAll();

        FuelType fuelType = allFuelTypes.get(0);
        VehicleModel vehicleModel = allVehicleModels.get(0);
        VehicleType vehicleType = allVehicleTypes.get(0);


        VehicleDTO vehicleDTO = vehicleMapper.toDto(firstVehicle);
        VehicleDTO vehicleDTOtoSave = VehicleDTO.builder()
                .brandId(vehicleModel.getVehicleBrand().getId())
                .brandName(vehicleModel.getVehicleBrand().getName())
                .modelId(vehicleModel.getId())
                .modelName(vehicleModel.getName())
                .fuelTypeId(fuelType.getId())
                .fuelTypeName(fuelType.getName())
                .vehicleTypeId(vehicleType.getId())
                .vehicleTypeName(vehicleType.getName())
                .registrationNumber(FIST_REGISTRATION_NUMBER)
                .vin(FIRST_VIN)
                .productionYear(FIRST_PRODUCTION_YEAR)
                .firstRegistrationDate(FIRST_FIRST_REGISTRATION_DATE)
                .freePlacesForTechInspection(FIRST_FREE_PLACES_FOR_TECH_INSPECTION)
                .length(FIRST_LENGTH)
                .width(FIRST_WIDTH)
                .height(FIRST_HEIGHT)
                .createDate(DEFAULT_CREATE_DATE).build();
        VehicleDTO savedVehicle = vehicleService.save(vehicleDTOtoSave);

        List<Vehicle> allVehicles = vehicleRepository.findAll();
        int sizeBeforeTest = allVehicles.size();
        VehicleDTO vehicleDTOtoUpdate = VehicleDTO.builder()
                .id(savedVehicle.getId())
//                .brandId(updatedVehicle.getVehicleModel().getVehicleBrand().getId())
                .brandName(updatedVehicle.getVehicleModel().getVehicleBrand().getName())
                .modelId(updatedVehicle.getVehicleModel().getId())
                .modelName(updatedVehicle.getVehicleModel().getName())
                .fuelTypeId(updatedVehicle.getFuelType().getId())
                .fuelTypeName(updatedVehicle.getFuelType().getName())
                .vehicleTypeId(updatedVehicle.getVehicleType().getId())
                .vehicleTypeName(updatedVehicle.getVehicleType().getName())
                .registrationNumber(updatedVehicle.getRegistrationNumber())
                .vin(updatedVehicle.getVin())
                .productionYear(updatedVehicle.getProductionYear())
                .firstRegistrationDate(updatedVehicle.getFirstRegistrationDate())
                .freePlacesForTechInspection(updatedVehicle.getFreePlacesForTechInspection())
                .length(updatedVehicle.getLength())
                .width(updatedVehicle.getWidth())
                .height(updatedVehicle.getHeight())
                .createDate(updatedVehicle.getCreateDate())
                .modifyDate(updatedVehicle.getModifyDate()).build();


        restVehicleMvc.perform(put(API_PATH + "/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(vehicleDTOtoUpdate)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    public void updateVehicleShouldThrowBadRequestForMissingBrandName() throws Exception {

        List<FuelType> allFuelTypes = fuelTypeRepository.findAll();
        List<VehicleModel> allVehicleModels = vehicleModelRepository.findAll();
        List<VehicleType> allVehicleTypes = vehicleTypeRepository.findAll();
        List<VehicleBrand> allBrands = vehicleBrandRepository.findAll();

        FuelType fuelType = allFuelTypes.get(0);
        VehicleModel vehicleModel = allVehicleModels.get(0);
        VehicleType vehicleType = allVehicleTypes.get(0);


        VehicleDTO vehicleDTO = vehicleMapper.toDto(firstVehicle);
        VehicleDTO vehicleDTOtoSave = VehicleDTO.builder()
                .brandId(vehicleModel.getVehicleBrand().getId())
                .brandName(vehicleModel.getVehicleBrand().getName())
                .modelId(vehicleModel.getId())
                .modelName(vehicleModel.getName())
                .fuelTypeId(fuelType.getId())
                .fuelTypeName(fuelType.getName())
                .vehicleTypeId(vehicleType.getId())
                .vehicleTypeName(vehicleType.getName())
                .registrationNumber(FIST_REGISTRATION_NUMBER)
                .vin(FIRST_VIN)
                .productionYear(FIRST_PRODUCTION_YEAR)
                .firstRegistrationDate(FIRST_FIRST_REGISTRATION_DATE)
                .freePlacesForTechInspection(FIRST_FREE_PLACES_FOR_TECH_INSPECTION)
                .length(FIRST_LENGTH)
                .width(FIRST_WIDTH)
                .height(FIRST_HEIGHT)
                .createDate(DEFAULT_CREATE_DATE).build();
        VehicleDTO savedVehicle = vehicleService.save(vehicleDTOtoSave);

        List<Vehicle> allVehicles = vehicleRepository.findAll();
        int sizeBeforeTest = allVehicles.size();
        VehicleDTO vehicleDTOtoUpdate = VehicleDTO.builder()
                .id(savedVehicle.getId())
                .brandId(updatedVehicle.getVehicleModel().getVehicleBrand().getId())
//                .brandName(updatedVehicle.getVehicleModel().getVehicleBrand().getName())
                .modelId(updatedVehicle.getVehicleModel().getId())
                .modelName(updatedVehicle.getVehicleModel().getName())
                .fuelTypeId(updatedVehicle.getFuelType().getId())
                .fuelTypeName(updatedVehicle.getFuelType().getName())
                .vehicleTypeId(updatedVehicle.getVehicleType().getId())
                .vehicleTypeName(updatedVehicle.getVehicleType().getName())
                .registrationNumber(updatedVehicle.getRegistrationNumber())
                .vin(updatedVehicle.getVin())
                .productionYear(updatedVehicle.getProductionYear())
                .firstRegistrationDate(updatedVehicle.getFirstRegistrationDate())
                .freePlacesForTechInspection(updatedVehicle.getFreePlacesForTechInspection())
                .length(updatedVehicle.getLength())
                .width(updatedVehicle.getWidth())
                .height(updatedVehicle.getHeight())
                .createDate(updatedVehicle.getCreateDate())
                .modifyDate(updatedVehicle.getModifyDate()).build();


        restVehicleMvc.perform(put(API_PATH + "/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(vehicleDTOtoUpdate)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    public void updateVehicleShouldThrowBadRequestForMissingModelId() throws Exception {

        List<FuelType> allFuelTypes = fuelTypeRepository.findAll();
        List<VehicleModel> allVehicleModels = vehicleModelRepository.findAll();
        List<VehicleType> allVehicleTypes = vehicleTypeRepository.findAll();
        List<VehicleBrand> allBrands = vehicleBrandRepository.findAll();

        FuelType fuelType = allFuelTypes.get(0);
        VehicleModel vehicleModel = allVehicleModels.get(0);
        VehicleType vehicleType = allVehicleTypes.get(0);


        VehicleDTO vehicleDTO = vehicleMapper.toDto(firstVehicle);
        VehicleDTO vehicleDTOtoSave = VehicleDTO.builder()
                .brandId(vehicleModel.getVehicleBrand().getId())
                .brandName(vehicleModel.getVehicleBrand().getName())
                .modelId(vehicleModel.getId())
                .modelName(vehicleModel.getName())
                .fuelTypeId(fuelType.getId())
                .fuelTypeName(fuelType.getName())
                .vehicleTypeId(vehicleType.getId())
                .vehicleTypeName(vehicleType.getName())
                .registrationNumber(FIST_REGISTRATION_NUMBER)
                .vin(FIRST_VIN)
                .productionYear(FIRST_PRODUCTION_YEAR)
                .firstRegistrationDate(FIRST_FIRST_REGISTRATION_DATE)
                .freePlacesForTechInspection(FIRST_FREE_PLACES_FOR_TECH_INSPECTION)
                .length(FIRST_LENGTH)
                .width(FIRST_WIDTH)
                .height(FIRST_HEIGHT)
                .createDate(DEFAULT_CREATE_DATE).build();
        VehicleDTO savedVehicle = vehicleService.save(vehicleDTOtoSave);

        List<Vehicle> allVehicles = vehicleRepository.findAll();
        int sizeBeforeTest = allVehicles.size();
        VehicleDTO vehicleDTOtoUpdate = VehicleDTO.builder()
                .id(savedVehicle.getId())
                .brandId(updatedVehicle.getVehicleModel().getVehicleBrand().getId())
                .brandName(updatedVehicle.getVehicleModel().getVehicleBrand().getName())
//                .modelId(updatedVehicle.getVehicleModel().getId())
                .modelName(updatedVehicle.getVehicleModel().getName())
                .fuelTypeId(updatedVehicle.getFuelType().getId())
                .fuelTypeName(updatedVehicle.getFuelType().getName())
                .vehicleTypeId(updatedVehicle.getVehicleType().getId())
                .vehicleTypeName(updatedVehicle.getVehicleType().getName())
                .registrationNumber(updatedVehicle.getRegistrationNumber())
                .vin(updatedVehicle.getVin())
                .productionYear(updatedVehicle.getProductionYear())
                .firstRegistrationDate(updatedVehicle.getFirstRegistrationDate())
                .freePlacesForTechInspection(updatedVehicle.getFreePlacesForTechInspection())
                .length(updatedVehicle.getLength())
                .width(updatedVehicle.getWidth())
                .height(updatedVehicle.getHeight())
                .createDate(updatedVehicle.getCreateDate())
                .modifyDate(updatedVehicle.getModifyDate()).build();


        restVehicleMvc.perform(put(API_PATH + "/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(vehicleDTOtoUpdate)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    public void updateVehicleShouldThrowBadRequestForMissingModelName() throws Exception {

        List<FuelType> allFuelTypes = fuelTypeRepository.findAll();
        List<VehicleModel> allVehicleModels = vehicleModelRepository.findAll();
        List<VehicleType> allVehicleTypes = vehicleTypeRepository.findAll();
        List<VehicleBrand> allBrands = vehicleBrandRepository.findAll();

        FuelType fuelType = allFuelTypes.get(0);
        VehicleModel vehicleModel = allVehicleModels.get(0);
        VehicleType vehicleType = allVehicleTypes.get(0);


        VehicleDTO vehicleDTO = vehicleMapper.toDto(firstVehicle);
        VehicleDTO vehicleDTOtoSave = VehicleDTO.builder()
                .brandId(vehicleModel.getVehicleBrand().getId())
                .brandName(vehicleModel.getVehicleBrand().getName())
                .modelId(vehicleModel.getId())
                .modelName(vehicleModel.getName())
                .fuelTypeId(fuelType.getId())
                .fuelTypeName(fuelType.getName())
                .vehicleTypeId(vehicleType.getId())
                .vehicleTypeName(vehicleType.getName())
                .registrationNumber(FIST_REGISTRATION_NUMBER)
                .vin(FIRST_VIN)
                .productionYear(FIRST_PRODUCTION_YEAR)
                .firstRegistrationDate(FIRST_FIRST_REGISTRATION_DATE)
                .freePlacesForTechInspection(FIRST_FREE_PLACES_FOR_TECH_INSPECTION)
                .length(FIRST_LENGTH)
                .width(FIRST_WIDTH)
                .height(FIRST_HEIGHT)
                .createDate(DEFAULT_CREATE_DATE).build();
        VehicleDTO savedVehicle = vehicleService.save(vehicleDTOtoSave);

        List<Vehicle> allVehicles = vehicleRepository.findAll();
        int sizeBeforeTest = allVehicles.size();
        VehicleDTO vehicleDTOtoUpdate = VehicleDTO.builder()
                .id(savedVehicle.getId())
                .brandId(updatedVehicle.getVehicleModel().getVehicleBrand().getId())
                .brandName(updatedVehicle.getVehicleModel().getVehicleBrand().getName())
                .modelId(updatedVehicle.getVehicleModel().getId())
//                .modelName(updatedVehicle.getVehicleModel().getName())
                .fuelTypeId(updatedVehicle.getFuelType().getId())
                .fuelTypeName(updatedVehicle.getFuelType().getName())
                .vehicleTypeId(updatedVehicle.getVehicleType().getId())
                .vehicleTypeName(updatedVehicle.getVehicleType().getName())
                .registrationNumber(updatedVehicle.getRegistrationNumber())
                .vin(updatedVehicle.getVin())
                .productionYear(updatedVehicle.getProductionYear())
                .firstRegistrationDate(updatedVehicle.getFirstRegistrationDate())
                .freePlacesForTechInspection(updatedVehicle.getFreePlacesForTechInspection())
                .length(updatedVehicle.getLength())
                .width(updatedVehicle.getWidth())
                .height(updatedVehicle.getHeight())
                .createDate(updatedVehicle.getCreateDate())
                .modifyDate(updatedVehicle.getModifyDate()).build();


        restVehicleMvc.perform(put(API_PATH + "/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(vehicleDTOtoUpdate)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    public void updateVehicleShouldThrowBadRequestForMissingFuelTypeId() throws Exception {

        List<FuelType> allFuelTypes = fuelTypeRepository.findAll();
        List<VehicleModel> allVehicleModels = vehicleModelRepository.findAll();
        List<VehicleType> allVehicleTypes = vehicleTypeRepository.findAll();
        List<VehicleBrand> allBrands = vehicleBrandRepository.findAll();

        FuelType fuelType = allFuelTypes.get(0);
        VehicleModel vehicleModel = allVehicleModels.get(0);
        VehicleType vehicleType = allVehicleTypes.get(0);


        VehicleDTO vehicleDTO = vehicleMapper.toDto(firstVehicle);
        VehicleDTO vehicleDTOtoSave = VehicleDTO.builder()
                .brandId(vehicleModel.getVehicleBrand().getId())
                .brandName(vehicleModel.getVehicleBrand().getName())
                .modelId(vehicleModel.getId())
                .modelName(vehicleModel.getName())
                .fuelTypeId(fuelType.getId())
                .fuelTypeName(fuelType.getName())
                .vehicleTypeId(vehicleType.getId())
                .vehicleTypeName(vehicleType.getName())
                .registrationNumber(FIST_REGISTRATION_NUMBER)
                .vin(FIRST_VIN)
                .productionYear(FIRST_PRODUCTION_YEAR)
                .firstRegistrationDate(FIRST_FIRST_REGISTRATION_DATE)
                .freePlacesForTechInspection(FIRST_FREE_PLACES_FOR_TECH_INSPECTION)
                .length(FIRST_LENGTH)
                .width(FIRST_WIDTH)
                .height(FIRST_HEIGHT)
                .createDate(DEFAULT_CREATE_DATE).build();
        VehicleDTO savedVehicle = vehicleService.save(vehicleDTOtoSave);

        List<Vehicle> allVehicles = vehicleRepository.findAll();
        int sizeBeforeTest = allVehicles.size();
        VehicleDTO vehicleDTOtoUpdate = VehicleDTO.builder()
                .id(savedVehicle.getId())
                .brandId(updatedVehicle.getVehicleModel().getVehicleBrand().getId())
                .brandName(updatedVehicle.getVehicleModel().getVehicleBrand().getName())
                .modelId(updatedVehicle.getVehicleModel().getId())
                .modelName(updatedVehicle.getVehicleModel().getName())
//                .fuelTypeId(updatedVehicle.getFuelType().getId())
                .fuelTypeName(updatedVehicle.getFuelType().getName())
                .vehicleTypeId(updatedVehicle.getVehicleType().getId())
                .vehicleTypeName(updatedVehicle.getVehicleType().getName())
                .registrationNumber(updatedVehicle.getRegistrationNumber())
                .vin(updatedVehicle.getVin())
                .productionYear(updatedVehicle.getProductionYear())
                .firstRegistrationDate(updatedVehicle.getFirstRegistrationDate())
                .freePlacesForTechInspection(updatedVehicle.getFreePlacesForTechInspection())
                .length(updatedVehicle.getLength())
                .width(updatedVehicle.getWidth())
                .height(updatedVehicle.getHeight())
                .createDate(updatedVehicle.getCreateDate())
                .modifyDate(updatedVehicle.getModifyDate()).build();


        restVehicleMvc.perform(put(API_PATH + "/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(vehicleDTOtoUpdate)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    public void updateVehicleShouldThrowBadRequestForMissingFuelTypeName() throws Exception {

        List<FuelType> allFuelTypes = fuelTypeRepository.findAll();
        List<VehicleModel> allVehicleModels = vehicleModelRepository.findAll();
        List<VehicleType> allVehicleTypes = vehicleTypeRepository.findAll();
        List<VehicleBrand> allBrands = vehicleBrandRepository.findAll();

        FuelType fuelType = allFuelTypes.get(0);
        VehicleModel vehicleModel = allVehicleModels.get(0);
        VehicleType vehicleType = allVehicleTypes.get(0);


        VehicleDTO vehicleDTO = vehicleMapper.toDto(firstVehicle);
        VehicleDTO vehicleDTOtoSave = VehicleDTO.builder()
                .brandId(vehicleModel.getVehicleBrand().getId())
                .brandName(vehicleModel.getVehicleBrand().getName())
                .modelId(vehicleModel.getId())
                .modelName(vehicleModel.getName())
                .fuelTypeId(fuelType.getId())
                .fuelTypeName(fuelType.getName())
                .vehicleTypeId(vehicleType.getId())
                .vehicleTypeName(vehicleType.getName())
                .registrationNumber(FIST_REGISTRATION_NUMBER)
                .vin(FIRST_VIN)
                .productionYear(FIRST_PRODUCTION_YEAR)
                .firstRegistrationDate(FIRST_FIRST_REGISTRATION_DATE)
                .freePlacesForTechInspection(FIRST_FREE_PLACES_FOR_TECH_INSPECTION)
                .length(FIRST_LENGTH)
                .width(FIRST_WIDTH)
                .height(FIRST_HEIGHT)
                .createDate(DEFAULT_CREATE_DATE).build();
        VehicleDTO savedVehicle = vehicleService.save(vehicleDTOtoSave);

        List<Vehicle> allVehicles = vehicleRepository.findAll();
        int sizeBeforeTest = allVehicles.size();
        VehicleDTO vehicleDTOtoUpdate = VehicleDTO.builder()
                .id(savedVehicle.getId())
                .brandId(updatedVehicle.getVehicleModel().getVehicleBrand().getId())
                .brandName(updatedVehicle.getVehicleModel().getVehicleBrand().getName())
                .modelId(updatedVehicle.getVehicleModel().getId())
                .modelName(updatedVehicle.getVehicleModel().getName())
                .fuelTypeId(updatedVehicle.getFuelType().getId())
//                .fuelTypeName(updatedVehicle.getFuelType().getName())
                .vehicleTypeId(updatedVehicle.getVehicleType().getId())
                .vehicleTypeName(updatedVehicle.getVehicleType().getName())
                .registrationNumber(updatedVehicle.getRegistrationNumber())
                .vin(updatedVehicle.getVin())
                .productionYear(updatedVehicle.getProductionYear())
                .firstRegistrationDate(updatedVehicle.getFirstRegistrationDate())
                .freePlacesForTechInspection(updatedVehicle.getFreePlacesForTechInspection())
                .length(updatedVehicle.getLength())
                .width(updatedVehicle.getWidth())
                .height(updatedVehicle.getHeight())
                .createDate(updatedVehicle.getCreateDate())
                .modifyDate(updatedVehicle.getModifyDate()).build();


        restVehicleMvc.perform(put(API_PATH + "/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(vehicleDTOtoUpdate)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    public void updateVehicleShouldThrowBadRequestForMissingVehicleTypeId() throws Exception {

        List<FuelType> allFuelTypes = fuelTypeRepository.findAll();
        List<VehicleModel> allVehicleModels = vehicleModelRepository.findAll();
        List<VehicleType> allVehicleTypes = vehicleTypeRepository.findAll();
        List<VehicleBrand> allBrands = vehicleBrandRepository.findAll();

        FuelType fuelType = allFuelTypes.get(0);
        VehicleModel vehicleModel = allVehicleModels.get(0);
        VehicleType vehicleType = allVehicleTypes.get(0);


        VehicleDTO vehicleDTO = vehicleMapper.toDto(firstVehicle);
        VehicleDTO vehicleDTOtoSave = VehicleDTO.builder()
                .brandId(vehicleModel.getVehicleBrand().getId())
                .brandName(vehicleModel.getVehicleBrand().getName())
                .modelId(vehicleModel.getId())
                .modelName(vehicleModel.getName())
                .fuelTypeId(fuelType.getId())
                .fuelTypeName(fuelType.getName())
                .vehicleTypeId(vehicleType.getId())
                .vehicleTypeName(vehicleType.getName())
                .registrationNumber(FIST_REGISTRATION_NUMBER)
                .vin(FIRST_VIN)
                .productionYear(FIRST_PRODUCTION_YEAR)
                .firstRegistrationDate(FIRST_FIRST_REGISTRATION_DATE)
                .freePlacesForTechInspection(FIRST_FREE_PLACES_FOR_TECH_INSPECTION)
                .length(FIRST_LENGTH)
                .width(FIRST_WIDTH)
                .height(FIRST_HEIGHT)
                .createDate(DEFAULT_CREATE_DATE).build();
        VehicleDTO savedVehicle = vehicleService.save(vehicleDTOtoSave);

        List<Vehicle> allVehicles = vehicleRepository.findAll();
        int sizeBeforeTest = allVehicles.size();
        VehicleDTO vehicleDTOtoUpdate = VehicleDTO.builder()
                .id(savedVehicle.getId())
                .brandId(updatedVehicle.getVehicleModel().getVehicleBrand().getId())
                .brandName(updatedVehicle.getVehicleModel().getVehicleBrand().getName())
                .modelId(updatedVehicle.getVehicleModel().getId())
                .modelName(updatedVehicle.getVehicleModel().getName())
                .fuelTypeId(updatedVehicle.getFuelType().getId())
                .fuelTypeName(updatedVehicle.getFuelType().getName())
//                .vehicleTypeId(updatedVehicle.getVehicleType().getId())
                .vehicleTypeName(updatedVehicle.getVehicleType().getName())
                .registrationNumber(updatedVehicle.getRegistrationNumber())
                .vin(updatedVehicle.getVin())
                .productionYear(updatedVehicle.getProductionYear())
                .firstRegistrationDate(updatedVehicle.getFirstRegistrationDate())
                .freePlacesForTechInspection(updatedVehicle.getFreePlacesForTechInspection())
                .length(updatedVehicle.getLength())
                .width(updatedVehicle.getWidth())
                .height(updatedVehicle.getHeight())
                .createDate(updatedVehicle.getCreateDate())
                .modifyDate(updatedVehicle.getModifyDate()).build();


        restVehicleMvc.perform(put(API_PATH + "/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(vehicleDTOtoUpdate)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    public void updateVehicleShouldThrowBadRequestForMissingVehicleTypeName() throws Exception {

        List<FuelType> allFuelTypes = fuelTypeRepository.findAll();
        List<VehicleModel> allVehicleModels = vehicleModelRepository.findAll();
        List<VehicleType> allVehicleTypes = vehicleTypeRepository.findAll();
        List<VehicleBrand> allBrands = vehicleBrandRepository.findAll();

        FuelType fuelType = allFuelTypes.get(0);
        VehicleModel vehicleModel = allVehicleModels.get(0);
        VehicleType vehicleType = allVehicleTypes.get(0);


        VehicleDTO vehicleDTO = vehicleMapper.toDto(firstVehicle);
        VehicleDTO vehicleDTOtoSave = VehicleDTO.builder()
                .brandId(vehicleModel.getVehicleBrand().getId())
                .brandName(vehicleModel.getVehicleBrand().getName())
                .modelId(vehicleModel.getId())
                .modelName(vehicleModel.getName())
                .fuelTypeId(fuelType.getId())
                .fuelTypeName(fuelType.getName())
                .vehicleTypeId(vehicleType.getId())
                .vehicleTypeName(vehicleType.getName())
                .registrationNumber(FIST_REGISTRATION_NUMBER)
                .vin(FIRST_VIN)
                .productionYear(FIRST_PRODUCTION_YEAR)
                .firstRegistrationDate(FIRST_FIRST_REGISTRATION_DATE)
                .freePlacesForTechInspection(FIRST_FREE_PLACES_FOR_TECH_INSPECTION)
                .length(FIRST_LENGTH)
                .width(FIRST_WIDTH)
                .height(FIRST_HEIGHT)
                .createDate(DEFAULT_CREATE_DATE).build();
        VehicleDTO savedVehicle = vehicleService.save(vehicleDTOtoSave);

        List<Vehicle> allVehicles = vehicleRepository.findAll();
        int sizeBeforeTest = allVehicles.size();
        VehicleDTO vehicleDTOtoUpdate = VehicleDTO.builder()
                .id(savedVehicle.getId())
                .brandId(updatedVehicle.getVehicleModel().getVehicleBrand().getId())
                .brandName(updatedVehicle.getVehicleModel().getVehicleBrand().getName())
                .modelId(updatedVehicle.getVehicleModel().getId())
                .modelName(updatedVehicle.getVehicleModel().getName())
                .fuelTypeId(updatedVehicle.getFuelType().getId())
                .fuelTypeName(updatedVehicle.getFuelType().getName())
                .vehicleTypeId(updatedVehicle.getVehicleType().getId())
//                .vehicleTypeName(updatedVehicle.getVehicleType().getName())
                .registrationNumber(updatedVehicle.getRegistrationNumber())
                .vin(updatedVehicle.getVin())
                .productionYear(updatedVehicle.getProductionYear())
                .firstRegistrationDate(updatedVehicle.getFirstRegistrationDate())
                .freePlacesForTechInspection(updatedVehicle.getFreePlacesForTechInspection())
                .length(updatedVehicle.getLength())
                .width(updatedVehicle.getWidth())
                .height(updatedVehicle.getHeight())
                .createDate(updatedVehicle.getCreateDate())
                .modifyDate(updatedVehicle.getModifyDate()).build();


        restVehicleMvc.perform(put(API_PATH + "/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(vehicleDTOtoUpdate)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    public void updateVehicleShouldThrowBadRequestForMissingRegistrationNumber() throws Exception {

        List<FuelType> allFuelTypes = fuelTypeRepository.findAll();
        List<VehicleModel> allVehicleModels = vehicleModelRepository.findAll();
        List<VehicleType> allVehicleTypes = vehicleTypeRepository.findAll();
        List<VehicleBrand> allBrands = vehicleBrandRepository.findAll();

        FuelType fuelType = allFuelTypes.get(0);
        VehicleModel vehicleModel = allVehicleModels.get(0);
        VehicleType vehicleType = allVehicleTypes.get(0);


        VehicleDTO vehicleDTO = vehicleMapper.toDto(firstVehicle);
        VehicleDTO vehicleDTOtoSave = VehicleDTO.builder()
                .brandId(vehicleModel.getVehicleBrand().getId())
                .brandName(vehicleModel.getVehicleBrand().getName())
                .modelId(vehicleModel.getId())
                .modelName(vehicleModel.getName())
                .fuelTypeId(fuelType.getId())
                .fuelTypeName(fuelType.getName())
                .vehicleTypeId(vehicleType.getId())
                .vehicleTypeName(vehicleType.getName())
                .registrationNumber(FIST_REGISTRATION_NUMBER)
                .vin(FIRST_VIN)
                .productionYear(FIRST_PRODUCTION_YEAR)
                .firstRegistrationDate(FIRST_FIRST_REGISTRATION_DATE)
                .freePlacesForTechInspection(FIRST_FREE_PLACES_FOR_TECH_INSPECTION)
                .length(FIRST_LENGTH)
                .width(FIRST_WIDTH)
                .height(FIRST_HEIGHT)
                .createDate(DEFAULT_CREATE_DATE).build();
        VehicleDTO savedVehicle = vehicleService.save(vehicleDTOtoSave);

        List<Vehicle> allVehicles = vehicleRepository.findAll();
        int sizeBeforeTest = allVehicles.size();
        VehicleDTO vehicleDTOtoUpdate = VehicleDTO.builder()
                .id(savedVehicle.getId())
                .brandId(updatedVehicle.getVehicleModel().getVehicleBrand().getId())
                .brandName(updatedVehicle.getVehicleModel().getVehicleBrand().getName())
                .modelId(updatedVehicle.getVehicleModel().getId())
                .modelName(updatedVehicle.getVehicleModel().getName())
                .fuelTypeId(updatedVehicle.getFuelType().getId())
                .fuelTypeName(updatedVehicle.getFuelType().getName())
                .vehicleTypeId(updatedVehicle.getVehicleType().getId())
                .vehicleTypeName(updatedVehicle.getVehicleType().getName())
//                .registrationNumber(updatedVehicle.getRegistrationNumber())
                .vin(updatedVehicle.getVin())
                .productionYear(updatedVehicle.getProductionYear())
                .firstRegistrationDate(updatedVehicle.getFirstRegistrationDate())
                .freePlacesForTechInspection(updatedVehicle.getFreePlacesForTechInspection())
                .length(updatedVehicle.getLength())
                .width(updatedVehicle.getWidth())
                .height(updatedVehicle.getHeight())
                .createDate(updatedVehicle.getCreateDate())
                .modifyDate(updatedVehicle.getModifyDate()).build();


        restVehicleMvc.perform(put(API_PATH + "/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(vehicleDTOtoUpdate)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    public void updateVehicleShouldThrowBadRequestForMissingVin() throws Exception {

        List<FuelType> allFuelTypes = fuelTypeRepository.findAll();
        List<VehicleModel> allVehicleModels = vehicleModelRepository.findAll();
        List<VehicleType> allVehicleTypes = vehicleTypeRepository.findAll();
        List<VehicleBrand> allBrands = vehicleBrandRepository.findAll();

        FuelType fuelType = allFuelTypes.get(0);
        VehicleModel vehicleModel = allVehicleModels.get(0);
        VehicleType vehicleType = allVehicleTypes.get(0);


        VehicleDTO vehicleDTO = vehicleMapper.toDto(firstVehicle);
        VehicleDTO vehicleDTOtoSave = VehicleDTO.builder()
                .brandId(vehicleModel.getVehicleBrand().getId())
                .brandName(vehicleModel.getVehicleBrand().getName())
                .modelId(vehicleModel.getId())
                .modelName(vehicleModel.getName())
                .fuelTypeId(fuelType.getId())
                .fuelTypeName(fuelType.getName())
                .vehicleTypeId(vehicleType.getId())
                .vehicleTypeName(vehicleType.getName())
                .registrationNumber(FIST_REGISTRATION_NUMBER)
                .vin(FIRST_VIN)
                .productionYear(FIRST_PRODUCTION_YEAR)
                .firstRegistrationDate(FIRST_FIRST_REGISTRATION_DATE)
                .freePlacesForTechInspection(FIRST_FREE_PLACES_FOR_TECH_INSPECTION)
                .length(FIRST_LENGTH)
                .width(FIRST_WIDTH)
                .height(FIRST_HEIGHT)
                .createDate(DEFAULT_CREATE_DATE).build();
        VehicleDTO savedVehicle = vehicleService.save(vehicleDTOtoSave);

        List<Vehicle> allVehicles = vehicleRepository.findAll();
        int sizeBeforeTest = allVehicles.size();
        VehicleDTO vehicleDTOtoUpdate = VehicleDTO.builder()
                .id(savedVehicle.getId())
                .brandId(updatedVehicle.getVehicleModel().getVehicleBrand().getId())
                .brandName(updatedVehicle.getVehicleModel().getVehicleBrand().getName())
                .modelId(updatedVehicle.getVehicleModel().getId())
                .modelName(updatedVehicle.getVehicleModel().getName())
                .fuelTypeId(updatedVehicle.getFuelType().getId())
                .fuelTypeName(updatedVehicle.getFuelType().getName())
                .vehicleTypeId(updatedVehicle.getVehicleType().getId())
                .vehicleTypeName(updatedVehicle.getVehicleType().getName())
                .registrationNumber(updatedVehicle.getRegistrationNumber())
//                .vin(updatedVehicle.getVin())
                .productionYear(updatedVehicle.getProductionYear())
                .firstRegistrationDate(updatedVehicle.getFirstRegistrationDate())
                .freePlacesForTechInspection(updatedVehicle.getFreePlacesForTechInspection())
                .length(updatedVehicle.getLength())
                .width(updatedVehicle.getWidth())
                .height(updatedVehicle.getHeight())
                .createDate(updatedVehicle.getCreateDate())
                .modifyDate(updatedVehicle.getModifyDate()).build();


        restVehicleMvc.perform(put(API_PATH + "/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(vehicleDTOtoUpdate)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    public void updateVehicleShouldThrowBadRequestForMissingProductionYear() throws Exception {

        List<FuelType> allFuelTypes = fuelTypeRepository.findAll();
        List<VehicleModel> allVehicleModels = vehicleModelRepository.findAll();
        List<VehicleType> allVehicleTypes = vehicleTypeRepository.findAll();
        List<VehicleBrand> allBrands = vehicleBrandRepository.findAll();

        FuelType fuelType = allFuelTypes.get(0);
        VehicleModel vehicleModel = allVehicleModels.get(0);
        VehicleType vehicleType = allVehicleTypes.get(0);


        VehicleDTO vehicleDTO = vehicleMapper.toDto(firstVehicle);
        VehicleDTO vehicleDTOtoSave = VehicleDTO.builder()
                .brandId(vehicleModel.getVehicleBrand().getId())
                .brandName(vehicleModel.getVehicleBrand().getName())
                .modelId(vehicleModel.getId())
                .modelName(vehicleModel.getName())
                .fuelTypeId(fuelType.getId())
                .fuelTypeName(fuelType.getName())
                .vehicleTypeId(vehicleType.getId())
                .vehicleTypeName(vehicleType.getName())
                .registrationNumber(FIST_REGISTRATION_NUMBER)
                .vin(FIRST_VIN)
                .productionYear(FIRST_PRODUCTION_YEAR)
                .firstRegistrationDate(FIRST_FIRST_REGISTRATION_DATE)
                .freePlacesForTechInspection(FIRST_FREE_PLACES_FOR_TECH_INSPECTION)
                .length(FIRST_LENGTH)
                .width(FIRST_WIDTH)
                .height(FIRST_HEIGHT)
                .createDate(DEFAULT_CREATE_DATE).build();
        VehicleDTO savedVehicle = vehicleService.save(vehicleDTOtoSave);

        List<Vehicle> allVehicles = vehicleRepository.findAll();
        int sizeBeforeTest = allVehicles.size();
        VehicleDTO vehicleDTOtoUpdate = VehicleDTO.builder()
                .id(savedVehicle.getId())
                .brandId(updatedVehicle.getVehicleModel().getVehicleBrand().getId())
                .brandName(updatedVehicle.getVehicleModel().getVehicleBrand().getName())
                .modelId(updatedVehicle.getVehicleModel().getId())
                .modelName(updatedVehicle.getVehicleModel().getName())
                .fuelTypeId(updatedVehicle.getFuelType().getId())
                .fuelTypeName(updatedVehicle.getFuelType().getName())
                .vehicleTypeId(updatedVehicle.getVehicleType().getId())
                .vehicleTypeName(updatedVehicle.getVehicleType().getName())
                .registrationNumber(updatedVehicle.getRegistrationNumber())
                .vin(updatedVehicle.getVin())
//                .productionYear(updatedVehicle.getProductionYear())
                .firstRegistrationDate(updatedVehicle.getFirstRegistrationDate())
                .freePlacesForTechInspection(updatedVehicle.getFreePlacesForTechInspection())
                .length(updatedVehicle.getLength())
                .width(updatedVehicle.getWidth())
                .height(updatedVehicle.getHeight())
                .createDate(updatedVehicle.getCreateDate())
                .modifyDate(updatedVehicle.getModifyDate()).build();


        restVehicleMvc.perform(put(API_PATH + "/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(vehicleDTOtoUpdate)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    public void updateVehicleShouldThrowBadRequestForMissingFirstRegistrationDate() throws Exception {

        List<FuelType> allFuelTypes = fuelTypeRepository.findAll();
        List<VehicleModel> allVehicleModels = vehicleModelRepository.findAll();
        List<VehicleType> allVehicleTypes = vehicleTypeRepository.findAll();
        List<VehicleBrand> allBrands = vehicleBrandRepository.findAll();

        FuelType fuelType = allFuelTypes.get(0);
        VehicleModel vehicleModel = allVehicleModels.get(0);
        VehicleType vehicleType = allVehicleTypes.get(0);


        VehicleDTO vehicleDTO = vehicleMapper.toDto(firstVehicle);
        VehicleDTO vehicleDTOtoSave = VehicleDTO.builder()
                .brandId(vehicleModel.getVehicleBrand().getId())
                .brandName(vehicleModel.getVehicleBrand().getName())
                .modelId(vehicleModel.getId())
                .modelName(vehicleModel.getName())
                .fuelTypeId(fuelType.getId())
                .fuelTypeName(fuelType.getName())
                .vehicleTypeId(vehicleType.getId())
                .vehicleTypeName(vehicleType.getName())
                .registrationNumber(FIST_REGISTRATION_NUMBER)
                .vin(FIRST_VIN)
                .productionYear(FIRST_PRODUCTION_YEAR)
                .firstRegistrationDate(FIRST_FIRST_REGISTRATION_DATE)
                .freePlacesForTechInspection(FIRST_FREE_PLACES_FOR_TECH_INSPECTION)
                .length(FIRST_LENGTH)
                .width(FIRST_WIDTH)
                .height(FIRST_HEIGHT)
                .createDate(DEFAULT_CREATE_DATE).build();
        VehicleDTO savedVehicle = vehicleService.save(vehicleDTOtoSave);

        List<Vehicle> allVehicles = vehicleRepository.findAll();
        int sizeBeforeTest = allVehicles.size();
        VehicleDTO vehicleDTOtoUpdate = VehicleDTO.builder()
                .id(savedVehicle.getId())
                .brandId(updatedVehicle.getVehicleModel().getVehicleBrand().getId())
                .brandName(updatedVehicle.getVehicleModel().getVehicleBrand().getName())
                .modelId(updatedVehicle.getVehicleModel().getId())
                .modelName(updatedVehicle.getVehicleModel().getName())
                .fuelTypeId(updatedVehicle.getFuelType().getId())
                .fuelTypeName(updatedVehicle.getFuelType().getName())
                .vehicleTypeId(updatedVehicle.getVehicleType().getId())
                .vehicleTypeName(updatedVehicle.getVehicleType().getName())
                .registrationNumber(updatedVehicle.getRegistrationNumber())
                .vin(updatedVehicle.getVin())
                .productionYear(updatedVehicle.getProductionYear())
//                .firstRegistrationDate(updatedVehicle.getFirstRegistrationDate())
                .freePlacesForTechInspection(updatedVehicle.getFreePlacesForTechInspection())
                .length(updatedVehicle.getLength())
                .width(updatedVehicle.getWidth())
                .height(updatedVehicle.getHeight())
                .createDate(updatedVehicle.getCreateDate())
                .modifyDate(updatedVehicle.getModifyDate()).build();


        restVehicleMvc.perform(put(API_PATH + "/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(vehicleDTOtoUpdate)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    public void updateVehicleShouldThrowBadRequestForMissingFreePlacesForTechInspection() throws Exception {

        List<FuelType> allFuelTypes = fuelTypeRepository.findAll();
        List<VehicleModel> allVehicleModels = vehicleModelRepository.findAll();
        List<VehicleType> allVehicleTypes = vehicleTypeRepository.findAll();
        List<VehicleBrand> allBrands = vehicleBrandRepository.findAll();

        FuelType fuelType = allFuelTypes.get(0);
        VehicleModel vehicleModel = allVehicleModels.get(0);
        VehicleType vehicleType = allVehicleTypes.get(0);


        VehicleDTO vehicleDTO = vehicleMapper.toDto(firstVehicle);
        VehicleDTO vehicleDTOtoSave = VehicleDTO.builder()
                .brandId(vehicleModel.getVehicleBrand().getId())
                .brandName(vehicleModel.getVehicleBrand().getName())
                .modelId(vehicleModel.getId())
                .modelName(vehicleModel.getName())
                .fuelTypeId(fuelType.getId())
                .fuelTypeName(fuelType.getName())
                .vehicleTypeId(vehicleType.getId())
                .vehicleTypeName(vehicleType.getName())
                .registrationNumber(FIST_REGISTRATION_NUMBER)
                .vin(FIRST_VIN)
                .productionYear(FIRST_PRODUCTION_YEAR)
                .firstRegistrationDate(FIRST_FIRST_REGISTRATION_DATE)
                .freePlacesForTechInspection(FIRST_FREE_PLACES_FOR_TECH_INSPECTION)
                .length(FIRST_LENGTH)
                .width(FIRST_WIDTH)
                .height(FIRST_HEIGHT)
                .createDate(DEFAULT_CREATE_DATE).build();
        VehicleDTO savedVehicle = vehicleService.save(vehicleDTOtoSave);

        List<Vehicle> allVehicles = vehicleRepository.findAll();
        int sizeBeforeTest = allVehicles.size();
        VehicleDTO vehicleDTOtoUpdate = VehicleDTO.builder()
                .id(savedVehicle.getId())
                .brandId(updatedVehicle.getVehicleModel().getVehicleBrand().getId())
                .brandName(updatedVehicle.getVehicleModel().getVehicleBrand().getName())
                .modelId(updatedVehicle.getVehicleModel().getId())
                .modelName(updatedVehicle.getVehicleModel().getName())
                .fuelTypeId(updatedVehicle.getFuelType().getId())
                .fuelTypeName(updatedVehicle.getFuelType().getName())
                .vehicleTypeId(updatedVehicle.getVehicleType().getId())
                .vehicleTypeName(updatedVehicle.getVehicleType().getName())
                .registrationNumber(updatedVehicle.getRegistrationNumber())
                .vin(updatedVehicle.getVin())
                .productionYear(updatedVehicle.getProductionYear())
                .firstRegistrationDate(updatedVehicle.getFirstRegistrationDate())
//                .freePlacesForTechInspection(updatedVehicle.getFreePlacesForTechInspection())
                .length(updatedVehicle.getLength())
                .width(updatedVehicle.getWidth())
                .height(updatedVehicle.getHeight())
                .createDate(updatedVehicle.getCreateDate())
                .modifyDate(updatedVehicle.getModifyDate()).build();


        restVehicleMvc.perform(put(API_PATH + "/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(vehicleDTOtoUpdate)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    public void updateVehicleShouldThrowBadRequestForMissingLength() throws Exception {

        List<FuelType> allFuelTypes = fuelTypeRepository.findAll();
        List<VehicleModel> allVehicleModels = vehicleModelRepository.findAll();
        List<VehicleType> allVehicleTypes = vehicleTypeRepository.findAll();
        List<VehicleBrand> allBrands = vehicleBrandRepository.findAll();

        FuelType fuelType = allFuelTypes.get(0);
        VehicleModel vehicleModel = allVehicleModels.get(0);
        VehicleType vehicleType = allVehicleTypes.get(0);


        VehicleDTO vehicleDTO = vehicleMapper.toDto(firstVehicle);
        VehicleDTO vehicleDTOtoSave = VehicleDTO.builder()
                .brandId(vehicleModel.getVehicleBrand().getId())
                .brandName(vehicleModel.getVehicleBrand().getName())
                .modelId(vehicleModel.getId())
                .modelName(vehicleModel.getName())
                .fuelTypeId(fuelType.getId())
                .fuelTypeName(fuelType.getName())
                .vehicleTypeId(vehicleType.getId())
                .vehicleTypeName(vehicleType.getName())
                .registrationNumber(FIST_REGISTRATION_NUMBER)
                .vin(FIRST_VIN)
                .productionYear(FIRST_PRODUCTION_YEAR)
                .firstRegistrationDate(FIRST_FIRST_REGISTRATION_DATE)
                .freePlacesForTechInspection(FIRST_FREE_PLACES_FOR_TECH_INSPECTION)
                .length(FIRST_LENGTH)
                .width(FIRST_WIDTH)
                .height(FIRST_HEIGHT)
                .createDate(DEFAULT_CREATE_DATE).build();
        VehicleDTO savedVehicle = vehicleService.save(vehicleDTOtoSave);

        List<Vehicle> allVehicles = vehicleRepository.findAll();
        int sizeBeforeTest = allVehicles.size();
        VehicleDTO vehicleDTOtoUpdate = VehicleDTO.builder()
                .id(savedVehicle.getId())
                .brandId(updatedVehicle.getVehicleModel().getVehicleBrand().getId())
                .brandName(updatedVehicle.getVehicleModel().getVehicleBrand().getName())
                .modelId(updatedVehicle.getVehicleModel().getId())
                .modelName(updatedVehicle.getVehicleModel().getName())
                .fuelTypeId(updatedVehicle.getFuelType().getId())
                .fuelTypeName(updatedVehicle.getFuelType().getName())
                .vehicleTypeId(updatedVehicle.getVehicleType().getId())
                .vehicleTypeName(updatedVehicle.getVehicleType().getName())
                .registrationNumber(updatedVehicle.getRegistrationNumber())
                .vin(updatedVehicle.getVin())
                .productionYear(updatedVehicle.getProductionYear())
                .firstRegistrationDate(updatedVehicle.getFirstRegistrationDate())
                .freePlacesForTechInspection(updatedVehicle.getFreePlacesForTechInspection())
//                .length(updatedVehicle.getLength())
                .width(updatedVehicle.getWidth())
                .height(updatedVehicle.getHeight())
                .createDate(updatedVehicle.getCreateDate())
                .modifyDate(updatedVehicle.getModifyDate()).build();


        restVehicleMvc.perform(put(API_PATH + "/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(vehicleDTOtoUpdate)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    public void updateVehicleShouldThrowBadRequestForMissingWidth() throws Exception {

        List<FuelType> allFuelTypes = fuelTypeRepository.findAll();
        List<VehicleModel> allVehicleModels = vehicleModelRepository.findAll();
        List<VehicleType> allVehicleTypes = vehicleTypeRepository.findAll();
        List<VehicleBrand> allBrands = vehicleBrandRepository.findAll();

        FuelType fuelType = allFuelTypes.get(0);
        VehicleModel vehicleModel = allVehicleModels.get(0);
        VehicleType vehicleType = allVehicleTypes.get(0);


        VehicleDTO vehicleDTO = vehicleMapper.toDto(firstVehicle);
        VehicleDTO vehicleDTOtoSave = VehicleDTO.builder()
                .brandId(vehicleModel.getVehicleBrand().getId())
                .brandName(vehicleModel.getVehicleBrand().getName())
                .modelId(vehicleModel.getId())
                .modelName(vehicleModel.getName())
                .fuelTypeId(fuelType.getId())
                .fuelTypeName(fuelType.getName())
                .vehicleTypeId(vehicleType.getId())
                .vehicleTypeName(vehicleType.getName())
                .registrationNumber(FIST_REGISTRATION_NUMBER)
                .vin(FIRST_VIN)
                .productionYear(FIRST_PRODUCTION_YEAR)
                .firstRegistrationDate(FIRST_FIRST_REGISTRATION_DATE)
                .freePlacesForTechInspection(FIRST_FREE_PLACES_FOR_TECH_INSPECTION)
                .length(FIRST_LENGTH)
                .width(FIRST_WIDTH)
                .height(FIRST_HEIGHT)
                .createDate(DEFAULT_CREATE_DATE).build();
        VehicleDTO savedVehicle = vehicleService.save(vehicleDTOtoSave);

        List<Vehicle> allVehicles = vehicleRepository.findAll();
        int sizeBeforeTest = allVehicles.size();
        VehicleDTO vehicleDTOtoUpdate = VehicleDTO.builder()
                .id(savedVehicle.getId())
                .brandId(updatedVehicle.getVehicleModel().getVehicleBrand().getId())
                .brandName(updatedVehicle.getVehicleModel().getVehicleBrand().getName())
                .modelId(updatedVehicle.getVehicleModel().getId())
                .modelName(updatedVehicle.getVehicleModel().getName())
                .fuelTypeId(updatedVehicle.getFuelType().getId())
                .fuelTypeName(updatedVehicle.getFuelType().getName())
                .vehicleTypeId(updatedVehicle.getVehicleType().getId())
                .vehicleTypeName(updatedVehicle.getVehicleType().getName())
                .registrationNumber(updatedVehicle.getRegistrationNumber())
                .vin(updatedVehicle.getVin())
                .productionYear(updatedVehicle.getProductionYear())
                .firstRegistrationDate(updatedVehicle.getFirstRegistrationDate())
                .freePlacesForTechInspection(updatedVehicle.getFreePlacesForTechInspection())
                .length(updatedVehicle.getLength())
//                .width(updatedVehicle.getWidth())
                .height(updatedVehicle.getHeight())
                .createDate(updatedVehicle.getCreateDate())
                .modifyDate(updatedVehicle.getModifyDate()).build();


        restVehicleMvc.perform(put(API_PATH + "/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(vehicleDTOtoUpdate)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    public void updateVehicleShouldThrowBadRequestForMissingHeight() throws Exception {

        List<FuelType> allFuelTypes = fuelTypeRepository.findAll();
        List<VehicleModel> allVehicleModels = vehicleModelRepository.findAll();
        List<VehicleType> allVehicleTypes = vehicleTypeRepository.findAll();
        List<VehicleBrand> allBrands = vehicleBrandRepository.findAll();

        FuelType fuelType = allFuelTypes.get(0);
        VehicleModel vehicleModel = allVehicleModels.get(0);
        VehicleType vehicleType = allVehicleTypes.get(0);


        VehicleDTO vehicleDTO = vehicleMapper.toDto(firstVehicle);
        VehicleDTO vehicleDTOtoSave = VehicleDTO.builder()
                .brandId(vehicleModel.getVehicleBrand().getId())
                .brandName(vehicleModel.getVehicleBrand().getName())
                .modelId(vehicleModel.getId())
                .modelName(vehicleModel.getName())
                .fuelTypeId(fuelType.getId())
                .fuelTypeName(fuelType.getName())
                .vehicleTypeId(vehicleType.getId())
                .vehicleTypeName(vehicleType.getName())
                .registrationNumber(FIST_REGISTRATION_NUMBER)
                .vin(FIRST_VIN)
                .productionYear(FIRST_PRODUCTION_YEAR)
                .firstRegistrationDate(FIRST_FIRST_REGISTRATION_DATE)
                .freePlacesForTechInspection(FIRST_FREE_PLACES_FOR_TECH_INSPECTION)
                .length(FIRST_LENGTH)
                .width(FIRST_WIDTH)
                .height(FIRST_HEIGHT)
                .createDate(DEFAULT_CREATE_DATE).build();
        VehicleDTO savedVehicle = vehicleService.save(vehicleDTOtoSave);

        List<Vehicle> allVehicles = vehicleRepository.findAll();
        int sizeBeforeTest = allVehicles.size();
        VehicleDTO vehicleDTOtoUpdate = VehicleDTO.builder()
                .id(savedVehicle.getId())
                .brandId(updatedVehicle.getVehicleModel().getVehicleBrand().getId())
                .brandName(updatedVehicle.getVehicleModel().getVehicleBrand().getName())
                .modelId(updatedVehicle.getVehicleModel().getId())
                .modelName(updatedVehicle.getVehicleModel().getName())
                .fuelTypeId(updatedVehicle.getFuelType().getId())
                .fuelTypeName(updatedVehicle.getFuelType().getName())
                .vehicleTypeId(updatedVehicle.getVehicleType().getId())
                .vehicleTypeName(updatedVehicle.getVehicleType().getName())
                .registrationNumber(updatedVehicle.getRegistrationNumber())
                .vin(updatedVehicle.getVin())
                .productionYear(updatedVehicle.getProductionYear())
                .firstRegistrationDate(updatedVehicle.getFirstRegistrationDate())
                .freePlacesForTechInspection(updatedVehicle.getFreePlacesForTechInspection())
                .length(updatedVehicle.getLength())
                .width(updatedVehicle.getWidth())
//                .height(updatedVehicle.getHeight())
                .createDate(updatedVehicle.getCreateDate())
                .modifyDate(updatedVehicle.getModifyDate()).build();


        restVehicleMvc.perform(put(API_PATH + "/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(vehicleDTOtoUpdate)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    public void getVehicleById() throws Exception {
        createGlobalVehiclesInDB();
        List<Vehicle> vehicleList = vehicleRepository.findAll();
        Vehicle firstVehicleFromDB = vehicleList.get(0);
        Long firstVehicleId = firstVehicleFromDB.getId();

        restVehicleMvc.perform(get(API_PATH + "/getById/{id}", firstVehicleId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id").value(this.firstVehicle.getId().intValue()))
                .andExpect(jsonPath("$.registrationNumber").value(this.firstVehicle.getRegistrationNumber()))
                .andExpect(jsonPath("$.vin").value(this.firstVehicle.getVin()))
                .andExpect(jsonPath("$.productionYear").value(this.firstVehicle.getProductionYear().toString()))
                .andExpect(jsonPath("$.firstRegistrationDate").value(this.firstVehicle.getFirstRegistrationDate()
                        .toString()))
                .andExpect(jsonPath("$.freePlacesForTechInspection").value(this.firstVehicle.getFreePlacesForTechInspection()
                        .toString()))
                .andExpect(jsonPath("$.length").value(this.firstVehicle.getLength().toString()))
                .andExpect(jsonPath("$.width").value(this.firstVehicle.getWidth().toString()))
                .andExpect(jsonPath("$.height").value(this.firstVehicle.getHeight().toString()))
                .andExpect(jsonPath("$.brandId").value(this.firstVehicle.getVehicleModel().getVehicleBrand().getId()))
                .andExpect(jsonPath("$.brandName").value(this.firstVehicle.getVehicleModel().getVehicleBrand()
                        .getName()))
                .andExpect(jsonPath("$.modelId").value(this.firstVehicle.getVehicleModel().getId()))
                .andExpect(jsonPath("$.modelName").value(this.firstVehicle.getVehicleModel().getName()))
                .andExpect(jsonPath("$.fuelTypeId").value(this.firstVehicle.getFuelType().getId()))
                .andExpect(jsonPath("$.fuelTypeName").value(this.firstVehicle.getFuelType().getName()))
                .andExpect(jsonPath("$.vehicleTypeId").value(this.firstVehicle.getVehicleType().getId()))
                .andExpect(jsonPath("$.vehicleTypeName").value(this.firstVehicle.getVehicleType().getName()))
                .andExpect(jsonPath("$.createDate").value(DEFAULT_CREATE_DATE.toString()));
    }

    @Test
    @Transactional
    public void getVehicleByIdAndShouldNotBeFound() throws Exception {
        createGlobalVehiclesInDB();
        List<Vehicle> vehicleList = vehicleRepository.findAll();
        Vehicle firstVehicleFromDB = vehicleList.get(0);
        Long firstVehicleId = firstVehicleFromDB.getId();

        restVehicleMvc.perform(get(API_PATH + "/getById/{id}", firstVehicleId + 10L))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    public void getAllVehicles() throws Exception {
        createGlobalVehiclesInDB();

        restVehicleMvc.perform(get(API_PATH + "/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isNotEmpty());
    }

    @Test
    @Transactional
    public void findVehicleWithFilterBrandId() throws Exception {
        createGlobalVehiclesInDB();
        List<Vehicle> vehicleList = vehicleRepository.findAll();
        defaultVehicleShouldBeFound("brandId=" + firstVehicle.getVehicleModel().getVehicleBrand().getId());
        defaultVehicleShouldNotBeFound("brandId=" + 123L);
    }

    @Test
    @Transactional
    public void findVehicleWithFilterBrandName() throws Exception {
        createGlobalVehiclesInDB();
        List<Vehicle> vehicleList = vehicleRepository.findAll();
        defaultVehicleShouldBeFound("brandName=" + firstVehicle.getVehicleModel().getVehicleBrand().getName());
        defaultVehicleShouldNotBeFound("brandName=" + "badBand");
    }

    @Test
    @Transactional
    public void findVehicleWithFilterModelId() throws Exception {
        createGlobalVehiclesInDB();
        List<Vehicle> vehicleList = vehicleRepository.findAll();
        defaultVehicleShouldBeFound("modelId=" + firstVehicle.getVehicleModel().getId());
        defaultVehicleShouldNotBeFound("modelId=" + 123L);
    }

    @Test
    @Transactional
    public void findVehicleWithFilterModelName() throws Exception {
        createGlobalVehiclesInDB();
        List<Vehicle> vehicleList = vehicleRepository.findAll();
        defaultVehicleShouldBeFound("modelName=" + firstVehicle.getVehicleModel().getName());
        defaultVehicleShouldNotBeFound("modelName=" + "badModel");
    }

    @Test
    @Transactional
    public void findVehicleWithFilterVehicleTypeId() throws Exception {
        createGlobalVehiclesInDB();
        List<Vehicle> vehicleList = vehicleRepository.findAll();
        defaultVehicleShouldBeFound("vehicleTypeId=" + firstVehicle.getVehicleType().getId());
        defaultVehicleShouldNotBeFound("vehicleTypeId=" + 123L);
    }

    @Test
    @Transactional
    public void findVehicleWithFilterVehicleTypeName() throws Exception {
        createGlobalVehiclesInDB();
        List<Vehicle> vehicleList = vehicleRepository.findAll();
        defaultVehicleShouldBeFound("vehicleTypeName=" + firstVehicle.getVehicleType().getName());
        defaultVehicleShouldNotBeFound("vehicleTypeName=" + "badVehicleType");
    }

    @Test
    @Transactional
    public void findVehicleWithFilterFuelTypeId() throws Exception {
        createGlobalVehiclesInDB();
        List<Vehicle> vehicleList = vehicleRepository.findAll();
        defaultVehicleShouldBeFound("fuelTypeId=" + firstVehicle.getFuelType().getId());
        defaultVehicleShouldNotBeFound("fuelTypeId=" + 123L);
    }

    @Test
    @Transactional
    public void findVehicleWithFilterFuelTypeName() throws Exception {
        createGlobalVehiclesInDB();
        List<Vehicle> vehicleList = vehicleRepository.findAll();
        defaultVehicleShouldBeFound("fuelTypeName=" + firstVehicle.getFuelType().getName());
        defaultVehicleShouldNotBeFound("fuelTypeName=" + "badFuelType");
    }

    @Test
    @Transactional
    public void findVehicleWithFilterRegistrationNumber() throws Exception {
        createGlobalVehiclesInDB();
        List<Vehicle> vehicleList = vehicleRepository.findAll();
        defaultVehicleShouldBeFound("registrationNumber=" + firstVehicle.getRegistrationNumber());
        defaultVehicleShouldNotBeFound("registrationNumber=" + "bad");
    }

    @Test
    @Transactional
    public void findVehicleWithFilterVin() throws Exception {
        createGlobalVehiclesInDB();
        List<Vehicle> vehicleList = vehicleRepository.findAll();
        defaultVehicleShouldBeFound("vin=" + firstVehicle.getVin());
        defaultVehicleShouldNotBeFound("vin=" + "badVin");
    }

    @Test
    @Transactional
    public void findVehicleWithFilterProductionYear() throws Exception {
        createGlobalVehiclesInDB();
        List<Vehicle> vehicleList = vehicleRepository.findAll();
        defaultVehicleShouldBeFound("productionYearStartWith=" + firstVehicle.getProductionYear());
        defaultVehicleShouldNotBeFound("productionYearStartWith=" + UPDATED_PRODUCTION_YEAR);
    }

    @Test
    @Transactional
    public void findVehicleWithFilterFreePlacesForTechInspection() throws Exception {
        createGlobalVehiclesInDB();
        List<Vehicle> vehicleList = vehicleRepository.findAll();
        defaultVehicleShouldBeFound("freePlacesForTechInspectionStartWith=" + firstVehicle.getFreePlacesForTechInspection());
        defaultVehicleShouldNotBeFound("freePlacesForTechInspectionStartWith=" + 22);
    }

    @Test
    @Transactional
    public void findVehicleWithFilterFirstRegistrationDate() throws Exception {
        createGlobalVehiclesInDB();
        List<Vehicle> vehicleList = vehicleRepository.findAll();
        defaultVehicleShouldBeFound("firstRegistrationDateStartWith=" + firstVehicle.getFirstRegistrationDate());
        defaultVehicleShouldNotBeFound("firstRegistrationDateStartWith=" + UPDATED_FIRST_REGISTRATION_DATE);
    }


    @Test
    @Transactional
    public void findVehicleWithFilterFirstLength() throws Exception {
        createGlobalVehiclesInDB();
        List<Vehicle> vehicleList = vehicleRepository.findAll();
        defaultVehicleShouldBeFound("lengthStartWith=" + firstVehicle.getLength());
        defaultVehicleShouldNotBeFound("lengthStartWith=" + new BigDecimal("22.3"));
    }

    @Test
    @Transactional
    public void findVehicleWithFilterFirstWidth() throws Exception {
        createGlobalVehiclesInDB();
        List<Vehicle> vehicleList = vehicleRepository.findAll();
        defaultVehicleShouldBeFound("widthStartWith=" + firstVehicle.getWidth());
        defaultVehicleShouldNotBeFound("widthStartWith=" + new BigDecimal("22.3"));
    }

    @Test
    @Transactional
    public void findVehicleWithFilterFirstHeight() throws Exception {
        createGlobalVehiclesInDB();
        List<Vehicle> vehicleList = vehicleRepository.findAll();
        defaultVehicleShouldBeFound("heightStartWith=" + firstVehicle.getHeight());
        defaultVehicleShouldNotBeFound("heightStartWith=" + new BigDecimal("22.3"));
    }


    private void defaultVehicleShouldBeFound(String filter) throws Exception {
        restVehicleMvc.perform(get(API_PATH + "/?sort=id,desc&" + filter))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(firstVehicle.getId().intValue())))
                .andExpect(jsonPath("$.[*].registrationNumber").value(hasItem(firstVehicle.getRegistrationNumber())))
                .andExpect(jsonPath("$.[*].vin").value(hasItem(firstVehicle.getVin())))
                .andExpect(jsonPath("$.[*].productionYear").value(hasItem(firstVehicle.getProductionYear().intValue())))
                .andExpect(jsonPath("$.[*].firstRegistrationDate").value(hasItem(firstVehicle.getFirstRegistrationDate()
                        .toString())))
                .andExpect(jsonPath("$.[*].freePlacesForTechInspection").value(hasItem(firstVehicle.getFreePlacesForTechInspection()
                        .intValue())))
                .andExpect(jsonPath("$.[*].length").value(hasItem(firstVehicle.getLength().doubleValue())))
                .andExpect(jsonPath("$.[*].width").value(hasItem(firstVehicle.getWidth().doubleValue())))
                .andExpect(jsonPath("$.[*].height").value(hasItem(firstVehicle.getHeight().doubleValue())))
                .andExpect(jsonPath("$.[*].brandId").value(hasItem(firstVehicle.getVehicleModel().getVehicleBrand()
                        .getId().intValue())))
                .andExpect(jsonPath("$.[*].brandName").value(hasItem(firstVehicle.getVehicleModel().getVehicleBrand()
                        .getName())))
                .andExpect(jsonPath("$.[*].modelId").value(hasItem(firstVehicle.getVehicleModel().getId().intValue())))
                .andExpect(jsonPath("$.[*].modelName").value(hasItem(firstVehicle.getVehicleModel().getName())))
                .andExpect(jsonPath("$.[*].fuelTypeId").value(hasItem(firstVehicle.getFuelType().getId().intValue())))
                .andExpect(jsonPath("$.[*].fuelTypeName").value(hasItem(firstVehicle.getFuelType().getName())))
                .andExpect(jsonPath("$.[*].vehicleTypeId").value(hasItem(firstVehicle.getVehicleType().getId()
                        .intValue())))
                .andExpect(jsonPath("$.[*].vehicleTypeName").value(hasItem(firstVehicle.getVehicleType().getName())))
                .andExpect(jsonPath("$.[*].createDate").value(hasItem(DEFAULT_CREATE_DATE.toString())));
    }


    private void defaultVehicleShouldNotBeFound(String filter) throws Exception {
        restVehicleMvc.perform(get(API_PATH + "/?sort=id,desc&" + filter)).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE)).andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }

    private void createGlobalFuelTypes() {
        firstFuelType = FuelTypeControllerIT.createEntity(em);
        fuelTypeRepository.saveAndFlush(firstFuelType);

        secondFuelType = FuelTypeControllerIT.createSecondEntity(em);
        fuelTypeRepository.saveAndFlush(secondFuelType);

        updatedFuelType = FuelTypeControllerIT.createUpdatedEntity(em);
        fuelTypeRepository.saveAndFlush(updatedFuelType);
    }

    private void createGlobalVehicleTypes() {
        firstVehicleType = VehicleTypeControllerIT.createEntity(em);
        vehicleTypeRepository.saveAndFlush(firstVehicleType);
        secondVehicleType = VehicleTypeControllerIT.createSecondEntity(em);
        vehicleTypeRepository.saveAndFlush(secondVehicleType);
        updatedVehicleType = VehicleTypeControllerIT.createUpdatedEntity(em);
        vehicleTypeRepository.saveAndFlush(updatedVehicleType);
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
        updatedVehicleModel = VehicleModelControllerIT.createUpdatedEntity(em);
        updatedVehicleModel.setVehicleBrand(updatedVehicleBrand);
        em.persist(updatedVehicleModel);
        em.flush();
    }

    private void createGlobalVehicleBrands() {
        firstVehicleBrand = VehicleBrandControllerIT.createEntity(em);
        em.persist(firstVehicleBrand);
        em.flush();
        secondVehicleBrand = VehicleBrandControllerIT.createSecondEntity(em);
        em.persist(secondVehicleBrand);
        em.flush();
        updatedVehicleBrand = VehicleBrandControllerIT.createUpdatedEntity(em);
        em.persist(updatedVehicleBrand);
        em.flush();
    }

    private void createAllDefaultData() {
        createGlobalVehicleBrands();
        createGlobalVehicleTypes();
        createGlobalFuelTypes();
        createGlobalVehicleModels();
    }

    private void createGlobalVehicles() {
        firstVehicle = createEntity(em);
        firstVehicle.setFuelType(firstFuelType);
        firstVehicle.setVehicleType(firstVehicleType);
        firstVehicle.setVehicleModel(firstVehicleModel);
        secondVehicle = createSecondEntity(em);
        secondVehicle.setFuelType(secondFuelType);
        secondVehicle.setVehicleType(secondVehicleType);
        secondVehicle.setVehicleModel(secondVehicleModel);
        updatedVehicle = createUpdatedEntity(em);
        updatedVehicle.setFuelType(updatedFuelType);
        updatedVehicle.setVehicleType(updatedVehicleType);
        updatedVehicle.setVehicleModel(updatedVehicleModel);
    }

    private void createGlobalVehiclesInDB() {
        vehicleRepository.saveAndFlush(firstVehicle);
        vehicleRepository.saveAndFlush(secondVehicle);
    }

}
