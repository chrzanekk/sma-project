package pl.com.chrzanowski.sma.unit.dao;

import com.blazebit.persistence.PagedList;
import com.blazebit.persistence.querydsl.BlazeJPAQuery;
import com.querydsl.core.BooleanBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import pl.com.chrzanowski.sma.common.enumeration.UnitType;
import pl.com.chrzanowski.sma.unit.model.Unit;
import pl.com.chrzanowski.sma.unit.repository.UnitRepository;
import pl.com.chrzanowski.sma.unit.service.filter.UnitQuerySpec;

import java.util.List;
import java.util.Optional;

@Repository("unitJPA")
public class UnitJPADaoImpl implements UnitDao {

    private final Logger log = LoggerFactory.getLogger(UnitJPADaoImpl.class);

    private final UnitRepository unitRepository;
    private final UnitQuerySpec unitQuerySpec;

    public UnitJPADaoImpl(UnitRepository unitRepository, UnitQuerySpec unitQuerySpec) {
        this.unitRepository = unitRepository;
        this.unitQuerySpec = unitQuerySpec;
    }


    @Override
    public Unit save(Unit position) {
        log.debug("DAO: Save Unit : {}", position.getSymbol());
        return unitRepository.save(position);
    }

    @Override
    public Optional<Unit> findById(Long id) {
        log.debug("DAO: Find Unit : {}", id);
        return unitRepository.findById(id);
    }

    @Override
    public Page<Unit> findAll(BooleanBuilder specification, Pageable pageable) {
        log.debug("DAO: Find all units by specification with page: {}", specification);
        BlazeJPAQuery<Unit> baseQuery = unitQuerySpec.buildQuery(specification, pageable);
        PagedList<Unit> content = baseQuery.fetchPage((int) pageable.getOffset(), pageable.getPageSize());
        return new PageImpl<>(content, pageable, content.getTotalSize());
    }

    @Override
    public List<Unit> findAll(BooleanBuilder specification) {
        log.debug("DAO: Find all units by specification: {}", specification);
        return unitQuerySpec.buildQuery(specification, null).fetch();
    }

    @Override
    public void deleteById(Long id) {
        log.debug("DAO: Delete unit : {}", id);
        unitRepository.deleteById(id);
    }

    @Override
    public Optional<Unit> findBySymbolAndCompanyId(String symbol, Long companyId) {
        log.debug("DAO: Find Unit by symbol and companyId: {}, {}", symbol, companyId);
        return unitRepository.findBySymbolAndCompanyId(symbol, companyId);
    }

    @Override
    public List<Unit> findByCompanyIdAndUnitType(Long companyId, UnitType unitType) {
        log.debug("DAO: Find Unit by company and unitType: {}, {}", companyId, unitType);
        return unitRepository.findByCompanyIdAndUnitType(companyId, unitType);
    }

    @Override
    public List<Unit> findByCompanyId(Long companyId) {
        log.debug("DAO: Find Unit by companyId: {}", companyId);
        return unitRepository.findByCompanyId(companyId);
    }
}
