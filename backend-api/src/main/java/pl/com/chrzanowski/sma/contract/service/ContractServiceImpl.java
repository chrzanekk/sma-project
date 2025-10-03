package pl.com.chrzanowski.sma.contract.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.com.chrzanowski.sma.common.exception.ContractException;
import pl.com.chrzanowski.sma.common.exception.error.ContractErrorCode;
import pl.com.chrzanowski.sma.contract.dao.ContractDao;
import pl.com.chrzanowski.sma.contract.dto.ContractDTO;
import pl.com.chrzanowski.sma.contract.mapper.ContractDTOMapper;
import pl.com.chrzanowski.sma.contract.model.Contract;

import java.util.Optional;

@Service
@Transactional
public class ContractServiceImpl implements ContractService {

    private final Logger log = LoggerFactory.getLogger(ContractServiceImpl.class);

    private final ContractDao contractDao;
    private final ContractDTOMapper contractDTOMapper;

    public ContractServiceImpl(ContractDao contractDao, ContractDTOMapper contractDTOMapper) {
        this.contractDao = contractDao;
        this.contractDTOMapper = contractDTOMapper;
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
