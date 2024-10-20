package pl.com.chrzanowski.scma.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import pl.com.chrzanowski.scma.ScaffoldingCompanyManagementAppApplication;
import pl.com.chrzanowski.scma.controller.enumeration.WorkshopTestConstans;
import pl.com.chrzanowski.scma.domain.ServiceActionType;
import pl.com.chrzanowski.scma.domain.Workshop;
import pl.com.chrzanowski.scma.domain.enumeration.Country;
import pl.com.chrzanowski.scma.repository.ServiceActionTypeRepository;
import pl.com.chrzanowski.scma.repository.WorkshopRepository;
import pl.com.chrzanowski.scma.service.WorkshopService;
import pl.com.chrzanowski.scma.service.dto.ServiceActionTypeDTO;
import pl.com.chrzanowski.scma.service.dto.WorkshopDTO;
import pl.com.chrzanowski.scma.service.mapper.ServiceActionTypeMapper;
import pl.com.chrzanowski.scma.service.mapper.WorkshopMapper;

import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = ScaffoldingCompanyManagementAppApplication.class)
@AutoConfigureMockMvc
@WithMockUser
public class WorkshopControllerIT {
    private static final String API_PATH = "/api/workshops";

    private static final Instant DEFAULT_CREATE_DATE = Instant.ofEpochMilli(0L);
    private static final Instant DEFAULT_MODIFY_DATE = Instant.ofEpochMilli(36000L);

    @Autowired
    MockMvc restWorkshopMvc;
    @Autowired
    EntityManager em;
    @Autowired
    WorkshopRepository workshopRepository;
    @Autowired
    WorkshopService workshopService;
    @Autowired
    ServiceActionTypeRepository serviceActionTypeRepository;
    @Autowired
    ServiceActionTypeMapper serviceActionTypeMapper;
    @Autowired
    WorkshopMapper workshopMapper;
    @Autowired
    ObjectMapper objectMapper;

    private Workshop workshop;
    private Workshop secondWorkshop;
    private Workshop updatedWorkshop;
    private ServiceActionType firstServiceActionType;
    private ServiceActionType secondServiceActionType;


    public static Workshop createEntity(EntityManager em) {

        return new Workshop()
                .setName(WorkshopTestConstans.FIRST_NAME.getField())
                .setTaxNumber(WorkshopTestConstans.FIRST_TAX_NUMBER.getField())
                .setStreet(WorkshopTestConstans.FIRST_STREET.getField())
                .setBuildingNo(WorkshopTestConstans.FIRST_BUILDING_NO.getField())
                .setApartmentNo(WorkshopTestConstans.FIRST_APARTMENT_NO.getField())
                .setPostalCode(WorkshopTestConstans.FIRST_POSTAL_CODE.getField())
                .setCity(WorkshopTestConstans.FIRST_CITY.getField())
                .setCountry(Country.POLAND)
                .setCreateDate(DEFAULT_CREATE_DATE);
    }

    public static Workshop createSecondEntity(EntityManager em) {

        return new Workshop()
                .setName(WorkshopTestConstans.SECOND_NAME.getField())
                .setTaxNumber(WorkshopTestConstans.SECOND_TAX_NUMBER.getField())
                .setStreet(WorkshopTestConstans.SECOND_STREET.getField())
                .setBuildingNo(WorkshopTestConstans.SECOND_BUILDING_NO.getField())
                .setApartmentNo(WorkshopTestConstans.SECOND_APARTMENT_NO.getField())
                .setPostalCode(WorkshopTestConstans.SECOND_POSTAL_CODE.getField())
                .setCity(WorkshopTestConstans.SECOND_CITY.getField())
                .setCountry(Country.POLAND)
                .setCreateDate(DEFAULT_CREATE_DATE);
    }

    public static Workshop createUpdatedEntity(EntityManager em) {

        return new Workshop()
                .setName(WorkshopTestConstans.UPDATED_NAME.getField())
                .setTaxNumber(WorkshopTestConstans.UPDATED_TAX_NUMBER.getField())
                .setStreet(WorkshopTestConstans.UPDATED_STREET.getField())
                .setBuildingNo(WorkshopTestConstans.UPDATED_BUILDING_NO.getField())
                .setApartmentNo(WorkshopTestConstans.UPDATED_APARTMENT_NO.getField())
                .setPostalCode(WorkshopTestConstans.UPDATED_POSTAL_CODE.getField())
                .setCity(WorkshopTestConstans.UPDATED_CITY.getField())
                .setCountry(Country.POLAND)
                .setCreateDate(DEFAULT_CREATE_DATE)
                .setModifyDate(DEFAULT_MODIFY_DATE);
    }

    @BeforeEach
    public void initTest() {
        serviceActionTypeRepository.deleteAll();
        workshop = createEntity(em);
        secondWorkshop = createSecondEntity(em);
        updatedWorkshop = createUpdatedEntity(em);
    }

