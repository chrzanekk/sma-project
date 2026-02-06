import {DimensionType} from "@/enums/dimension-types-enum.ts";
import {ScaffoldingOperationType} from "@/enums/scaffolding-operation-types-enum.ts";
import {UnitBaseDTO} from "@/types/unit-types.ts";
import {CompanyBaseDTO} from "@/types/company-types.ts";
import {ScaffoldingType} from "@/enums/scaffolding-type-types-enum.ts";
import {TechnicalProtocolStatus} from "@/enums/technical-protocol-status-types-enum.ts";
import {ContractorBaseDTO} from "@/types/contractor-types.ts";
import {ContactBaseDTO} from "@/types/contact-types.ts";
import {AuditableTypes} from "@/types/auditable-types.ts";
import {ScaffoldingLogBaseDTO} from "@/types/scaffolding-log-types.ts";

export interface FetchableScaffoldingLogPositionDTO extends ScaffoldingLogPositionDTO, AuditableTypes {
    mappedChildPositions?: FetchableScaffoldingLogPositionDTO[];
}

export interface ScaffoldingLogPositionDTO extends ScaffoldingLogPositionBaseDTO {
    company: CompanyBaseDTO;
}

export interface ScaffoldingLogPositionBaseDTO {
    id?: number;
    scaffoldingNumber: string;
    assemblyLocation: string;
    assemblyDate: string;
    dismantlingDate: string;
    dismantlingNotificationDate: string;
    scaffoldingType: ScaffoldingType;
    scaffoldingFullDimension: string;
    scaffoldingFullDimensionUnit: UnitBaseDTO;
    fullWorkingTime: string;
    technicalProtocolStatus: TechnicalProtocolStatus;
    parentPosition: ScaffoldingLogPositionBaseDTO;
    childPositions: ScaffoldingLogPositionBaseDTO[];
    scaffoldingLog: ScaffoldingLogBaseDTO;
    contractor: ContractorBaseDTO;
    contractorContact: ContactBaseDTO;
    scaffoldingUser: ContractorBaseDTO;
    scaffoldingUserContact: ContactBaseDTO;
    dimensions: ScaffoldingLogPositionDimensionBaseDTO[];
    workingTimes: ScaffoldingLogPositionWorkingTimeBaseDTO[];
}

export interface BaseScaffoldingLogPositionFormValues {
    id?: number;
    scaffoldingNumber: string;
    assemblyLocation: string;
    assemblyDate: string;
    dismantlingDate: string;
    dismantlingNotificationDate: string;
    scaffoldingType: ScaffoldingType;
    scaffoldingFullDimension: string;
    scaffoldingFullDimensionUnit: UnitBaseDTO;
    fullWorkingTime: string;
    technicalProtocolStatus: TechnicalProtocolStatus;
    parentPosition: ScaffoldingLogPositionBaseDTO;
    childPositions: ScaffoldingLogPositionBaseDTO[];
    scaffoldingLog: ScaffoldingLogBaseDTO;
    contractor?: ContractorBaseDTO;
    contractorContact?: ContactBaseDTO;
    scaffoldingUser?: ContractorBaseDTO;
    scaffoldingUserContact?: ContactBaseDTO;
    dimensions: ScaffoldingLogPositionDimensionBaseDTO[];
    workingTimes: ScaffoldingLogPositionWorkingTimeBaseDTO[];
}


export interface ScaffoldingLogPositionDimensionBaseDTO {
    id?: number;
    height: string;
    width: string;
    length: string;
    dimensionType: DimensionType;
    dismantlingDate: string;
    assemblyDate: string;
    operationType: ScaffoldingOperationType;
    unit: UnitBaseDTO;
}

export interface ScaffoldingLogPositionDimensionDTO extends ScaffoldingLogPositionDimensionBaseDTO {
    company: CompanyBaseDTO;
    scaffoldingPosition: ScaffoldingLogPositionBaseDTO;
}

export interface ScaffoldingLogPositionWorkingTimeBaseDTO {
    id?: number;
    numberOfWorkers: number;
    numberOfHours: string;
    unit: UnitBaseDTO;
    operationType: ScaffoldingOperationType;
}

export interface ScaffoldingLogPositionWorkingTimeDTO extends ScaffoldingLogPositionWorkingTimeBaseDTO {
    company: CompanyBaseDTO;
    scaffoldingPosition: ScaffoldingLogPositionBaseDTO;
}