import {CompanyBaseDTO} from "@/types/company-types.ts";
import {ConstructionSiteBaseDTO} from "@/types/constrution-site-types.ts";
import {ContractorBaseDTO} from "@/types/contractor-types.ts";
import {ScaffoldingLogPositionBaseDTO} from "@/types/scaffolding-log-position-types.ts";
import {AuditableTypes} from "@/types/auditable-types.ts";

export interface FetchableScaffoldingLogDTO extends ScaffoldingLogDTO, AuditableTypes {
    positions?: ScaffoldingLogPositionBaseDTO[];
}

export interface ScaffoldingLogDTO extends ScaffoldingLogBaseDTO {
    company: CompanyBaseDTO;
    constructionSite: ConstructionSiteBaseDTO;
    contractor: ContractorBaseDTO;
}

export interface ScaffoldingLogBaseDTO {
    id?: number;
    name: string;
    additionalInfo: string;
}

export interface BaseScaffoldingLogFormValues {
    id?: number;
    name: string;
    additionalInfo: string;
    contractor?: ContractorBaseDTO;
    constructionSite?: ConstructionSiteBaseDTO;
}