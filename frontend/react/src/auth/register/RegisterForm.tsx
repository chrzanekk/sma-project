// src/components/form/RegisterForm.tsx

import {Form, Formik} from 'formik';
import * as Yup from 'yup';
import {Box, Button, Heading, Image, Stack} from "@chakra-ui/react";
import {errorNotification, successNotification} from "@/notifications/notifications.ts";
import {MyTextInput} from "@/components/form/CustomFields";
import {registerUser} from "@/services/auth-service.ts";
import {RegisterRequest} from "@/types/user-types.ts";
import {useTranslation} from "react-i18next";

const RegisterForm = () => {
    const {t} = useTranslation(['auth', 'common']);
    return (
        <Formik
            initialValues={{
                login: '',
                email: '',
                password: '',
                firstName: '',
                lastName: '',
                position: ''
            }}
            validationSchema={Yup.object({
                login: Yup.string()
                    .min(5, t('verification.minLength', {field: t('shared.login'), count: 5}))
                    .max(15, t('verification.maxLength', {field: t('shared.login'), count: 15}))
                    .required(t('verification.required', {field: t('shared.login')})),
                email: Yup.string()
                    .email(t('verification.invalidEmail'))
                    .required(t('verification.required', {field: t('shared.email')})),
                password: Yup.string()
                    .min(4, t('verification.minLength', {field: t('shared.password'), count: 4}))
                    .max(10, t('verification.maxLength', {field: t('shared.password'), count: 10}))
                    .required(t('verification.required', {field: t('shared.password')})),
                firstName: Yup.string()
                    .min(2, t('verification.minLength', {field: t('register.firstName'), count: 2}))
                    .max(10, t('verification.maxLength', {field: t('register.firstName'), count: 10}))
                    .required(t('verification.required', {field: t('register.firstName')})),
                lastName: Yup.string()
                    .min(2, t('verification.minLength', {field: t('register.lastName'), count: 2}))
                    .max(10, t('verification.maxLength', {field: t('register.lastName'), count: 10}))
                    .required(t('verification.required', {field: t('register.lastName')})),
            })}
            onSubmit={async (register: RegisterRequest, {setSubmitting}) => {
                setSubmitting(true);
                try {
                    await registerUser(register);
                    successNotification(
                        t('success',{ns:"common"}),
                        t('notifications.registerSuccessDetails', {login: register.login})
                    );
                } catch (err: any) {
                    console.error(err);
                    errorNotification(
                        t('error',{ns:"common"}),
                        err.response?.data?.message || t('notifications.registerErrorDetails')
                    );
                } finally {
                    setSubmitting(false)
                }
            }}
        >
            {({isValid, isSubmitting}) => (
                <Form>
                    <Box display="flex" justifyContent="center">
                        <Image
                            alt="ResetPassword Image"
                            objectFit="scale-down"
                            src="/img/sma-logo.png"
                            width="200px"
                            height="auto"
                        />
                    </Box>
                    <Box display="flex" justifyContent="center">
                        <Heading fontSize="2xl" mb={15}>{t('register.header')}</Heading>
                    </Box>
                    <Stack spacing="24px">
                        <MyTextInput
                            label={t('shared.login')}
                            name="login"
                            type="text"
                            placeholder={t('shared.login')}
                        />
                        <MyTextInput
                            label={t('shared.email')}
                            name="email"
                            type="email"
                            placeholder="example@example.com"
                        />
                        <MyTextInput
                            label={t('shared.password')}
                            name="password"
                            type="password"
                            placeholder={t('shared.password')}
                        />
                        <MyTextInput
                            label={t('register.firstName')}
                            name="firstName"
                            type="text"
                            placeholder={t('register.firstName')}
                        />
                        <MyTextInput
                            label={t('register.lastName')}
                            name="lastName"
                            type="text"
                            placeholder={t('register.lastName')}
                        />
                        <Button isDisabled={!isValid || isSubmitting} type="submit">
                            {t('register.submit')}
                        </Button>
                    </Stack>
                </Form>
            )}
        </Formik>
    );
};

export default RegisterForm;
