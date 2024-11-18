import {useAuth} from "@/context/AuthContext.tsx";
import {useNavigate} from "react-router-dom";
import {Form, Formik} from "formik";
import * as Yup from "yup";
import {errorNotification} from "@/notifications/notifications.ts";
import {Box, Button, Heading, Image, Stack} from "@chakra-ui/react";
import {MyTextInput} from "@/components/form/CustomFields.tsx";


const LoginForm = () => {
    const {login} = useAuth();
    const navigate = useNavigate();
    return (
        <Formik
            validateOnMount={true}
            validationSchema={
                Yup.object({
                    login: Yup.string()
                        .required("Login is required."),
                    password: Yup.string()
                        .min(4, "Password cannot be less than 4 characters")
                        .max(10, "Password cannot be more than 10 characters")
                        .required("Password is required.")
                })
            }
            initialValues={{login: '', password: '', rememberMe: false}}
            onSubmit={(values, {setSubmitting}) => {
                setSubmitting(true);
                login(values).then(() => {
                    navigate("/dashboard")
                    console.log("Successfully log in")
                }).catch(err => {
                    errorNotification(err.code, err.response.data.message)
                }).finally(() => {
                    setSubmitting(false)
                })
            }}>

            {({isValid, isSubmitting}) => (
                <Form>
                    <Stack spacing={15}>
                        <Box display="flex" justifyContent="center">
                            <Image
                                alt={'ResetPassword Image'}
                                objectFit={"scale-down"}
                                src={
                                    '/img/sma-logo.png'
                                }
                                width={'200px'}
                                height={'auto'}
                            />
                        </Box>
                        <Box display="flex" justifyContent="center">
                            <Heading fontSize={'2xl'} mb={15}>Login your to account/Zaloguj się /jeszcze nie zrobiłem
                                tłumaczeń/</Heading>
                        </Box>
                        <MyTextInput
                            label={"Login"}
                            name={"login"}
                            type={"text"}
                            placeholder={"login"}
                        />
                        <MyTextInput
                            label={"Password"}
                            name={"password"}
                            type={"password"}
                            placeholder={"************"}
                        />
                        <Button
                            type="submit"
                            isDisabled={!isValid || isSubmitting}>
                            Login
                        </Button>
                    </Stack>
                </Form>
            )}

        </Formik>
    )
}

export default LoginForm;