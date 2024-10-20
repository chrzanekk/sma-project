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
import pl.com.chrzanowski.scma.domain.FuelType;
import pl.com.chrzanowski.scma.domain.*;
import pl.com.chrzanowski.scma.domain.enumeration.*;
import pl.com.chrzanowski.scma.repository.*;
import pl.com.chrzanowski.scma.service.TireService;
import pl.com.chrzanowski.scma.service.dto.TireDTO;
import pl.com.chrzanowski.scma.service.mapper.TireMapper;

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
public class TireControllerIT {
    private static final String API_PATH = "/api/tires";

    private static final Instant DEFAULT_CREATE_DATE = Instant.ofEpochMilli(0L);
    private static final Instant DEFAULT_MODIFY_DATE = Instant.ofEpochMilli(36000L);
    private static final String FIRST_BRAND = "first brand";
    private static final String SECOND_BRAND = "second brand";
    private static final String UPDATED_BRAND = "updated brand";
    private static final String FIRST_MODEL = "first model";
    private static final String SECOND_MODEL = "second model";
    private static final String UPDATED_MODEL = "updated model";

    private static final Integer FIRST_WIDTH = 255;
    private static final Integer SECOND_WIDTH = 265;
    private static final Integer UPDATED_WIDTH = 275;
    private static final Integer FIRST_PROFILE = 55;
    private static final Integer SECOND_PROFILE = 45;
    private static final Integer UPDATED_PROFILE = 35;
    private static final Integer FIRST_DIAMETER = 16;
    private static final Integer SECOND_DIAMETER = 17;
    private static final Integer UPDATED_DIAMETER = 18;
    private static final TireSpeedIndex FIRST_SPEED_INDEX = TireSpeedIndex.V;
    private static final TireSpeedIndex SECOND_SPEED_INDEX = TireSpeedIndex.W;
    private static final TireSpeedIndex UPDATED_SPEED_INDEX = TireSpeedIndex.U;
    private static final TireLoadCapacityIndex FIRST_CAPACITY_INDEX = TireLoadCapacityIndex.NINETY_NINE;
    private static final TireLoadCapacityIndex SECOND_CAPACITY_INDEX = TireLoadCapacityIndex.NINETY_ONE;
    private static final TireLoadCapacityIndex UPDATED_CAPACITY_INDEX = TireLoadCapacityIndex.NINETY_FIVE;
    private static final TireReinforcedIndex FIRST_REINFORCED_INDEX = TireReinforcedIndex.SL;
    private static final TireReinforcedIndex SECOND_REINFORCED_INDEX = TireReinforcedIndex.XL;
    private static final TireReinforcedIndex UPDATED_REINFORCED_INDEX = TireReinforcedIndex.C;
    private static final TireSeasonType FIRST_SEASON_TYPE = TireSeasonType.ALL_SEASON;
    private static final TireSeasonType SECOND_SEASON_TYPE = TireSeasonType.SUMMER;
    private static final TireSeasonType UPDATED_SEASON_TYPE = TireSeasonType.WINTER;
    private static final TireType FIRST_TIRE_TYPE = TireType.D;
    private static final TireType SECOND_TIRE_TYPE = TireType.D;
    private static final TireType UPDATED_TIRE_TYPE = TireType.R;
    private static final TireStatus FIRST_TIRE_STATUS = TireStatus.MOUNTED;
    private static final TireStatus SECOND_TIRE_STATUS = TireStatus.STOKED;
    private static final TireStatus UPDATED_TIRE_STATUS = TireStatus.DISPOSED;
    private static final Integer FIRST_PRODUCTION_YEAR = 2021;
    private static final Integer SECOND_PRODUCTION_YEAR = 2012;
    private static final Integer UPDATED_PRODUCTION_YEAR = 2022;
    private static final LocalDate FIRST_PURCHASE_DATE = LocalDate.of(2002, 1, 1);
    private static final LocalDate SECOND_PURCHASE_DATE = LocalDate.of(2012, 1, 1);
    private static final LocalDate UPDATED_PURCHASE_DATE = LocalDate.of(2022, 1, 1);


    @Autowired
    private MockMvc restTireMvc;
    @Autowired
    private EntityManager em;
    @Autowired
    private TireRepository tireRepository;
    @Autowired
    private TireService tireService;
    @Autowired
    private TireMapper tireMapper;
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
    private Tire firstTire;
    private Tire secondTire;
    private Tire thirdTire;

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

    public static Tire createEntity(EntityManager em) {
        return new Tire()
                .setBrand(FIRST_BRAND)
                .setModel(FIRST_MODEL)
                .setWidth(FIRST_WIDTH)
                .setProfile(FIRST_PROFILE)
                .setDiameter(FIRST_DIAMETER)
                .setSpeedIndex(FIRST_SPEED_INDEX)
                .setCapacityIndex(FIRST_CAPACITY_INDEX)
                .setTireReinforcedIndex(FIRST_REINFORCED_INDEX)
                .setTireSeasonType(FIRST_SEASON_TYPE)
                .setRunOnFlat(false)
                .setType(FIRST_TIRE_TYPE)
                .setTireStatus(FIRST_TIRE_STATUS)
                .setPurchaseDate(FIRST_PURCHASE_DATE)
                .setProductionYear(FIRST_PRODUCTION_YEAR)
                .setCreateDate(DEFAULT_CREATE_DATE);
    }

    public static Tire createSecondEntity(EntityManager em) {
        return new Tire()
                .setBrand(SECOND_BRAND)
                .setModel(SECOND_MODEL)
                .setWidth(SECOND_WIDTH)
                .setProfile(SECOND_PROFILE)
                .setDiameter(SECOND_DIAMETER)
                .setSpeedIndex(SECOND_SPEED_INDEX)
                .setCapacityIndex(SECOND_CAPACITY_INDEX)
                .setTireReinforcedIndex(SECOND_REINFORCED_INDEX)
                .setTireSeasonType(SECOND_SEASON_TYPE)
                .setRunOnFlat(false)
                .setType(SECOND_TIRE_TYPE)
                .setTireStatus(SECOND_TIRE_STATUS)
                .setPurchaseDate(SECOND_PURCHASE_DATE)
                .setProductionYear(SECOND_PRODUCTION_YEAR)
                .setCreateDate(DEFAULT_CREATE_DATE);
    }

    public static Tire createUpdatedEntity(EntityManager em) {
        return new Tire()
                .setBrand(UPDATED_BRAND)
                .setModel(UPDATED_MODEL)
                .setWidth(UPDATED_WIDTH)
                .setProfile(UPDATED_PROFILE)
                .setDiameter(UPDATED_DIAMETER)
                .setSpeedIndex(UPDATED_SPEED_INDEX)
                .setCapacityIndex(UPDATED_CAPACITY_INDEX)
                .setTireReinforcedIndex(UPDATED_REINFORCED_INDEX)
                .setTireSeasonType(UPDATED_SEASON_TYPE)
                .setType(UPDATED_TIRE_TYPE)
                .setRunOnFlat(true)
                .setTireStatus(UPDATED_TIRE_STATUS)
                .setPurchaseDate(UPDATED_PURCHASE_DATE)
                .setProductionYear(UPDATED_PRODUCTION_YEAR)
                .setCreateDate(DEFAULT_CREATE_DATE)
                .setModifyDate(DEFAULT_MODIFY_DATE);
    }


