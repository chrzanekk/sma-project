// src/search/construction-site-search-adapter.ts
import { ConstructionSiteFilter, ConstructionSiteFilterBuilder } from "@/types/filters/construction-site-filter";
import { ConstructionSiteDTO } from "@/types/constrution-site-types";
import { getConstructionSiteByFilter } from "@/services/construction-site-service";

export interface ConstructionSiteSearchAdapterOptions {
    fixed?: Partial<ConstructionSiteFilter>;    // stałe/dynamiczne filtry (companyId, contractorId itd.)
    defaults?: Partial<ConstructionSiteFilter>; // domyślne paging/sort
    mapResults?: (items: ConstructionSiteDTO[]) => ConstructionSiteDTO[]; // opcjonalna transformacja wyników
}

/**
 * Zwraca funkcję zgodną z (q: string) => Promise<ConstructionSiteDTO[]>,
 * która buduje pełny ConstructionSiteFilter (z q, fixed i defaults), a następnie woła serwis.
 */
export function makeConstructionSiteSearchAdapter(opts: ConstructionSiteSearchAdapterOptions = {}) {
    const {
        fixed = {},
        defaults = { page: 0, size: 10, sort: "id,asc" },
        mapResults,
    } = opts;

    return async (q: string): Promise<ConstructionSiteDTO[]> => {
        const b = new ConstructionSiteFilterBuilder();

        // Domyślnie q mapujemy na nameStartsWith (główne pole wyszukiwania)
        const term = q?.trim();
        if (term) {
            b.withNameStartsWith(term);
        }

        // Wstrzyknięcie wszystkich wspieranych pól z fixed
        if (fixed.id !== undefined) b.withId(fixed.id);
        if (fixed.nameStartsWith) b.withNameStartsWith(fixed.nameStartsWith);
        if (fixed.addressStartsWith) b.withAddressStartsWith(fixed.addressStartsWith);
        if (fixed.shortNameStartsWith) b.withShortNameStartsWith(fixed.shortNameStartsWith);
        if (fixed.codeStartsWith) b.withCodeStartsWith(fixed.codeStartsWith);
        if (fixed.countryCode) b.withCountryCode(fixed.countryCode);
        if (fixed.companyId !== undefined) b.withCompanyId(fixed.companyId);
        if (fixed.contractorNameStartsWith) b.withContractorNameStartsWith(fixed.contractorNameStartsWith);

        // Domyślne paging/sort
        const page = defaults.page ?? 0;
        const size = defaults.size ?? 10;
        b.withPaging(page, size);
        if (defaults.sort) b.withSort(defaults.sort);

        const filter: ConstructionSiteFilter = b.build();

        const { constructionSites } = await getConstructionSiteByFilter(filter as any);
        return mapResults ? mapResults(constructionSites) : constructionSites;
    };
}
