'use client'

import {Box, Flex, Link, Stack} from '@chakra-ui/react'
import {useAuth} from "@/context/AuthContext.tsx";
import {useNavigate} from "react-router-dom";
import {useEffect} from "react";
import AppBanner from "@/auth/common/AppBanner.tsx";
import ResetPasswordForm from "@/auth/resetPassword/ResetPasswordForm.tsx";
import {useTranslation} from "react-i18next";
import {themeColors} from "@/theme/theme-colors.ts";
import {ThemeToggle} from "@/layout/ThemeToggle.tsx";

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
        <Box backgroundColor={themeColors.bgColorSecondary()} p={3} minH={'100vh'}>
            <ThemeToggle/>
            <Stack direction={{base: 'column', md: 'row'}}>
                <Flex p={4} flex={1} align={'center'} justify={'center'}>
                    <Stack gap={4} w={'full'} maxW={'md'}>
                        <ResetPasswordForm/>
                        <Link color={"green.400"} href="/register" alignSelf={'center'}>
                            {t('shared.dontHaveAccount')}
                        </Link>
                        <Link color={"green.400"} href="/" alignSelf={'center'}>
                            {t('shared.goToLogin')}
                        </Link>
                    </Stack>
                </Flex>
                <AppBanner/>
            </Stack>
        </Box>

    )
}

export default ResetPassword