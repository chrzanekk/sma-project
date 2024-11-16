import {createContext, ReactNode, useContext, useEffect, useState} from "react";
import {login as performLogin} from "../services/auth-service.ts";
import {jwtDecode} from "jwt-decode";
import {User} from "../types/user-types.ts";
import {AuthContextType, DecodedToken} from "@/types/auth-context-types.ts";

const AuthContext = createContext<AuthContextType>({} as AuthContextType);

// Typy dla dzieci komponentu
interface AuthProviderProps {
    children: ReactNode;
}

export const AuthProvider = ({children}: AuthProviderProps) => {
    const [user, setUser] = useState<User | null>(null);

    const setUserFromToken = () => {
        const token = localStorage.getItem("auth");
        if (token) {
            const decodedToken = jwtDecode<DecodedToken>(token);
            setUserData(decodedToken);
        }
    };

    const setUserData = (decodedToken: DecodedToken) => {
        const userData: User = {
            id: decodedToken.id,
            email: decodedToken.email,
            username: decodedToken.sub,
            roles: decodedToken.scopes,
        };
        setUser(userData);
        localStorage.setItem("user", JSON.stringify(userData));
    };

    const updateUserData = (updatedData: Partial<User>) => {
        const newUser = {
            ...user,
            ...updatedData,
        } as User;
        setUser(newUser);
        localStorage.setItem("user", JSON.stringify(newUser));
    };

    useEffect(() => {
        const storedUser = localStorage.getItem("user");
        if (storedUser) {
            setUser(JSON.parse(storedUser) as User);
        } else {
            setUserFromToken();
        }
    }, []);

    const login = async (usernameAndPassword: any) => {
        try {
            const res = await performLogin(usernameAndPassword);

            const jwtToken = res.headers["authorization"];
            if (!jwtToken) {
                throw new Error("Authorization token not found in response headers");
            }

            localStorage.setItem("auth", jwtToken);

            const decodedToken = jwtDecode<DecodedToken>(jwtToken);
            setUserData(decodedToken);

            return res;
        } catch (err) {
            console.error("Login error:", err);
            throw err;
        }
    };

    const logOut = () => {
        localStorage.removeItem("auth");
        localStorage.removeItem("user");
        setUser(null);
    };

    const isAuthenticated = () => {
        const token = localStorage.getItem("auth");
        if (!token) {
            return false;
        }
        const {exp: expiration} = jwtDecode<DecodedToken>(token);
        if (Date.now() > expiration * 1000) {
            logOut();
            return false;
        }
        return true;
    };

    return (
        <AuthContext.Provider
            value={{
                user,
                login,
                logOut,
                isAuthenticated,
                setUserFromToken,
                updateUserData,
                setUserData,
            }}
        >
            {children}
        </AuthContext.Provider>
    );
};

export const useAuth = () => useContext(AuthContext);

export default AuthContext;
