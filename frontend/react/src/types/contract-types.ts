import {CompanyBaseDTO} from "@/types/company-type.ts";
import {ContractorBaseDTO} from "@/types/contractor-types.ts";
import {ConstructionSiteBaseDTO} from "@/types/constrution-site-types.ts";
import {AuditableType} from "@/types/auditable-type.ts";


export interface FetchableContractDTO extends ContractDTO, AuditableType {

}

export interface ContractDTO extends BaseContractDTO {
    company: CompanyBaseDTO;
    contractor: ContractorBaseDTO;
    constructionSite: ConstructionSiteBaseDTO;
}

export interface BaseContractDTO {
    id?: number;
    number: string;
    description: string;
    value: string;
    currency: string;
    startDate: string;
    endDate: string;
    signupDate: string;
    realEndDate: string;
}

