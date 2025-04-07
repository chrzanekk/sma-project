package pl.com.chrzanowski.sma.company.service;

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
import pl.com.chrzanowski.sma.company.mapper.CompanyBaseMapper;
import pl.com.chrzanowski.sma.company.model.Company;

import java.util.Map;
import java.util.Optional;

@Service
@Transactional
public class CompanyServiceImpl implements CompanyService {

    private final Logger log = LoggerFactory.getLogger(CompanyServiceImpl.class);
    private final CompanyDao companyDao;
    private final CompanyBaseMapper companyBaseMapper;

    public CompanyServiceImpl(CompanyDao companyDao, CompanyBaseMapper companyBaseMapper) {
        this.companyDao = companyDao;
        this.companyBaseMapper = companyBaseMapper;
    }


    @Override
    public CompanyBaseDTO save(CompanyBaseDTO companyBaseDTO) {
        log.debug("Request to save Company : {}", companyBaseDTO);
        validateRequiredFields(companyBaseDTO);
        Company company = companyBaseMapper.toEntity(companyBaseDTO);
        Company savedCompany = companyDao.save(company);
        return companyBaseMapper.toDto(savedCompany);
    }

    @Override
    public CompanyBaseDTO update(CompanyBaseDTO companyBaseDTO) {
        log.debug("Request to update Company : {}", companyBaseDTO);
        validateRequiredFields(companyBaseDTO);
        Company existingCompany = companyDao.findById(companyBaseDTO.getId())
                .orElseThrow(() -> new CompanyException(CompanyErrorCode.COMPANY_NOT_FOUND, "Company not found with id " + companyBaseDTO.getId()));

        companyBaseMapper.updateFromDto(companyBaseDTO, existingCompany);
        Company savedCompany = companyDao.save(existingCompany);
        return companyBaseMapper.toDto(savedCompany);
    }

    @Override
    public CompanyBaseDTO findById(Long id) {
        log.debug("Request to get company by id : {}", id);
        Optional<Company> company = companyDao.findById(id);
        return companyBaseMapper.toDto(company.orElseThrow(() -> new CompanyException(CompanyErrorCode.COMPANY_NOT_FOUND, "Company not found with id " + id)));
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
