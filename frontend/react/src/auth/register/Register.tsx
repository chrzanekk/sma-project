import {useAuth} from "@/context/AuthContext.tsx";
import {useNavigate} from "react-router-dom";
import {useEffect} from "react";
import {Flex, Link, Stack} from "@chakra-ui/react";
import RegisterForm from "@/auth/register/RegisterForm.tsx";
import AppBanner from "@/auth/common/AppBanner.tsx";

const Register = () => {
    const {user} = useAuth();
    const navigate = useNavigate();

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
                        Have an account? Login now.
                    </Link>
                </Stack>
            </Flex>
            <AppBanner/>
        </Stack>
    )
}

export default Register;