import {useAuth} from "@/context/AuthContext.tsx";
import {useNavigate} from "react-router-dom";
import {useEffect} from "react";
import {Flex, Link, Stack} from "@chakra-ui/react";
import RegisterForm from "@/auth/register/RegisterForm.tsx";
import AppBanner from "@/auth/common/AppBanner.tsx";
import {useTranslation} from "react-i18next";

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
        <Stack minH={'100vh'} direction={{base: 'column', md: 'row'}}>
            <Flex p={8} flex={1} align={'center'} justify={'center'}>

                <Stack spacing={4} w={'full'} maxW={'md'}>

                    <RegisterForm/>

                    <Link color={"purple.600"} href={'/'} alignSelf={'center'}>
                        {t('register.haveAccount')}
                    </Link>
                </Stack>
            </Flex>
            <AppBanner/>
        </Stack>
    )
}

export default Register;