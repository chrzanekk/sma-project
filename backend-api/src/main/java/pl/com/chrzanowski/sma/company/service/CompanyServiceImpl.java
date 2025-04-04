package pl.com.chrzanowski.sma.company.service;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pl.com.chrzanowski.sma.common.exception.CompanyException;
import pl.com.chrzanowski.sma.common.exception.PropertyMissingException;
import pl.com.chrzanowski.sma.common.exception.error.CompanyErrorCode;
import pl.com.chrzanowski.sma.company.dao.CompanyDao;
import pl.com.chrzanowski.sma.company.dto.CompanyBaseDTO;
import pl.com.chrzanowski.sma.company.mapper.CompanyMapper;
import pl.com.chrzanowski.sma.company.model.Company;
import pl.com.chrzanowski.sma.user.service.UserService;

import java.util.List;
import java.util.Map;
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
        validateRequiredFields(companyBaseDTO);
        Company company = companyMapper.toEntity(companyBaseDTO);
        Company savedCompany = companyDao.save(company);
        return companyMapper.toDto(savedCompany);
    }

    @Override
    public CompanyBaseDTO update(CompanyBaseDTO companyBaseDTO) {
        log.debug("Request to update Company : {}", companyBaseDTO);
        validateRequiredFields(companyBaseDTO);
        Company existingCompany = companyDao.findById(companyBaseDTO.getId())
                .orElseThrow(() -> new CompanyException(CompanyErrorCode.COMPANY_NOT_FOUND, "Company not found with id " + companyBaseDTO.getId()));

        companyMapper.updateFromDto(companyBaseDTO, existingCompany);
        Company savedCompany = companyDao.save(existingCompany);
        return companyMapper.toDto(savedCompany);
    }

    @Override
    public CompanyBaseDTO findById(Long id) {
        log.debug("Request to get company by id : {}", id);
        Optional<Company> company = companyDao.findById(id);
        return companyMapper.toDto(company.orElseThrow(() -> new CompanyException(CompanyErrorCode.COMPANY_NOT_FOUND, "Company not found with id " + id)));
    }

    @Override
    public List<CompanyBaseDTO> findAll() {
        log.debug("Request to get all company");
        List<Company> allCompany = companyDao.findAll();
        return companyMapper.toDtoList(allCompany);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Company : {}", id);
        companyDao.deleteById(id);
    }

    private static void validateRequiredFields(CompanyBaseDTO companyBaseDTO) {
        if (StringUtils.isBlank(companyBaseDTO.getName())) {
            throw new PropertyMissingException(CompanyErrorCode.NAME_MISSING, "Company name is required", Map.of("name", companyBaseDTO.getName()));
        }
    }
}
