import {useEffect, useState} from "react";
import {useAuth} from "@/context/AuthContext.tsx";
import {errorNotification} from "@/notifications/notifications.ts";
import {getUserInfo} from "@/services/account-service.ts";
import {isTokenExpired} from "@/utils/token-utils.ts";
import {UserInfo} from "@/types/user-types.ts";
import {useTranslation} from "react-i18next";

const fetchUser = (): [UserInfo | null, boolean] => {
    const [user, setUser] = useState<UserInfo | null>(null);
    const {logOut} = useAuth();
    const [loading, setLoading] = useState(true);
    const {t} = useTranslation('auth');

    useEffect(() => {
        const token: string | null = localStorage.getItem("auth");
        if (token) {
            if (isTokenExpired(token)) {
                errorNotification(
                    t('notifications.sessionExpired'),
                    t('notifications.logInAgain')
                );
                logOut();
                setLoading(false);
                return;
            }

            getUserInfo()
                .then((response) => {
                    setUser(response);
                })
                .catch((error) => {
                    errorNotification(t('error', {ns: 'common'}), error.response?.data?.message || t('notifications.loginFailed'));
                    logOut();
                })
                .finally(() => {
                    setLoading(false)
                });
        } else {
            setLoading(false);
        }
    }, [logOut, t]);

    return [user, loading];
};

export default fetchUser;
