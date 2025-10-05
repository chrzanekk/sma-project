package pl.com.chrzanowski.sma.contract.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.com.chrzanowski.sma.common.exception.*;
import pl.com.chrzanowski.sma.common.exception.error.*;
import pl.com.chrzanowski.sma.company.dao.CompanyDao;
import pl.com.chrzanowski.sma.company.model.Company;
import pl.com.chrzanowski.sma.constructionsite.dao.ConstructionSiteDao;
import pl.com.chrzanowski.sma.constructionsite.model.ConstructionSite;
import pl.com.chrzanowski.sma.contact.dao.ContactDao;
import pl.com.chrzanowski.sma.contact.model.Contact;
import pl.com.chrzanowski.sma.contract.dao.ContractDao;
import pl.com.chrzanowski.sma.contract.dto.ContractDTO;
import pl.com.chrzanowski.sma.contract.mapper.ContractDTOMapper;
import pl.com.chrzanowski.sma.contract.model.Contract;
import pl.com.chrzanowski.sma.contractor.dao.ContractorDao;
import pl.com.chrzanowski.sma.contractor.model.Contractor;

import java.util.Optional;

@Service
@Transactional
public class ContractServiceImpl implements ContractService {

    private final Logger log = LoggerFactory.getLogger(ContractServiceImpl.class);

    private final ContractDao contractDao;
    private final ContractDTOMapper contractDTOMapper;

    private final ContactDao contactDao;
    private final ConstructionSiteDao constructionSiteDao;
    private final ContractorDao contractorDao;
    private final CompanyDao companyDao;


    public ContractServiceImpl(ContractDao contractDao, ContractDTOMapper contractDTOMapper, ConstructionSiteDao constructionSiteDao, ContractorDao contractorDao, CompanyDao companyDao, ContactDao contactDao) {
        this.contractDao = contractDao;
        this.contractDTOMapper = contractDTOMapper;
        this.contactDao = contactDao;
        this.constructionSiteDao = constructionSiteDao;
        this.contractorDao = contractorDao;
        this.companyDao = companyDao;
    }

    @Override
    @Transactional
    public ContractDTO save(ContractDTO contractDTO) {
        log.debug("Request to save Contract : {}", contractDTO.getNumber());
        Contract contract = contractDTOMapper.toEntity(contractDTO);
        Contract savedContract = contractDao.save(contract);
        return contractDTOMapper.toDto(savedContract);
    }

    @Override
    public ContractDTO update(ContractDTO contractDTO) {
        log.debug("Request to update Contract : {}", contractDTO.getId());
        Contract existingContract = contractDao.findById(contractDTO.getId()).orElseThrow(() -> new ContractException(ContractErrorCode.CONTRACT_NOT_FOUND, "Contract with id " + contractDTO.getId() + " not found"));
        contractDTOMapper.updateFromDto(contractDTO, existingContract);

        // Ręcznie ustaw relacje jeśli się zmieniły
        if (contractDTO.getContractor() != null && contractDTO.getContractor().getId() != null) {
            // Pobierz aktualnego kontrahenta z bazy
            Contractor contractor = contractorDao.findById(contractDTO.getContractor().getId())
                    .orElseThrow(() -> new ContractorException(ContractorErrorCode.CONTRACTOR_NOT_FOUND,
                            "Contractor with id " + contractDTO.getContractor().getId() + " not found"));
            existingContract.setContractor(contractor);
        }

        if (contractDTO.getConstructionSite() != null && contractDTO.getConstructionSite().getId() != null) {
            ConstructionSite site = constructionSiteDao.findById(contractDTO.getConstructionSite().getId())
                    .orElseThrow(() -> new ConstructionSiteException(
                            ConstructionSiteErrorCode.CONSTRUCTION_SITE_NOT_FOUND,
                            "Construction site with id " + contractDTO.getConstructionSite().getId() + " not found"
                    ));
            existingContract.setConstructionSite(site);
        }

        if (contractDTO.getContact() != null && contractDTO.getContact().getId() != null) {
            Contact contact = contactDao.findById(contractDTO.getContact().getId())
                    .orElseThrow(() -> new ContactException(
                            ContactErrorCode.CONTACT_NOT_FOUND,
                            "Contact with id " + contractDTO.getContact().getId() + " not found"
                    ));
            existingContract.setContact(contact);
        }

        // Company raczej się nie zmienia, ale jeśli trzeba:
        if (contractDTO.getCompany() != null && contractDTO.getCompany().getId() != null) {
            Company company = companyDao.findById(contractDTO.getCompany().getId())
                    .orElseThrow(() -> new CompanyException(
                            CompanyErrorCode.COMPANY_NOT_FOUND,
                            "Company with id " + contractDTO.getCompany().getId() + " not found"
                    ));
            existingContract.setCompany(company);
        }

        Contract updatedContract = contractDao.save(existingContract);
        return contractDTOMapper.toDto(updatedContract);
    }

    @Override
    public ContractDTO findById(Long aLong) {
        log.debug("Find contract by id : {}", aLong);
        Optional<Contract> contract = contractDao.findById(aLong);
        return contractDTOMapper.toDto(contract.orElseThrow(() -> new ContractException(ContractErrorCode.CONTRACT_NOT_FOUND, "Contract with id " + aLong + " not found")));
    }

    @Override
    public void delete(Long aLong) {
        log.debug("Request to delete Contract : {}", aLong);
        contractDao.deleteById(aLong);
    }
}
