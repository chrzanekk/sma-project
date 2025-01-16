import {useLocation, useNavigate} from "react-router-dom";
import {Form, Formik} from "formik";
import * as Yup from "yup";
import {errorNotification, successNotification} from "@/notifications/notifications.ts";
import {Box, Button, Heading, Image, Stack} from "@chakra-ui/react";
import {MyTextInput} from "@/components/shared/CustomFields.tsx";
import {resetPassword} from "@/services/auth-service.ts";
import {useTranslation} from "react-i18next";


const NewPasswordForm = () => {

    const navigate = useNavigate();
    const location = useLocation();
    const queryParams = new URLSearchParams(location.search);
    const token = queryParams.get('token');
    const {t} = useTranslation(['auth', 'common']);


    if (!token) {
        errorNotification(t('common.error'), t("newPassword.invalidToken"))
        navigate("/")
        return null;
    }
    return (
        <Formik
            validateOnMount={true}
            validationSchema={Yup.object({
                password: Yup.string()
                    .min(6, t('verification.minLength', {field: t('shared.password'), count: 6}))
                    .max(20, t('verification.maxLength', {field: t('shared.password'), count: 20}))
                    .required(t('verification.required', {field: t('shared.password')})),
                confirmPassword: Yup.string()
                    .oneOf([Yup.ref("password")], t('verification.passwordNotMatch'))
                    .required(t('verification.required', {field: t('newPassword.confirmPassword')})),
            })}
            initialValues={{password: "", confirmPassword: ""}}
            onSubmit={(values, {setSubmitting}) => {
                setSubmitting(true);

                const requestData = {
                    password: values.password,
                    confirmPassword: values.confirmPassword,
                    token: token,
                };

                resetPassword(requestData)
                    .then(() => {
                        navigate("/");
                        successNotification(t('success', {ns: "common"}), t("notifications.resetPasswordSuccess"));
                    })
                    .catch(() => {
                    })
                    .finally(() => {
                        setSubmitting(false);
                    });
            }}
        >
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
                            <Heading fontSize="2xl" mb={15}>
                                {t("newPassword.header")}
                            </Heading>
                        </Box>
                        <MyTextInput
                            label={t("newPassword.newPassword")}
                            name="password"
                            type="password"
                            placeholder={t("newPassword.newPassword")}
                        />
                        <MyTextInput
                            label={t("newPassword.confirmPassword")}
                            name="confirmPassword"
                            type="password"
                            placeholder={t("newPassword.confirmPassword")}
                        />
                        <Button
                            type="submit"
                            disabled={!isValid || isSubmitting}
                        >
                            {t("newPassword.submit")}
                        </Button>
                    </Stack>
                </Form>
            )}
        </Formik>
    )
}

export default NewPasswordForm;