'use client'

import {Flex, Link, Stack} from '@chakra-ui/react'
import {useAuth} from "@/context/AuthContext.tsx";
import {useNavigate} from "react-router-dom";
import {useEffect} from "react";
import AppBanner from "@/auth/common/AppBanner.tsx";
import ResetPasswordForm from "@/auth/resetPassword/ResetPasswordForm.tsx";
import {useTranslation} from "react-i18next";

const ResetPassword = () => {

    const {user} = useAuth();
    const navigate = useNavigate();
    const {t} = useTranslation('auth')

    useEffect(() => {
        if (user) {
            navigate("/dashboard");
        }
    })

    return (
        <Stack minH={'100vh'} direction={{base: 'column', md: 'row'}}>
            <Flex p={8} flex={1} align={'center'} justify={'center'}>
                <Stack spacing={4} w={'full'} maxW={'md'}>
                    <ResetPasswordForm/>
                    <Link color={"blue.600"} href="/register" alignSelf={'center'}>
                        {t('shared.dontHaveAccount')}
                    </Link>
                    <Link color={"blue.600"} href="/" alignSelf={'center'}>
                        {t('shared.goToLogin')}
                    </Link>
                </Stack>
            </Flex>
            <AppBanner/>
        </Stack>
    )
}

export default ResetPassword