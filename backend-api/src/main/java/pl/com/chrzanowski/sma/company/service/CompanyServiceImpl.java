package pl.com.chrzanowski.sma.company.service;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.com.chrzanowski.sma.common.exception.CompanyException;
import pl.com.chrzanowski.sma.common.exception.PropertyMissingException;
import pl.com.chrzanowski.sma.common.exception.error.CompanyErrorCode;
import pl.com.chrzanowski.sma.company.dao.CompanyDao;
import pl.com.chrzanowski.sma.company.dto.CompanyDTO;
import pl.com.chrzanowski.sma.company.mapper.CompanyBaseMapper;
import pl.com.chrzanowski.sma.company.mapper.CompanyDTOMapper;
import pl.com.chrzanowski.sma.company.model.Company;

import java.util.Map;
import java.util.Optional;

@Service
@Transactional
public class CompanyServiceImpl implements CompanyService {

    private final Logger log = LoggerFactory.getLogger(CompanyServiceImpl.class);
    private final CompanyDao companyDao;
    private final CompanyBaseMapper companyBaseMapper;
    private final CompanyDTOMapper companyDTOMapper;

    public CompanyServiceImpl(CompanyDao companyDao, CompanyBaseMapper companyBaseMapper, CompanyDTOMapper companyDTOMapper) {
        this.companyDao = companyDao;
        this.companyBaseMapper = companyBaseMapper;
        this.companyDTOMapper = companyDTOMapper;
    }


    @Override
    @Transactional
    public CompanyDTO save(CompanyDTO companyDTO) {
        log.debug("Request to save Company : {}", companyDTO);
        validateRequiredFields(companyDTO);
        Company company = companyDTOMapper.toEntity(companyDTO);
        Company savedCompany = companyDao.save(company);
        return companyDTOMapper.toDto(savedCompany);
    }

    @Override
    @Transactional
    public CompanyDTO update(CompanyDTO companyBaseDTO) {
        log.debug("Request to update Company : {}", companyBaseDTO);
        validateRequiredFields(companyBaseDTO);
        Company existingCompany = companyDao.findById(companyBaseDTO.getId())
                .orElseThrow(() -> new CompanyException(CompanyErrorCode.COMPANY_NOT_FOUND, "Company not found with id " + companyBaseDTO.getId()));

        companyDTOMapper.updateFromDto(companyBaseDTO, existingCompany);
        Company savedCompany = companyDao.save(existingCompany);
        return companyDTOMapper.toDto(savedCompany);
    }

    @Override
    @Transactional
    public CompanyDTO findById(Long id) {
        log.debug("Request to get company by id : {}", id);
        Optional<Company> company = companyDao.findById(id);
        return companyDTOMapper.toDto(company.orElseThrow(() -> new CompanyException(CompanyErrorCode.COMPANY_NOT_FOUND, "Company not found with id " + id)));
    }


    @Override
    @Transactional
    public void delete(Long id) {
        log.debug("Request to delete Company : {}", id);
        companyDao.deleteById(id);
    }

    private static void validateRequiredFields(CompanyDTO companyBaseDTO) {
        if (StringUtils.isBlank(companyBaseDTO.getName())) {
            throw new PropertyMissingException(CompanyErrorCode.NAME_MISSING, "Company name is required", Map.of("name", companyBaseDTO.getName()));
        }
    }
}
