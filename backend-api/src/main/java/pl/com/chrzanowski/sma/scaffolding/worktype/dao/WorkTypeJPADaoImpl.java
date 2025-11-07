package pl.com.chrzanowski.sma.scaffolding.worktype.dao;

import com.blazebit.persistence.PagedList;
import com.blazebit.persistence.querydsl.BlazeJPAQuery;
import com.querydsl.core.BooleanBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import pl.com.chrzanowski.sma.scaffolding.worktype.model.WorkType;
import pl.com.chrzanowski.sma.scaffolding.worktype.repository.WorkTypeRepository;
import pl.com.chrzanowski.sma.scaffolding.worktype.service.filter.WorkTypeQuerySpec;

import java.util.List;
import java.util.Optional;

import static pl.com.chrzanowski.sma.company.model.QCompany.company;
import static pl.com.chrzanowski.sma.scaffolding.worktype.model.QWorkType.workType;

@Repository("worktypeJPA")
public class WorkTypeJPADaoImpl implements WorkTypeDao {

    private final Logger log = LoggerFactory.getLogger(WorkTypeJPADaoImpl.class);

    private final WorkTypeRepository workTypeRepository;
    private final WorkTypeQuerySpec worktypeQuerySpec;

    public WorkTypeJPADaoImpl(WorkTypeRepository workTypeRepository, WorkTypeQuerySpec worktypeQuerySpec) {
        this.workTypeRepository = workTypeRepository;
        this.worktypeQuerySpec = worktypeQuerySpec;
    }

    @Override
    public WorkType save(WorkType entity) {
        log.debug("DAO: Save WorkType : {}", entity.getId());
        return workTypeRepository.save(entity);
    }

    @Override
    public Optional<WorkType> findById(Long aLong) {
        log.debug("DAO: Find WorkType by id : {}", aLong);
        return workTypeRepository.findById(aLong);
    }

    @Override
    public List<WorkType> findAll(BooleanBuilder specification) {
        log.debug("DAO: Find All WorkType : {}", specification);
        return worktypeQuerySpec.buildQuery(specification, null).fetch();
    }

    @Override
    public Page<WorkType> findAll(BooleanBuilder specification, Pageable pageable) {
        log.debug("DAO: Find All WorkType with page: {}", specification);
        BlazeJPAQuery<WorkType> query = worktypeQuerySpec.buildQuery(specification, pageable);

        query.leftJoin(workType.company, company).fetch();

        PagedList<WorkType> content = query.fetchPage((int) pageable.getOffset(), pageable.getPageSize());

        return new PageImpl<>(content, pageable, content.getTotalSize());
    }

    @Override
    public void deleteById(Long aLong) {
        log.debug("DAO: Delete WorkType by id : {}", aLong);
        workTypeRepository.deleteById(aLong);
    }
}
