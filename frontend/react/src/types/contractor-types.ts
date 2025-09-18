import {BaseContactDTOForContractor} from "@/types/contact-types.ts";
import {Country} from "@/types/country-types.ts";
import {AuditableTypes} from "@/types/auditable-types.ts";
import {CompanyBaseDTO} from "@/types/company-types.ts";

export interface FetchableContractorDTO extends ContractorDTO, AuditableTypes {
}

export interface ContractorDTO extends ContractorBaseDTO {
    contacts?: Array<BaseContactDTOForContractor>;
}

export interface ContractorUpdateDTO extends ContractorBaseDTO {
    addedContacts?: Array<BaseContactDTOForContractor>;
    deletedContacts?: Array<BaseContactDTOForContractor>;
}


export interface ContractorBaseDTO {
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
