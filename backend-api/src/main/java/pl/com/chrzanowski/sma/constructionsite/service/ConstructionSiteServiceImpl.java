package pl.com.chrzanowski.sma.constructionsite.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.com.chrzanowski.sma.constructionsite.dto.ConstructionSiteBaseDTO;

@Service
@Transactional
public class ConstructionSiteServiceImpl implements ConstructionSiteService {


    @Override
    @Transactional
    public ConstructionSiteBaseDTO save(ConstructionSiteBaseDTO constructionSiteBaseDTO) {
        return null;
    }

    @Override
    @Transactional
    public ConstructionSiteBaseDTO update(ConstructionSiteBaseDTO constructionSiteBaseDTO) {
        return null;
    }

    @Override
    @Transactional
    public ConstructionSiteBaseDTO findById(Long aLong) {
        return null;
    }

    @Override
    @Transactional
    public void delete(Long aLong) {

    }
}
