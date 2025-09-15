package pl.com.chrzanowski.sma.common.controller;

import lombok.RequiredArgsConstructor;
import pl.com.chrzanowski.sma.common.service.BaseCrudService;
import pl.com.chrzanowski.sma.common.service.QueryService;

@RequiredArgsConstructor
public abstract class BaseCrudController<RD, CD, UD, ID, F> {
    private final BaseCrudService<RD, ID> crudService;
    private final QueryService<RD, F> queryService;

    //todo do przeprojektowania interfejsy serwisów tak by mozna było zautomatyzować implementacje bazowych endpointów
}