    @Test
    @Transactional
    public void createWorkshop() throws Exception {
        createGlobalActionTypes();
        List<Workshop> workshopListBeforeTest = workshopRepository.findAll();
        List<ServiceActionTypeDTO> serviceActionTypeListBeforeTest =
                serviceActionTypeMapper.toDto(serviceActionTypeRepository.findAll());

        int sizeBeforeTest = workshopListBeforeTest.size();

        WorkshopDTO workshopDTO = workshopMapper.toDto(workshop);
        WorkshopDTO workshopDTOtoSave = WorkshopDTO.builder()
                .name(workshopDTO.getName())
                .taxNumber(workshopDTO.getTaxNumber())
                .street(workshopDTO.getStreet())
                .buildingNo(workshopDTO.getBuildingNo())
                .apartmentNo(workshopDTO.getApartmentNo())
                .postalCode(workshopDTO.getPostalCode())
                .city(workshopDTO.getCity())
                .country(workshopDTO.getCountry())
                .createDate(workshopDTO.getCreateDate())
                .serviceActionTypes(new HashSet<>(serviceActionTypeListBeforeTest)).build();


        restWorkshopMvc.perform(post(API_PATH + "/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(workshopDTOtoSave))).andExpect(status().isOk());

        List<Workshop> workshopList = workshopRepository.findAll();
        List<ServiceActionType> serviceActionTypeList = serviceActionTypeRepository.findAll();
        Workshop firstWorkshopFromDB = workshopList.get(0);
        int sizeAfterTest = workshopList.size();
        assertThat(sizeAfterTest).isEqualTo(sizeBeforeTest + 1);
        assertThat(firstWorkshopFromDB.getName()).isEqualTo(WorkshopTestConstans.FIRST_NAME.getField());
        assertThat(firstWorkshopFromDB.getStreet()).isEqualTo(WorkshopTestConstans.FIRST_STREET.getField());
        assertThat(firstWorkshopFromDB.getBuildingNo()).isEqualTo(WorkshopTestConstans.FIRST_BUILDING_NO.getField());
        assertThat(firstWorkshopFromDB.getApartmentNo()).isEqualTo(WorkshopTestConstans.FIRST_APARTMENT_NO.getField());
        assertThat(firstWorkshopFromDB.getTaxNumber()).isEqualTo(WorkshopTestConstans.FIRST_TAX_NUMBER.getField());
        assertThat(firstWorkshopFromDB.getPostalCode()).isEqualTo(WorkshopTestConstans.FIRST_POSTAL_CODE.getField());
        assertThat(firstWorkshopFromDB.getCity()).isEqualTo(WorkshopTestConstans.FIRST_CITY.getField());
        assertThat(firstWorkshopFromDB.getCountry()).isEqualTo(Country.POLAND);
        assertThat(firstWorkshopFromDB.getCreateDate()).isEqualTo(DEFAULT_CREATE_DATE);
        assertThat(firstWorkshopFromDB.getServiceActionTypes().size()).isEqualTo(2);
    }

    @Test
    @Transactional
    public void createWorkshopShouldThrowBadRequestForNull() throws Exception {
        restWorkshopMvc.perform(post(API_PATH + "/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(null))).andExpect(status().isBadRequest());

    }

    @Test
    @Transactional
    public void createWorkshopShouldThrowExceptionForMissingName() throws Exception {
        createGlobalActionTypes();
        List<Workshop> workshopListBeforeTest = workshopRepository.findAll();
        List<ServiceActionTypeDTO> serviceActionTypeListBeforeTest =
                serviceActionTypeMapper.toDto(serviceActionTypeRepository.findAll());


        WorkshopDTO workshopDTO = workshopMapper.toDto(workshop);
        WorkshopDTO workshopDTOtoSave = WorkshopDTO.builder()
                .taxNumber(workshopDTO.getTaxNumber())
                .street(workshopDTO.getStreet())
                .buildingNo(workshopDTO.getBuildingNo())
                .apartmentNo(workshopDTO.getApartmentNo())
                .postalCode(workshopDTO.getPostalCode())
                .city(workshopDTO.getCity())
                .country(workshopDTO.getCountry())
                .createDate(workshopDTO.getCreateDate())
                .serviceActionTypes(new HashSet<>(serviceActionTypeListBeforeTest)).build();


        restWorkshopMvc.perform(post(API_PATH + "/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(workshopDTOtoSave))).andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    public void createWorkshopShouldThrowExceptionForMissingTaxNumber() throws Exception {
        createGlobalActionTypes();
        List<Workshop> workshopListBeforeTest = workshopRepository.findAll();
        List<ServiceActionTypeDTO> serviceActionTypeListBeforeTest =
                serviceActionTypeMapper.toDto(serviceActionTypeRepository.findAll());


        WorkshopDTO workshopDTO = workshopMapper.toDto(workshop);
        WorkshopDTO workshopDTOtoSave = WorkshopDTO.builder()
                .name(workshopDTO.getName())
                .street(workshopDTO.getStreet())
                .buildingNo(workshopDTO.getBuildingNo())
                .apartmentNo(workshopDTO.getApartmentNo())
                .postalCode(workshopDTO.getPostalCode())
                .city(workshopDTO.getCity())
                .country(workshopDTO.getCountry())
                .createDate(workshopDTO.getCreateDate())
                .serviceActionTypes(new HashSet<>(serviceActionTypeListBeforeTest)).build();


        restWorkshopMvc.perform(post(API_PATH + "/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(workshopDTOtoSave))).andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    public void createWorkshopShouldThrowExceptionForMissingStreet() throws Exception {
        createGlobalActionTypes();
        List<Workshop> workshopListBeforeTest = workshopRepository.findAll();
        List<ServiceActionTypeDTO> serviceActionTypeListBeforeTest =
                serviceActionTypeMapper.toDto(serviceActionTypeRepository.findAll());


        WorkshopDTO workshopDTO = workshopMapper.toDto(workshop);
        WorkshopDTO workshopDTOtoSave = WorkshopDTO.builder()
                .name(workshopDTO.getName())
                .taxNumber(workshopDTO.getTaxNumber())
                .buildingNo(workshopDTO.getBuildingNo())
                .apartmentNo(workshopDTO.getApartmentNo())
                .postalCode(workshopDTO.getPostalCode())
                .city(workshopDTO.getCity())
                .country(workshopDTO.getCountry())
                .createDate(workshopDTO.getCreateDate())
                .serviceActionTypes(new HashSet<>(serviceActionTypeListBeforeTest)).build();


        restWorkshopMvc.perform(post(API_PATH + "/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(workshopDTOtoSave))).andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    public void createWorkshopShouldThrowExceptionForMissingBuildingNo() throws Exception {
        createGlobalActionTypes();
        List<Workshop> workshopListBeforeTest = workshopRepository.findAll();
        List<ServiceActionTypeDTO> serviceActionTypeListBeforeTest =
                serviceActionTypeMapper.toDto(serviceActionTypeRepository.findAll());


        WorkshopDTO workshopDTO = workshopMapper.toDto(workshop);
        WorkshopDTO workshopDTOtoSave = WorkshopDTO.builder()
                .name(workshopDTO.getName())
                .taxNumber(workshopDTO.getTaxNumber())
                .street(workshopDTO.getStreet())
                .apartmentNo(workshopDTO.getApartmentNo())
                .postalCode(workshopDTO.getPostalCode())
                .city(workshopDTO.getCity())
                .country(workshopDTO.getCountry())
                .createDate(workshopDTO.getCreateDate())
                .serviceActionTypes(new HashSet<>(serviceActionTypeListBeforeTest)).build();


        restWorkshopMvc.perform(post(API_PATH + "/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(workshopDTOtoSave))).andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    public void createWorkshopShouldThrowExceptionForMissingPostalCode() throws Exception {
        createGlobalActionTypes();
        List<Workshop> workshopListBeforeTest = workshopRepository.findAll();
        List<ServiceActionTypeDTO> serviceActionTypeListBeforeTest =
                serviceActionTypeMapper.toDto(serviceActionTypeRepository.findAll());


        WorkshopDTO workshopDTO = workshopMapper.toDto(workshop);
        WorkshopDTO workshopDTOtoSave = WorkshopDTO.builder()
                .name(workshopDTO.getName())
                .taxNumber(workshopDTO.getTaxNumber())
                .street(workshopDTO.getStreet())
                .buildingNo(workshopDTO.getBuildingNo())
                .apartmentNo(workshopDTO.getApartmentNo())
                .city(workshopDTO.getCity())
                .country(workshopDTO.getCountry())
                .createDate(workshopDTO.getCreateDate())
                .serviceActionTypes(new HashSet<>(serviceActionTypeListBeforeTest)).build();


        restWorkshopMvc.perform(post(API_PATH + "/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(workshopDTOtoSave))).andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    public void createWorkshopShouldThrowExceptionForMissingCity() throws Exception {
        createGlobalActionTypes();
        List<Workshop> workshopListBeforeTest = workshopRepository.findAll();
        List<ServiceActionTypeDTO> serviceActionTypeListBeforeTest =
                serviceActionTypeMapper.toDto(serviceActionTypeRepository.findAll());


        WorkshopDTO workshopDTO = workshopMapper.toDto(workshop);
        WorkshopDTO workshopDTOtoSave = WorkshopDTO.builder()
                .name(workshopDTO.getName())
                .taxNumber(workshopDTO.getTaxNumber())
                .street(workshopDTO.getStreet())
                .buildingNo(workshopDTO.getBuildingNo())
                .apartmentNo(workshopDTO.getApartmentNo())
                .postalCode(workshopDTO.getPostalCode())
                .country(workshopDTO.getCountry())
                .createDate(workshopDTO.getCreateDate())
                .serviceActionTypes(new HashSet<>(serviceActionTypeListBeforeTest)).build();


        restWorkshopMvc.perform(post(API_PATH + "/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(workshopDTOtoSave))).andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    public void createWorkshopShouldThrowExceptionForMissingCountry() throws Exception {
        createGlobalActionTypes();
        List<Workshop> workshopListBeforeTest = workshopRepository.findAll();
        List<ServiceActionTypeDTO> serviceActionTypeListBeforeTest =
                serviceActionTypeMapper.toDto(serviceActionTypeRepository.findAll());


        WorkshopDTO workshopDTO = workshopMapper.toDto(workshop);
        WorkshopDTO workshopDTOtoSave = WorkshopDTO.builder()
                .name(workshopDTO.getName())
                .taxNumber(workshopDTO.getTaxNumber())
                .street(workshopDTO.getStreet())
                .buildingNo(workshopDTO.getBuildingNo())
                .apartmentNo(workshopDTO.getApartmentNo())
                .postalCode(workshopDTO.getPostalCode())
                .city(workshopDTO.getCity())
                .createDate(workshopDTO.getCreateDate())
                .serviceActionTypes(new HashSet<>(serviceActionTypeListBeforeTest)).build();


        restWorkshopMvc.perform(post(API_PATH + "/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(workshopDTOtoSave))).andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    public void createWorkshopShouldThrowExceptionForMissingServiceActionTypes() throws Exception {
        createGlobalActionTypes();
        List<Workshop> workshopListBeforeTest = workshopRepository.findAll();
        List<ServiceActionTypeDTO> serviceActionTypeListBeforeTest =
                serviceActionTypeMapper.toDto(serviceActionTypeRepository.findAll());


        WorkshopDTO workshopDTO = workshopMapper.toDto(workshop);
        WorkshopDTO workshopDTOtoSave = WorkshopDTO.builder()
                .name(workshopDTO.getName())
                .taxNumber(workshopDTO.getTaxNumber())
                .street(workshopDTO.getStreet())
                .buildingNo(workshopDTO.getBuildingNo())
                .apartmentNo(workshopDTO.getApartmentNo())
                .postalCode(workshopDTO.getPostalCode())
                .city(workshopDTO.getCity())
                .country(workshopDTO.getCountry())
                .createDate(workshopDTO.getCreateDate())
                .build();


        restWorkshopMvc.perform(post(API_PATH + "/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(workshopDTOtoSave))).andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    public void createWorkshopShouldThrowBadRequestForEmptyObject() throws Exception {
        restWorkshopMvc.perform(post(API_PATH + "/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(WorkshopDTO.builder().build())))
                .andExpect(status().isBadRequest());

    }

    @Test
    @Transactional
    public void updateWorkshop() throws Exception {
        createGlobalActionTypes();
        createGlobalWorkshopInDB();
        List<Workshop> workshopListBeforeTest = workshopRepository.findAll();
        Workshop workshopFromDB = workshopListBeforeTest.get(0);
        Long workshopId = workshopFromDB.getId();
        List<ServiceActionTypeDTO> serviceActionTypeListBeforeTest =
                serviceActionTypeMapper.toDto(serviceActionTypeRepository.findAll());
        Set<ServiceActionTypeDTO> updatedServiceActionTypes = new HashSet<>();
        updatedServiceActionTypes.add(serviceActionTypeListBeforeTest.get(0));

        int sizeBeforeTest = workshopListBeforeTest.size();
        Workshop updatedWorkshop = createUpdatedEntity(em);

        WorkshopDTO workshopDTO = workshopMapper.toDto(updatedWorkshop);
        WorkshopDTO workshopDTOtoUpdate = WorkshopDTO.builder()
                .id(workshopId)
                .name(workshopDTO.getName())
                .taxNumber(workshopDTO.getTaxNumber())
                .street(workshopDTO.getStreet())
                .buildingNo(workshopDTO.getBuildingNo())
                .apartmentNo(workshopDTO.getApartmentNo())
                .postalCode(workshopDTO.getPostalCode())
                .city(workshopDTO.getCity())
                .country(workshopDTO.getCountry())
                .createDate(workshopDTO.getCreateDate())
                .modifyDate(workshopDTO.getModifyDate())
                .serviceActionTypes(updatedServiceActionTypes).build();


        restWorkshopMvc.perform(put(API_PATH + "/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(workshopDTOtoUpdate))).andExpect(status().isOk());

        List<Workshop> workshopList = workshopRepository.findAll();
        List<ServiceActionType> serviceActionTypeList = serviceActionTypeRepository.findAll();
        Workshop firstWorkshopFromDB = workshopList.get(0);
        int sizeAfterTest = workshopList.size();
        assertThat(sizeAfterTest).isEqualTo(sizeBeforeTest);
        assertThat(firstWorkshopFromDB.getName()).isEqualTo(WorkshopTestConstans.UPDATED_NAME.getField());
        assertThat(firstWorkshopFromDB.getStreet()).isEqualTo(WorkshopTestConstans.UPDATED_STREET.getField());
        assertThat(firstWorkshopFromDB.getBuildingNo()).isEqualTo(WorkshopTestConstans.UPDATED_BUILDING_NO.getField());
        assertThat(firstWorkshopFromDB.getApartmentNo()).isEqualTo(WorkshopTestConstans.UPDATED_APARTMENT_NO.getField());
        assertThat(firstWorkshopFromDB.getTaxNumber()).isEqualTo(WorkshopTestConstans.UPDATED_TAX_NUMBER.getField());
        assertThat(firstWorkshopFromDB.getPostalCode()).isEqualTo(WorkshopTestConstans.UPDATED_POSTAL_CODE.getField());
        assertThat(firstWorkshopFromDB.getCity()).isEqualTo(WorkshopTestConstans.UPDATED_CITY.getField());
        assertThat(firstWorkshopFromDB.getCountry()).isEqualTo(Country.POLAND);
        assertThat(firstWorkshopFromDB.getCreateDate()).isEqualTo(DEFAULT_CREATE_DATE);
        assertThat(firstWorkshopFromDB.getServiceActionTypes().size()).isEqualTo(1);
    }

    @Test
    @Transactional
    public void updateWorkshopShouldThrowBadRequest() throws Exception {
        restWorkshopMvc.perform(put(API_PATH + "/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(null))).andExpect(status().isBadRequest());

    }

    @Test
    @Transactional
    public void updateWorkshopShouldThrowBadRequestForMissingId() throws Exception {
        createGlobalActionTypes();
        createGlobalWorkshopInDB();
        List<Workshop> workshopListBeforeTest = workshopRepository.findAll();
        Workshop workshopFromDB = workshopListBeforeTest.get(0);
        Long workshopId = workshopFromDB.getId();
        List<ServiceActionTypeDTO> serviceActionTypeListBeforeTest =
                serviceActionTypeMapper.toDto(serviceActionTypeRepository.findAll());
        Set<ServiceActionTypeDTO> updatedServiceActionTypes = new HashSet<>();
        updatedServiceActionTypes.add(serviceActionTypeListBeforeTest.get(0));

        int sizeBeforeTest = workshopListBeforeTest.size();
        Workshop updatedWorkshop = createUpdatedEntity(em);

        WorkshopDTO workshopDTO = workshopMapper.toDto(updatedWorkshop);
        WorkshopDTO workshopDTOtoUpdate = WorkshopDTO.builder()
                .name(workshopDTO.getName())
                .taxNumber(workshopDTO.getTaxNumber())
                .street(workshopDTO.getStreet())
                .buildingNo(workshopDTO.getBuildingNo())
                .apartmentNo(workshopDTO.getApartmentNo())
                .postalCode(workshopDTO.getPostalCode())
                .city(workshopDTO.getCity())
                .country(workshopDTO.getCountry())
                .createDate(workshopDTO.getCreateDate())
                .modifyDate(workshopDTO.getModifyDate())
                .serviceActionTypes(updatedServiceActionTypes).build();


        restWorkshopMvc.perform(put(API_PATH + "/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(workshopDTOtoUpdate))).andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    public void updateWorkshopShouldThrowBadRequestForMissingName() throws Exception {
        createGlobalActionTypes();
        createGlobalWorkshopInDB();
        List<Workshop> workshopListBeforeTest = workshopRepository.findAll();
        Workshop workshopFromDB = workshopListBeforeTest.get(0);
        Long workshopId = workshopFromDB.getId();
        List<ServiceActionTypeDTO> serviceActionTypeListBeforeTest =
                serviceActionTypeMapper.toDto(serviceActionTypeRepository.findAll());
        Set<ServiceActionTypeDTO> updatedServiceActionTypes = new HashSet<>();
        updatedServiceActionTypes.add(serviceActionTypeListBeforeTest.get(0));

        int sizeBeforeTest = workshopListBeforeTest.size();
        Workshop updatedWorkshop = createUpdatedEntity(em);

        WorkshopDTO workshopDTO = workshopMapper.toDto(updatedWorkshop);
        WorkshopDTO workshopDTOtoUpdate = WorkshopDTO.builder()
                .id(workshopId)
                .taxNumber(workshopDTO.getTaxNumber())
                .street(workshopDTO.getStreet())
                .buildingNo(workshopDTO.getBuildingNo())
                .apartmentNo(workshopDTO.getApartmentNo())
                .postalCode(workshopDTO.getPostalCode())
                .city(workshopDTO.getCity())
                .country(workshopDTO.getCountry())
                .createDate(workshopDTO.getCreateDate())
                .modifyDate(workshopDTO.getModifyDate())
                .serviceActionTypes(updatedServiceActionTypes).build();


        restWorkshopMvc.perform(put(API_PATH + "/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(workshopDTOtoUpdate))).andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    public void updateWorkshopShouldThrowBadRequestForMissingTaxNumber() throws Exception {
        createGlobalActionTypes();
        createGlobalWorkshopInDB();
        List<Workshop> workshopListBeforeTest = workshopRepository.findAll();
        Workshop workshopFromDB = workshopListBeforeTest.get(0);
        Long workshopId = workshopFromDB.getId();
        List<ServiceActionTypeDTO> serviceActionTypeListBeforeTest =
                serviceActionTypeMapper.toDto(serviceActionTypeRepository.findAll());
        Set<ServiceActionTypeDTO> updatedServiceActionTypes = new HashSet<>();
        updatedServiceActionTypes.add(serviceActionTypeListBeforeTest.get(0));

        int sizeBeforeTest = workshopListBeforeTest.size();
        Workshop updatedWorkshop = createUpdatedEntity(em);

        WorkshopDTO workshopDTO = workshopMapper.toDto(updatedWorkshop);
        WorkshopDTO workshopDTOtoUpdate = WorkshopDTO.builder()
                .id(workshopId)
                .name(workshopDTO.getName())
                .street(workshopDTO.getStreet())
                .buildingNo(workshopDTO.getBuildingNo())
                .apartmentNo(workshopDTO.getApartmentNo())
                .postalCode(workshopDTO.getPostalCode())
                .city(workshopDTO.getCity())
                .country(workshopDTO.getCountry())
                .createDate(workshopDTO.getCreateDate())
                .modifyDate(workshopDTO.getModifyDate())
                .serviceActionTypes(updatedServiceActionTypes).build();


        restWorkshopMvc.perform(put(API_PATH + "/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(workshopDTOtoUpdate))).andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    public void updateWorkshopShouldThrowBadRequestForMissingStreet() throws Exception {
        createGlobalActionTypes();
        createGlobalWorkshopInDB();
        List<Workshop> workshopListBeforeTest = workshopRepository.findAll();
        Workshop workshopFromDB = workshopListBeforeTest.get(0);
        Long workshopId = workshopFromDB.getId();
        List<ServiceActionTypeDTO> serviceActionTypeListBeforeTest =
                serviceActionTypeMapper.toDto(serviceActionTypeRepository.findAll());
        Set<ServiceActionTypeDTO> updatedServiceActionTypes = new HashSet<>();
        updatedServiceActionTypes.add(serviceActionTypeListBeforeTest.get(0));

        int sizeBeforeTest = workshopListBeforeTest.size();
        Workshop updatedWorkshop = createUpdatedEntity(em);

        WorkshopDTO workshopDTO = workshopMapper.toDto(updatedWorkshop);
        WorkshopDTO workshopDTOtoUpdate = WorkshopDTO.builder()
                .id(workshopId)
                .name(workshopDTO.getName())
                .taxNumber(workshopDTO.getTaxNumber())
                .buildingNo(workshopDTO.getBuildingNo())
                .apartmentNo(workshopDTO.getApartmentNo())
                .postalCode(workshopDTO.getPostalCode())
                .city(workshopDTO.getCity())
                .country(workshopDTO.getCountry())
                .createDate(workshopDTO.getCreateDate())
                .modifyDate(workshopDTO.getModifyDate())
                .serviceActionTypes(updatedServiceActionTypes).build();


        restWorkshopMvc.perform(put(API_PATH + "/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(workshopDTOtoUpdate))).andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    public void updateWorkshopShouldThrowBadRequestForMissingBuildingNo() throws Exception {
        createGlobalActionTypes();
        createGlobalWorkshopInDB();
        List<Workshop> workshopListBeforeTest = workshopRepository.findAll();
        Workshop workshopFromDB = workshopListBeforeTest.get(0);
        Long workshopId = workshopFromDB.getId();
        List<ServiceActionTypeDTO> serviceActionTypeListBeforeTest =
                serviceActionTypeMapper.toDto(serviceActionTypeRepository.findAll());
        Set<ServiceActionTypeDTO> updatedServiceActionTypes = new HashSet<>();
        updatedServiceActionTypes.add(serviceActionTypeListBeforeTest.get(0));

        int sizeBeforeTest = workshopListBeforeTest.size();
        Workshop updatedWorkshop = createUpdatedEntity(em);

        WorkshopDTO workshopDTO = workshopMapper.toDto(updatedWorkshop);
        WorkshopDTO workshopDTOtoUpdate = WorkshopDTO.builder()
                .id(workshopId)
                .name(workshopDTO.getName())
                .taxNumber(workshopDTO.getTaxNumber())
                .street(workshopDTO.getStreet())
                .apartmentNo(workshopDTO.getApartmentNo())
                .postalCode(workshopDTO.getPostalCode())
                .city(workshopDTO.getCity())
                .country(workshopDTO.getCountry())
                .createDate(workshopDTO.getCreateDate())
                .modifyDate(workshopDTO.getModifyDate())
                .serviceActionTypes(updatedServiceActionTypes).build();


        restWorkshopMvc.perform(put(API_PATH + "/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(workshopDTOtoUpdate))).andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    public void updateWorkshopShouldThrowBadRequestForMissingPostalCode() throws Exception {
        createGlobalActionTypes();
        createGlobalWorkshopInDB();
        List<Workshop> workshopListBeforeTest = workshopRepository.findAll();
        Workshop workshopFromDB = workshopListBeforeTest.get(0);
        Long workshopId = workshopFromDB.getId();
        List<ServiceActionTypeDTO> serviceActionTypeListBeforeTest =
                serviceActionTypeMapper.toDto(serviceActionTypeRepository.findAll());
        Set<ServiceActionTypeDTO> updatedServiceActionTypes = new HashSet<>();
        updatedServiceActionTypes.add(serviceActionTypeListBeforeTest.get(0));

        int sizeBeforeTest = workshopListBeforeTest.size();
        Workshop updatedWorkshop = createUpdatedEntity(em);

        WorkshopDTO workshopDTO = workshopMapper.toDto(updatedWorkshop);
        WorkshopDTO workshopDTOtoUpdate = WorkshopDTO.builder()
                .id(workshopId)
                .name(workshopDTO.getName())
                .taxNumber(workshopDTO.getTaxNumber())
                .street(workshopDTO.getStreet())
                .buildingNo(workshopDTO.getBuildingNo())
                .apartmentNo(workshopDTO.getApartmentNo())
                .city(workshopDTO.getCity())
                .country(workshopDTO.getCountry())
                .createDate(workshopDTO.getCreateDate())
                .modifyDate(workshopDTO.getModifyDate())
                .serviceActionTypes(updatedServiceActionTypes).build();


        restWorkshopMvc.perform(put(API_PATH + "/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(workshopDTOtoUpdate))).andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    public void updateWorkshopShouldThrowBadRequestForMissingCity() throws Exception {
        createGlobalActionTypes();
        createGlobalWorkshopInDB();
        List<Workshop> workshopListBeforeTest = workshopRepository.findAll();
        Workshop workshopFromDB = workshopListBeforeTest.get(0);
        Long workshopId = workshopFromDB.getId();
        List<ServiceActionTypeDTO> serviceActionTypeListBeforeTest =
                serviceActionTypeMapper.toDto(serviceActionTypeRepository.findAll());
        Set<ServiceActionTypeDTO> updatedServiceActionTypes = new HashSet<>();
        updatedServiceActionTypes.add(serviceActionTypeListBeforeTest.get(0));

        int sizeBeforeTest = workshopListBeforeTest.size();
        Workshop updatedWorkshop = createUpdatedEntity(em);

        WorkshopDTO workshopDTO = workshopMapper.toDto(updatedWorkshop);
        WorkshopDTO workshopDTOtoUpdate = WorkshopDTO.builder()
                .id(workshopId)
                .name(workshopDTO.getName())
                .taxNumber(workshopDTO.getTaxNumber())
                .street(workshopDTO.getStreet())
                .buildingNo(workshopDTO.getBuildingNo())
                .apartmentNo(workshopDTO.getApartmentNo())
                .postalCode(workshopDTO.getPostalCode())
                .country(workshopDTO.getCountry())
                .createDate(workshopDTO.getCreateDate())
                .modifyDate(workshopDTO.getModifyDate())
                .serviceActionTypes(updatedServiceActionTypes).build();


        restWorkshopMvc.perform(put(API_PATH + "/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(workshopDTOtoUpdate))).andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    public void updateWorkshopShouldThrowBadRequestForMissingCountry() throws Exception {
        createGlobalActionTypes();
        createGlobalWorkshopInDB();
        List<Workshop> workshopListBeforeTest = workshopRepository.findAll();
        Workshop workshopFromDB = workshopListBeforeTest.get(0);
        Long workshopId = workshopFromDB.getId();
        List<ServiceActionTypeDTO> serviceActionTypeListBeforeTest =
                serviceActionTypeMapper.toDto(serviceActionTypeRepository.findAll());
        Set<ServiceActionTypeDTO> updatedServiceActionTypes = new HashSet<>();
        updatedServiceActionTypes.add(serviceActionTypeListBeforeTest.get(0));

        int sizeBeforeTest = workshopListBeforeTest.size();
        Workshop updatedWorkshop = createUpdatedEntity(em);

        WorkshopDTO workshopDTO = workshopMapper.toDto(updatedWorkshop);
        WorkshopDTO workshopDTOtoUpdate = WorkshopDTO.builder()
                .id(workshopId)
                .name(workshopDTO.getName())
                .taxNumber(workshopDTO.getTaxNumber())
                .street(workshopDTO.getStreet())
                .buildingNo(workshopDTO.getBuildingNo())
                .apartmentNo(workshopDTO.getApartmentNo())
                .postalCode(workshopDTO.getPostalCode())
                .city(workshopDTO.getCity())
                .createDate(workshopDTO.getCreateDate())
                .modifyDate(workshopDTO.getModifyDate())
                .serviceActionTypes(updatedServiceActionTypes).build();


        restWorkshopMvc.perform(put(API_PATH + "/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(workshopDTOtoUpdate))).andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    public void updateWorkshopShouldThrowBadRequestForMissingServiceActionTypes() throws Exception {
        createGlobalActionTypes();
        createGlobalWorkshopInDB();
        List<Workshop> workshopListBeforeTest = workshopRepository.findAll();
        Workshop workshopFromDB = workshopListBeforeTest.get(0);
        Long workshopId = workshopFromDB.getId();
        List<ServiceActionTypeDTO> serviceActionTypeListBeforeTest =
                serviceActionTypeMapper.toDto(serviceActionTypeRepository.findAll());
        Set<ServiceActionTypeDTO> updatedServiceActionTypes = new HashSet<>();
        updatedServiceActionTypes.add(serviceActionTypeListBeforeTest.get(0));

        int sizeBeforeTest = workshopListBeforeTest.size();
        Workshop updatedWorkshop = createUpdatedEntity(em);

        WorkshopDTO workshopDTO = workshopMapper.toDto(updatedWorkshop);
        WorkshopDTO workshopDTOtoUpdate = WorkshopDTO.builder()
                .id(workshopId)
                .name(workshopDTO.getName())
                .taxNumber(workshopDTO.getTaxNumber())
                .street(workshopDTO.getStreet())
                .buildingNo(workshopDTO.getBuildingNo())
                .apartmentNo(workshopDTO.getApartmentNo())
                .postalCode(workshopDTO.getPostalCode())
                .city(workshopDTO.getCity())
                .country(workshopDTO.getCountry())
                .createDate(workshopDTO.getCreateDate())
                .modifyDate(workshopDTO.getModifyDate())
                .build();


        restWorkshopMvc.perform(put(API_PATH + "/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(workshopDTOtoUpdate))).andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    public void getWorkshopById() throws Exception {
        createGlobalActionTypes();
        createGlobalWorkshopInDB();
        createSecondGlobalWorkshopInDB();
        List<Workshop> workshopListBeforeTest = workshopRepository.findAll();
        Workshop workshopFromDB = workshopListBeforeTest.get(0);
        Long workshopId = workshopFromDB.getId();

        restWorkshopMvc.perform(get(API_PATH + "/getById/{id}", workshopId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id").value(workshopFromDB.getId().intValue()))
                .andExpect(jsonPath("$.name").value(workshopFromDB.getName()))
                .andExpect(jsonPath("$.street").value(workshopFromDB.getStreet()))
                .andExpect(jsonPath("$.buildingNo").value(workshopFromDB.getBuildingNo()))
                .andExpect(jsonPath("$.apartmentNo").value(workshopFromDB.getApartmentNo()))
                .andExpect(jsonPath("$.taxNumber").value(workshopFromDB.getTaxNumber()))
                .andExpect(jsonPath("$.postalCode").value(workshopFromDB.getPostalCode()))
                .andExpect(jsonPath("$.city").value(workshopFromDB.getCity()))
                .andExpect(jsonPath("$.country").value(workshopFromDB.getCountry().toString()))
                .andExpect(jsonPath("$.createDate").value(DEFAULT_CREATE_DATE.toString()));
    }

    @Test
    @Transactional
    public void getWorkshopByIdAndShouldNotBeFound() throws Exception {
        createGlobalActionTypes();
        createGlobalWorkshopInDB();
        createSecondGlobalWorkshopInDB();
        List<Workshop> workshopListBeforeTest = workshopRepository.findAll();


        restWorkshopMvc.perform(get(API_PATH + "/getById/{id}", 123L))
                .andExpect(status().isBadRequest());

    }

    @Test
    @Transactional
    public void findAllWorkshops() throws Exception {
        createGlobalActionTypes();
        createGlobalWorkshopInDB();
        createSecondGlobalWorkshopInDB();

        restWorkshopMvc.perform(get(API_PATH + "/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isNotEmpty());
    }

    @Test
    @Transactional
    public void findWorkshopWithNameFilter() throws Exception {
        createGlobalWorkshopInDB();
        List<Workshop> workshopList = workshopRepository.findAll();
        defaultWorkshopShouldBeFound("name=" + WorkshopTestConstans.FIRST_NAME.getField());
        defaultWorkshopShouldNotBeFound("name=" + WorkshopTestConstans.SECOND_NAME.getField());
    }

    @Test
    @Transactional
    public void findWorkshopWithCityFilter() throws Exception {
        createGlobalWorkshopInDB();
        List<Workshop> workshopList = workshopRepository.findAll();
        defaultWorkshopShouldBeFound("city=" + WorkshopTestConstans.FIRST_CITY.getField());
        defaultWorkshopShouldNotBeFound("city=" + WorkshopTestConstans.SECOND_CITY.getField());
    }

    @Test
    @Transactional
    public void findWorkshopWithTaxNumberFilter() throws Exception {
        createGlobalWorkshopInDB();
        List<Workshop> workshopList = workshopRepository.findAll();
        defaultWorkshopShouldBeFound("taxNumber=" + WorkshopTestConstans.FIRST_TAX_NUMBER.getField());
        defaultWorkshopShouldNotBeFound("taxNumber=" + WorkshopTestConstans.SECOND_TAX_NUMBER.getField());
    }

    @Test
    @Transactional
    public void findWorkshopWithStreetFilter() throws Exception {
        createGlobalWorkshopInDB();
        List<Workshop> workshopList = workshopRepository.findAll();
        defaultWorkshopShouldBeFound("street=" + WorkshopTestConstans.FIRST_STREET.getField());
        defaultWorkshopShouldNotBeFound("street=" + WorkshopTestConstans.SECOND_STREET.getField());
    }

    @Test
    @Transactional
    public void findWorkshopWithBuildingNoFilter() throws Exception {
        createGlobalWorkshopInDB();
        List<Workshop> workshopList = workshopRepository.findAll();
        defaultWorkshopShouldBeFound("buildingNo=" + WorkshopTestConstans.FIRST_BUILDING_NO.getField());
        defaultWorkshopShouldNotBeFound("buildingNo=" + WorkshopTestConstans.SECOND_BUILDING_NO.getField());
    }

    @Test
    @Transactional
    public void findWorkshopWithApartmentNoFilter() throws Exception {
        createGlobalWorkshopInDB();
        List<Workshop> workshopList = workshopRepository.findAll();
        defaultWorkshopShouldBeFound("apartmentNo=" + WorkshopTestConstans.FIRST_APARTMENT_NO.getField());
        defaultWorkshopShouldNotBeFound("apartmentNo=" + WorkshopTestConstans.SECOND_APARTMENT_NO.getField());
    }

    @Test
    @Transactional
    public void findWorkshopWithPostalCodeFilter() throws Exception {
        createGlobalWorkshopInDB();
        List<Workshop> workshopList = workshopRepository.findAll();
        defaultWorkshopShouldBeFound("postalCode=" + WorkshopTestConstans.FIRST_POSTAL_CODE.getField());
        defaultWorkshopShouldNotBeFound("postalCode=" + WorkshopTestConstans.SECOND_POSTAL_CODE.getField());
    }

    @Test
    @Transactional
    public void findWorkshopWithCountryFilter() throws Exception {
        createGlobalWorkshopInDB();
        List<Workshop> workshopList = workshopRepository.findAll();
        defaultWorkshopShouldBeFound("country=" + Country.POLAND);
        defaultWorkshopShouldNotBeFound("country=" + Country.ENGLAND);
    }

    @Test
    @Transactional
    public void deleteWorkshopById() throws Exception {
        createGlobalWorkshopInDB();
        List<Workshop> workshopList = workshopRepository.findAll();
        int sizeBeforeTest = workshopList.size();

        restWorkshopMvc.perform(delete(API_PATH + "/delete/{id}", workshop.getId()))
                .andExpect(status().isOk());
        List<Workshop> allAfterTest = workshopRepository.findAll();
        int sizeAfterTest = allAfterTest.size();

        Assertions.assertThat(sizeAfterTest).isEqualTo(sizeBeforeTest - 1);
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
        List<ServiceActionType> serviceActionTypes = serviceActionTypeRepository.findAll();
        Set<ServiceActionType> serviceActionTypeSet = new HashSet<>(serviceActionTypes);
        workshop.setServiceActionTypes(serviceActionTypeSet);
        em.persist(workshop);
        em.flush();
    }

    private void createSecondGlobalWorkshopInDB() {
        List<ServiceActionType> serviceActionTypes = serviceActionTypeRepository.findAll();
        Set<ServiceActionType> serviceActionTypeSet = new HashSet<>();
        serviceActionTypeSet.add(serviceActionTypes.get(0));
        secondWorkshop.setServiceActionTypes(serviceActionTypeSet);
        em.persist(secondWorkshop);
        em.flush();
    }

    private void createGlobalUpdatedWorkshopInDB() {
        List<ServiceActionType> serviceActionTypes = serviceActionTypeRepository.findAll();
        Set<ServiceActionType> serviceActionTypeSet = new HashSet<>();
        serviceActionTypeSet.add(serviceActionTypes.get(0));
        updatedWorkshop.setServiceActionTypes(serviceActionTypeSet);
        em.persist(updatedWorkshop);
        em.flush();
    }

    private void defaultWorkshopShouldBeFound(String filter) throws Exception {
        restWorkshopMvc.perform(get(API_PATH + "/?sort=id,desc&" + filter)).andExpect(status().isOk())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.[*].id").value(workshop.getId().intValue()))
                .andExpect(jsonPath("$.[*].name").value(workshop.getName()))
                .andExpect(jsonPath("$.[*].street").value(workshop.getStreet()))
                .andExpect(jsonPath("$.[*].buildingNo").value(workshop.getBuildingNo()))
                .andExpect(jsonPath("$.[*].apartmentNo").value(workshop.getApartmentNo()))
                .andExpect(jsonPath("$.[*].taxNumber").value(workshop.getTaxNumber()))
                .andExpect(jsonPath("$.[*].postalCode").value(workshop.getPostalCode()))
                .andExpect(jsonPath("$.[*].city").value(workshop.getCity()))
                .andExpect(jsonPath("$.[*].country").value(workshop.getCountry().toString()))
                .andExpect(jsonPath("$.[*].createDate").value(DEFAULT_CREATE_DATE.toString()));
    }

    private void defaultUpdatedWorkshopTypeShouldBeFound(String filter) throws Exception {
        restWorkshopMvc.perform(get(API_PATH + "/?sort=id,desc&" + filter)).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.[*].id").value(workshop.getId().intValue()))
                .andExpect(jsonPath("$.[*].name").value(workshop.getName()))
                .andExpect(jsonPath("$.[*].street").value(workshop.getStreet()))
                .andExpect(jsonPath("$.[*].buildingNo").value(workshop.getBuildingNo()))
                .andExpect(jsonPath("$.[*].apartmentNo").value(workshop.getApartmentNo()))
                .andExpect(jsonPath("$.[*].taxNumber").value(workshop.getTaxNumber()))
                .andExpect(jsonPath("$.[*].postalCode").value(workshop.getPostalCode()))
                .andExpect(jsonPath("$.[*].city").value(workshop.getCity()))
                .andExpect(jsonPath("$.[*].country").value(workshop.getCountry().toString()))
                .andExpect(jsonPath("$.[*].createDate").value(DEFAULT_CREATE_DATE.toString()))
                .andExpect(jsonPath("$.[*].createDate").value(DEFAULT_MODIFY_DATE.toString()));
    }

    private void defaultWorkshopShouldNotBeFound(String filter) throws Exception {
        restWorkshopMvc.perform(get(API_PATH + "/?sort=id,desc&" + filter)).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE)).andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }


}
