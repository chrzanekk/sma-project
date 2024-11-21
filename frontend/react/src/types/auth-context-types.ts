import {User, UserFromToken} from "@/types/user-types.ts";
import {JwtPayload} from "@/types/jwt-payload.ts";

export interface AuthContextType {
    user: UserFromToken | null;
    login: (usernameAndPassword: any) => Promise<any>;
    logOut: () => void;
    isAuthenticated: () => boolean;
    setUserFromToken: () => void;
    updateUserData: (updatedData: Partial<User>) => void;
    setUserData: (decodedToken: JwtPayload) => void;
}