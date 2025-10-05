// src/search/contact-search-adapter.ts
import { ContactFilter, ContactFilterBuilder } from "@/types/filters/contact-filter";
import { ContactDTO } from "@/types/contact-types";
import { getContactsByFilter } from "@/services/contact-service";

export interface ContactSearchAdapterOptions {
    fixed?: Partial<ContactFilter>;    // stałe/dynamiczne filtry wstrzykiwane zawsze (companyId, contractorId itd.)
    defaults?: Partial<ContactFilter>; // domyślne paging/sort
    mapResults?: (items: ContactDTO[]) => ContactDTO[]; // opcjonalna transformacja wyników
    // mapQuery?: (q: string) => { firstNameStartsWith?: string; lastNameStartsWith?: string; emailStartsWith?: string; phoneStartsWith?: string }
    // jeśli chcesz własne mapowanie q -> konkretne pole/pola
}

/**
 * Zwraca funkcję zgodną z (q: string) => Promise<ContactDTO[]>,
 * która buduje pełny ContactFilter (z q, fixed i defaults), a następnie woła serwis.
 */
export function makeContactSearchAdapter(opts: ContactSearchAdapterOptions = {}) {
    const {
        fixed = {},
        defaults = { page: 0, size: 10, sort: "id,asc" },
        mapResults,
    } = opts;

    return async (q: string): Promise<ContactDTO[]> => {
        const b = new ContactFilterBuilder();

        const term = q?.trim();
        if (term) {
            b.withLastNameStartsWith(term);
        }

        if (fixed.id !== undefined) b.withId(fixed.id);
        if (fixed.firstNameStartsWith) b.withFirstNameStartsWith(fixed.firstNameStartsWith);
        if (fixed.lastNameStartsWith) b.withLastNameStartsWith(fixed.lastNameStartsWith);
        if (fixed.emailStartsWith) b.withEmailStartsWith(fixed.emailStartsWith);
        if (fixed.phoneStartsWith) b.withPhoneStartsWith(fixed.phoneStartsWith);
        if (fixed.companyId !== undefined) b.withCompanyId(fixed.companyId);
        if (fixed.contractorId !== undefined) b.withContractorId(fixed.contractorId);
        if (fixed.contractorNameStartsWith) b.withContractorNameStartsWith(fixed.contractorNameStartsWith);

        const page = defaults.page ?? 0;
        const size = defaults.size ?? 10;
        b.withPaging(page, size);
        if (defaults.sort) b.withSort(defaults.sort);

        const filter: ContactFilter = b.build();

        const { contacts } = await getContactsByFilter(filter as any);
        return mapResults ? mapResults(contacts) : contacts;
    };
}
