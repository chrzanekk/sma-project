package pl.com.chrzanowski.sma.role.dao;

import com.querydsl.core.BooleanBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pl.com.chrzanowski.sma.role.model.Role;

import java.util.List;
import java.util.Optional;

public interface RoleDao {

    List<Role> findAll();

    Optional<Role> findByName(String name);

    Role saveRole(Role role);

    void deleteById(Long id);

    Optional<Role> findById(Long id);

    List<Role> findAll(BooleanBuilder specification);

    Page<Role> findAll(BooleanBuilder specification, Pageable pageable);
}
