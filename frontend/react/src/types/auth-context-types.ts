import {UserInfo} from "@/types/user-types.ts";

export interface AuthContextType {
    user: UserInfo | null;
    loginUser: (usernameAndPassword: any) => Promise<any>;
    logOut: () => void;
    isAuthenticated: () => boolean;
    setAuth: (updatedUser: UserInfo) => void;
}
