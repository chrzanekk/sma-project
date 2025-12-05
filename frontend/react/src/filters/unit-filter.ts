export interface UnitFilter {
    id?: number;
    symbolContains?: string;
    descriptionContains?: string;
    companyId?: number;

    page?: number;
    size?: number;
    sort?: string;
}

export class UnitFilterBuilder {
    private readonly f: Partial<UnitFilter> = {};

    withId(id: number) {
        this.f.id = id;
        return this;
    }

    withSymbolContains(symbolContains: string) {
        this.f.symbolContains = symbolContains;
        return this;
    }

    withDescriptionContains(descriptionContains: string) {
        this.f.descriptionContains = descriptionContains;
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

    withCompanyId(id: number) {
        this.f.companyId = id;
        return this;
    }

    build(): UnitFilter {
        return {...this.f};
    }
}