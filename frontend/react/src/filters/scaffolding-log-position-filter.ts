import {TechnicalProtocolStatus} from "@/enums/technical-protocol-status-types-enum.ts";
import {ScaffoldingType} from "@/enums/scaffolding-type-types-enum.ts";

export interface ScaffoldingLogPositionFilter {
    id?: number;
    scaffoldingNumberContains?: string;
    assemblyLocationContains?: string;
    assemblyDateGreaterOrEqual?: string;
    assemblyDateLessOrEqual?: string;
    dismantlingDateGreaterOrEqual?: string;
    dismantlingDateLessOrEqual?: string;
    dismantlingNotificationDateGreaterOrEqual?: string;
    dismantlingNotificationDateLessOrEqual?: string;
    technicalProtocolStatus?: TechnicalProtocolStatus;
    scaffoldingType?: ScaffoldingType;
    scaffoldingFullDimensionGreaterOrEqual?: string;
    scaffoldingFullDimensionLessOrEqual?: string;
    scaffoldingLogNameContains?: string;
    scaffoldingLogAdditionalInfoContains?: string;
    contractorNameContains?: string;
    contractorContactNameContains?: string;
    scaffoldingUserNameContains?: string;
    scaffoldingUserContactNameContains?: string;
    parentPositionOnly?: boolean;

    page?: number;
    size?: number;
    sort?: string;
}

export class ScaffoldingLogPositionFilterBuilder {
    private readonly f: Partial<ScaffoldingLogPositionFilter> = {};

    withId(id: number) {
        this.f.id = id;
        return this;
    }

    withScaffoldingNumberContains(scaffoldingNumberContains: string) {
        this.f.scaffoldingNumberContains = scaffoldingNumberContains;
        return this;
    }

    withAssemblyLocationContains(assemblyLocationContains: string) {
        this.f.assemblyLocationContains = assemblyLocationContains;
        return this;
    }

    withAssemblyDateGreaterOrEqual(date: string) {
        this.f.assemblyDateGreaterOrEqual = date;
        return this;
    }

    withAssemblyDateLessOrEqual(date: string) {
        this.f.assemblyDateLessOrEqual = date;
        return this;
    }

    withDismantlingDateGreaterOrEqual(date: string) {
        this.f.dismantlingDateGreaterOrEqual = date;
        return this;
    }

    withDismantlingDateLessOrEqual(date: string) {
        this.f.dismantlingDateLessOrEqual = date;
        return this;
    }

    withDismantlingNotificationDateGreaterOrEqual(date: string) {
        this.f.dismantlingNotificationDateGreaterOrEqual = date;
        return this;
    }

    withDismantlingNotificationDateLessOrEqual(date: string) {
        this.f.dismantlingNotificationDateLessOrEqual = date;
        return this;
    }

    withTechnicalProtocolStatus(status: TechnicalProtocolStatus) {
        this.f.technicalProtocolStatus = status;
        return this;
    }

    withScaffoldingType(type: ScaffoldingType) {
        this.f.scaffoldingType = type;
        return this;
    }

    withScaffoldingFullDimensionGreaterOrEqual(dimension: string) {
        this.f.scaffoldingFullDimensionGreaterOrEqual = dimension;
        return this;
    }

    withScaffoldingFullDimensionLessOrEqual(dimension: string) {
        this.f.scaffoldingFullDimensionLessOrEqual = dimension;
        return this;
    }

    withScaffoldingLogNameContains(name: string) {
        this.f.scaffoldingLogNameContains = name;
        return this;
    }

    withScaffoldingLogAdditionalInfoContains(info: string) {
        this.f.scaffoldingLogAdditionalInfoContains = info;
        return this;
    }

    withContractorNameContains(name: string) {
        this.f.contractorNameContains = name;
        return this;
    }

    withContractorContactNameContains(name: string) {
        this.f.contractorContactNameContains = name;
        return this;
    }

    withScaffoldingUserNameContains(name: string) {
        this.f.scaffoldingUserNameContains = name;
        return this;
    }

    withScaffoldingUserContactNameContains(name: string) {
        this.f.scaffoldingUserContactNameContains = name;
        return this;
    }

    withParentPositionOnly(isParent: boolean) {
        this.f.parentPositionOnly = isParent;
        return this;
    }

    withPage(page: number) {
        this.f.page = page;
        return this;
    }

    withSize(size: number) {
        this.f.size = size;
        return this;
    }

    withSort(sort: string) {
        this.f.sort = sort;
        return this;
    }

    build(): ScaffoldingLogPositionFilter {
        return {...this.f};
    }
}
