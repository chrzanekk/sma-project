import {useEffect, useState} from "react";
import {getUserInfo} from "@/services/account-service.ts";
import {errorNotification} from "@/notifications/notifications.ts";
import {UserInfo} from "@/types/user-types.ts";
import {isTokenExpired} from "@/utils/token-utils.ts";
import {useTranslation} from "react-i18next";

const useUser = () => {
    const [user, setUser] = useState<UserInfo | null>(() => {
        const storedUser = localStorage.getItem("user");
        return storedUser ? JSON.parse(storedUser) : null;
    });

    const [tokenValid, setTokenValid] = useState(() => {
        const token = localStorage.getItem("auth");
        return token && !isTokenExpired(token);
    });

    const [initialized, setInitialized] = useState(false);
    const [hasShownNotification, setHasShownNotification] = useState(false);

    const {t} = useTranslation('auth');

    useEffect(() => {

        if (initialized) return;

        const token = localStorage.getItem("auth");

        if (!token || isTokenExpired(token)) {
            console.warn("Token is missing or expired. Logging out...");
            if (!hasShownNotification) {
                errorNotification(
                    t("notifications.sessionExpired"),
                    t("notifications.logInAgain")
                );
                setHasShownNotification(true);
            }
            localStorage.removeItem('auth');
            localStorage.removeItem('user');
            setUser(null);
            setTokenValid(false);
            setInitialized(true);
            return;
        }

        if (!user) {
            console.log("Fetching user information...");
            getUserInfo()
                .then((response) => {
                    console.log("User info fetched successfully:", response);
                    setUser(response);
                    localStorage.setItem("user", JSON.stringify(response));
                })
                .catch((error) => {
                    console.error("Error fetching user information:", error);
                    if (!hasShownNotification) {
                        errorNotification(
                            t("error", {ns: "common"}),
                            error.response?.data?.message || t("notifications.loginFailed")
                        );
                        setHasShownNotification(true);
                    }
                }).finally(() => {
                setInitialized(true);
            });
        } else {
            setInitialized(true);
        }
    }, [initialized, user, hasShownNotification, t]);

    const updateUser = (updatedUser: Partial<UserInfo>) => {
        const newUser = {...user, ...updatedUser} as UserInfo;
        setUser(newUser);
        localStorage.setItem("user", JSON.stringify(newUser));
    };

    const clearUser = () => {
        setUser(null);
        localStorage.removeItem("user");
    };

    return {user, updateUser, clearUser, tokenValid};
};

export default useUser;
