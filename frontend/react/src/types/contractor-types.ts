import {BaseContactDTOForContractor} from "@/types/contact-types.ts";
import {Country} from "@/types/country-type.ts";
import {AuditableType} from "@/types/auditable-type.ts";
import {CompanyBaseDTO} from "@/types/company-type.ts";

export interface FetchableContractorDTO extends ContractorDTO, AuditableType {
}

export interface ContractorDTO extends BaseContractorDTO {
    contacts?: Array<BaseContactDTOForContractor>;
}

export interface ContractorUpdateDTO extends BaseContractorDTO {
    addedContacts?: Array<BaseContactDTOForContractor>;
    deletedContacts?: Array<BaseContactDTOForContractor>;
}


export interface BaseContractorDTO {
    id?: number;
    name: string;
    taxNumber: string;
    street: string;
    buildingNo: string;
    apartmentNo: string;
    postalCode: string;
    city: string;
    country: Country;
    customer: boolean | undefined;
    supplier: boolean | undefined;
    scaffoldingUser: boolean | undefined;
    company: CompanyBaseDTO;
}

export interface ContractorFormValues {
    id?: number;
    name: string;
    taxNumber: string;
    street: string;
    buildingNo: string;
    apartmentNo: string;
    postalCode: string;
    city: string;
    country: string;
    customer: boolean | undefined;
    supplier: boolean | undefined;
    scaffoldingUser: boolean | undefined;
    contacts?: Array<BaseContactDTOForContractor>;
}
