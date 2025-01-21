import {useTranslation} from "react-i18next";
import {themeColors} from "@/theme/theme-colors.ts";
import {Field, Form, Formik} from "formik";
import * as Yup from "yup";
import {AdminEditPasswordChangeRequest} from "@/types/user-types.ts";
import {setNewUserPassword} from "@/services/user-service.ts";
import {errorNotification, successNotification} from "@/notifications/notifications.ts";
import {Button, Input, InputProps, Stack, Text} from "@chakra-ui/react";
import React from "react";
import {formatMessage} from "@/notifications/FormatMessage.tsx";

interface EditUserPasswordFormProps {
    onSuccess: () => void;
    userId: number;
    login: string;
}

const EditUserPasswordForm: React.FC<EditUserPasswordFormProps> = ({onSuccess, userId, login}) => {
    const {t} = useTranslation(['auth', 'common'])

    const inputProps: InputProps = {
        size: "sm",
        bg: themeColors.bgColorSecondary(),
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
                        formatMessage('notifications.userPasswordSetSuccess', {login: login})
                    );
                    onSuccess();
                } catch (err: any) {
                    console.error(err);
                    errorNotification(
                        t('error', {ns: "common"}),
                        err.response?.data?.message || t('notifications.userPasswordSetError', {
                            login: login,
                            ns: "common"
                        })
                    );
                } finally {
                    setSubmitting(false)
                }
            }}
        >
            {({errors, touched, handleChange, isValid, isSubmitting, dirty}) => (
                <Form>
                    <Stack gap="8px">
                        <Field name="newPassword">
                            {({field}: any) => (
                                <div>
                                    <Text fontSize="sm" fontWeight="bold" mb="1">
                                        {t('updateProfile.newPassword')}
                                    </Text>
                                    <Input
                                        {...field}
                                        type="password"
                                        placeholder="********"
                                        onChange={handleChange}
                                        {...inputProps}
                                    />
                                    {touched.newPassword && errors.newPassword && (
                                        <Text color="red.500" fontSize="xs" mt="1">
                                            {errors.newPassword}
                                        </Text>
                                    )}
                                </div>
                            )}
                        </Field>

                        {/* Confirm Password Field */}
                        <Field name="confirmPassword">
                            {({field}: any) => (
                                <div>
                                    <Text fontSize="sm" fontWeight="bold" mb="1">
                                        {t('updateProfile.confirmPassword')}
                                    </Text>
                                    <Input
                                        {...field}
                                        type="password"
                                        placeholder="********"
                                        onChange={handleChange}
                                        {...inputProps}
                                    />
                                    {touched.confirmPassword && errors.confirmPassword && (
                                        <Text color="red.500" fontSize="xs" mt="1">
                                            {errors.confirmPassword}
                                        </Text>
                                    )}
                                </div>
                            )}
                        </Field>
                        <Button disabled={!isValid || isSubmitting || !dirty} type="submit" colorScheme="green">
                            {t('newPassword.submit')}
                        </Button>
                    </Stack>
                </Form>
            )}
        </Formik>
    );
}

export default EditUserPasswordForm;