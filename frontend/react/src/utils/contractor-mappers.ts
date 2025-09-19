// utils/contractor-mappers.ts
import { ContractorBaseDTO, ContractorDTO, ContractorFormValues } from "@/types/contractor-types";

export const mapContractorBaseToFormValues = (c: ContractorBaseDTO): ContractorFormValues => ({
    id: c.id,
    name: c.name,
    taxNumber: c.taxNumber,
    street: c.street,
    buildingNo: c.buildingNo,
    apartmentNo: c.apartmentNo,
    postalCode: c.postalCode,
    city: c.city,
    country: c.country.code,           // Country -> string code
    customer: c.customer,
    supplier: c.supplier,
    scaffoldingUser: c.scaffoldingUser,
    contacts: undefined,               // brak w BaseDTO, zostaw puste/undefined
});

export const mapContractorDtoToFormValues = (c: ContractorDTO): ContractorFormValues => ({
    id: c.id,
    name: c.name,
    taxNumber: c.taxNumber,
    street: c.street,
    buildingNo: c.buildingNo,
    apartmentNo: c.apartmentNo,
    postalCode: c.postalCode,
    city: c.city,
    country: c.country.code,
    customer: c.customer,
    supplier: c.supplier,
    scaffoldingUser: c.scaffoldingUser,
    contacts: c.contacts ?? [],
});
