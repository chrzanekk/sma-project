import {createContext, ReactNode, useContext, useEffect, useState} from "react";
import {login as performLogin} from "../services/auth-service.ts";
import {jwtDecode} from "jwt-decode";
import {UserFromToken} from "../types/user-types.ts";
import {AuthContextType} from "@/types/auth-context-types.ts";
import {JwtPayload} from "@/types/jwt-payload.ts";
import {useTranslation} from "react-i18next";
import {errorNotification, successNotification} from "@/notifications/notifications.ts";

const AuthContext = createContext<AuthContextType>({} as AuthContextType);

interface AuthProviderProps {
    children: ReactNode;
}

export const AuthProvider = ({children}: AuthProviderProps) => {
    const [user, setUser] = useState<UserFromToken | null>(null);

    const {t} = useTranslation(['auth', 'common']);

    const setUserFromToken = () => {
        const token = localStorage.getItem("auth");
        if (token) {
            const decodedToken = jwtDecode<JwtPayload>(token);
            setUserData(decodedToken);
        }
    };

    const setUserData = (decodedToken: JwtPayload) => {
        const userData: UserFromToken = {
            id: decodedToken.id,
            email: decodedToken.email,
            login: decodedToken.sub,
            roles: decodedToken.authorities,
        };
        setUser(userData);
        localStorage.setItem("user", JSON.stringify(userData));
    };

    const updateUserData = (updatedData: Partial<UserFromToken>) => {
        const newUser = {
            ...user,
            ...updatedData,
        } as UserFromToken;
        setUser(newUser);
        localStorage.setItem("user", JSON.stringify(newUser));
    };

    useEffect(() => {
        const storedUser = localStorage.getItem("user");
        if (storedUser) {
            setUser(JSON.parse(storedUser) as UserFromToken);
        } else {
            setUserFromToken();
        }
    }, []);

    const login = async (usernameAndPassword: any) => {
        const res = await performLogin(usernameAndPassword);

        const jwtToken = res.headers["authorization"];
        if (!jwtToken) {
            errorNotification(t('error', {ns: "common"}), t('notifications.authTokenNotFoundInHeader'))
        }
        localStorage.setItem("auth", jwtToken);

        const decodedToken = jwtDecode<JwtPayload>(jwtToken);
        setUserData(decodedToken);

        return res;
    };

    const logOut = () => {
        localStorage.removeItem("auth");
        localStorage.removeItem("user");
        setUser(null);
        successNotification(t('success', {ns: "common"}), t('notifications.logoutSuccess'))
    };

    const isAuthenticated = () => {
        const token = localStorage.getItem("auth");
        if (!token) {
            return false;
        }
        const {exp: expiration} = jwtDecode<JwtPayload>(token);
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
