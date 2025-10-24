package pl.com.chrzanowski.sma.position.dao;

import com.blazebit.persistence.PagedList;
import com.blazebit.persistence.querydsl.BlazeJPAQuery;
import com.querydsl.core.BooleanBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import pl.com.chrzanowski.sma.position.model.Position;
import pl.com.chrzanowski.sma.position.repository.PositionRepository;
import pl.com.chrzanowski.sma.position.service.filter.PositionQuerySpec;

import java.util.List;
import java.util.Optional;

@Repository("positionJPA")
public class PositionJPADaoImpl implements PositionDao {

    private final Logger log = LoggerFactory.getLogger(PositionJPADaoImpl.class);

    private final PositionRepository positionRepository;
    private final PositionQuerySpec positionQuerySpec;


    public PositionJPADaoImpl(PositionRepository positionRepository, PositionQuerySpec positionQuerySpec) {
        this.positionRepository = positionRepository;
        this.positionQuerySpec = positionQuerySpec;
    }

    @Override
    public Position save(Position position) {
        log.debug("DAO: Save Position : {}", position.getName());
        return positionRepository.save(position);
    }

    @Override
    public Optional<Position> findById(Long id) {
        log.debug("DAO: Find Position : {}", id);
        return positionRepository.findById(id);
    }

    @Override
    public Page<Position> findAll(BooleanBuilder specification, Pageable pageable) {
        log.debug("DAO: Find all positions by specification with page: {}", specification);
        BlazeJPAQuery<Position> baseQuery = positionQuerySpec.buildQuery(specification, pageable);
        PagedList<Position> content = baseQuery.fetchPage((int) pageable.getOffset(), pageable.getPageSize());
        return new PageImpl<>(content, pageable, content.getTotalSize());
    }

    @Override
    public List<Position> findAll(BooleanBuilder specification) {
        log.debug("DAO: Find all positions by specification: {}", specification);
        return positionQuerySpec.buildQuery(specification, null).fetch();
    }

    @Override
    public void deleteById(Long id) {
        log.debug("DAO: Delete Position : {}", id);
        positionRepository.deleteById(id);
    }
}
