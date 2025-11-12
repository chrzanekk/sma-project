package pl.com.chrzanowski.sma.scaffolding.position.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.com.chrzanowski.sma.scaffolding.position.dto.ScaffoldingLogPositionDTO;


@Service
@Transactional
public class ScaffoldingLogPositionServiceImpl implements ScaffoldingLogPositionService {
    @Override
    public ScaffoldingLogPositionDTO save(ScaffoldingLogPositionDTO createDto) {
        return null;
    }

    @Override
    public ScaffoldingLogPositionDTO update(ScaffoldingLogPositionDTO updateDto) {
        return null;
    }

    @Override
    public ScaffoldingLogPositionDTO findById(Long aLong) {
        return null;
    }

    @Override
    public void delete(Long aLong) {

    }
}
