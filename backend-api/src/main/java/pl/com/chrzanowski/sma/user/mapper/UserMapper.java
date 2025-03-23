package pl.com.chrzanowski.sma.user.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import pl.com.chrzanowski.sma.common.mapper.EntityMapper;
import pl.com.chrzanowski.sma.company.dto.CompanyBaseDTO;
import pl.com.chrzanowski.sma.company.mapper.CompanyMapper;
import pl.com.chrzanowski.sma.company.model.Company;
import pl.com.chrzanowski.sma.role.mapper.RoleMapper;
import pl.com.chrzanowski.sma.user.dto.UserDTO;
import pl.com.chrzanowski.sma.user.model.User;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = {RoleMapper.class, CompanyMapper.class})
public abstract class UserMapper implements EntityMapper<UserDTO, User> {

    @Autowired
    protected CompanyMapper companyMapper;

    @Mapping(target = "roles", source = "roles")
    @Mapping(target = "companies", expression = "java(mapCompanies(user.getCompanies()))")
    public abstract UserDTO toDto(User user);

    @Mapping(target = "roles", source = "roles")
    @Mapping(target = "companies", source = "companies")
    public abstract User toEntity(UserDTO userDTO);

    public User fromId(Long id) {
        if (id == null) {
            return null;
        }
        User user = new User();
        user.setId(id);
        return user;
    }

    // Metoda mapująca kolekcję companies – zabezpieczona przed LazyInitializationException
    Set<CompanyBaseDTO> mapCompanies(Set<Company> companies) {
        if (companies == null || !org.hibernate.Hibernate.isInitialized(companies)) {
            return new HashSet<>();
        }
        return companies.stream()
                .map(c -> getCompanyMapper().toDto(c))
                .collect(Collectors.toSet());
    }
    // Metoda, którą MapStruct wstrzyknie – pozwala uzyskać dostęp do bean CompanyMapper
    public CompanyMapper getCompanyMapper() {
        return companyMapper;
    }
}
