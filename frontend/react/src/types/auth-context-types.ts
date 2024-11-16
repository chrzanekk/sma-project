import {User} from "@/types/user-types.ts";

export interface AuthContextType {
    user: User | null;
    login: (usernameAndPassword: any) => Promise<any>;
    logOut: () => void;
    isAuthenticated: () => boolean;
    setUserFromToken: () => void;
    updateUserData: (updatedData: Partial<User>) => void;
    setUserData: (decodedToken: DecodedToken) => void;
}

export interface DecodedToken {
    id: number;
    email: string;
    sub: string;
    scopes: Set<string>;
    exp: number;
}