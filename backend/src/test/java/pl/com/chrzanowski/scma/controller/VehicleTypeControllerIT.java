package pl.com.chrzanowski.scma.controller;

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
import pl.com.chrzanowski.scma.domain.VehicleType;
import pl.com.chrzanowski.scma.repository.VehicleTypeRepository;
import pl.com.chrzanowski.scma.service.VehicleTypeService;
import pl.com.chrzanowski.scma.service.dto.VehicleTypeDTO;
import pl.com.chrzanowski.scma.service.mapper.VehicleTypeMapper;

import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = ScaffoldingCompanyManagementAppApplication.class)
@AutoConfigureMockMvc
@WithMockUser
public class VehicleTypeControllerIT {

    private static final String API_PATH = "/api/vehicleTypes";

    private static final String FIRST_DEFAULT_NAME = "firstDefaultVehicleType";
    private static final String FIRST_UPDATED_NAME = "firstUpdatedVehicleType";
    private static final String FIRST_BAD_NAME = "firstBadVehicleType";
    private static final String SECOND_DEFAULT_NAME = "secondDefaultVehicleType";
    private static final String SECOND_UPDATED_NAME = "secondUpdatedVehicleType";
    private static final Instant DEFAULT_CREATE_DATE = Instant.ofEpochMilli(0L);
    private static final Instant DEFAULT_MODIFY_DATE = Instant.ofEpochMilli(36000L);
    private static final Instant DEFAULT_REMOVE_DATE = Instant.ofEpochMilli(720000L);

    @Autowired
    private VehicleTypeRepository vehicleTypeRepository;
    @Autowired
    private VehicleTypeService vehicleTypeService;
    @Autowired
    private VehicleTypeMapper vehicleTypeMapper;
    @Autowired
    MockMvc restVehicleTypeMockMvc;
    @Autowired
    private EntityManager em;
    private VehicleType vehicleType;
    private VehicleType secondVehicleType;

    public static VehicleType createEntity(EntityManager em) {
        return new VehicleType().name(FIRST_DEFAULT_NAME).createDate(DEFAULT_CREATE_DATE);
    }
    public static VehicleType createSecondEntity(EntityManager em) {
        return new VehicleType().name(SECOND_DEFAULT_NAME).createDate(DEFAULT_CREATE_DATE);
    }

    public static VehicleType createUpdatedEntity(EntityManager em) {
        return new VehicleType().name(FIRST_UPDATED_NAME).createDate(DEFAULT_CREATE_DATE)
                .modifyDate(DEFAULT_MODIFY_DATE);
    }

    @BeforeEach
    public void initTest() {
        vehicleType = createEntity(em);
    }


    @Test
    @Transactional
    public void createVehicleType() throws Exception {
        int sizeBeforeTest = vehicleTypeRepository.findAll().size();

        VehicleTypeDTO vehicleTypeDTO = vehicleTypeMapper.toDto(vehicleType);

        restVehicleTypeMockMvc.perform(post(API_PATH + "/add").contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(vehicleTypeDTO))).andExpect(status().isOk());

