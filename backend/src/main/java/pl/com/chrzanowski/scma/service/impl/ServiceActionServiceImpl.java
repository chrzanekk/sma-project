package pl.com.chrzanowski.scma.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import pl.com.chrzanowski.scma.domain.ServiceAction;
import pl.com.chrzanowski.scma.domain.enumeration.TypeOfService;
import pl.com.chrzanowski.scma.exception.ObjectNotFoundException;
import pl.com.chrzanowski.scma.exception.ServiceActionException;
import pl.com.chrzanowski.scma.repository.ServiceActionRepository;
import pl.com.chrzanowski.scma.service.ServiceActionService;
import pl.com.chrzanowski.scma.service.dto.ServiceActionDTO;
import pl.com.chrzanowski.scma.service.dto.ServiceActionTypeDTO;
import pl.com.chrzanowski.scma.service.dto.SummaryValueServiceActionDTO;
import pl.com.chrzanowski.scma.service.filter.serviceaction.ServiceActionFilter;
import pl.com.chrzanowski.scma.service.filter.serviceaction.ServiceActionSpecification;
import pl.com.chrzanowski.scma.service.mapper.ServiceActionMapper;
import pl.com.chrzanowski.scma.util.DateTimeUtil;
import pl.com.chrzanowski.scma.util.TaxCalculationUtil;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ServiceActionServiceImpl implements ServiceActionService {

    private final Logger log = LoggerFactory.getLogger(ServiceActionServiceImpl.class);

    private final ServiceActionRepository serviceActionRepository;
    private final ServiceActionMapper serviceActionMapper;
    private final ServiceActionSpecification serviceActionSpecification;

    public ServiceActionServiceImpl(ServiceActionRepository serviceActionRepository,
                                    ServiceActionMapper serviceActionMapper,
                                    ServiceActionSpecification serviceActionSpecification) {
        this.serviceActionRepository = serviceActionRepository;
        this.serviceActionMapper = serviceActionMapper;
        this.serviceActionSpecification = serviceActionSpecification;
    }

    @Override
    public ServiceActionDTO save(ServiceActionDTO serviceActionDTO) {
        log.debug("Save service action: {}", serviceActionDTO);
        validateOilServiceType(serviceActionDTO);
        ServiceActionDTO serviceActionDTOtoSave = ServiceActionDTO.builder(serviceActionDTO)
                .taxValue(TaxCalculationUtil.calculateTaxValue(serviceActionDTO.getNetValue(), serviceActionDTO.getTaxRate()))
                .grossValue(TaxCalculationUtil.calculateGrossValue(serviceActionDTO.getNetValue(), serviceActionDTO.getTaxRate()))
                .createDate(DateTimeUtil.setDateTimeIfNotExists(serviceActionDTO.getCreateDate())).build();
        ServiceAction serviceAction =
                serviceActionRepository.save(serviceActionMapper.toEntity(serviceActionDTOtoSave));
        return serviceActionMapper.toDto(serviceAction);
    }

    @Override
    public ServiceActionDTO update(ServiceActionDTO serviceActionDTO) {
        log.debug("Update service action: {}", serviceActionDTO);
        if (serviceActionDTO.getId() != null) {
            validateOilServiceType(serviceActionDTO);
            ServiceActionDTO serviceActionDTOtoUpdate = ServiceActionDTO.builder(serviceActionDTO)
                    .taxValue(TaxCalculationUtil.calculateTaxValue(serviceActionDTO.getNetValue(), serviceActionDTO.getTaxRate()))
                    .grossValue(TaxCalculationUtil.calculateGrossValue(serviceActionDTO.getNetValue(), serviceActionDTO.getTaxRate()))
                    .modifyDate(Instant.now()).build();
            ServiceAction serviceActionToUpdate = serviceActionMapper.toEntity(serviceActionDTOtoUpdate);
            ServiceAction serviceAction =
                    serviceActionRepository.save(serviceActionMapper.toEntity(serviceActionDTOtoUpdate));
            return serviceActionMapper.toDto(serviceAction);
        } else {
            throw new ObjectNotFoundException("Id not found");
        }
    }


    private void validateOilServiceType(ServiceActionDTO serviceActionDTO) {
        LocalDate sevenDaysBeforeNow = LocalDate.now().minusDays(7L);
        List<ServiceActionDTO> allServiceActionsForLastSevenDays =
                serviceActionMapper.toDto(serviceActionRepository.findServiceActionByVehicleIdEqualsAndServiceDateGreaterThanEqual(serviceActionDTO.getVehicleId(), sevenDaysBeforeNow));
        validateOilServiceActionInPastSevenDays(allServiceActionsForLastSevenDays);
    }

    private void validateOilServiceActionInPastSevenDays(List<ServiceActionDTO> allServiceActionsForLastSevenDays) {
        List<ServiceActionDTO> listOfOilServiceActions = filterOilServiceActions(allServiceActionsForLastSevenDays);
        if (!listOfOilServiceActions.isEmpty()) {
            throw new ServiceActionException("Oil service was done in last 7 days");
        }
    }

    private static List<ServiceActionDTO> filterOilServiceActions(List<ServiceActionDTO> allServiceActionsForLastSevenDays) {
        List<ServiceActionDTO> listOfOilServiceActions = new ArrayList<>();
        allServiceActionsForLastSevenDays.forEach(serviceActionDTO -> {
            Set<ServiceActionTypeDTO> filtered = serviceActionDTO.getServiceActionTypes().stream()
                    .filter(serviceActionTypeDTO -> serviceActionTypeDTO.getTypeOfService()
                            .equals(TypeOfService.OIL_SERVICE)).collect(Collectors.toSet());
            if (filtered.size() != 0) {
                listOfOilServiceActions.add(serviceActionDTO);
            }
        });
        return listOfOilServiceActions;
    }


    @Override
    public List<ServiceActionDTO> findByFilter(ServiceActionFilter serviceActionFilter) {
        log.debug("Find service actions by filter: {}", serviceActionFilter);
        Specification<ServiceAction> spec = ServiceActionSpecification.createSpecification(serviceActionFilter);
        List<ServiceAction> result = serviceActionRepository.findAll(spec);
        List<ServiceActionDTO> resultDTO = serviceActionMapper.toDto(result);
        return handleSummaryValuesOfServiceActions(resultDTO);
    }

    private List<ServiceActionDTO> handleSummaryValuesOfServiceActions(List<ServiceActionDTO> result) {
        SummaryValueServiceActionDTO summaryValues = calculateSummaryValues(result);
        return addSummaryToFirstElementOfResult(summaryValues, result);
    }

    private SummaryValueServiceActionDTO calculateSummaryValues(List<ServiceActionDTO> list) {
        return SummaryValueServiceActionDTO.builder()
                .summaryGrossValue(calculateSummaryGrossValue(list))
                .summaryTaxValue(calculateSummaryTaxValue(list))
                .summaryNetValue(calculateSummaryNetValue(list)).build();
    }

    private BigDecimal calculateSummaryGrossValue(List<ServiceActionDTO> list) {
        return list.stream().map(ServiceActionDTO::getGrossValue).reduce(BigDecimal.ZERO
                , BigDecimal::add);
    }

    private BigDecimal calculateSummaryNetValue(List<ServiceActionDTO> list) {
        return list.stream().map(ServiceActionDTO::getNetValue).reduce(BigDecimal.ZERO
                , BigDecimal::add);
    }

    private BigDecimal calculateSummaryTaxValue(List<ServiceActionDTO> list) {
        return list.stream().map(ServiceActionDTO::getTaxValue).reduce(BigDecimal.ZERO
                , BigDecimal::add);
    }

    private List<ServiceActionDTO> addSummaryToFirstElementOfResult(SummaryValueServiceActionDTO summaryValues,
                                                                    List<ServiceActionDTO> listToUpdate) {
        List<ServiceActionDTO> result;
        if (!listToUpdate.isEmpty()) {
            result = new ArrayList<>();
            ServiceActionDTO firstElement = listToUpdate.get(0);
            ServiceActionDTO firstUpdatedElement = ServiceActionDTO.builder(firstElement)
                    .summaryGrossValue(summaryValues.getSummaryGrossValue())
                    .summaryNetValue(summaryValues.getSummaryNetValue())
                    .summaryTaxValue(summaryValues.getSummaryTaxValue()).build();
            result.add(firstUpdatedElement);
            listToUpdate.stream().skip(1).forEach(result::add);
        } else {
            result = listToUpdate;
        }
        return result;
    }

    @Override
    public Page<ServiceActionDTO> findByFilterAndPage(ServiceActionFilter serviceActionFilter, Pageable pageable) {
        log.debug("Find service actions by filter with page: {}", serviceActionFilter);
        Specification<ServiceAction> spec = ServiceActionSpecification.createSpecification(serviceActionFilter);
        Page<ServiceActionDTO> result = serviceActionRepository.findAll(spec, pageable).map(serviceActionMapper::toDto);
        return new PageImpl<>(handleSummaryValuesOfServiceActions(result.getContent()), pageable, result.getTotalElements());
    }

    @Override
    public ServiceActionDTO findById(Long id) {
        log.debug("Find service action by id: {}", id);
        Optional<ServiceAction> serviceActionOptional = serviceActionRepository.findById(id);
        return serviceActionMapper.toDto(serviceActionOptional.orElseThrow(() -> new ObjectNotFoundException("Service" +
                " action not found")));
    }

    @Override
    public List<ServiceActionDTO> findAll() {
        log.debug("Find all service actions.");
        return serviceActionMapper.toDto(serviceActionRepository.findAll());
    }

    @Override
    public void delete(Long id) {
        log.debug("Delete service action by id {}", id);
        serviceActionRepository.deleteServiceActionById(id);
    }
}
