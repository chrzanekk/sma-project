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
import pl.com.chrzanowski.scma.domain.VehicleBrand;
import pl.com.chrzanowski.scma.repository.VehicleBrandRepository;
import pl.com.chrzanowski.scma.service.VehicleBrandService;
import pl.com.chrzanowski.scma.service.dto.VehicleBrandDTO;
import pl.com.chrzanowski.scma.service.mapper.VehicleBrandMapper;

import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = ScaffoldingCompanyManagementAppApplication.class)
@AutoConfigureMockMvc
@WithMockUser
public class VehicleBrandControllerIT {


    private static final String API_PATH = "/api/vehicleBrands";

    private static final String FIRST_DEFAULT_NAME = "firstDefaultVehicleBrand";
    private static final String FIRST_UPDATED_NAME = "firstUpdatedVehicleBrand";
    private static final String FIRST_BAD_NAME = "firstBadVehicleBrand";
    private static final String SECOND_DEFAULT_NAME = "secondDefaultVehicleBrand";
    private static final String SECOND_UPDATED_NAME = "secondUpdatedVehicleBrand";
    private static final Instant DEFAULT_CREATE_DATE = Instant.ofEpochMilli(0L);
    private static final Instant DEFAULT_MODIFY_DATE = Instant.ofEpochMilli(36000L);
    private static final Instant DEFAULT_REMOVE_DATE = Instant.ofEpochMilli(720000L);

    @Autowired
    private VehicleBrandRepository vehicleBrandRepository;
    @Autowired
    private VehicleBrandService vehicleBrandService;
    @Autowired
    private VehicleBrandMapper vehicleBrandMapper;
    @Autowired
    MockMvc restVehicleBrandMockMvc;
    @Autowired
    private EntityManager em;
    private VehicleBrand vehicleBrand;
    private VehicleBrand secondVehicleBrand;

    public static VehicleBrand createEntity(EntityManager em) {
        return new VehicleBrand().name(FIRST_DEFAULT_NAME).createDate(DEFAULT_CREATE_DATE);
    }
    public static VehicleBrand createSecondEntity(EntityManager em) {
        return new VehicleBrand().name(SECOND_DEFAULT_NAME).createDate(DEFAULT_CREATE_DATE);
    }

    public static VehicleBrand createUpdatedEntity(EntityManager em) {
        return new VehicleBrand().name(FIRST_UPDATED_NAME).createDate(DEFAULT_CREATE_DATE)
                .modifyDate(DEFAULT_MODIFY_DATE);
    }

    @BeforeEach
    public void initTest() {
        vehicleBrand = createEntity(em);
    }


    @Test
    @Transactional
    public void createVehicleBrand() throws Exception {
        int sizeBeforeTest = vehicleBrandRepository.findAll().size();

        VehicleBrandDTO vehicleBrandDTO = vehicleBrandMapper.toDto(vehicleBrand);

        restVehicleBrandMockMvc.perform(post(API_PATH + "/add").contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(vehicleBrandDTO))).andExpect(status().isOk());

