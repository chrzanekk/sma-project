import {useAuth} from "@/context/AuthContext.tsx";
import {useNavigate} from "react-router-dom";
import {Form, Formik} from "formik";
import * as Yup from "yup";
import {successNotification} from "@/notifications/notifications.ts";
import {Box, Button, Heading, Image, Stack} from "@chakra-ui/react";
import {MyTextInput} from "@/components/shared/CustomFields.tsx";
import {useTranslation} from "react-i18next";


const LoginForm = () => {
    const {loginUser} = useAuth();
    const navigate = useNavigate();
    const {t} = useTranslation(['auth', 'common'])
    return (
        <Formik
            validateOnMount={true}
            validationSchema={
                Yup.object({
                    login: Yup.string()
                        .required(t('verification.required', {field: t('shared.login')})),
                    password: Yup.string()
                        .min(4, t('verification.minLength', {field: t('shared.password'), count: 4}))
                        .max(10, t('verification.maxLength', {field: t('shared.password'), count: 10}))
                        .required(t('verification.required', {field: t('shared.password')})),
                })
            }
            initialValues={{login: '', password: '', rememberMe: false}}
            onSubmit={(values, {setSubmitting}) => {
                setSubmitting(true);
                loginUser(values).then(() => {
                    successNotification(t('success', {ns: "common"}), t("notifications.loginSuccess"))
                    navigate("/dashboard")
                }).catch(() => {
                }).finally(() => {
                    setSubmitting(false)
                })
            }}>

            {({isValid, isSubmitting}) => (
                <Form>
                    <Stack gap={15}>
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
                            <Heading fontSize={'2xl'} mb={15}>{t('login.header')}</Heading>
                        </Box>
                        <MyTextInput
                            label={t('shared.login')}
                            name={"login"}
                            type={"text"}
                            placeholder={t('shared.login')}
                        />
                        <MyTextInput
                            label={t('shared.password')}
                            name={"password"}
                            type={"password"}
                            placeholder={"************"}
                        />
                        <Button
                            type="submit"
                            disabled={!isValid || isSubmitting}>
                            Login
                        </Button>
                    </Stack>
                </Form>
            )}

        </Formik>
    )
}

export default LoginForm;