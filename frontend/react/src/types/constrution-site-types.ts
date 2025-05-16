import {Country} from "@/types/country-type.ts";
import {BaseContractorDTO, ContractorDTO} from "@/types/contractor-types.ts";
import {CompanyBaseDTO} from "@/types/company-type.ts";
import {AuditableType} from "@/types/auditable-type.ts";


export interface FetchableConstructionSiteDTO extends ConstructionSiteDTO, AuditableType {
}


export interface ConstructionSiteCreateDTO extends ConstructionSiteBaseDTO {
    company: CompanyBaseDTO;
    contractor: ContractorDTO;
}

export interface ConstructionSiteUpdateDTO extends ConstructionSiteBaseDTO {
    company: CompanyBaseDTO;
    addedContractors?: Array<ContractorDTO>;
    deletedContractors?: Array<ContractorDTO>;
}

export interface ConstructionSiteDTO extends ConstructionSiteBaseDTO {
    contractors?: Array<BaseContractorDTO>;
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
    country: Country;
    shortName: string;
    code: string;
    contractors?: Array<BaseContractorDTO>;
}