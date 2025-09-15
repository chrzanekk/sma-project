package pl.com.chrzanowski.sma.constructionsite.service;

import pl.com.chrzanowski.sma.common.service.BaseCrudService;
import pl.com.chrzanowski.sma.constructionsite.dto.ConstructionSiteCreateDTO;
import pl.com.chrzanowski.sma.constructionsite.dto.ConstructionSiteDTO;
import pl.com.chrzanowski.sma.constructionsite.dto.ConstructionSiteUpdateDTO;

public interface ConstructionSiteService extends BaseCrudService<ConstructionSiteDTO, ConstructionSiteDTO, ConstructionSiteDTO, Long> {

    ConstructionSiteDTO create(ConstructionSiteCreateDTO constructionSiteCreateDTO);

    ConstructionSiteDTO update(ConstructionSiteUpdateDTO constructionSiteUpdateDTO);
}
