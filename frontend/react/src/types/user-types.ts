import {RoleDTO} from "@/types/role-types.ts";

export interface LoginRequest {
    login: string;
    password: string;
    rememberMe?: boolean;
}

export interface NewPasswordPutRequest {
    password: string;
    confirmPassword: string;
    token: string;
}

export interface PasswordResetRequest {
    email: string;
}

export interface RegisterRequest {
    login: string;
    email: string;
    password: string;
    firstName: string;
    lastName: string;
    position: string | "";
    role?: Set<string>;
}

export interface UserInfo {
    id?: number;
    email: string;
    login: string;
    firstName: string;
    lastName: string;
    position: string;
    locked: boolean;
    enabled: boolean;
    roles?: string[];
}

export interface UserUpdateRequest {
    id?: number;
    email: string;
    login: string;
    firstName: string;
    lastName: string;
    position: string;
    locked: boolean;
    enabled: boolean;
    roles: string[] | [];
    createdDatetime?: string;
    lastModifiedDatetime?: string;
}

export interface UserEditPasswordChangeRequest {
    userId: number;
    password: string;
    newPassword: string;
}

export interface RoleUpdateRequest {
    userId: number;
    roles: Array<RoleDTO>;
}

export interface UserDTO {
    id?: number;
    email: string;
    login: string;
    password?: string;
    firstName: string;
    lastName: string;
    position: string;
    locked: boolean;
    enabled: boolean;
    roles?: Array<RoleDTO>;
    createdDatetime?: string;
    lastModifiedDatetime?: string;
}

export interface UserFormDTO {
    id?: number;
    email: string;
    login: string;
    password?: string;
    firstName: string;
    lastName: string;
    position: string;
    locked: boolean;
    enabled: boolean;
    roles: string[] | [];
    createdDatetime?: string;
    lastModifiedDatetime?: string;
}

export interface AdminEditPasswordChangeRequest {
    userId: number;
    newPassword: string;
}

export interface AdminEditRoleUpdateRequest {
    userId?: number;
    roles: string[];
}

export interface UserFilter {
    emailStartsWith?: string;
    loginStartsWith?: string;
    firstNameStartsWith?: string;
    lastNameStartsWith?: string;
    positionStartsWith?: string;
    isLocked?: boolean;
    isEnabled?: boolean;
    roles: string[];
}