import {UserInfo} from "@/types/user-types.ts";
import {useEffect, useState} from "react";
import {errorNotification} from "@/notifications/notifications.ts";
import {getUserInfo} from "@/services/account-service.ts";
import {isTokenExpired} from "@/utils/token-utils.ts";
import {useAuth} from "@/context/AuthContext.tsx";


export const getUser = (): UserInfo | null => {
    const [user, setUser] = useState<UserInfo | null>(null);

    useEffect(() => {
        const {logOut} = useAuth();
        const token: string | null = localStorage.getItem("auth");
        if (token) {
            if (isTokenExpired(token)) {
                errorNotification("Sesja wygasła", "Twoja sesja wygasła, zaloguj się ponownie.");
                logOut();
                return;
            }
            getUserInfo().then(response => {
                setUser(response)
            }).catch(error => {
                console.log(error)
                errorNotification(error.code, error.response.data.message)
            })
        }
    }, []);
    return user;
}