package pl.com.chrzanowski.sma.role.dao;

import com.blazebit.persistence.PagedList;
import com.blazebit.persistence.querydsl.BlazeJPAQuery;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.JPQLQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import pl.com.chrzanowski.sma.role.model.Role;
import pl.com.chrzanowski.sma.role.repository.RoleRepository;
import pl.com.chrzanowski.sma.role.service.filter.RoleQuerySpec;

import java.util.List;
import java.util.Optional;

import static pl.com.chrzanowski.sma.contact.model.QContact.contact;

@Repository("roleJPA")
public class RoleJPADaoImpl implements RoleDao {

    private final Logger log = LoggerFactory.getLogger(RoleJPADaoImpl.class);

    private final RoleRepository roleRepository;

    private final RoleQuerySpec roleQuerySpec;

    public RoleJPADaoImpl(RoleRepository roleRepository, RoleQuerySpec roleQuerySpec) {
        this.roleRepository = roleRepository;
        this.roleQuerySpec = roleQuerySpec;
    }

    @Override
    public List<Role> findAll() {
        log.debug("JPA DAO: Fetching all roles.");
        return roleRepository.findAll();
    }

    @Override
    public Optional<Role> findByName(String name) {
        log.debug("JPA DAO: Fetching role {}", name);
        return roleRepository.findByName(name);
    }

    @Override
    public Role saveRole(Role role) {
        log.debug("JPA DAO: Adding new role {} to database", role.getName());
        return roleRepository.save(role);
    }

    @Override
    public void deleteById(Long id) {
        log.debug("JPA DAO: Deleting role with id {}", id);
        roleRepository.deleteById(id);
    }

    @Override
    public Optional<Role> findById(Long id) {
        log.debug("JPA DAO: Finding role with id {}", id);
        return roleRepository.findById(id);
    }

    @Override
    public List<Role> findAll(BooleanBuilder specification) {
        log.debug("JPA DAO: Fetching all roles by specification {}", specification);
        JPQLQuery<Role> query = roleQuerySpec.buildQuery(specification, null);
        return query.fetch();
    }

    @Override
    public Page<Role> findAll(BooleanBuilder specification, Pageable pageable) {
        log.debug("JPA DAO: Fetching all roles by specification and page {}", specification);
        BlazeJPAQuery<Role> baseQuery = roleQuerySpec.buildQuery(specification, pageable);

        PagedList<Role> content = baseQuery.fetchPage((int) pageable.getOffset(), pageable.getPageSize());
        return new PageImpl<>(content, pageable, content.getTotalSize());
    }
}
