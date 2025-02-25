import {BaseContractorDTOForContact} from "@/types/contractor-types.ts";

//tylko do pobierania danych i wyświetlania - ma dodatkowe pola audytujace
export interface FetchableContactDTO {
    id?: number;
    firstName: string;
    lastName: string;
    phoneNumber: string;
    email: string;
    additionalInfo: string;
    contractors?: Array<BaseContractorDTOForContact>
    createdDatetime: string;
    lastModifiedDatetime: string;
    createdById: number;
    createdByFirstName: string;
    createdByLastName: string;
    modifiedById: number;
    modifiedByFirstName: string;
    modifiedByLastName: string;
}

//interfejs do listy kontaktów w obiekcie Contractor by uniknąć odwołania cyklicznego
export interface BaseContactDTOForContractor {
    id?: number;
    firstName: string;
    lastName: string;
    phoneNumber: string;
    email: string;
    additionalInfo: string;
}


//interfejs użyty w serwisie - contractors może byc puste
export interface ContactDTO {
    id?: number;
    firstName: string;
    lastName: string;
    phoneNumber: string;
    email: string;
    additionalInfo: string;
    contractors?: Array<BaseContractorDTOForContact>
}

//interfejs używany w formularzach
export interface BaseContactFormValues {
    id?: number;
    firstName: string;
    lastName: string;
    phoneNumber: string;
    email: string;
    additionalInfo: string;
}
