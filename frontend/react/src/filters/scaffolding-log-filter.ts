export interface ScaffoldingLogFilter {
    id?: number;
    companyId?: number;
    constructionSiteId?: number;
    contractorId?: number;
    nameContains?: string;
    additionalInfoContains?: string;
    constructionSiteNameContains?: string;
    contractorNameContains?: string;

    page?: number;
    size?: number;
    sort?: string;
}

export class ScaffoldingLogFilterBuilder {
    private readonly f: Partial<ScaffoldingLogFilter> = {};

    withId(id: number) {
        this.f.id = id;
        return this;
    }

    withNameContains(nameContains: string) {
        this.f.nameContains = nameContains;
        return this;
    }

    withAdditionalInfoContains(additionalInfo: string) {
        this.f.additionalInfoContains = additionalInfo;
        return this;
    }

    withContractorNameContains(contractorName: string) {
        this.f.contractorNameContains = contractorName;
        return this;
    }

    withConstructionSiteNameContains(constructionSiteName: string) {
        this.f.constructionSiteNameContains = constructionSiteName;
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

    withContractorId(contractorId: number) {
        this.f.contractorId = contractorId;
        return this;
    }

    withConstructionSiteId(contractorSiteId: number) {
        this.f.constructionSiteId = contractorSiteId;
        return this;
    }

    build(): ScaffoldingLogFilter {
        return {...this.f};
    }
}