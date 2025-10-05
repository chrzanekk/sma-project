import {Country} from "@/types/country-types.ts";
import {ContractorBaseDTO, ContractorDTO} from "@/types/contractor-types.ts";
import {CompanyBaseDTO} from "@/types/company-types.ts";
import {AuditableTypes} from "@/types/auditable-types.ts";


export interface FetchableConstructionSiteDTO extends ConstructionSiteDTO, AuditableTypes {
}


export interface ConstructionSiteCreateDTO extends ConstructionSiteBaseDTO {
    company: CompanyBaseDTO;
    contractors?: Array<ContractorBaseDTO>;
}

export interface ConstructionSiteUpdateDTO extends ConstructionSiteBaseDTO {
    company: CompanyBaseDTO;
    addedContractors?: Array<ContractorDTO>;
    deletedContractors?: Array<ContractorDTO>;
}

export interface ConstructionSiteDTO extends ConstructionSiteBaseDTO {
    contractors?: Array<ContractorBaseDTO>;
    company: CompanyBaseDTO;
}

export interface ConstructionSiteBaseDTO {
    id?: number;
    name: string;
    address: string;
    country: Country;
    shortName: string;
    code: string;
}

export interface ConstructionSiteFormValues {
    id?: number;
    name: string;
    address: string;
    country: string;
    shortName: string;
    code: string;
    contractors?: Array<ContractorBaseDTO>;
}