import {serializeQueryParams} from "@/utils/query-params-serializer.ts";
import {api, getAuthConfig} from "@/services/axios-config.ts";
import {parsePaginationResponse} from "@/utils/api-utils.ts";
import {ContactDTO, FetchableContactDTO} from "@/types/contact-types.ts";
import {getSelectedCompany, getSelectedCompanyId} from "@/utils/company-utils";
import {CompanyBaseDTO} from "@/types/company-type.ts";

const CONTACTS_API_BASE = "/api/contacts";


export const getContactsByFilter = async (filter: Record<string, any>) => {
    try {
        const queryParams = serializeQueryParams({
            ...filter,
            companyId: getSelectedCompanyId(),
            size: filter.size || 10,
            page: filter.page || 0
        });
        const response = await api.get(`${CONTACTS_API_BASE}/page?${queryParams}`, getAuthConfig());
        const {items, totalPages} = parsePaginationResponse(response);
        console.table(response)
        return {
            contacts: items as FetchableContactDTO[],
            totalPages
        };
    } catch (err) {
        return {contacts: [], totalPages: 1};
    }
}

export const getFreeContactsByFilter = async (filter: Record<string, any>) => {
    try {
        const queryParams = serializeQueryParams({
            ...filter,
            companyId: getSelectedCompanyId(),
            size: filter.size || 10,
            page: filter.page || 0
        });
        const response = await api.get(`${CONTACTS_API_BASE}/free/page?${queryParams}`, getAuthConfig());
        const {items, totalPages} = parsePaginationResponse(response);
        return {
            contacts: items as FetchableContactDTO[],
            totalPages
        };
    } catch (err) {
        return {contacts: [], totalPages: 1};
    }
}

export const getContactById = async (id: number) => {
    try {
        const response = await api.get(`${CONTACTS_API_BASE}/getById/${id}`, getAuthConfig());
        const contactDTO: FetchableContactDTO = response.data;
        return contactDTO;
    } catch (err) {
        throw err;
    }
}

export const addContact = async (addContact: ContactDTO) => {
    try {
        const company: CompanyBaseDTO | null = getSelectedCompany();
        const payload = {
            ...addContact,
            company
        }
        const response = await api.post(`${CONTACTS_API_BASE}/add`, payload, getAuthConfig());
        return response.data;
    } catch (err) {
        throw err;
    }
}

export const updateContact = async (updateContact: ContactDTO) => {
    try {
        const company: CompanyBaseDTO | null = getSelectedCompany();
        const payload = {
            ...updateContact,
            company
        }
        const response = await api.put(`${CONTACTS_API_BASE}/update`, payload, getAuthConfig());
        return response.data;
    } catch (err) {
        throw err;
    }
}

export const deleteContactById = async (id: number) => {
    await api.delete(`${CONTACTS_API_BASE}/delete/${id}`, getAuthConfig());
}