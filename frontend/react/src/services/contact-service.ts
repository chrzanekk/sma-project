import {serializeQueryParams} from "@/utils/query-params-serializer.ts";
import {api, getAuthConfig} from "@/services/axios-config.ts";
import {parsePaginationResponse} from "@/utils/api-utils.ts";
import {AddContactDTO, ContactDTO, EditContactDTO} from "@/types/contact-types.ts";


const CONTACTS_API_BASE = "/api/contacts";

export const getContactsByFilter = async (filter: Record<string, any>) => {
    try {
        const queryParams = serializeQueryParams({
            ...filter,
            size: filter.size || 10,
            page: filter.page || 0
        });
        const response = await api.get(`${CONTACTS_API_BASE}/page?${queryParams}`, getAuthConfig());
        const {items, totalPages} = parsePaginationResponse(response);
        return {
            contacts: items as ContactDTO[],
            totalPages
        };
    } catch (err) {
        return {contacts: [], totalPages: 1};
    }
}

export const getContactById = async (id: number) => {
    try {
        const response = await api.get(`${CONTACTS_API_BASE}/getById/${id}`, getAuthConfig());
        const contactDTO: ContactDTO = response.data;
        return contactDTO;
    } catch (err) {
        throw err;
    }
}

export const addContact = async (addContact: AddContactDTO) => {
    try {
        const response = await api.post(`${CONTACTS_API_BASE}/add`, addContact, getAuthConfig());
        return response.data;
    } catch (err) {
        throw err;
    }
}

export const updateContact = async (updateContact: EditContactDTO) => {
    try {
        const response = await api.put(`${CONTACTS_API_BASE}/update`, updateContact, getAuthConfig());
        return response.data;
    } catch (err) {
        throw err;
    }
}

export const deleteContactById = async (id: number) => {
    await api.delete(`${CONTACTS_API_BASE}/delete/${id}`, getAuthConfig());
}