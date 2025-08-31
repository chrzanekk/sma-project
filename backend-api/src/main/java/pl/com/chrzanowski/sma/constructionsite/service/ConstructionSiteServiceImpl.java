package pl.com.chrzanowski.sma.constructionsite.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.com.chrzanowski.sma.common.exception.ConstructionSiteException;
import pl.com.chrzanowski.sma.common.exception.error.ConstructionSiteErrorCode;
import pl.com.chrzanowski.sma.constructionsite.dao.ConstructionSiteDao;
import pl.com.chrzanowski.sma.constructionsite.dto.ConstructionSiteCreateDTO;
import pl.com.chrzanowski.sma.constructionsite.dto.ConstructionSiteDTO;
import pl.com.chrzanowski.sma.constructionsite.dto.ConstructionSiteUpdateDTO;
import pl.com.chrzanowski.sma.constructionsite.mapper.ConstructionSiteDTOMapper;
import pl.com.chrzanowski.sma.constructionsite.model.ConstructionSite;
import pl.com.chrzanowski.sma.constructionsitecontractor.model.ConstructionSiteContractor;
import pl.com.chrzanowski.sma.constructionsitecontractor.service.ConstructionSiteContractorService;
import pl.com.chrzanowski.sma.contractor.dto.ContractorBaseDTO;
import pl.com.chrzanowski.sma.contractor.dto.ContractorDTO;
import pl.com.chrzanowski.sma.contractor.mapper.ContractorBaseMapper;
import pl.com.chrzanowski.sma.contractor.mapper.ContractorDTOMapper;
import pl.com.chrzanowski.sma.contractor.model.Contractor;
import pl.com.chrzanowski.sma.contractor.service.ContractorService;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class ConstructionSiteServiceImpl implements ConstructionSiteService {

    private final Logger log = LoggerFactory.getLogger(ConstructionSiteServiceImpl.class);

    private final ConstructionSiteDao constructionSiteDao;
    private final ConstructionSiteDTOMapper constructionSiteDTOMapper;
    private final ContractorDTOMapper contractorDTOMapper;
    private final ConstructionSiteContractorService constructionSiteContractorService;
    private final ContractorService contractorService;
    private final ContractorBaseMapper contractorBaseMapper;

    public ConstructionSiteServiceImpl(ConstructionSiteDao constructionSiteDao, ConstructionSiteDTOMapper constructionSiteDTOMapper, ContractorDTOMapper contractorDTOMapper, ConstructionSiteContractorService constructionSiteContractorService, ContractorService contractorService, ContractorBaseMapper contractorBaseMapper) {
        this.constructionSiteDao = constructionSiteDao;
        this.constructionSiteDTOMapper = constructionSiteDTOMapper;
        this.contractorDTOMapper = contractorDTOMapper;
        this.constructionSiteContractorService = constructionSiteContractorService;
        this.contractorService = contractorService;
        this.contractorBaseMapper = contractorBaseMapper;
    }


    @Override
    @Transactional
    public ConstructionSiteDTO save(ConstructionSiteDTO constructionSiteDTO) {
        log.debug("Request to save ConstructionSite : {}", constructionSiteDTO.getName());
        ConstructionSite constructionSite = constructionSiteDTOMapper.toEntity(ConstructionSiteDTO.builder()
                .id(null)
                .name(constructionSiteDTO.getName())
                .address(constructionSiteDTO.getAddress())
                .country(constructionSiteDTO.getCountry())
                .code(constructionSiteDTO.getCode())
                .shortName(constructionSiteDTO.getShortName())
                .company(constructionSiteDTO.getCompany())
                .build());
        constructionSite = constructionSiteDao.save(constructionSite);
        return constructionSiteDTOMapper.toDto(constructionSite);
    }

    @Override
    public ConstructionSiteDTO create(ConstructionSiteCreateDTO constructionSiteCreateDTO) {
        log.debug("Request to create ConstructionSite : {}", constructionSiteCreateDTO.getName());
        //todo save more than one contractors when creating construction site

        List<ContractorBaseDTO> allContractors = constructionSiteCreateDTO.getContractors();

        ConstructionSite entity = constructionSiteDTOMapper.toEntity(ConstructionSiteDTO.builder()
                .id(null)
                .name(constructionSiteCreateDTO.getName())
                .address(constructionSiteCreateDTO.getAddress())
                .country(constructionSiteCreateDTO.getCountry())
                .code(constructionSiteCreateDTO.getCode())
                .shortName(constructionSiteCreateDTO.getShortName())
                .company(constructionSiteCreateDTO.getCompany())
                .contractors(allContractors)
                .build());

        ConstructionSite saved = constructionSiteDao.save(entity);

        allContractors.forEach(contractor -> {
            ConstructionSiteContractor link = new ConstructionSiteContractor(saved,
                    contractorBaseMapper.toEntity(contractor));
            constructionSiteContractorService.save(link);
        });

        ConstructionSite reloaded = constructionSiteDao.findById(saved.getId())
                .orElseThrow(() -> new ConstructionSiteException(
                        ConstructionSiteErrorCode.CONSTRUCTION_SITE_NOT_FOUND,
                        "Construction site with id " + saved.getId() + " not found"));

        return constructionSiteDTOMapper.toDto(reloaded);
    }

    @Override
    @Transactional
    public ConstructionSiteDTO update(ConstructionSiteDTO constructionSiteDTO) {
        log.debug("Request to update ConstructionSite : {}", constructionSiteDTO.getId());
        ConstructionSite existingConstructionSite = constructionSiteDao.findById(constructionSiteDTO.getId())
                .orElseThrow(() -> new ConstructionSiteException(ConstructionSiteErrorCode.CONSTRUCTION_SITE_NOT_FOUND, "Construction site with id " + constructionSiteDTO.getId() + " not found"));

        constructionSiteDTOMapper.updateFromDto(constructionSiteDTO, existingConstructionSite);
        ConstructionSite constructionSite = constructionSiteDao.save(existingConstructionSite);
        return constructionSiteDTOMapper.toDto(constructionSite);
    }

    @Override
    public ConstructionSiteDTO update(ConstructionSiteUpdateDTO dto) {
        log.debug("Request to update ConstructionSite with contractors: {}", dto.getId());

        ConstructionSiteUpdateDTO updatedDTO = removeContractorConflicts(dto);

        ConstructionSite existing = constructionSiteDao.findById(dto.getId())
                .orElseThrow(() -> new ConstructionSiteException(ConstructionSiteErrorCode.CONSTRUCTION_SITE_NOT_FOUND, "Construction site with id " + updatedDTO.getId().toString() + " not found"));

        List<Contractor> allToAdd = updatedDTO.getAddedContractors().stream()
                .map(contractorDTO -> {
                    if (contractorDTO.getId() == null) {
                        ContractorDTO savedDTO = contractorService.save(contractorDTO);
                        return contractorDTOMapper.toEntity(savedDTO);
                    } else {
                        return contractorDTOMapper.toEntity(contractorDTO);
                    }
                }).toList();

        allToAdd.forEach(contractor -> {
            boolean alreadyAssigned = existing.getSiteContractors().stream()
                    .anyMatch(siteContractor -> siteContractor.getContractor().getId().equals(contractor.getId()));
            if (!alreadyAssigned) {
                ConstructionSiteContractor csc = new ConstructionSiteContractor(existing, contractor);
                existing.getSiteContractors().add(csc);
            }
        });

        updatedDTO.getDeletedContractors().forEach(cdto -> {
            existing.getSiteContractors().removeIf(csc ->
                    csc.getId().getConstructionSiteId().equals(existing.getId()) &&
                            csc.getId().getContractorId().equals(cdto.getId())
            );
            constructionSiteContractorService
                    .deleteByConstructionSiteIdAndContractorId(existing.getId(), cdto.getId());
        });

        constructionSiteDTOMapper.updateFromUpdateDto(updatedDTO, existing);

        ConstructionSite savedEntity = constructionSiteDao.save(existing);

        return constructionSiteDTOMapper.toDto(savedEntity);
    }

    private static ConstructionSiteUpdateDTO removeContractorConflicts(ConstructionSiteUpdateDTO dto) {
        if (dto.getAddedContractors().isEmpty() || dto.getDeletedContractors().isEmpty()) {
            return dto;
        }

        Set<Long> duplicateIds = dto.getAddedContractors().stream()
                .map(ContractorDTO::getId)
                .filter(Objects::nonNull)
                .filter(id -> dto.getDeletedContractors().stream()
                        .map(ContractorDTO::getId)
                        .filter(Objects::nonNull)
                        .anyMatch(deletedId -> deletedId.equals(id)))
                .collect(Collectors.toSet());

        if (duplicateIds.isEmpty()) {
            return dto;
        }

        List<ContractorDTO> filteredAdded = dto.getAddedContractors().stream()
                .filter(c -> !duplicateIds.contains(c.getId()))
                .toList();

        List<ContractorDTO> filteredDeleted = dto.getDeletedContractors().stream()
                .filter(c -> !duplicateIds.contains(c.getId()))
                .toList();

        // Tworzymy nowy obiekt DTO z przefiltrowanymi listami
        return ConstructionSiteUpdateDTO.builder()
                .id(dto.getId())
                .name(dto.getName())
                .address(dto.getAddress())
                .code(dto.getCode())
                .shortName(dto.getShortName())
                .country(dto.getCountry())
                .company(dto.getCompany())
                .addedContractors(filteredAdded)
                .deletedContractors(filteredDeleted)
                .build();
    }


    @Override
    @Transactional
    public ConstructionSiteDTO findById(Long id) {
        log.debug("Request to get ConstructionSite by id: {}", id);
        Optional<ConstructionSite> optionalConstructionSite = constructionSiteDao.findById(id);
        return constructionSiteDTOMapper.toDto(optionalConstructionSite.orElseThrow(() -> new ConstructionSiteException(ConstructionSiteErrorCode.CONSTRUCTION_SITE_NOT_FOUND, "Construction site with id " + id + " not found")));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        log.debug("Request to delete ConstructionSite : {}", id);
        constructionSiteContractorService.deleteByIdConstructionSiteId(id);
        constructionSiteDao.deleteById(id);
    }
}
