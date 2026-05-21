// src/search/contractor-search-adapter.ts
import {ContractorFilter, ContractorFilterBuilder} from "@/filters/contractor-filter";
import {getContractorsByFilter} from "@/services/contractor-service";
import {ContractorDTO} from "@/types/contractor-types";

export interface ContractorSearchAdapterOptions {
    fixed?: Partial<ContractorFilter>;    // stałe lub dynamiczne filtry wstrzykiwane zawsze
    defaults?: Partial<ContractorFilter>; // domyślne paging/sort (i ew. inne defaulty)
    mapResults?: (items: ContractorDTO[]) => ContractorDTO[]; // opcjonalna transformacja wyników
}

/**
 * Zwraca funkcję zgodną z (q: string) => Promise<ContractorDTO[]>,
 * która buduje pełny ContractorFilter (z q i fixed/defaults) i woła serwis.
 */
export function makeContractorSearchAdapter(opts: ContractorSearchAdapterOptions = {}) {
    const {
        fixed = {},
        defaults = {page: 0, size: 10, sort: "id,asc"},
        mapResults,
    } = opts;

    return async (q: string): Promise<ContractorDTO[]> => {
        const b = new ContractorFilterBuilder();

        if (q && q.trim().length > 0) {
            b.withNameStartsWith(q.trim());
        }

        if (fixed.id !== undefined) b.withId(fixed.id);
        if (fixed.nameStartsWith) b.withNameStartsWith(fixed.nameStartsWith);
        if (fixed.taxNumberStartsWith) b.withTaxNumberStartsWith(fixed.taxNumberStartsWith);
        if (fixed.streetStartsWith) b.withStreetStartsWith(fixed.streetStartsWith);
        if (fixed.buildingNoStartsWith) b.withBuildingNoStartsWith(fixed.buildingNoStartsWith);
        if (fixed.apartmentNoStartsWith) b.withApartmentNoStartsWith(fixed.apartmentNoStartsWith);
        if (fixed.postalCodeStartsWith) b.withPostalCodeStartsWith(fixed.postalCodeStartsWith);
        if (fixed.cityStartsWith) b.withCityStartsWith(fixed.cityStartsWith);
        if (fixed.country !== undefined) b.withCountry(fixed.country);
        if (fixed.customer !== undefined) b.withCustomer(fixed.customer);
        if (fixed.supplier !== undefined) b.withSupplier(fixed.supplier);
        if (fixed.scaffoldingUser !== undefined) b.withScaffoldingUser(fixed.scaffoldingUser);
        if (fixed.companyId !== undefined) b.withCompanyId(fixed.companyId);

        const page = defaults.page ?? 0;
        const size = defaults.size ?? 10;
        b.withPaging(page, size);
        if (defaults.sort) b.withSort(defaults.sort);

        const filter: ContractorFilter = b.build();

        const {contractors} = await getContractorsByFilter(filter as any);
        return mapResults ? mapResults(contractors) : contractors;
    };
}
