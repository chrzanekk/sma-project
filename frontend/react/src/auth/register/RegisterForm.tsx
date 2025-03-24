import {Form, Formik} from 'formik';
import * as Yup from 'yup';
import {Box, Button, Heading, Stack} from "@chakra-ui/react";
import {successNotification} from "@/notifications/notifications.ts";
import {registerUser} from "@/services/auth-service.ts";
import {RegisterRequest} from "@/types/user-types.ts";
import {useTranslation} from "react-i18next";
import {CustomInputField} from "@/components/shared/CustomFormFields.tsx";
import {useThemeColors} from "@/theme/theme-colors.ts";
import {useNavigate} from "react-router-dom";

const RegisterForm = () => {
    const {t} = useTranslation(['auth', 'common']);
    const navigate = useNavigate();
    const themeColors = useThemeColors();

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
                    .max(20, t('verification.maxLength', {field: t('shared.login'), count: 20}))
                    .required(t('verification.required', {field: t('shared.login')})),
                email: Yup.string()
                    .email(t('verification.invalidEmail'))
                    .required(t('verification.required', {field: t('shared.email')})),
                password: Yup.string()
                    .min(4, t('verification.minLength', {field: t('shared.password'), count: 4}))
                    .max(50, t('verification.maxLength', {field: t('shared.password'), count: 50}))
                    .required(t('verification.required', {field: t('shared.password')})),
                firstName: Yup.string()
                    .min(2, t('verification.minLength', {field: t('shared.firstName'), count: 2}))
                    .max(30, t('verification.maxLength', {field: t('shared.firstName'), count: 30}))
                    .required(t('verification.required', {field: t('shared.firstName')})),
                lastName: Yup.string()
                    .min(2, t('verification.minLength', {field: t('shared.lastName'), count: 2}))
                    .max(30, t('verification.maxLength', {field: t('shared.lastName'), count: 30}))
                    .required(t('verification.required', {field: t('shared.lastName')})),
            })}
            onSubmit={async (register: RegisterRequest, {setSubmitting}) => {
                setSubmitting(true);

                registerUser(register).then(() => {
                    successNotification(
                        t('success', {ns: "common"}),
                        t('notifications.registerSuccessDetails', {login: register.login})
                    )
                    navigate("/")
                }).catch(() => {
                }).finally(() => {
                    setSubmitting(false);
                });
            }}
        >
            {({isValid, isSubmitting}) => (
                <Form>
                    <Box display="flex" justifyContent="center">
                        <Heading fontSize="2xl" mb={15} color={themeColors.fontColor}>{t('register.header')}</Heading>
                    </Box>
                    <Stack gap={2}>
                        <CustomInputField
                            label={t('shared.login')}
                            name="login"
                            type="text"
                            placeholder={t('shared.login')}
                        />
                        <CustomInputField
                            label={t('shared.email')}
                            name="email"
                            type="email"
                            placeholder="example@example.com"
                        />
                        <CustomInputField
                            label={t('shared.password')}
                            name="password"
                            type="password"
                            placeholder={t('shared.password')}
                        />
                        <CustomInputField
                            label={t('shared.firstName')}
                            name="firstName"
                            type="text"
                            placeholder={t('shared.firstName')}
                        />
                        <CustomInputField
                            label={t('shared.lastName')}
                            name="lastName"
                            type="text"
                            placeholder={t('shared.lastName')}
                        />
                        <Button disabled={!isValid || isSubmitting} type="submit" colorPalette={"green"}>
                            {t('register.submit')}
                        </Button>
                    </Stack>
                </Form>
            )}
        </Formik>
    );
};

export default RegisterForm;
