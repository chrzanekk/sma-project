'use client'

import {Box, Flex, Link, Stack} from '@chakra-ui/react'
import {useAuth} from "@/context/AuthContext.tsx";
import {useNavigate} from "react-router-dom";
import {useEffect} from "react";
import LoginForm from "./LoginForm.jsx";
import AppBanner from "@/auth/common/AppBanner.tsx";
import {useTranslation} from "react-i18next";
import {ThemeToggle} from "@/layout/ThemeToggle.tsx";
import {useThemeColors} from "@/theme/theme-colors.ts";

const Login = () => {
    const {user} = useAuth();
    const navigate = useNavigate();
    const {t} = useTranslation('auth')
    const themeColors = useThemeColors();

    useEffect(() => {
        if (user) {
            navigate("/dashboard");
        }
    })

    return (
        <Box backgroundColor={themeColors.bgColorSecondary} p={3} minH={'100vh'}>
            <ThemeToggle/>
            <Stack direction={{base: 'column', md: 'row'}}>
                <Flex p={8} flex={1} align={'center'} justify={'center'}>
                    <Stack p={4} w={'full'} maxW={'md'}>
                        <LoginForm/>
                        <Link color={"green.400"} href="/register" alignSelf={'center'}>
                            {t('shared.dontHaveAccount')}
                        </Link>
                        <Link color={"green.400"} href="/request-reset-password" alignSelf={'center'}>
                            {t('login.forgetPassword')}
                        </Link>
                    </Stack>
                </Flex>
                <AppBanner/>
            </Stack>
        </Box>

    )
}

export default Login