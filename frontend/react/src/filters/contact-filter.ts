
export interface ContactFilter {
    id?: number;
    firstNameStartsWith?: string;
    lastNameStartsWith?: string;
    emailStartsWith?: string;
    phoneStartsWith?: string;
    companyId?: number;
    contractorId?: number;
    contractorNameStartsWith?: string;

    page?: number;
    size?: number;
    sort?: string;
}


export class ContactFilterBuilder {
    private readonly f: Partial<ContactFilter> = {};

    withId(id: number) {
        this.f.id = id;
        return this;
    }
    withFirstNameStartsWith(v: string) {
        this.f.firstNameStartsWith = v;
        return this;
    }
    withLastNameStartsWith(v: string) {
        this.f.lastNameStartsWith = v;
        return this;
    }
    withEmailStartsWith(v: string) {
        this.f.emailStartsWith = v;
        return this;
    }
    withPhoneStartsWith(v: string) {
        this.f.phoneStartsWith = v;
        return this;
    }
    withCompanyId(id: number) {
        this.f.companyId = id;
        return this;
    }
    withContractorId(id: number) {
        this.f.contractorId = id;
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
    build(): ContactFilter {
        return { ...this.f };
    }
}
