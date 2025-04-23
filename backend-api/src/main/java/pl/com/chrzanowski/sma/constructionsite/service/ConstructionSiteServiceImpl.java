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
import pl.com.chrzanowski.sma.constructionsite.mapper.ConstructionSiteDTOMapper;
import pl.com.chrzanowski.sma.constructionsite.model.ConstructionSite;
import pl.com.chrzanowski.sma.constructionsitecontractor.service.ConstructionSiteContractorService;

import java.util.Optional;

@Service
@Transactional
public class ConstructionSiteServiceImpl implements ConstructionSiteService {

    private final Logger log = LoggerFactory.getLogger(ConstructionSiteServiceImpl.class);

    private final ConstructionSiteDao constructionSiteDao;
    private final ConstructionSiteDTOMapper constructionSiteDTOMapper;
    private final ConstructionSiteContractorService constructionSiteContractorService;

    public ConstructionSiteServiceImpl(ConstructionSiteDao constructionSiteDao, ConstructionSiteDTOMapper constructionSiteDTOMapper, ConstructionSiteContractorService constructionSiteContractorService) {
        this.constructionSiteDao = constructionSiteDao;
        this.constructionSiteDTOMapper = constructionSiteDTOMapper;
        this.constructionSiteContractorService = constructionSiteContractorService;
    }


    @Override
    @Transactional
    public ConstructionSiteDTO save(ConstructionSiteDTO constructionSiteDTO) {
        log.debug("Request to save ConstructionSite : {}", constructionSiteDTO.getName());
        ConstructionSite constructionSite = constructionSiteDTOMapper.toEntity(constructionSiteDTO);
        constructionSite = constructionSiteDao.save(constructionSite);
        return constructionSiteDTOMapper.toDto(constructionSite);
    }

    @Override
    public ConstructionSiteDTO create(ConstructionSiteCreateDTO constructionSiteCreateDTO) {
        log.debug("Request to create ConstructionSite : {}", constructionSiteCreateDTO.getName());
        //todo first get list of contractors and check if exists etc. - find in contractor service if this is ok.
        //todo second update contractor list in constructionSite entity - new must be saved
        //todo third save new Construction site
        //todo fourth save contractors of construction site

        return null;
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
