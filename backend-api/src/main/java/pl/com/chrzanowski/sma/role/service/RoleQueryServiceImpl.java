package pl.com.chrzanowski.sma.role.service;

import com.querydsl.core.BooleanBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.com.chrzanowski.sma.role.dao.RoleDao;
import pl.com.chrzanowski.sma.role.dto.RoleDTO;
import pl.com.chrzanowski.sma.role.mapper.RoleMapper;
import pl.com.chrzanowski.sma.role.service.filter.RoleFilter;
import pl.com.chrzanowski.sma.role.service.filter.RoleQuerySpec;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class RoleQueryServiceImpl implements RoleQueryService {

    private final Logger log = LoggerFactory.getLogger(RoleQueryServiceImpl.class);

    private final RoleDao roleDao;
    private final RoleMapper roleMapper;

    public RoleQueryServiceImpl(RoleDao roleDao, RoleMapper roleMapper) {
        this.roleDao = roleDao;
        this.roleMapper = roleMapper;
    }

    @Override
    @Transactional
    public List<RoleDTO> findByFilter(RoleFilter filter) {
        log.debug("Query: Find all roles by filter: {}", filter);
        BooleanBuilder specification = RoleQuerySpec.buildPredicate(filter);
        return roleMapper.toDtoList(roleDao.findAll(specification));
    }

    @Override
    @Transactional
    public Page<RoleDTO> findByFilter(RoleFilter filter, Pageable pageable) {
        log.debug("Query: Find all roles by filter and page: {}", filter);
        BooleanBuilder specification = RoleQuerySpec.buildPredicate(filter);
        return roleDao.findAll(specification, pageable).map(roleMapper::toDto);
    }
}