        List<VehicleBrand> vehicleBrandList = vehicleBrandRepository.findAll();
        VehicleBrand firstVehicleBrandFromDB = vehicleBrandList.get(0);
        int sizeAfterTest = vehicleBrandList.size();
        assertThat(sizeAfterTest).isEqualTo(sizeBeforeTest + 1);
        assertThat(firstVehicleBrandFromDB.getName()).isEqualTo(FIRST_DEFAULT_NAME);
        assertThat(firstVehicleBrandFromDB.getCreateDate()).isEqualTo(DEFAULT_CREATE_DATE);
    }

    @Test
    @Transactional
    public void createVehicleBrandShouldThrowBadRequestForEmptyObject() throws Exception {
        int sizeBeforeTest = vehicleBrandRepository.findAll().size();

        VehicleBrandDTO vehicleBrandDTO = VehicleBrandDTO.builder().build();

        restVehicleBrandMockMvc.perform(post(API_PATH + "/add").contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(vehicleBrandDTO))).andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    public void createVehicleBrandShouldThrowExceptionWhenNameIsMissing() throws Exception {
        int sizeBeforeTest = vehicleBrandRepository.findAll().size();

        VehicleBrandDTO vehicleBrandDTO = VehicleBrandDTO.builder()
                        .createDate(DEFAULT_CREATE_DATE).build();


        restVehicleBrandMockMvc.perform(post(API_PATH + "/add").contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(vehicleBrandDTO))).andExpect(status().isBadRequest());

    }

    @Test
    @Transactional
    public void updateVehicleBrand() throws Exception {
        createGlobalTwoVehicleBrands();
        int sizeBeforeTest = vehicleBrandRepository.findAll().size();

        VehicleBrandDTO vehicleBrandDTO = vehicleBrandMapper.toDto(vehicleBrand);
        VehicleBrandDTO vehicleBrandDTOtoUpdate =
                VehicleBrandDTO.builder().id(vehicleBrandDTO.getId()).name(FIRST_UPDATED_NAME)
                        .createDate(vehicleBrandDTO.getCreateDate()).modifyDate(DEFAULT_MODIFY_DATE).build();

        restVehicleBrandMockMvc.perform(put(API_PATH + "/update").contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(vehicleBrandDTOtoUpdate))).andExpect(status().isOk());

        List<VehicleBrand> allVehicleBrands = vehicleBrandRepository.findAll();
        VehicleBrand firstVehicleBrandFromDB = allVehicleBrands.get(0);
        int sizeAfterTest = allVehicleBrands.size();
        assertThat(sizeAfterTest).isEqualTo(sizeBeforeTest);
        assertThat(firstVehicleBrandFromDB.getId()).isEqualTo(vehicleBrandDTO.getId());
        assertThat(firstVehicleBrandFromDB.getName()).isEqualTo(FIRST_UPDATED_NAME);
        assertThat(firstVehicleBrandFromDB.getCreateDate()).isEqualTo(DEFAULT_CREATE_DATE);
        assertThat(firstVehicleBrandFromDB.getModifyDate()).isEqualTo(DEFAULT_MODIFY_DATE);
    }
    @Test
    @Transactional
    public void updateVehicleBrandShouldThrowBadRequestWhenIdIsMissing() throws Exception {
        createGlobalTwoVehicleBrands();
        int sizeBeforeTest = vehicleBrandRepository.findAll().size();

        VehicleBrandDTO vehicleBrandDTO = vehicleBrandMapper.toDto(vehicleBrand);
        VehicleBrandDTO vehicleBrandDTOtoUpdate =
                VehicleBrandDTO.builder()
                        .name(FIRST_UPDATED_NAME)
                        .createDate(vehicleBrandDTO.getCreateDate())
                        .modifyDate(DEFAULT_MODIFY_DATE).build();

        restVehicleBrandMockMvc.perform(put(API_PATH + "/update").contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(vehicleBrandDTOtoUpdate))).andExpect(status().isBadRequest());

    }
    @Test
    @Transactional
    public void updateVehicleBrandShouldThrowBadRequestWhenNameIsMissing() throws Exception {
        createGlobalTwoVehicleBrands();
        int sizeBeforeTest = vehicleBrandRepository.findAll().size();

        VehicleBrandDTO vehicleBrandDTO = vehicleBrandMapper.toDto(vehicleBrand);
        VehicleBrandDTO vehicleBrandDTOtoUpdate =
                VehicleBrandDTO.builder()
                        .id(vehicleBrandDTO.getId())
                        .createDate(vehicleBrandDTO.getCreateDate())
                        .modifyDate(DEFAULT_MODIFY_DATE).build();

        restVehicleBrandMockMvc.perform(put(API_PATH + "/update").contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(vehicleBrandDTOtoUpdate))).andExpect(status().isBadRequest());

    }

    @Test
    @Transactional
    public void findAllVehicleBrands() throws Exception {
        createGlobalTwoVehicleBrands();

        int sizeBeforeTest = vehicleBrandRepository.findAll().size();

        restVehicleBrandMockMvc.perform(get(API_PATH + "/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isNotEmpty());;

        List<VehicleBrand> allVehicleBrands = vehicleBrandRepository.findAll();
        int sizeAfterTest = allVehicleBrands.size();
        assertThat(sizeAfterTest).isEqualTo(sizeBeforeTest);
        VehicleBrand firstVehicleBrandFromDB = allVehicleBrands.get(0);
        assertThat(firstVehicleBrandFromDB.getId()).isEqualTo(vehicleBrand.getId());
        assertThat(firstVehicleBrandFromDB.getName()).isEqualTo(FIRST_DEFAULT_NAME);
        assertThat(firstVehicleBrandFromDB.getCreateDate()).isEqualTo(DEFAULT_CREATE_DATE);
        VehicleBrand secondVehicleBrandFromDB = allVehicleBrands.get(1);
        assertThat(secondVehicleBrandFromDB.getId()).isEqualTo(secondVehicleBrand.getId());
        assertThat(secondVehicleBrandFromDB.getName()).isEqualTo(SECOND_DEFAULT_NAME);
        assertThat(secondVehicleBrandFromDB.getCreateDate()).isEqualTo(DEFAULT_CREATE_DATE);
    }

    @Test
    @Transactional
    public void findVehicleBrandsWithNameFilter() throws Exception {
        createGlobalTwoVehicleBrands();
        defaultVehicleBrandShouldBeFound("name=" + FIRST_DEFAULT_NAME);
        defaultVehicleBrandShouldNotBeFound("name=" + FIRST_BAD_NAME);
    }
    @Test
    @Transactional
    public void findVehicleBrandsWithIdFilter() throws Exception {
        createGlobalTwoVehicleBrands();
        defaultVehicleBrandShouldBeFound("id=" + vehicleBrand.getId());
        defaultVehicleBrandShouldNotBeFound("id=" + 100L);
    }
    @Test
    @Transactional
    public void findVehicleBrandsWithCreateDateFilter() throws Exception {
        createGlobalTwoVehicleBrands();
        defaultVehicleBrandShouldBeFound("createDateStartWith=" + DEFAULT_CREATE_DATE.toString());
        defaultVehicleBrandShouldNotBeFound("createDateStartWith=" + DEFAULT_REMOVE_DATE.toString());
    }

    @Test
    @Transactional
    public void findUpdatedVehicleBrandWithNameFilter() throws Exception {
        createGlobalTwoUpdatedVehicleModels();
        defaultUpdatedVehicleBrandShouldBeFound("name=" + FIRST_UPDATED_NAME);
        defaultVehicleBrandShouldNotBeFound("name=" + FIRST_BAD_NAME);
    }

    @Test
    @Transactional
    public void findVehicleBrandById() throws Exception {
        createGlobalTwoVehicleBrands();

        restVehicleBrandMockMvc.perform(get(API_PATH + "/getById/{id}", vehicleBrand.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id").value(vehicleBrand.getId().intValue()))
                .andExpect(jsonPath("$.name").value(vehicleBrand.getName()))
                .andExpect(jsonPath("$.createDate").value(DEFAULT_CREATE_DATE.toString()));
    }

    @Test
    @Transactional
    public void deleteVehicleBrandById() throws Exception {
        createGlobalTwoVehicleBrands();

        List<VehicleBrand> allBeforeTest = vehicleBrandRepository.findAll();
        int sizeBeforeTest = allBeforeTest.size();

        restVehicleBrandMockMvc.perform(delete(API_PATH + "/delete/{id}", vehicleBrand.getId()))
                .andExpect(status().isOk());
        List<VehicleBrand> allAfterTest = vehicleBrandRepository.findAll();
        int sizeAfterTest = allAfterTest.size();

        assertThat(sizeAfterTest).isEqualTo(sizeBeforeTest - 1);

    }


    private void createGlobalTwoVehicleBrands() {
        em.persist(vehicleBrand);
        em.flush();

        secondVehicleBrand = createEntity(em);
        secondVehicleBrand.setName(SECOND_DEFAULT_NAME);
        secondVehicleBrand.setCreateDate(DEFAULT_CREATE_DATE);
        em.persist(secondVehicleBrand);
        em.flush();
    }

    private void createGlobalTwoUpdatedVehicleModels() {
        vehicleBrand.setModifyDate(DEFAULT_MODIFY_DATE);
        vehicleBrand.setName(FIRST_UPDATED_NAME);
        em.persist(vehicleBrand);
        em.flush();

        secondVehicleBrand = createUpdatedEntity(em);
        secondVehicleBrand.setName(SECOND_UPDATED_NAME);
        em.persist(secondVehicleBrand);
        em.flush();
    }

    private void defaultVehicleBrandShouldBeFound(String filter) throws Exception {
        restVehicleBrandMockMvc.perform(get(API_PATH + "/?sort=id,desc&" + filter)).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(vehicleBrand.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(vehicleBrand.getName())))
                .andExpect(jsonPath("$.[*].createDate").value(hasItem(DEFAULT_CREATE_DATE.toString())));
    }

    private void defaultUpdatedVehicleBrandShouldBeFound(String filter) throws Exception {
        restVehicleBrandMockMvc.perform(get(API_PATH + "/?sort=id,desc&" + filter)).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(vehicleBrand.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(vehicleBrand.getName())))
                .andExpect(jsonPath("$.[*].createDate").value(hasItem(DEFAULT_CREATE_DATE.toString())))
                .andExpect(jsonPath("$.[*].modifyDate").value(hasItem(DEFAULT_MODIFY_DATE.toString())));
    }

    private void defaultVehicleBrandShouldNotBeFound(String filter) throws Exception {
        restVehicleBrandMockMvc.perform(get(API_PATH + "/?sort=id,desc&" + filter)).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE)).andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }

}
