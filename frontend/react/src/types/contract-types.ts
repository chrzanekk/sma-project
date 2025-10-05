import {CompanyBaseDTO} from "@/types/company-types.ts";
import {ContractorBaseDTO} from "@/types/contractor-types.ts";
import {ConstructionSiteBaseDTO} from "@/types/constrution-site-types.ts";
import {AuditableTypes} from "@/types/auditable-types.ts";
import {ContactBaseDTO} from "@/types/contact-types.ts";


export interface FetchableContractDTO extends ContractDTO, AuditableTypes {

}

export interface ContractDTO extends BaseContractDTO {
    company: CompanyBaseDTO;
    contractor: ContractorBaseDTO;
    constructionSite: ConstructionSiteBaseDTO;
    contact: ContactBaseDTO;
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

export interface BaseContractFormValues extends BaseContractDTO {
    contractor?: ContractorBaseDTO;
    constructionSite?: ConstructionSiteBaseDTO;
    company?: CompanyBaseDTO;
    contact?: ContactBaseDTO;
}