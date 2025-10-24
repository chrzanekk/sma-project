package pl.com.chrzanowski.sma.contract.dao;

import com.blazebit.persistence.PagedList;
import com.blazebit.persistence.querydsl.BlazeJPAQuery;
import com.blazebit.persistence.querydsl.BlazeJPAQueryFactory;
import com.querydsl.core.BooleanBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import pl.com.chrzanowski.sma.contract.model.Contract;
import pl.com.chrzanowski.sma.contract.repository.ContractRepository;
import pl.com.chrzanowski.sma.contract.service.filter.ContractQuerySpec;

import java.util.List;
import java.util.Optional;

import static pl.com.chrzanowski.sma.company.model.QCompany.company;
import static pl.com.chrzanowski.sma.constructionsite.model.QConstructionSite.constructionSite;
import static pl.com.chrzanowski.sma.contact.model.QContact.contact;
import static pl.com.chrzanowski.sma.contract.model.QContract.contract;
import static pl.com.chrzanowski.sma.contractor.model.QContractor.contractor;

@Repository("contractJPA")
public class ContractJPADaoImpl implements ContractDao {

    private final Logger log = LoggerFactory.getLogger(ContractJPADaoImpl.class);

    private final ContractRepository contractRepository;
    private final ContractQuerySpec contractQuerySpec;
    private final BlazeJPAQueryFactory queryFactory;

    public ContractJPADaoImpl(ContractRepository contractRepository, ContractQuerySpec contractQuerySpec, BlazeJPAQueryFactory queryFactory) {
        this.contractRepository = contractRepository;
        this.contractQuerySpec = contractQuerySpec;
        this.queryFactory = queryFactory;
    }


    @Override
    public Contract save(Contract contract) {
        log.debug("DAO: Save contract : {}", contract);
        return contractRepository.save(contract);
    }

    @Override
    public List<Contract> saveAll(List<Contract> contracts) {
        log.debug("DAO: Save all contracts");
        return contractRepository.saveAll(contracts);
    }

    @Override
    public Optional<Contract> findById(Long id) {
        log.debug("DAO: Find contract : {}", id);
        return contractRepository.findById(id);
    }

    @Override
    public List<Contract> findAll() {
        log.debug("DAO: Find all contracts");
        return contractRepository.findAll();
    }

    @Override
    public Page<Contract> findAll(BooleanBuilder specification, Pageable pageable) {
        log.debug("DAO: Find all contracts by specification with page: {}", specification);
        BlazeJPAQuery<Contract> baseQuery = contractQuerySpec.buildQuery(specification, pageable);

        baseQuery.leftJoin(contract.contractor, contractor).fetchJoin()
                .leftJoin(contract.company, company).fetchJoin()
                .leftJoin(contract.constructionSite, constructionSite).fetchJoin()
                .leftJoin(contract.contact, contact).fetchJoin();

        PagedList<Contract> content = baseQuery.fetchPage((int) pageable.getOffset(), pageable.getPageSize());

        return new PageImpl<>(content, pageable, content.getTotalSize());
    }

    @Override
    public List<Contract> findAll(BooleanBuilder specification) {
        log.debug("DAO: Find all contracts by specification: {}", specification);
        return contractQuerySpec.buildQuery(specification, null).fetch();
    }

    @Override
    public void deleteById(Long id) {
        log.debug("DAO: Delete contract : {}", id);
        contractRepository.deleteById(id);
    }

    @Override
    public Page<Contract> findByContractorId(Long contractorId, Pageable pageable) {

        PagedList<Contract> contracts = queryFactory
                .selectFrom(contract)
                .where(contract.contractor.id.eq(contractorId))
                .orderBy(contract.id.asc())
                .fetchPage((int) pageable.getOffset(), pageable.getPageSize());
        return new PageImpl<>(contracts, pageable, contracts.getTotalSize());
    }
}
