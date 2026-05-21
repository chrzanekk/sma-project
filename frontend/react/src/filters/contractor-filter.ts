// src/types/filters/contractor-filter.ts
import { Country } from "@/types/country-types.ts";

export interface ContractorFilter {
    id?: number;
    nameStartsWith?: string;
    taxNumberStartsWith?: string;
    streetStartsWith?: string;
    buildingNoStartsWith?: string;
    apartmentNoStartsWith?: string;
    postalCodeStartsWith?: string;
    cityStartsWith?: string;
    country?: Country;
    customer?: boolean;
    supplier?: boolean;
    scaffoldingUser?: boolean;
    companyId?: number;
    page?: number;
    size?: number;
    sort?: string;
}

export class ContractorFilterBuilder {
    private readonly f: Partial<ContractorFilter> = {};

    withId(id: number) { this.f.id = id; return this; }
    withNameStartsWith(v: string) { this.f.nameStartsWith = v; return this; }
    withTaxNumberStartsWith(v: string) { this.f.taxNumberStartsWith = v; return this; }
    withStreetStartsWith(v: string) { this.f.streetStartsWith = v; return this; }
    withBuildingNoStartsWith(v: string) { this.f.buildingNoStartsWith = v; return this; }
    withApartmentNoStartsWith(v: string) { this.f.apartmentNoStartsWith = v; return this; }
    withPostalCodeStartsWith(v: string) { this.f.postalCodeStartsWith = v; return this; }
    withCityStartsWith(v: string) { this.f.cityStartsWith = v; return this; }
    withCountry(country: Country) { this.f.country = country; return this; }
    withCustomer(flag: boolean) { this.f.customer = flag; return this; }
    withSupplier(flag: boolean) { this.f.supplier = flag; return this; }
    withScaffoldingUser(flag: boolean) { this.f.scaffoldingUser = flag; return this; }
    withCompanyId(id: number) { this.f.companyId = id; return this; }
    withPaging(page: number, size: number) { this.f.page = page; this.f.size = size; return this; }
    withSort(sort: string) { this.f.sort = sort; return this; }

    build(): ContractorFilter {
        // Opcjonalnie: usunięcie undefined dla czystszego query
        const obj = { ...this.f };
        Object.keys(obj).forEach((k) => (obj as any)[k] === undefined && delete (obj as any)[k]);
        return obj as ContractorFilter;
    }
}