        List<VehicleType> allVehicleTypes = vehicleTypeRepository.findAll();
        VehicleType firstVehicleTypeFromDB = allVehicleTypes.get(0);
        int sizeAfterTest = allVehicleTypes.size();
        assertThat(sizeAfterTest).isEqualTo(sizeBeforeTest + 1);
        assertThat(firstVehicleTypeFromDB.getName()).isEqualTo(FIRST_DEFAULT_NAME);
        assertThat(firstVehicleTypeFromDB.getCreateDate()).isEqualTo(DEFAULT_CREATE_DATE);
    }

    @Test
    @Transactional
    public void createVehicleTypeShouldThrowBadExceptionIfEmptyObject() throws Exception {

        restVehicleTypeMockMvc.perform(post(API_PATH + "/add").contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(VehicleTypeDTO.builder().build())))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    public void updateVehicleType() throws Exception {
        createGlobalTwoVehicleTypes();
        int sizeBeforeTest = vehicleTypeRepository.findAll().size();

        VehicleTypeDTO vehicleTypeDTO = vehicleTypeMapper.toDto(vehicleType);
        VehicleTypeDTO vehicleTypeDTOtoUpdate =
                VehicleTypeDTO.builder().id(vehicleTypeDTO.getId()).name(FIRST_UPDATED_NAME)
                        .createDate(vehicleTypeDTO.getCreateDate()).modifyDate(DEFAULT_MODIFY_DATE).build();

        restVehicleTypeMockMvc.perform(put(API_PATH + "/update").contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(vehicleTypeDTOtoUpdate))).andExpect(status().isOk());

        List<VehicleType> allVehicleTypes = vehicleTypeRepository.findAll();
        VehicleType firstVehicleType = allVehicleTypes.get(0);
        int sizeAfterTest = allVehicleTypes.size();
        assertThat(sizeAfterTest).isEqualTo(sizeBeforeTest);
        assertThat(firstVehicleType.getId()).isEqualTo(vehicleTypeDTO.getId());
        assertThat(firstVehicleType.getName()).isEqualTo(FIRST_UPDATED_NAME);
        assertThat(firstVehicleType.getCreateDate()).isEqualTo(DEFAULT_CREATE_DATE);
        assertThat(firstVehicleType.getModifyDate()).isEqualTo(DEFAULT_MODIFY_DATE);
    }

    @Test
    @Transactional
    public void updateVehicleTypeShouldThrowBadRequestIfIdIsMissing() throws Exception {
        createGlobalTwoVehicleTypes();
        int sizeBeforeTest = vehicleTypeRepository.findAll().size();

        VehicleTypeDTO vehicleTypeDTO = vehicleTypeMapper.toDto(vehicleType);
        VehicleTypeDTO vehicleTypeDTOtoUpdate =
                VehicleTypeDTO.builder()
                        .name(FIRST_UPDATED_NAME)
                        .createDate(vehicleTypeDTO.getCreateDate())
                        .modifyDate(DEFAULT_MODIFY_DATE).build();

        restVehicleTypeMockMvc.perform(put(API_PATH + "/update").contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(vehicleTypeDTOtoUpdate))).andExpect(status().isBadRequest());

    }

    @Test
    @Transactional
    public void updateVehicleTypeShouldThrowBadRequestIfNameIsMissing() throws Exception {
        createGlobalTwoVehicleTypes();
        int sizeBeforeTest = vehicleTypeRepository.findAll().size();

        VehicleTypeDTO vehicleTypeDTO = vehicleTypeMapper.toDto(vehicleType);
        VehicleTypeDTO vehicleTypeDTOtoUpdate =
                VehicleTypeDTO.builder()
                        .id(vehicleTypeDTO.getId())
                        .createDate(vehicleTypeDTO.getCreateDate())
                        .modifyDate(DEFAULT_MODIFY_DATE).build();

        restVehicleTypeMockMvc.perform(put(API_PATH + "/update").contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(vehicleTypeDTOtoUpdate))).andExpect(status().isBadRequest());

    }

    @Test
    @Transactional
    public void findAllVehicleTypes() throws Exception {
        createGlobalTwoVehicleTypes();

        int sizeBeforeTest = vehicleTypeRepository.findAll().size();

        restVehicleTypeMockMvc.perform(get(API_PATH + "/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isNotEmpty());

        List<VehicleType> allVehicleTypes = vehicleTypeRepository.findAll();
        int sizeAfterTest = allVehicleTypes.size();
        assertThat(sizeAfterTest).isEqualTo(sizeBeforeTest);
        VehicleType firstVehicleType = allVehicleTypes.get(0);
        assertThat(firstVehicleType.getId()).isEqualTo(vehicleType.getId());
        assertThat(firstVehicleType.getName()).isEqualTo(FIRST_DEFAULT_NAME);
        assertThat(firstVehicleType.getCreateDate()).isEqualTo(DEFAULT_CREATE_DATE);
        VehicleType secondVehicleTypeFromDB = allVehicleTypes.get(1);
        assertThat(secondVehicleTypeFromDB.getId()).isEqualTo(secondVehicleType.getId());
        assertThat(secondVehicleTypeFromDB.getName()).isEqualTo(SECOND_DEFAULT_NAME);
        assertThat(secondVehicleTypeFromDB.getCreateDate()).isEqualTo(DEFAULT_CREATE_DATE);
    }

    @Test
    @Transactional
    public void findVehicleTypeWithNameFilter() throws Exception {
        createGlobalTwoVehicleTypes();
        defaultVehicleTypeShouldBeFound("name=" + FIRST_DEFAULT_NAME);
        defaultVehicleTypeShouldNotBeFound("name=" + FIRST_BAD_NAME);
    }
    @Test
    @Transactional
    public void findVehicleTypeWithIdFilter() throws Exception {
        createGlobalTwoVehicleTypes();
        defaultVehicleTypeShouldBeFound("id=" + vehicleType.getId());
        defaultVehicleTypeShouldNotBeFound("id=" + 100L);
    }
    @Test
    @Transactional
    public void findVehicleTypeWithCreateDateFilter() throws Exception {
        createGlobalTwoVehicleTypes();
        defaultVehicleTypeShouldBeFound("createDateStartWith=" + DEFAULT_CREATE_DATE.toString());
        defaultVehicleTypeShouldNotBeFound("createDateStartWith=" + DEFAULT_REMOVE_DATE.toString());
    }

    @Test
    @Transactional
    public void findUpdatedVehicleTypeWithNameFilter() throws Exception {
        createGlobalTwoUpdatedVehicleTypes();
        defaultUpdatedVehicleTypeShouldBeFound("name=" + FIRST_UPDATED_NAME);
        defaultVehicleTypeShouldNotBeFound("name=" + FIRST_BAD_NAME);
    }

    @Test
    @Transactional
    public void findVehicleTypeById() throws Exception {
        createGlobalTwoVehicleTypes();

        restVehicleTypeMockMvc.perform(get(API_PATH + "/getById/{id}", vehicleType.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id").value(vehicleType.getId().intValue()))
                .andExpect(jsonPath("$.name").value(vehicleType.getName()))
                .andExpect(jsonPath("$.createDate").value(DEFAULT_CREATE_DATE.toString()));
    }

    @Test
    @Transactional
    public void findVehicleTypeByWrongIdShouldThrowBadRequestException() throws Exception {
        createGlobalTwoVehicleTypes();

        restVehicleTypeMockMvc.perform(get(API_PATH + "/getById/{id}", 1000L))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    public void deleteVehicleTypeById() throws Exception {
        createGlobalTwoVehicleTypes();

        List<VehicleType> allBeforeTest = vehicleTypeRepository.findAll();
        int sizeBeforeTest = allBeforeTest.size();

        restVehicleTypeMockMvc.perform(delete(API_PATH + "/delete/{id}", vehicleType.getId()))
                .andExpect(status().isOk());
        List<VehicleType> allAfterTest = vehicleTypeRepository.findAll();
        int sizeAfterTest = allAfterTest.size();

        assertThat(sizeAfterTest).isEqualTo(sizeBeforeTest - 1);

    }


    private void createGlobalTwoVehicleTypes() {
        em.persist(vehicleType);
        em.flush();

        secondVehicleType = createEntity(em);
        secondVehicleType.setName(SECOND_DEFAULT_NAME);
        secondVehicleType.setCreateDate(DEFAULT_CREATE_DATE);
        em.persist(secondVehicleType);
        em.flush();
    }

    private void createGlobalTwoUpdatedVehicleTypes() {
        vehicleType.setModifyDate(DEFAULT_MODIFY_DATE);
        vehicleType.setName(FIRST_UPDATED_NAME);
        em.persist(vehicleType);
        em.flush();

        secondVehicleType = createUpdatedEntity(em);
        secondVehicleType.setName(SECOND_UPDATED_NAME);
        em.persist(secondVehicleType);
        em.flush();
    }

    private void defaultVehicleTypeShouldBeFound(String filter) throws Exception {
        restVehicleTypeMockMvc.perform(get(API_PATH + "/?sort=id,desc&" + filter)).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(vehicleType.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(vehicleType.getName())))
                .andExpect(jsonPath("$.[*].createDate").value(hasItem(DEFAULT_CREATE_DATE.toString())));
    }

    private void defaultUpdatedVehicleTypeShouldBeFound(String filter) throws Exception {
        restVehicleTypeMockMvc.perform(get(API_PATH + "/?sort=id,desc&" + filter)).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(vehicleType.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(vehicleType.getName())))
                .andExpect(jsonPath("$.[*].createDate").value(hasItem(DEFAULT_CREATE_DATE.toString())))
                .andExpect(jsonPath("$.[*].modifyDate").value(hasItem(DEFAULT_MODIFY_DATE.toString())));
    }

    private void defaultVehicleTypeShouldNotBeFound(String filter) throws Exception {
        restVehicleTypeMockMvc.perform(get(API_PATH + "/?sort=id,desc&" + filter)).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE)).andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }

}
