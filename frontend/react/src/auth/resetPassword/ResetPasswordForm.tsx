import {useNavigate} from "react-router-dom";
import {Form, Formik} from "formik";
import * as Yup from "yup";
import {successNotification} from "@/notifications/notifications.ts";
import {Box, Button, Heading, Stack} from "@chakra-ui/react";
import {requestPasswordReset} from "@/services/auth-service.ts";
import {useTranslation} from "react-i18next";
import {CustomInputField} from "@/components/shared/CustomFormFields.tsx";
import {themeColors} from "@/theme/theme-colors.ts";


const ResetPasswordForm = () => {

    const navigate = useNavigate();
    const {t} = useTranslation(['auth', 'common']);

    return (
        <Formik
            validateOnMount={true}
            validationSchema={Yup.object({
                email: Yup.string()
                    .email(t('verification.invalidEmail'))
                    .required(t('verification.required', {field: t('shared.email')})),
            })}
            initialValues={{email: ""}}
            onSubmit={(values, {setSubmitting}) => {
                setSubmitting(true);

                const requestData = {
                    email: values.email
                };

                requestPasswordReset(requestData)
                    .then(() => {
                        navigate("/");
                        successNotification(t('success', {ns: "common"}), t('notifications.resetPasswordSuccess'));
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
                            <Heading fontSize="2xl" mb={15} color={themeColors.fontColor()}>
                                {t('resetPassword.header')}
                            </Heading>
                        </Box>
                        <CustomInputField
                            label={t('shared.email')}
                            name="email"
                            type="email"
                            placeholder={t('resetPassword.placeholder')}
                        />
                        <Button
                            type="submit"
                            colorPalette={"green"}
                            disabled={!isValid || isSubmitting}
                        >
                            {t('resetPassword.submit')}
                        </Button>
                    </Stack>
                </Form>
            )}
        </Formik>
    )
}

export default ResetPasswordForm;