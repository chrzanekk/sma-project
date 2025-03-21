package pl.com.chrzanowski.sma.company.service;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pl.com.chrzanowski.sma.auth.dto.response.UserInfoResponse;
import pl.com.chrzanowski.sma.common.exception.CompanyException;
import pl.com.chrzanowski.sma.common.exception.error.CompanyErrorCode;
import pl.com.chrzanowski.sma.company.dao.CompanyDao;
import pl.com.chrzanowski.sma.company.dto.CompanyBaseDTO;
import pl.com.chrzanowski.sma.company.mapper.CompanyMapper;
import pl.com.chrzanowski.sma.company.model.Company;
import pl.com.chrzanowski.sma.user.model.User;
import pl.com.chrzanowski.sma.user.service.UserService;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CompanyServiceImpl implements CompanyService {

    private final Logger log = LoggerFactory.getLogger(CompanyServiceImpl.class);
    private final CompanyDao companyDao;
    private final CompanyMapper companyMapper;
    private final UserService userService;
    private final EntityManager em;

    public CompanyServiceImpl(CompanyDao companyDao, CompanyMapper companyMapper, UserService userService, EntityManager em) {
        this.companyDao = companyDao;
        this.companyMapper = companyMapper;
        this.userService = userService;
        this.em = em;
    }

    @Override
    public CompanyBaseDTO findByName(String name) {
        log.debug("Request to get company by name : {}", name);
        Optional<Company> company = companyDao.findByName(name);
        return company.map(companyMapper::toDto).orElseThrow(() -> new CompanyException(CompanyErrorCode.COMPANY_NOT_FOUND, "Company not found with name " + name));
    }

    @Override
    public CompanyBaseDTO save(CompanyBaseDTO companyBaseDTO) {
        log.debug("Request to save Company : {}", companyBaseDTO);

        UserInfoResponse loggedUser = userService.getUserWithAuthorities();

        Company company = companyMapper.toEntity(companyBaseDTO);
        company.setCreatedBy(em.getReference(User.class, loggedUser.id()));
        company.setModifiedBy(em.getReference(User.class, loggedUser.id()));
        company.setCreatedDatetime(Instant.now());
        company.setLastModifiedDatetime(Instant.now());
        Company savedCompany = companyDao.save(company);
        return companyMapper.toDto(savedCompany);
    }

    @Override
    public CompanyBaseDTO update(CompanyBaseDTO companyBaseDTO) {
        return null;
    }

    @Override
    public CompanyBaseDTO findById(Long aLong) {
        return null;
    }

    @Override
    public List<CompanyBaseDTO> findAll() {
        return List.of();
    }

    @Override
    public void delete(Long aLong) {

    }
}
