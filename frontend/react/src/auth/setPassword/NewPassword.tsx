'use client'

import {Flex, Stack} from '@chakra-ui/react'
import {useAuth} from "@/context/AuthContext.tsx";
import {useNavigate} from "react-router-dom";
import {useEffect} from "react";
import AppBanner from "@/auth/common/AppBanner.tsx";
import NewPasswordForm from "@/auth/setPassword/NewPasswordForm.tsx";

const NewPassword = () => {

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
                <Stack gap={4} w={'full'} maxW={'md'}>

                    <NewPasswordForm/>
                </Stack>
            </Flex>
            <AppBanner/>
        </Stack>
    )
}

export default NewPassword