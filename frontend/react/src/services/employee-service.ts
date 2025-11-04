import {EmployeeFilter} from "@/types/filters/employee-filter.ts";
import {serializeQueryParams} from "@/utils/query-params-serializer.ts";
import {getSelectedCompany, getSelectedCompanyId} from "@/utils/company-utils.ts";
import {api, getAuthConfig} from "@/services/axios-config.ts";
import {parsePaginationResponse} from "@/utils/api-utils.ts";
import {EmployeeDTO, FetchableEmployeeDTO} from "@/types/employee-types.ts";
import {CompanyBaseDTO} from "@/types/company-types.ts";

const EMPLOYEE_API_BASE = "/api/employees";

export const getEmployeeByFilter = async (filter: EmployeeFilter) => {
    try {
        const queryParams = serializeQueryParams({
            ...filter,
            companyId: getSelectedCompanyId(),
            size: filter.size || 10,
            page: filter.page || 0
        });
        const response = await api.get(`${EMPLOYEE_API_BASE}?${queryParams}`, getAuthConfig())
        const {items, totalPages} = parsePaginationResponse(response);
        return {
            employees: items as FetchableEmployeeDTO[],
            totalPages
        };
    } catch (error) {
        return {employees: [], totalPages: 1}
    }
}


export const getEmployeeById = async (id: number) => {
    try {
        const response = await api.get(`${EMPLOYEE_API_BASE}/${id}`, getAuthConfig())
        const positionDTO: FetchableEmployeeDTO = response.data;
        return positionDTO;
    } catch (error) {
        throw error;
    }
}

export const addEmployee = async (addEmployee: EmployeeDTO) => {
    try {
        const company: CompanyBaseDTO | null = getSelectedCompany();
        const payload = {
            ...addEmployee,
            company
        }
        const response = await api.post(`${EMPLOYEE_API_BASE}`, payload, getAuthConfig());
        return response.data;
    } catch (error) {
        throw error;
    }
}

export const updateEmployee = async (updateEmployee: EmployeeDTO) => {
    try {
        const company: CompanyBaseDTO | null = getSelectedCompany();
        const payload = {
            ...updateEmployee,
            company
        }
        const response = await api.put(`${EMPLOYEE_API_BASE}/${updateEmployee.id}`, payload, getAuthConfig());
        return response.data;
    } catch (error) {
        throw error;
    }
}

export const deleteEmployeeById = async (id: number) => {
    await api.delete(`${EMPLOYEE_API_BASE}/${id}`, getAuthConfig())
}