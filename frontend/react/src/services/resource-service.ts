import {ResourceDTO, ResourceUpdateRequest} from "@/types/resource-types.ts";
import {api, getAuthConfig} from "@/services/axios-config.ts";

const RESOURCES_API_BASE = '/api/resources'

export const getAllResources = async (): Promise<ResourceDTO[]> => {
    const response = await api.get(RESOURCES_API_BASE, getAuthConfig())
    return response.data;
}

export const updateResourceRoles = async (updateRequest: ResourceUpdateRequest): Promise<ResourceUpdateRequest> => {
    const response = await api.put(RESOURCES_API_BASE + `/${updateRequest.resourceId}/roles`, updateRequest, getAuthConfig());
    return response.data;
}

export const reloadSecurity = async (): Promise<void> => {
    await api.post(RESOURCES_API_BASE + "/reload")
}