export interface ResourceDTO {
    id: number;
    resourceKey: string;
    displayName: string;
    endpointPattern: string;
    description: string;
    httpMethod: string | null;
    isActive: boolean;
    isPublic: boolean;
    allowedRoles: string[];
}

export interface ResourceUpdateRequest {
    resourceId: number;
    roleNames: string[];
}