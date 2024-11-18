'use client'

import {Flex, Link, Stack} from '@chakra-ui/react'
import {useAuth} from "@/context/AuthContext.tsx";
import {useNavigate} from "react-router-dom";
import {useEffect} from "react";
import AppBanner from "@/auth/common/AppBanner.tsx";
import ResetPasswordForm from "@/auth/resetPassword/ResetPasswordForm.tsx";

const ResetPassword = () => {

    const {user} = useAuth();
    const navigate = useNavigate();

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
                        Dont have an account? Register now
                    </Link>
                    <Link color={"blue.600"} href="/" alignSelf={'center'}>
                        Back to login page.
                    </Link>
                </Stack>
            </Flex>
            <AppBanner/>
        </Stack>
    )
}

export default ResetPassword