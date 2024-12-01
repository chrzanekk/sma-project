import { useEffect, useState } from "react";
import { getUserInfo } from "@/services/account-service.ts";
import { errorNotification } from "@/notifications/notifications.ts";
import { UserInfo } from "@/types/user-types.ts";
import { isTokenExpired } from "@/utils/token-utils.ts";
import { useAuth } from "@/context/AuthContext.tsx";
import { useTranslation } from "react-i18next";

const useUser = () => {
    const [user, setUser] = useState<UserInfo | null>(() => {
        const storedUser = localStorage.getItem("user");
        return storedUser ? JSON.parse(storedUser) : null;
    });

    const { logOut } = useAuth();
    const { t } = useTranslation('auth');

    useEffect(() => {
        const token = localStorage.getItem("auth");
        if (token && isTokenExpired(token)) {
            errorNotification(
                t('notifications.sessionExpired'),
                t('notifications.logInAgain')
            );
            logOut();
            return;
        }

        if (!user && token) {
            getUserInfo()
                .then((response) => {
                    setUser(response);
                    localStorage.setItem("user", JSON.stringify(response));
                })
                .catch((error) => {
                    console.error(error);
                    errorNotification(
                        t('error', { ns: 'common' }),
                        error.response?.data?.message || t('notifications.loginFailed')
                    );
                    logOut();
                });
        }
    }, [user, logOut, t]);

    const updateUser = (updatedUser: Partial<UserInfo>) => {
        const newUser = { ...user, ...updatedUser } as UserInfo;
        setUser(newUser);
        localStorage.setItem("user", JSON.stringify(newUser));
    };

    const clearUser = () => {
        setUser(null);
        localStorage.removeItem("user");
    };

    return { user, updateUser, clearUser };
};

export default useUser;
