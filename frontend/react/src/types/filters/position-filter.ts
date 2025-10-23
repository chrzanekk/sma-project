export interface PositionFilter {
    id?: number;
    nameContains?: string;
    descriptionContains?: string;
    companyId?: number;

    page?: number;
    size?: number;
    sort?: string;
}

export class PositionFilterBuilder {
    private readonly f: Partial<PositionFilter> = {};

    withId(id: number) {
        this.f.id = id;
        return this;
    }

    withNameContains(nameContains: string) {
        this.f.nameContains = nameContains;
    }

    withDescriptionContains(descriptionContains: string) {
        this.f.descriptionContains = descriptionContains;
    }

    withPaging(page: number, size: number) {
        this.f.page = page;
        this.f.size = size;
        return this;
    }

    withCompanyId(id: number) {
        this.f.companyId = id;
        return this;
    }

    build(): PositionFilter {
        return {...this.f};
    }
}