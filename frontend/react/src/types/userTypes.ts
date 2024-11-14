export interface LoginRequest {
    username: string;
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
    username: string;
    email: string;
    password: string;
    role?: Set<string>;
}

export interface User {
    id?: number;
    email: string;
    username: string;
    password?: string;
    roles?: Set<string>;
}