    @BeforeEach
    public void initTest() {
        createGlobalVehicles();
        firstTire = createEntity(em);
        firstTire.setVehicle(firstVehicle);
        secondTire = createSecondEntity(em);
        secondTire.setVehicle(secondVehicle);
        thirdTire = createUpdatedEntity(em);
        thirdTire.setVehicle(updatedVehicle);

    }

    private void createGlobalTireInDB() {
        em.persist(firstTire);
        em.flush();
    }

    private void createSecondGlobalTireInDB() {
        em.persist(secondTire);
        em.flush();
    }

    private void createGlobalVehicles() {
        createGlobalFuelTypes();
        createGlobalVehicleBrands();
        createGlobalVehicleModels();
        createGlobalVehicleTypes();
        firstVehicle = VehicleControllerIT.createFirstBasicVehicle();
        firstVehicle.setFuelType(firstFuelType);
        firstVehicle.setVehicleType(firstVehicleType);
        firstVehicle.setVehicleModel(firstVehicleModel);
        em.persist(firstVehicle);
        em.flush();
        secondVehicle = VehicleControllerIT.createSecondBasicVehicle();
        secondVehicle.setFuelType(secondFuelType);
        secondVehicle.setVehicleType(secondVehicleType);
        secondVehicle.setVehicleModel(secondVehicleModel);
        em.persist(secondVehicle);
        em.flush();
        updatedVehicle = VehicleControllerIT.createUpdatedBasicVehicle();
        updatedVehicle.setFuelType(updatedFuelType);
        updatedVehicle.setVehicleType(updatedVehicleType);
        updatedVehicle.setVehicleModel(updatedVehicleModel);
        em.persist(updatedVehicle);
        em.flush();
    }

