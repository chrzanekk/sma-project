// src/types/filters/construction-site-filter.ts

export interface ConstructionSiteFilter {
    id?: number;
    nameStartsWith?: string;
    addressStartsWith?: string;
    shortNameStartsWith?: string;
    codeStartsWith?: string;
    country?: string;
    companyId?: number;
    contractorNameStartsWith?: string;

    page?: number;
    size?: number;
    sort?: string;
}

export class ConstructionSiteFilterBuilder {
    private readonly f: Partial<ConstructionSiteFilter> = {};

    withId(id: number) {
        this.f.id = id;
        return this;
    }

    withNameStartsWith(v: string) {
        this.f.nameStartsWith = v;
        return this;
    }

    withAddressStartsWith(v: string) {
        this.f.addressStartsWith = v;
        return this;
    }

    withShortNameStartsWith(v: string) {
        this.f.shortNameStartsWith = v;
        return this;
    }

    withCodeStartsWith(v: string) {
        this.f.codeStartsWith = v;
        return this;
    }

    withCountry(v: string) {
        this.f.country = v;
        return this;
    }

    withCompanyId(id: number) {
        this.f.companyId = id;
        return this;
    }

    withContractorNameStartsWith(v: string) {
        this.f.contractorNameStartsWith = v;
        return this;
    }

    withPaging(page: number, size: number) {
        this.f.page = page;
        this.f.size = size;
        return this;
    }

    withSort(sort: string) {
        this.f.sort = sort;
        return this;
    }

    build(): ConstructionSiteFilter {
        return { ...this.f };
    }
}
