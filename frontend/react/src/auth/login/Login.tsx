'use client'

import {Flex, Link, Stack} from '@chakra-ui/react'
import {useAuth} from "@/context/AuthContext.tsx";
import {useNavigate} from "react-router-dom";
import {useEffect} from "react";
import LoginForm from "./LoginForm.jsx";
import AppBanner from "@/auth/common/AppBanner.tsx";
import {useTranslation} from "react-i18next";

const Login = () => {

    const {user} = useAuth();
    const navigate = useNavigate();
    const {t} =useTranslation('auth')

    useEffect(() => {
        if (user) {
            navigate("/dashboard");
        }
    })

    return (
        <Stack minH={'100vh'} direction={{base: 'column', md: 'row'}}>
            <Flex p={8} flex={1} align={'center'} justify={'center'}>
                <Stack p={4} w={'full'} maxW={'md'}>
                    <LoginForm/>
                    <Link color={"blue.600"} href="/register" alignSelf={'center'}>
                        {t('shared.dontHaveAccount')}
                    </Link>
                    <Link color={"blue.600"} href="/request-reset-password" alignSelf={'center'}>
                        {t('login.forgetPassword')}
                    </Link>
                </Stack>
            </Flex>
            <AppBanner/>
        </Stack>
    )
}

export default Login