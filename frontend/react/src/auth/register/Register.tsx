import {useAuth} from "@/context/AuthContext.tsx";
import {useNavigate} from "react-router-dom";
import {useEffect} from "react";
import {Box, Flex, Link, Stack} from "@chakra-ui/react";
import RegisterForm from "@/auth/register/RegisterForm.tsx";
import AppBanner from "@/auth/common/AppBanner.tsx";
import {useTranslation} from "react-i18next";
import {ThemeToggle} from "@/layout/ThemeToggle.tsx";
import {themeColors} from "@/theme/theme-colors.ts";

const Register = () => {
    const {user} = useAuth();
    const navigate = useNavigate();
    const {t} = useTranslation('auth');

    useEffect(() => {
        if (user) {
            navigate("/");
        }
    })

    return (
        <Box backgroundColor={themeColors.bgColorSecondary()} p={3} minH={'100vh'}>
                <ThemeToggle/>
            <Stack direction={{base: 'column', md: 'row'}}>
                <Flex p={8} flex={1} align={'center'} justify={'center'}>

                    <Stack gap={4} w={'full'} maxW={'md'}>
                        <RegisterForm/>
                        <Link color={"green.400"} href={'/'} alignSelf={'center'}>
                            {t('shared.haveAccount')}
                        </Link>
                    </Stack>
                </Flex>
                <AppBanner/>
            </Stack>
        </Box>

    )
}

export default Register;