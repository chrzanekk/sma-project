import {useTranslation} from "react-i18next";
import {Form, Formik} from "formik";
import * as Yup from "yup";
import {AdminEditPasswordChangeRequest} from "@/types/user-types.ts";
import {setNewUserPassword} from "@/services/user-service.ts";
import {errorNotification, successNotification} from "@/notifications/notifications.ts";
import {Button, Stack} from "@chakra-ui/react";
import React from "react";
import {formatMessage} from "@/notifications/FormatMessage.tsx";
import {CustomInputField} from "@/components/shared/CustomFormFields.tsx";


interface EditUserPasswordFormProps {
    onSuccess: () => void;
    userId: number;
    login: string;
}

const EditUserPasswordForm: React.FC<EditUserPasswordFormProps> = ({onSuccess, userId, login}) => {
    const {t} = useTranslation(['auth', 'common'])

    return (
        <Formik
            initialValues={{
                newPassword: "",
                confirmPassword: ""
            }}
            validationSchema={Yup.object({
                newPassword: Yup.string()
                    .min(6, t('verification.minLength', {field: t('updateProfile.newPassword'), count: 6}))
                    .max(50, t('verification.maxLength', {field: t('updateProfile.newPassword'), count: 50}))
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
            {({isValid, isSubmitting, dirty}) => (
                <Form>
                    <Stack gap="8px">
                        <CustomInputField name={"newPassword"} label={t('updateProfile.newPassword')}
                                          placeholder={t('updateProfile.newPassword')} type={"password"}/>
                        <CustomInputField name={"confirmPassword"} label={t('updateProfile.confirmPassword')}
                                          placeholder={t('updateProfile.confirmPassword')} type={"password"}/>
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