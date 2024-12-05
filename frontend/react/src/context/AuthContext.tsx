import {createContext, ReactNode, useContext} from "react";
import {useTranslation} from "react-i18next";
import {errorNotification} from "@/notifications/notifications.ts";
import useUser from "@/hooks/UseUser.tsx";
import {AuthContextType} from "@/types/auth-context-types.ts";
import {isTokenExpired} from "@/utils/token-utils.ts";
import {getUserInfo} from "@/services/account-service.ts";
import {login} from "@/services/auth-service.ts";

const AuthContext = createContext<AuthContextType>({} as AuthContextType);

interface AuthProviderProps {
    children: ReactNode;
}

export const AuthProvider = ({children}: AuthProviderProps) => {
    const {user, updateUser, clearUser} = useUser();
    const {t} = useTranslation('auth');

    const loginUser = async (usernameAndPassword: any): Promise<void> => {
        try {
            const res = await login(usernameAndPassword);

            const jwtToken = res.data.id_token;
            if (!jwtToken) {
                errorNotification(
                    t('error', {ns: "common"}),
                    t('notifications.authTokenNotFoundInHeader')
                );
                return;
            }

            localStorage.setItem("auth", jwtToken);

            const userInfo = await getUserInfo();
            updateUser(userInfo);

        } catch (error) {
            errorNotification(
                t('error', {ns: "common"}),
                t('notifications.loginFailed')
            );
            throw error;
        }
    };

    const logOut = () => {
        localStorage.removeItem('auth');
        clearUser();
    };

    const isAuthenticated = () => {
        const token = localStorage.getItem("auth");
        if (!token) {
            return false;
        }
        return !isTokenExpired(token);
    };

    const setAuth = (updatedUser: any) => {
        updateUser(updatedUser);
        localStorage.setItem("user", JSON.stringify(updatedUser));
    };

    return (
        <AuthContext.Provider
            value={{
                user,
                loginUser,
                logOut,
                isAuthenticated,
                setAuth,
            }}
        >
            {children}
        </AuthContext.Provider>
    );
};

export const useAuth = () => useContext(AuthContext);

export default AuthContext;
