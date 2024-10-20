package pl.com.chrzanowski.scma.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pl.com.chrzanowski.scma.repository.SummaryValueServiceActionRepository;
import pl.com.chrzanowski.scma.service.SummaryValueServiceActionService;
import pl.com.chrzanowski.scma.service.dto.SummaryValueServiceActionDTO;

@Service
public class SummaryValueServiceActionServiceImpl implements SummaryValueServiceActionService {

    private final Logger log = LoggerFactory.getLogger(SummaryValueServiceActionDTO.class);

    private final SummaryValueServiceActionRepository summaryValueServiceActionRepository;

    public SummaryValueServiceActionServiceImpl(SummaryValueServiceActionRepository summaryValueServiceActionRepository) {
        this.summaryValueServiceActionRepository = summaryValueServiceActionRepository;
    }

    @Override
    public SummaryValueServiceActionDTO getSummaryValues() {
        log.debug("Get summary tax,gross and net value of all service actions.");
        return summaryValueServiceActionRepository.getSummaryValues();
    }
}
