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

export interface User {
    id?: number;
    email: string;
    login: string;
    password?: string;
    roles?: Set<string>;
}

export interface UserInfo {
    id?: number;
    email: string;
    login: string;
    firstName: string;
    lastName: string;
    position: string;
    roles?: Set<string>;
}

export interface UserPasswordChangeRequest {
    userId: number;
    password: string;
    newPassword: string;
}