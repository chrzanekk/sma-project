export interface EmployeeFilter {
    id?: number;
    firstNameContains?: string;
    lastNameContains?: string;
    positionContains?: string;
    hourRateStartsWith?: string;
    hourRateEndsWith?: string;
    companyContains?: string;
    companyId?: number;
    positionId?: number;

    page?: number;
    size?: number;
    sort?: string;
}

export class EmployeeFilterBuilder {
    private readonly f: Partial<EmployeeFilter> = {};

    withId(id: number) {
        this.f.id = id;
        return this;
    }

    withFirstNameContains(firstNameContains: string) {
        this.f.firstNameContains = firstNameContains;
        return this;
    }

    withLastNameContains(lastNameContains: string) {
        this.f.lastNameContains = lastNameContains;
        return this;
    }

    withPositionContains(positionContains: string) {
        this.f.positionContains = positionContains;
        return this;
    }

    withCompanyContains(companyContains: string) {
        this.f.companyContains = companyContains;
        return this;
    }

    withHourRateStartsWith(hourRateStartsWith: string) {
        this.f.hourRateStartsWith = hourRateStartsWith;
        return this;
    }

    withHourRateEndsWith(hourRateEndsWith: string) {
        this.f.hourRateEndsWith = hourRateEndsWith;
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

    withPositionId(id: number) {
        this.f.positionId = id;
        return this;
    }

    build(): EmployeeFilter {
        return {...this.f};
    }
}