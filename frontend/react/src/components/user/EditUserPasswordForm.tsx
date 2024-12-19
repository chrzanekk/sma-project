import {useTranslation} from "react-i18next";
import {themeColors} from "@/theme/theme-colors.ts";
import {Field, Form, Formik} from "formik";
import * as Yup from "yup";
import {AdminEditPasswordChangeRequest} from "@/types/user-types.ts";
import {setNewUserPassword} from "@/services/user-service.ts";
import {errorNotification, successNotification} from "@/notifications/notifications.ts";
import {Button, FormControl, FormErrorMessage, FormLabel, Input, Stack} from "@chakra-ui/react";
import React from "react";

interface EditUserPasswordFormProps {
    onSuccess: () => void;
    userId: number;
    login: string;
}

const EditUserPasswordForm: React.FC<EditUserPasswordFormProps> = ({onSuccess, userId, login}) => {
    const {t} = useTranslation(['auth', 'common'])

    const inputProps = {
        size: "sm",
        bg: themeColors.bgColorLight(),
        borderRadius: "md"
    }
    return (
        <Formik
            initialValues={{
                newPassword: "",
                confirmPassword: ""
            }}
            validationSchema={Yup.object({
                newPassword: Yup.string()
                    .min(6, t('verification.minLength', {field: t('updateProfile.newPassword'), count: 6}))
                    .max(20, t('verification.maxLength', {field: t('updateProfile.newPassword'), count: 20}))
                    .required(t('verification.required', {field: t('updateProfile.newPassword')})),
                confirmPassword: Yup.string()
                    .oneOf([Yup.ref("newPassword")], t('verification.passwordNotMatch'))
                    .required(t('verification.required', {field: t('updateProfile.confirmPassword')})),
            })}
            onSubmit={async (values, {setSubmitting}) => {
                setSubmitting(true);
                try {
                    console.log("Submitting values:", values);
                    console.log("User ID:", userId);
                    const payload: AdminEditPasswordChangeRequest = {
                        userId: userId,
                        newPassword: values.newPassword
                    }
                    console.log("Payload being sent:", payload);
                    await setNewUserPassword(payload);
                    successNotification(
                        t('success', {ns: "common"}),
                        t('notifications.userPasswordSetSuccess', {login: login, ns: "common"})
                    );
                    onSuccess();
                } catch (err: any) {
                    console.error(err);
                    errorNotification(
                        t('error', {ns: "common"}),
                        err.response?.data?.message || t('notifications.userPasswordSetError', {login: login, ns: "common"})
                    );
                } finally {
                    setSubmitting(false)
                }
            }}
        >
            {({errors, touched, isValid, isSubmitting}) => (
                <Form>
                    <Stack spacing="8px">
                        <Field name="newPassword">
                            {({field}: { field: any }) => (
                                <FormControl isInvalid={!!errors.newPassword && touched.newPassword}>
                                    <FormLabel>{t('newPassword.newPassword')}</FormLabel>
                                    <Input {...field}
                                           type="password"
                                           placeholder="********"
                                           {...inputProps} />
                                    <FormErrorMessage>{errors.newPassword}</FormErrorMessage>
                                </FormControl>
                            )}
                        </Field>
                        <Field name="confirmPassword">
                            {({field}: { field: any }) => (
                                <FormControl
                                    isInvalid={!!errors.confirmPassword && touched.confirmPassword}
                                >
                                    <FormLabel>{t("newPassword.confirmPassword")}</FormLabel>
                                    <Input
                                        {...field}
                                        type="password"
                                        placeholder="********"
                                        {...inputProps}
                                    />
                                    <FormErrorMessage>{errors.confirmPassword}</FormErrorMessage>
                                </FormControl>
                            )}
                        </Field>
                        <Button isDisabled={!isValid || isSubmitting} type="submit" colorScheme="green">
                            {t('newPassword.submit')}
                        </Button>
                    </Stack>
                </Form>
            )}
        </Formik>
    );
}

export default EditUserPasswordForm;