    @Test
    @Transactional
    @WithMockUser(username = "admin", authorities = {"ROLE_ADMIN"})
    public void createTire() throws Exception {
        List<Tire> allTiresBeforeTest = tireRepository.findAll();
        int sizeBeforeTest = allTiresBeforeTest.size();

        TireDTO tireDTO = tireMapper.toDto(firstTire);

        restTireMvc.perform(post(API_PATH + "/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(tireDTO))).andExpect(status().isOk());

        List<Tire> allTiresAfterTest = tireRepository.findAll();
        int sizeAfterTest = allTiresAfterTest.size();
        assertThat(sizeAfterTest).isEqualTo(sizeBeforeTest + 1);
    }
    @Test
    @Transactional
    public void createTireShouldSwitchOldTireOnVehicleToStatusStoked() throws Exception {
        tireRepository.deleteAll();
        createGlobalTireInDB();
        firstTire.setTireStatus(TireStatus.MOUNTED);
        tireRepository.saveAndFlush(firstTire);

        secondTire.setTireStatus(TireStatus.STOKED);
        secondTire.setVehicle(firstVehicle);
        List<Tire> allTiresBeforeTest = tireRepository.findAll();
        int sizeBeforeTest = allTiresBeforeTest.size();

        TireDTO tireDTO = tireMapper.toDto(secondTire);
        TireDTO tireDTOtoMount = TireDTO.builder(tireDTO).tireStatus(TireStatus.MOUNTED).build();

        restTireMvc.perform(post(API_PATH + "/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(tireDTOtoMount))).andExpect(status().isOk());

        List<Tire> allTiresAfterTest = tireRepository.findAll();
        int sizeAfterTest = allTiresAfterTest.size();
        assertThat(sizeAfterTest).isEqualTo(sizeBeforeTest + 1);
        assertThat(allTiresAfterTest.get(0).getTireStatus()).isEqualTo(TireStatus.STOKED);
        assertThat(allTiresAfterTest.get(1).getTireStatus()).isEqualTo(TireStatus.MOUNTED);
    }

    @Test
    @Transactional
    public void createTireShouldThrowBadRequestForMissingBrand() throws Exception {
        List<Tire> allTiresBeforeTest = tireRepository.findAll();

        TireDTO tireDTO = TireDTO.builder()
                .model(FIRST_MODEL)
                .width(FIRST_WIDTH)
                .profile(FIRST_PROFILE)
                .diameter(FIRST_DIAMETER)
                .tireReinforcedIndex(FIRST_REINFORCED_INDEX)
                .speedIndex(FIRST_SPEED_INDEX)
                .capacityIndex(FIRST_CAPACITY_INDEX)
                .tireSeasonType(FIRST_SEASON_TYPE)
                .type(FIRST_TIRE_TYPE)
                .runOnFlat(false)
                .createDate(DEFAULT_CREATE_DATE).build();

        restTireMvc.perform(post(API_PATH + "/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(tireDTO))).andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    public void createTireShouldThrowBadRequestForMissingModel() throws Exception {
        List<Tire> allTiresBeforeTest = tireRepository.findAll();

        TireDTO tireDTO = TireDTO.builder()
                .brand(FIRST_BRAND)
                .width(FIRST_WIDTH)
                .profile(FIRST_PROFILE)
                .diameter(FIRST_DIAMETER)
                .tireReinforcedIndex(FIRST_REINFORCED_INDEX)
                .speedIndex(FIRST_SPEED_INDEX)
                .capacityIndex(FIRST_CAPACITY_INDEX)
                .tireSeasonType(FIRST_SEASON_TYPE)
                .type(FIRST_TIRE_TYPE)
                .runOnFlat(false)
                .productionYear(FIRST_PRODUCTION_YEAR)
                .purchaseDate(FIRST_PURCHASE_DATE)
                .tireStatus(FIRST_TIRE_STATUS)
                .vehicleId(firstVehicle.getId())
                .createDate(DEFAULT_CREATE_DATE).build();

        restTireMvc.perform(post(API_PATH + "/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(tireDTO))).andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    public void createTireShouldThrowBadRequestForMissingWidth() throws Exception {
        List<Tire> allTiresBeforeTest = tireRepository.findAll();

        TireDTO tireDTO = TireDTO.builder()
                .brand(FIRST_BRAND)
                .model(FIRST_MODEL)
                .profile(FIRST_PROFILE)
                .diameter(FIRST_DIAMETER)
                .tireReinforcedIndex(FIRST_REINFORCED_INDEX)
                .speedIndex(FIRST_SPEED_INDEX)
                .capacityIndex(FIRST_CAPACITY_INDEX)
                .tireSeasonType(FIRST_SEASON_TYPE)
                .type(FIRST_TIRE_TYPE)
                .runOnFlat(false)
                .productionYear(FIRST_PRODUCTION_YEAR)
                .purchaseDate(FIRST_PURCHASE_DATE)
                .tireStatus(FIRST_TIRE_STATUS)
                .vehicleId(firstVehicle.getId())
                .createDate(DEFAULT_CREATE_DATE).build();

        restTireMvc.perform(post(API_PATH + "/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(tireDTO))).andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    public void createTireShouldThrowBadRequestForMissingProfile() throws Exception {
        List<Tire> allTiresBeforeTest = tireRepository.findAll();

        TireDTO tireDTO = TireDTO.builder()
                .brand(FIRST_BRAND)
                .model(FIRST_MODEL)
                .width(FIRST_WIDTH)
                .diameter(FIRST_DIAMETER)
                .tireReinforcedIndex(FIRST_REINFORCED_INDEX)
                .speedIndex(FIRST_SPEED_INDEX)
                .capacityIndex(FIRST_CAPACITY_INDEX)
                .tireSeasonType(FIRST_SEASON_TYPE)
                .type(FIRST_TIRE_TYPE)
                .runOnFlat(false)
                .productionYear(FIRST_PRODUCTION_YEAR)
                .purchaseDate(FIRST_PURCHASE_DATE)
                .tireStatus(FIRST_TIRE_STATUS)
                .vehicleId(firstVehicle.getId())
                .createDate(DEFAULT_CREATE_DATE).build();

        restTireMvc.perform(post(API_PATH + "/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(tireDTO))).andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    public void createTireShouldThrowBadRequestForMissingDiameter() throws Exception {
        List<Tire> allTiresBeforeTest = tireRepository.findAll();

        TireDTO tireDTO = TireDTO.builder()
                .brand(FIRST_BRAND)
                .model(FIRST_MODEL)
                .width(FIRST_WIDTH)
                .profile(FIRST_PROFILE)
                .tireReinforcedIndex(FIRST_REINFORCED_INDEX)
                .speedIndex(FIRST_SPEED_INDEX)
                .capacityIndex(FIRST_CAPACITY_INDEX)
                .tireSeasonType(FIRST_SEASON_TYPE)
                .type(FIRST_TIRE_TYPE)
                .runOnFlat(false)
                .productionYear(FIRST_PRODUCTION_YEAR)
                .purchaseDate(FIRST_PURCHASE_DATE)
                .tireStatus(FIRST_TIRE_STATUS)
                .vehicleId(firstVehicle.getId())
                .createDate(DEFAULT_CREATE_DATE).build();

        restTireMvc.perform(post(API_PATH + "/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(tireDTO))).andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    public void createTireShouldThrowBadRequestForMissingReinforcedIndex() throws Exception {
        List<Tire> allTiresBeforeTest = tireRepository.findAll();

        TireDTO tireDTO = TireDTO.builder()
                .brand(FIRST_BRAND)
                .model(FIRST_MODEL)
                .width(FIRST_WIDTH)
                .profile(FIRST_PROFILE)
                .diameter(FIRST_DIAMETER)
                .speedIndex(FIRST_SPEED_INDEX)
                .capacityIndex(FIRST_CAPACITY_INDEX)
                .tireSeasonType(FIRST_SEASON_TYPE)
                .type(FIRST_TIRE_TYPE)
                .runOnFlat(false)
                .productionYear(FIRST_PRODUCTION_YEAR)
                .purchaseDate(FIRST_PURCHASE_DATE)
                .tireStatus(FIRST_TIRE_STATUS)
                .vehicleId(firstVehicle.getId())
                .createDate(DEFAULT_CREATE_DATE).build();

        restTireMvc.perform(post(API_PATH + "/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(tireDTO))).andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    public void createTireShouldThrowBadRequestForMissingSpeedIndex() throws Exception {
        List<Tire> allTiresBeforeTest = tireRepository.findAll();

        TireDTO tireDTO = TireDTO.builder()
                .brand(FIRST_BRAND)
                .model(FIRST_MODEL)
                .width(FIRST_WIDTH)
                .profile(FIRST_PROFILE)
                .diameter(FIRST_DIAMETER)
                .tireReinforcedIndex(FIRST_REINFORCED_INDEX)
                .capacityIndex(FIRST_CAPACITY_INDEX)
                .tireSeasonType(FIRST_SEASON_TYPE)
                .type(FIRST_TIRE_TYPE)
                .runOnFlat(false)
                .productionYear(FIRST_PRODUCTION_YEAR)
                .purchaseDate(FIRST_PURCHASE_DATE)
                .tireStatus(FIRST_TIRE_STATUS)
                .vehicleId(firstVehicle.getId())
                .createDate(DEFAULT_CREATE_DATE).build();

        restTireMvc.perform(post(API_PATH + "/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(tireDTO))).andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    public void createTireShouldThrowBadRequestForMissingCapacityIndex() throws Exception {
        List<Tire> allTiresBeforeTest = tireRepository.findAll();

        TireDTO tireDTO = TireDTO.builder()
                .brand(FIRST_BRAND)
                .model(FIRST_MODEL)
                .width(FIRST_WIDTH)
                .profile(FIRST_PROFILE)
                .diameter(FIRST_DIAMETER)
                .tireReinforcedIndex(FIRST_REINFORCED_INDEX)
                .speedIndex(FIRST_SPEED_INDEX)
                .tireSeasonType(FIRST_SEASON_TYPE)
                .type(FIRST_TIRE_TYPE)
                .runOnFlat(false)
                .productionYear(FIRST_PRODUCTION_YEAR)
                .purchaseDate(FIRST_PURCHASE_DATE)
                .tireStatus(FIRST_TIRE_STATUS)
                .vehicleId(firstVehicle.getId())
                .createDate(DEFAULT_CREATE_DATE).build();

        restTireMvc.perform(post(API_PATH + "/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(tireDTO))).andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    public void createTireShouldThrowBadRequestForMissingSeasonType() throws Exception {
        List<Tire> allTiresBeforeTest = tireRepository.findAll();

        TireDTO tireDTO = TireDTO.builder()
                .brand(FIRST_BRAND)
                .model(FIRST_MODEL)
                .width(FIRST_WIDTH)
                .profile(FIRST_PROFILE)
                .diameter(FIRST_DIAMETER)
                .tireReinforcedIndex(FIRST_REINFORCED_INDEX)
                .speedIndex(FIRST_SPEED_INDEX)
                .capacityIndex(FIRST_CAPACITY_INDEX)
                .type(FIRST_TIRE_TYPE)
                .runOnFlat(false)
                .productionYear(FIRST_PRODUCTION_YEAR)
                .purchaseDate(FIRST_PURCHASE_DATE)
                .tireStatus(FIRST_TIRE_STATUS)
                .vehicleId(firstVehicle.getId())
                .createDate(DEFAULT_CREATE_DATE).build();

        restTireMvc.perform(post(API_PATH + "/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(tireDTO))).andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    public void createTireShouldThrowBadRequestForMissingType() throws Exception {
        List<Tire> allTiresBeforeTest = tireRepository.findAll();

        TireDTO tireDTO = TireDTO.builder()
                .brand(FIRST_BRAND)
                .model(FIRST_MODEL)
                .width(FIRST_WIDTH)
                .profile(FIRST_PROFILE)
                .diameter(FIRST_DIAMETER)
                .tireReinforcedIndex(FIRST_REINFORCED_INDEX)
                .speedIndex(FIRST_SPEED_INDEX)
                .capacityIndex(FIRST_CAPACITY_INDEX)
                .tireSeasonType(FIRST_SEASON_TYPE)
                .runOnFlat(false)
                .productionYear(FIRST_PRODUCTION_YEAR)
                .purchaseDate(FIRST_PURCHASE_DATE)
                .tireStatus(FIRST_TIRE_STATUS)
                .vehicleId(firstVehicle.getId())
                .createDate(DEFAULT_CREATE_DATE).build();

        restTireMvc.perform(post(API_PATH + "/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(tireDTO))).andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    public void createTireShouldThrowBadRequestForMissingRunOnFlat() throws Exception {
        List<Tire> allTiresBeforeTest = tireRepository.findAll();

        TireDTO tireDTO = TireDTO.builder()
                .brand(FIRST_BRAND)
                .model(FIRST_MODEL)
                .width(FIRST_WIDTH)
                .profile(FIRST_PROFILE)
                .diameter(FIRST_DIAMETER)
                .tireReinforcedIndex(FIRST_REINFORCED_INDEX)
                .speedIndex(FIRST_SPEED_INDEX)
                .capacityIndex(FIRST_CAPACITY_INDEX)
                .tireSeasonType(FIRST_SEASON_TYPE)
                .type(FIRST_TIRE_TYPE)
                .productionYear(FIRST_PRODUCTION_YEAR)
                .purchaseDate(FIRST_PURCHASE_DATE)
                .tireStatus(FIRST_TIRE_STATUS)
                .vehicleId(firstVehicle.getId())
                .createDate(DEFAULT_CREATE_DATE).build();

        restTireMvc.perform(post(API_PATH + "/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(tireDTO))).andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    public void createTireShouldThrowBadRequestForMissingProductionYear() throws Exception {
        List<Tire> allTiresBeforeTest = tireRepository.findAll();

        TireDTO tireDTO = TireDTO.builder()
                .brand(FIRST_BRAND)
                .model(FIRST_MODEL)
                .width(FIRST_WIDTH)
                .profile(FIRST_PROFILE)
                .diameter(FIRST_DIAMETER)
                .tireReinforcedIndex(FIRST_REINFORCED_INDEX)
                .speedIndex(FIRST_SPEED_INDEX)
                .capacityIndex(FIRST_CAPACITY_INDEX)
                .tireSeasonType(FIRST_SEASON_TYPE)
                .type(FIRST_TIRE_TYPE)
                .runOnFlat(true)
                .purchaseDate(FIRST_PURCHASE_DATE)
                .tireStatus(FIRST_TIRE_STATUS)
                .vehicleId(firstVehicle.getId())
                .createDate(DEFAULT_CREATE_DATE).build();

        restTireMvc.perform(post(API_PATH + "/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(tireDTO))).andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    public void createTireShouldThrowBadRequestForMissingPurchaseDate() throws Exception {
        List<Tire> allTiresBeforeTest = tireRepository.findAll();

        TireDTO tireDTO = TireDTO.builder()
                .brand(FIRST_BRAND)
                .model(FIRST_MODEL)
                .width(FIRST_WIDTH)
                .profile(FIRST_PROFILE)
                .diameter(FIRST_DIAMETER)
                .tireReinforcedIndex(FIRST_REINFORCED_INDEX)
                .speedIndex(FIRST_SPEED_INDEX)
                .capacityIndex(FIRST_CAPACITY_INDEX)
                .tireSeasonType(FIRST_SEASON_TYPE)
                .type(FIRST_TIRE_TYPE)
                .runOnFlat(true)
                .productionYear(FIRST_PRODUCTION_YEAR)
                .tireStatus(FIRST_TIRE_STATUS)
                .vehicleId(firstVehicle.getId())
                .createDate(DEFAULT_CREATE_DATE).build();

        restTireMvc.perform(post(API_PATH + "/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(tireDTO))).andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    public void createTireShouldThrowBadRequestForMissingTireStatus() throws Exception {
        List<Tire> allTiresBeforeTest = tireRepository.findAll();

        TireDTO tireDTO = TireDTO.builder()
                .brand(FIRST_BRAND)
                .model(FIRST_MODEL)
                .width(FIRST_WIDTH)
                .profile(FIRST_PROFILE)
                .diameter(FIRST_DIAMETER)
                .tireReinforcedIndex(FIRST_REINFORCED_INDEX)
                .speedIndex(FIRST_SPEED_INDEX)
                .capacityIndex(FIRST_CAPACITY_INDEX)
                .tireSeasonType(FIRST_SEASON_TYPE)
                .type(FIRST_TIRE_TYPE)
                .runOnFlat(true)
                .productionYear(FIRST_PRODUCTION_YEAR)
                .purchaseDate(FIRST_PURCHASE_DATE)
                .vehicleId(firstVehicle.getId())
                .createDate(DEFAULT_CREATE_DATE).build();

        restTireMvc.perform(post(API_PATH + "/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(tireDTO))).andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    public void createTireShouldThrowBadRequestForMissingVehicleId() throws Exception {
        List<Tire> allTiresBeforeTest = tireRepository.findAll();

        TireDTO tireDTO = TireDTO.builder()
                .brand(FIRST_BRAND)
                .model(FIRST_MODEL)
                .width(FIRST_WIDTH)
                .profile(FIRST_PROFILE)
                .diameter(FIRST_DIAMETER)
                .tireReinforcedIndex(FIRST_REINFORCED_INDEX)
                .speedIndex(FIRST_SPEED_INDEX)
                .capacityIndex(FIRST_CAPACITY_INDEX)
                .tireSeasonType(FIRST_SEASON_TYPE)
                .type(FIRST_TIRE_TYPE)
                .runOnFlat(true)
                .productionYear(FIRST_PRODUCTION_YEAR)
                .purchaseDate(FIRST_PURCHASE_DATE)
                .tireStatus(FIRST_TIRE_STATUS)
                .createDate(DEFAULT_CREATE_DATE).build();

        restTireMvc.perform(post(API_PATH + "/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(tireDTO))).andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    public void updateTire() throws Exception {
        createGlobalTireInDB();
        List<Tire> allTiresBeforeTest = tireRepository.findAll();
        int sizeBeforeTest = allTiresBeforeTest.size();
        thirdTire.setId(allTiresBeforeTest.get(0).getId());

        TireDTO tireDTO = tireMapper.toDto(thirdTire);

        restTireMvc.perform(put(API_PATH + "/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(tireDTO))).andExpect(status().isOk());

        List<Tire> allTiresAfterTest = tireRepository.findAll();
        int sizeAfterTest = allTiresAfterTest.size();
        assertThat(sizeAfterTest).isEqualTo(sizeBeforeTest);
    }
    @Test
    @Transactional
    public void updateTireShouldSwitchAnotherTireToStokedStatus() throws Exception {
        tireRepository.deleteAll();
        createGlobalTireInDB();
        firstTire.setTireStatus(TireStatus.MOUNTED);
        tireRepository.saveAndFlush(firstTire);
        createSecondGlobalTireInDB();
        secondTire.setTireStatus(TireStatus.STOKED);
        secondTire.setVehicle(firstVehicle);
        tireRepository.saveAndFlush(secondTire);
        List<Tire> allTiresBeforeTest = tireRepository.findAll();
        int sizeBeforeTest = allTiresBeforeTest.size();

        TireDTO tireDTO = tireMapper.toDto(secondTire);
        TireDTO updatedTireDTO = TireDTO.builder(tireDTO).tireStatus(TireStatus.MOUNTED).build();

        restTireMvc.perform(put(API_PATH + "/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(updatedTireDTO))).andExpect(status().isOk());

        List<Tire> allTiresAfterTest = tireRepository.findAll();
        int sizeAfterTest = allTiresAfterTest.size();
        assertThat(sizeAfterTest).isEqualTo(sizeBeforeTest);
        assertThat(allTiresAfterTest.get(0).getTireStatus()).isEqualTo(TireStatus.STOKED);
        assertThat(allTiresAfterTest.get(1).getTireStatus()).isEqualTo(TireStatus.MOUNTED);
    }

    @Test
    @Transactional
    public void updateTireShouldThrowBadRequestForMissingBrand() throws Exception {
        createGlobalTireInDB();
        List<Tire> allTiresBeforeTest = tireRepository.findAll();
        int sizeBeforeTest = allTiresBeforeTest.size();
        Long id = allTiresBeforeTest.get(0).getId();

        TireDTO tireDTO = TireDTO.builder()
                .id(id)
                .model(UPDATED_MODEL)
                .width(UPDATED_WIDTH)
                .profile(UPDATED_PROFILE)
                .diameter(UPDATED_DIAMETER)
                .tireReinforcedIndex(UPDATED_REINFORCED_INDEX)
                .speedIndex(UPDATED_SPEED_INDEX)
                .capacityIndex(UPDATED_CAPACITY_INDEX)
                .tireSeasonType(UPDATED_SEASON_TYPE)
                .type(UPDATED_TIRE_TYPE)
                .runOnFlat(false)
                .productionYear(UPDATED_PRODUCTION_YEAR)
                .purchaseDate(UPDATED_PURCHASE_DATE)
                .tireStatus(UPDATED_TIRE_STATUS)
                .vehicleId(secondVehicle.getId())
                .modifyDate(DEFAULT_MODIFY_DATE)
                .createDate(DEFAULT_CREATE_DATE).build();

        restTireMvc.perform(put(API_PATH + "/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(tireDTO))).andExpect(status().isBadRequest());

    }

    @Test
    @Transactional
    public void updateTireShouldThrowBadRequestForMissingModel() throws Exception {
        createGlobalTireInDB();
        List<Tire> allTiresBeforeTest = tireRepository.findAll();
        int sizeBeforeTest = allTiresBeforeTest.size();
        Long id = allTiresBeforeTest.get(0).getId();

        TireDTO tireDTO = TireDTO.builder()
                .id(id)
                .brand(UPDATED_BRAND)
                .width(UPDATED_WIDTH)
                .profile(UPDATED_PROFILE)
                .diameter(UPDATED_DIAMETER)
                .tireReinforcedIndex(UPDATED_REINFORCED_INDEX)
                .speedIndex(UPDATED_SPEED_INDEX)
                .capacityIndex(UPDATED_CAPACITY_INDEX)
                .tireSeasonType(UPDATED_SEASON_TYPE)
                .type(UPDATED_TIRE_TYPE)
                .runOnFlat(false)
                .productionYear(UPDATED_PRODUCTION_YEAR)
                .purchaseDate(UPDATED_PURCHASE_DATE)
                .tireStatus(UPDATED_TIRE_STATUS)
                .vehicleId(secondVehicle.getId())
                .modifyDate(DEFAULT_MODIFY_DATE)
                .createDate(DEFAULT_CREATE_DATE).build();

        restTireMvc.perform(put(API_PATH + "/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(tireDTO))).andExpect(status().isBadRequest());

    }

    @Test
    @Transactional
    public void updateTireShouldThrowBadRequestForMissingWidth() throws Exception {
        createGlobalTireInDB();
        List<Tire> allTiresBeforeTest = tireRepository.findAll();
        int sizeBeforeTest = allTiresBeforeTest.size();
        Long id = allTiresBeforeTest.get(0).getId();

        TireDTO tireDTO = TireDTO.builder()
                .id(id)
                .brand(UPDATED_BRAND)
                .model(UPDATED_MODEL)
                .profile(UPDATED_PROFILE)
                .diameter(UPDATED_DIAMETER)
                .tireReinforcedIndex(UPDATED_REINFORCED_INDEX)
                .speedIndex(UPDATED_SPEED_INDEX)
                .capacityIndex(UPDATED_CAPACITY_INDEX)
                .tireSeasonType(UPDATED_SEASON_TYPE)
                .type(UPDATED_TIRE_TYPE)
                .runOnFlat(false)
                .productionYear(UPDATED_PRODUCTION_YEAR)
                .purchaseDate(UPDATED_PURCHASE_DATE)
                .tireStatus(UPDATED_TIRE_STATUS)
                .vehicleId(secondVehicle.getId())
                .modifyDate(DEFAULT_MODIFY_DATE)
                .createDate(DEFAULT_CREATE_DATE).build();

        restTireMvc.perform(put(API_PATH + "/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(tireDTO))).andExpect(status().isBadRequest());

    }

    @Test
    @Transactional
    public void updateTireShouldThrowBadRequestForMissingProfile() throws Exception {
        createGlobalTireInDB();
        List<Tire> allTiresBeforeTest = tireRepository.findAll();
        int sizeBeforeTest = allTiresBeforeTest.size();
        Long id = allTiresBeforeTest.get(0).getId();

        TireDTO tireDTO = TireDTO.builder()
                .id(id)
                .brand(UPDATED_BRAND)
                .model(UPDATED_MODEL)
                .width(UPDATED_WIDTH)
                .diameter(UPDATED_DIAMETER)
                .tireReinforcedIndex(UPDATED_REINFORCED_INDEX)
                .speedIndex(UPDATED_SPEED_INDEX)
                .capacityIndex(UPDATED_CAPACITY_INDEX)
                .tireSeasonType(UPDATED_SEASON_TYPE)
                .type(UPDATED_TIRE_TYPE)
                .runOnFlat(false)
                .productionYear(UPDATED_PRODUCTION_YEAR)
                .purchaseDate(UPDATED_PURCHASE_DATE)
                .tireStatus(UPDATED_TIRE_STATUS)
                .vehicleId(secondVehicle.getId())
                .modifyDate(DEFAULT_MODIFY_DATE)
                .createDate(DEFAULT_CREATE_DATE).build();

        restTireMvc.perform(put(API_PATH + "/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(tireDTO))).andExpect(status().isBadRequest());

    }

    @Test
    @Transactional
    public void updateTireShouldThrowBadRequestForMissingDiameter() throws Exception {
        createGlobalTireInDB();
        List<Tire> allTiresBeforeTest = tireRepository.findAll();
        int sizeBeforeTest = allTiresBeforeTest.size();
        Long id = allTiresBeforeTest.get(0).getId();

        TireDTO tireDTO = TireDTO.builder()
                .id(id)
                .brand(UPDATED_BRAND)
                .model(UPDATED_MODEL)
                .width(UPDATED_WIDTH)
                .profile(UPDATED_PROFILE)
                .tireReinforcedIndex(UPDATED_REINFORCED_INDEX)
                .speedIndex(UPDATED_SPEED_INDEX)
                .capacityIndex(UPDATED_CAPACITY_INDEX)
                .tireSeasonType(UPDATED_SEASON_TYPE)
                .type(UPDATED_TIRE_TYPE)
                .runOnFlat(false)
                .productionYear(UPDATED_PRODUCTION_YEAR)
                .purchaseDate(UPDATED_PURCHASE_DATE)
                .tireStatus(UPDATED_TIRE_STATUS)
                .vehicleId(secondVehicle.getId())
                .modifyDate(DEFAULT_MODIFY_DATE)
                .createDate(DEFAULT_CREATE_DATE).build();

        restTireMvc.perform(put(API_PATH + "/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(tireDTO))).andExpect(status().isBadRequest());

    }

    @Test
    @Transactional
    public void updateTireShouldThrowBadRequestForMissingReinforcedIndex() throws Exception {
        createGlobalTireInDB();
        List<Tire> allTiresBeforeTest = tireRepository.findAll();
        int sizeBeforeTest = allTiresBeforeTest.size();
        Long id = allTiresBeforeTest.get(0).getId();

        TireDTO tireDTO = TireDTO.builder()
                .id(id)
                .brand(UPDATED_BRAND)
                .model(UPDATED_MODEL)
                .width(UPDATED_WIDTH)
                .profile(UPDATED_PROFILE)
                .diameter(UPDATED_DIAMETER)
                .speedIndex(UPDATED_SPEED_INDEX)
                .capacityIndex(UPDATED_CAPACITY_INDEX)
                .tireSeasonType(UPDATED_SEASON_TYPE)
                .type(UPDATED_TIRE_TYPE)
                .runOnFlat(false)
                .productionYear(UPDATED_PRODUCTION_YEAR)
                .purchaseDate(UPDATED_PURCHASE_DATE)
                .tireStatus(UPDATED_TIRE_STATUS)
                .vehicleId(secondVehicle.getId())
                .modifyDate(DEFAULT_MODIFY_DATE)
                .createDate(DEFAULT_CREATE_DATE).build();

        restTireMvc.perform(put(API_PATH + "/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(tireDTO))).andExpect(status().isBadRequest());

    }

    @Test
    @Transactional
    public void updateTireShouldThrowBadRequestForMissingSpeedIndex() throws Exception {
        createGlobalTireInDB();
        List<Tire> allTiresBeforeTest = tireRepository.findAll();
        int sizeBeforeTest = allTiresBeforeTest.size();
        Long id = allTiresBeforeTest.get(0).getId();

        TireDTO tireDTO = TireDTO.builder()
                .id(id)
                .brand(UPDATED_BRAND)
                .model(UPDATED_MODEL)
                .width(UPDATED_WIDTH)
                .profile(UPDATED_PROFILE)
                .diameter(UPDATED_DIAMETER)
                .tireReinforcedIndex(UPDATED_REINFORCED_INDEX)
                .capacityIndex(UPDATED_CAPACITY_INDEX)
                .tireSeasonType(UPDATED_SEASON_TYPE)
                .type(UPDATED_TIRE_TYPE)
                .runOnFlat(false)
                .productionYear(UPDATED_PRODUCTION_YEAR)
                .purchaseDate(UPDATED_PURCHASE_DATE)
                .tireStatus(UPDATED_TIRE_STATUS)
                .vehicleId(secondVehicle.getId())
                .modifyDate(DEFAULT_MODIFY_DATE)
                .createDate(DEFAULT_CREATE_DATE).build();

        restTireMvc.perform(put(API_PATH + "/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(tireDTO))).andExpect(status().isBadRequest());

    }

    @Test
    @Transactional
    public void updateTireShouldThrowBadRequestForMissingCapacityIndex() throws Exception {
        createGlobalTireInDB();
        List<Tire> allTiresBeforeTest = tireRepository.findAll();
        int sizeBeforeTest = allTiresBeforeTest.size();
        Long id = allTiresBeforeTest.get(0).getId();

        TireDTO tireDTO = TireDTO.builder()
                .id(id)
                .brand(UPDATED_BRAND)
                .model(UPDATED_MODEL)
                .width(UPDATED_WIDTH)
                .profile(UPDATED_PROFILE)
                .diameter(UPDATED_DIAMETER)
                .tireReinforcedIndex(UPDATED_REINFORCED_INDEX)
                .speedIndex(UPDATED_SPEED_INDEX)
                .tireSeasonType(UPDATED_SEASON_TYPE)
                .type(UPDATED_TIRE_TYPE)
                .runOnFlat(false)
                .productionYear(UPDATED_PRODUCTION_YEAR)
                .purchaseDate(UPDATED_PURCHASE_DATE)
                .tireStatus(UPDATED_TIRE_STATUS)
                .vehicleId(secondVehicle.getId())
                .modifyDate(DEFAULT_MODIFY_DATE)
                .createDate(DEFAULT_CREATE_DATE).build();

        restTireMvc.perform(put(API_PATH + "/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(tireDTO))).andExpect(status().isBadRequest());

    }

    @Test
    @Transactional
    public void updateTireShouldThrowBadRequestForMissingSeasonType() throws Exception {
        createGlobalTireInDB();
        List<Tire> allTiresBeforeTest = tireRepository.findAll();
        int sizeBeforeTest = allTiresBeforeTest.size();
        Long id = allTiresBeforeTest.get(0).getId();

        TireDTO tireDTO = TireDTO.builder()
                .id(id)
                .brand(UPDATED_BRAND)
                .model(UPDATED_MODEL)
                .width(UPDATED_WIDTH)
                .profile(UPDATED_PROFILE)
                .diameter(UPDATED_DIAMETER)
                .tireReinforcedIndex(UPDATED_REINFORCED_INDEX)
                .speedIndex(UPDATED_SPEED_INDEX)
                .capacityIndex(UPDATED_CAPACITY_INDEX)
                .type(UPDATED_TIRE_TYPE)
                .runOnFlat(false)
                .productionYear(UPDATED_PRODUCTION_YEAR)
                .purchaseDate(UPDATED_PURCHASE_DATE)
                .tireStatus(UPDATED_TIRE_STATUS)
                .vehicleId(secondVehicle.getId())
                .modifyDate(DEFAULT_MODIFY_DATE)
                .createDate(DEFAULT_CREATE_DATE).build();

        restTireMvc.perform(put(API_PATH + "/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(tireDTO))).andExpect(status().isBadRequest());

    }

    @Test
    @Transactional
    public void updateTireShouldThrowBadRequestForMissingType() throws Exception {
        createGlobalTireInDB();
        List<Tire> allTiresBeforeTest = tireRepository.findAll();
        int sizeBeforeTest = allTiresBeforeTest.size();
        Long id = allTiresBeforeTest.get(0).getId();

        TireDTO tireDTO = TireDTO.builder()
                .id(id)
                .brand(UPDATED_BRAND)
                .model(UPDATED_MODEL)
                .width(UPDATED_WIDTH)
                .profile(UPDATED_PROFILE)
                .diameter(UPDATED_DIAMETER)
                .tireReinforcedIndex(UPDATED_REINFORCED_INDEX)
                .speedIndex(UPDATED_SPEED_INDEX)
                .capacityIndex(UPDATED_CAPACITY_INDEX)
                .tireSeasonType(UPDATED_SEASON_TYPE)
                .runOnFlat(false)
                .productionYear(UPDATED_PRODUCTION_YEAR)
                .purchaseDate(UPDATED_PURCHASE_DATE)
                .tireStatus(UPDATED_TIRE_STATUS)
                .vehicleId(secondVehicle.getId())
                .modifyDate(DEFAULT_MODIFY_DATE)
                .createDate(DEFAULT_CREATE_DATE).build();

        restTireMvc.perform(put(API_PATH + "/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(tireDTO))).andExpect(status().isBadRequest());

    }

    @Test
    @Transactional
    public void updateTireShouldThrowBadRequestForMissingRunOnFlat() throws Exception {
        createGlobalTireInDB();
        List<Tire> allTiresBeforeTest = tireRepository.findAll();
        int sizeBeforeTest = allTiresBeforeTest.size();
        Long id = allTiresBeforeTest.get(0).getId();

        TireDTO tireDTO = TireDTO.builder()
                .id(id)
                .brand(UPDATED_BRAND)
                .model(UPDATED_MODEL)
                .width(UPDATED_WIDTH)
                .profile(UPDATED_PROFILE)
                .diameter(UPDATED_DIAMETER)
                .tireReinforcedIndex(UPDATED_REINFORCED_INDEX)
                .speedIndex(UPDATED_SPEED_INDEX)
                .capacityIndex(UPDATED_CAPACITY_INDEX)
                .tireSeasonType(UPDATED_SEASON_TYPE)
                .type(UPDATED_TIRE_TYPE)
                .productionYear(UPDATED_PRODUCTION_YEAR)
                .purchaseDate(UPDATED_PURCHASE_DATE)
                .tireStatus(UPDATED_TIRE_STATUS)
                .vehicleId(secondVehicle.getId())
                .modifyDate(DEFAULT_MODIFY_DATE)
                .createDate(DEFAULT_CREATE_DATE).build();

        restTireMvc.perform(put(API_PATH + "/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(tireDTO))).andExpect(status().isBadRequest());

    }

    @Test
    @Transactional
    public void updateTireShouldThrowBadRequestForMissingProductionYear() throws Exception {
        createGlobalTireInDB();
        List<Tire> allTiresBeforeTest = tireRepository.findAll();
        int sizeBeforeTest = allTiresBeforeTest.size();
        Long id = allTiresBeforeTest.get(0).getId();

        TireDTO tireDTO = TireDTO.builder()
                .id(id)
                .brand(UPDATED_BRAND)
                .model(UPDATED_MODEL)
                .width(UPDATED_WIDTH)
                .profile(UPDATED_PROFILE)
                .diameter(UPDATED_DIAMETER)
                .tireReinforcedIndex(UPDATED_REINFORCED_INDEX)
                .speedIndex(UPDATED_SPEED_INDEX)
                .capacityIndex(UPDATED_CAPACITY_INDEX)
                .tireSeasonType(UPDATED_SEASON_TYPE)
                .type(UPDATED_TIRE_TYPE)
                .runOnFlat(false)
                .purchaseDate(UPDATED_PURCHASE_DATE)
                .tireStatus(UPDATED_TIRE_STATUS)
                .vehicleId(secondVehicle.getId())
                .modifyDate(DEFAULT_MODIFY_DATE)
                .createDate(DEFAULT_CREATE_DATE).build();

        restTireMvc.perform(put(API_PATH + "/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(tireDTO))).andExpect(status().isBadRequest());

    }

    @Test
    @Transactional
    public void updateTireShouldThrowBadRequestForMissingPurchaseDate() throws Exception {
        createGlobalTireInDB();
        List<Tire> allTiresBeforeTest = tireRepository.findAll();
        int sizeBeforeTest = allTiresBeforeTest.size();
        Long id = allTiresBeforeTest.get(0).getId();

        TireDTO tireDTO = TireDTO.builder()
                .id(id)
                .brand(UPDATED_BRAND)
                .model(UPDATED_MODEL)
                .width(UPDATED_WIDTH)
                .profile(UPDATED_PROFILE)
                .diameter(UPDATED_DIAMETER)
                .tireReinforcedIndex(UPDATED_REINFORCED_INDEX)
                .speedIndex(UPDATED_SPEED_INDEX)
                .capacityIndex(UPDATED_CAPACITY_INDEX)
                .tireSeasonType(UPDATED_SEASON_TYPE)
                .type(UPDATED_TIRE_TYPE)
                .runOnFlat(false)
                .productionYear(UPDATED_PRODUCTION_YEAR)
                .tireStatus(UPDATED_TIRE_STATUS)
                .vehicleId(secondVehicle.getId())
                .modifyDate(DEFAULT_MODIFY_DATE)
                .createDate(DEFAULT_CREATE_DATE).build();

        restTireMvc.perform(put(API_PATH + "/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(tireDTO))).andExpect(status().isBadRequest());

    }

    @Test
    @Transactional
    public void updateTireShouldThrowBadRequestForMissingTireStatus() throws Exception {
        createGlobalTireInDB();
        List<Tire> allTiresBeforeTest = tireRepository.findAll();
        int sizeBeforeTest = allTiresBeforeTest.size();
        Long id = allTiresBeforeTest.get(0).getId();

        TireDTO tireDTO = TireDTO.builder()
                .id(id)
                .brand(UPDATED_BRAND)
                .model(UPDATED_MODEL)
                .width(UPDATED_WIDTH)
                .profile(UPDATED_PROFILE)
                .diameter(UPDATED_DIAMETER)
                .tireReinforcedIndex(UPDATED_REINFORCED_INDEX)
                .speedIndex(UPDATED_SPEED_INDEX)
                .capacityIndex(UPDATED_CAPACITY_INDEX)
                .tireSeasonType(UPDATED_SEASON_TYPE)
                .type(UPDATED_TIRE_TYPE)
                .runOnFlat(false)
                .productionYear(UPDATED_PRODUCTION_YEAR)
                .purchaseDate(UPDATED_PURCHASE_DATE)
                .vehicleId(secondVehicle.getId())
                .modifyDate(DEFAULT_MODIFY_DATE)
                .createDate(DEFAULT_CREATE_DATE).build();

        restTireMvc.perform(put(API_PATH + "/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(tireDTO))).andExpect(status().isBadRequest());

    }

    @Test
    @Transactional
    public void updateTireShouldThrowBadRequestForMissingVehicleId() throws Exception {
        createGlobalTireInDB();
        List<Tire> allTiresBeforeTest = tireRepository.findAll();
        int sizeBeforeTest = allTiresBeforeTest.size();
        Long id = allTiresBeforeTest.get(0).getId();

        TireDTO tireDTO = TireDTO.builder()
                .id(id)
                .brand(UPDATED_BRAND)
                .model(UPDATED_MODEL)
                .width(UPDATED_WIDTH)
                .profile(UPDATED_PROFILE)
                .diameter(UPDATED_DIAMETER)
                .tireReinforcedIndex(UPDATED_REINFORCED_INDEX)
                .speedIndex(UPDATED_SPEED_INDEX)
                .capacityIndex(UPDATED_CAPACITY_INDEX)
                .tireSeasonType(UPDATED_SEASON_TYPE)
                .type(UPDATED_TIRE_TYPE)
                .runOnFlat(false)
                .productionYear(UPDATED_PRODUCTION_YEAR)
                .purchaseDate(UPDATED_PURCHASE_DATE)
                .tireStatus(UPDATED_TIRE_STATUS)
                .modifyDate(DEFAULT_MODIFY_DATE)
                .createDate(DEFAULT_CREATE_DATE).build();

        restTireMvc.perform(put(API_PATH + "/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(tireDTO))).andExpect(status().isBadRequest());

    }

    @Test
    @Transactional
    public void findAllTires() throws Exception {
        createGlobalTireInDB();
        createSecondGlobalTireInDB();

        int sizeBeforeTest = tireRepository.findAll().size();

        MvcResult result = restTireMvc.perform(get(API_PATH + "/all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isNotEmpty()).andReturn();


        int sizeAfterTest = result.getResponse().getContentLength();
        String list = result.getResponse().getContentAsString();
        JSONArray jsonArray = new JSONArray(list);
        assertThat(sizeBeforeTest).isEqualTo(jsonArray.length());
    }

    @Test
    @Transactional
    public void findTireById() throws Exception {
        createGlobalTireInDB();
        List<Tire> tireList = tireRepository.findAll();
        int sizeBeforeTest = tireList.size();

        restTireMvc.perform(get(API_PATH + "/getById/{id}", firstTire.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id").value(firstTire.getId().intValue()))
                .andExpect(jsonPath("$.brand").value(firstTire.getBrand()))
                .andExpect(jsonPath("$.model").value(firstTire.getModel()))
                .andExpect(jsonPath("$.width").value(firstTire.getWidth()))
                .andExpect(jsonPath("$.profile").value(firstTire.getProfile()))
                .andExpect(jsonPath("$.diameter").value(firstTire.getDiameter()))
                .andExpect(jsonPath("$.type").value(firstTire.getType().toString()))
                .andExpect(jsonPath("$.tireReinforcedIndex").value(firstTire.getTireReinforcedIndex().toString()))
                .andExpect(jsonPath("$.speedIndex").value(firstTire.getSpeedIndex().toString()))
                .andExpect(jsonPath("$.capacityIndex").value(firstTire.getCapacityIndex().toString()))
                .andExpect(jsonPath("$.tireSeasonType").value(firstTire.getTireSeasonType().toString()))
                .andExpect(jsonPath("$.runOnFlat").value(firstTire.getRunOnFlat()))
                .andExpect(jsonPath("$.tireStatus").value(firstTire.getTireStatus().toString()))
                .andExpect(jsonPath("$.productionYear").value(firstTire.getProductionYear().intValue()))
                .andExpect(jsonPath("$.purchaseDate").value(firstTire.getPurchaseDate().toString()));
    }

    @Test
    @Transactional
    public void deleteTireById() throws Exception {
        createGlobalTireInDB();

        List<Tire> tireList = tireRepository.findAll();
        int sizeBeforeTest = tireList.size();

        restTireMvc.perform(delete(API_PATH + "/delete/{id}", firstTire.getId()))
                .andExpect(status().isOk());
        List<Tire> tireListAfterTest = tireRepository.findAll();
        int sizeAfterTest = tireListAfterTest.size();

        assertThat(sizeAfterTest).isEqualTo(sizeBeforeTest - 1);
    }

    @Test
    @Transactional
    public void findTireByBrand() throws Exception {
        createGlobalTireInDB();
        createSecondGlobalTireInDB();
        defaultTireShouldBeFound("brand=" + FIRST_BRAND);
        defaultTireShouldNotBeFound("brand=" + UPDATED_BRAND);
    }

    @Test
    @Transactional
    public void findTireByModel() throws Exception {
        createGlobalTireInDB();
        createSecondGlobalTireInDB();
        defaultTireShouldBeFound("model=" + FIRST_MODEL);
        defaultTireShouldNotBeFound("model=" + UPDATED_MODEL);
    }

    @Test
    @Transactional
    public void findTireByWidth() throws Exception {
        createGlobalTireInDB();
        createSecondGlobalTireInDB();
        defaultTireShouldBeFound("widthStartsWith=" + FIRST_WIDTH + "&widthEndWith=" + FIRST_WIDTH);
        defaultTireShouldNotBeFound("widthStartsWith=" + UPDATED_WIDTH + "&widthEndWith=" + UPDATED_WIDTH);
    }

    @Test
    @Transactional
    public void findTireByProfile() throws Exception {
        createGlobalTireInDB();
        createSecondGlobalTireInDB();
        defaultTireShouldBeFound("profileStartsWith=" + FIRST_PROFILE + "&profileEndWith=" + FIRST_PROFILE);
        defaultTireShouldNotBeFound("profileStartsWith=" + UPDATED_PROFILE + "&profileEndWith=" + UPDATED_PROFILE);
    }

    @Test
    @Transactional
    public void findTireByDiameter() throws Exception {
        createGlobalTireInDB();
        createSecondGlobalTireInDB();
        defaultTireShouldBeFound("diameterStartsWith=" + FIRST_DIAMETER + "&diameterEndWith=" + FIRST_DIAMETER);
        defaultTireShouldNotBeFound("diameterStartsWith=" + UPDATED_DIAMETER + "&diameterEndWith=" + UPDATED_DIAMETER);
    }

    @Test
    @Transactional
    public void findTireByType() throws Exception {
        createGlobalTireInDB();
        createSecondGlobalTireInDB();
        defaultTireShouldBeFound("type=" + FIRST_TIRE_TYPE);
        defaultTireShouldNotBeFound("type=" + UPDATED_TIRE_TYPE);
    }

    @Test
    @Transactional
    public void findTireByReinforcedIndex() throws Exception {
        createGlobalTireInDB();
        createSecondGlobalTireInDB();
        defaultTireShouldBeFound("reinforcedIndex=" + FIRST_REINFORCED_INDEX);
        defaultTireShouldNotBeFound("reinforcedIndex=" + UPDATED_REINFORCED_INDEX);
    }

    @Test
    @Transactional
    public void findTireBySpeedIndex() throws Exception {
        createGlobalTireInDB();
        createSecondGlobalTireInDB();
        defaultTireShouldBeFound("speedIndex=" + FIRST_SPEED_INDEX);
        defaultTireShouldNotBeFound("speedIndex=" + UPDATED_SPEED_INDEX);
    }

    @Test
    @Transactional
    public void findTireByCapacityIndex() throws Exception {
        createGlobalTireInDB();
        createSecondGlobalTireInDB();
        defaultTireShouldBeFound("loadCapacityIndex=" + FIRST_CAPACITY_INDEX);
        defaultTireShouldNotBeFound("loadCapacityIndex=" + UPDATED_CAPACITY_INDEX);
    }

    @Test
    @Transactional
    public void findTireBySeasonType() throws Exception {
        createGlobalTireInDB();
        createSecondGlobalTireInDB();
        defaultTireShouldBeFound("seasonType=" + FIRST_SEASON_TYPE);
        defaultTireShouldNotBeFound("seasonType=" + UPDATED_SEASON_TYPE);
    }

    @Test
    @Transactional
    public void findTireByRunOnFlat() throws Exception {
        createGlobalTireInDB();
        createSecondGlobalTireInDB();
        defaultTireShouldBeFound("runOnFlat=" + firstTire.getRunOnFlat());
        defaultTireShouldNotBeFound("runOnFlat=true");
    }


    private void defaultTireShouldBeFound(String filter) throws Exception {
        restTireMvc.perform(get(API_PATH + "/?sort=id,desc&" + filter)).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(firstTire.getId().intValue())))
                .andExpect(jsonPath("$.[*].brand").value(hasItem(firstTire.getBrand())))
                .andExpect(jsonPath("$.[*].model").value(hasItem(firstTire.getModel())))
                .andExpect(jsonPath("$.[*].width").value(hasItem(firstTire.getWidth())))
                .andExpect(jsonPath("$.[*].profile").value(hasItem(firstTire.getProfile())))
                .andExpect(jsonPath("$.[*].diameter").value(hasItem(firstTire.getDiameter())))
                .andExpect(jsonPath("$.[*].type").value(hasItem(firstTire.getType().toString())))
                .andExpect(jsonPath("$.[*].tireReinforcedIndex").value(hasItem(firstTire.getTireReinforcedIndex()
                        .toString())))
                .andExpect(jsonPath("$.[*].speedIndex").value(hasItem(firstTire.getSpeedIndex().toString())))
                .andExpect(jsonPath("$.[*].capacityIndex").value(hasItem(firstTire.getCapacityIndex().toString())))
                .andExpect(jsonPath("$.[*].tireSeasonType").value(hasItem(firstTire.getTireSeasonType().toString())))
                .andExpect(jsonPath("$.[*].runOnFlat").value(hasItem(firstTire.getRunOnFlat())))
                .andExpect(jsonPath("$.[*].tireStatus").value(hasItem(firstTire.getTireStatus().toString())))
                .andExpect(jsonPath("$.[*].productionYear").value(hasItem(firstTire.getProductionYear().intValue())))
                .andExpect(jsonPath("$.[*].purchaseDate").value(hasItem(firstTire.getPurchaseDate().toString())));

    }

    private void defaultTireShouldNotBeFound(String filter) throws Exception {
        restTireMvc.perform(get(API_PATH + "/?sort=id,desc&" + filter)).andExpect(status().isOk())
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

}
