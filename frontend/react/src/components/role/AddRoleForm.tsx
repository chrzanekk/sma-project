import {themeColors} from "@/theme/theme-colors.ts";
import {useTranslation} from "react-i18next";
import {Field, Form, Formik} from "formik";
import * as Yup from 'yup';
import React from "react";
import {addNewRole} from "@/services/role-service.ts";
import {errorNotification, successNotification} from "@/notifications/notifications.ts";
import {formatMessage} from "@/notifications/FormatMessage.tsx";
import {FormControl, FormErrorMessage, FormLabel, Input, Stack} from "@chakra-ui/react";
import {RoleDTO} from "@/types/role-types.ts";

interface AddRoleFormProps {
    onSuccess: () => void;
}

const AddRoleForm: React.FC<AddRoleFormProps> = ({onSuccess}) => {

    const {t} = useTranslation('auth');
    const inputProps = {
        size: "sm",
        bg: themeColors.bgColorLight(),
        borderRadius: "md"
    }

    return (
        <Formik initialValues={{name: ''}}
                validationSchema={Yup.object({
                    name: Yup.string()
                        .required(t('verification.required', {field: t('shared.roleName')}))
                        .min(3, t('verification.min', {field: t('shared.roleName'), min: 3}))
                        .max(15, t('verification.max', {field: t('shared.roleName'), max: 15}))
                })}
                onSubmit={async (newRole: RoleDTO, {setSubmitting}) => {
                    setSubmitting(true);
                    try {
                        await addNewRole(newRole);
                        successNotification(
                            t('success', {ns: "common"}),
                            formatMessage('notifications.RoleAddedSuccess', {login: newRole.name})
                        );
                        onSuccess();
                    } catch (err: any) {
                        console.error(err);
                        errorNotification(
                            t('error', {ns: "common"}),
                            err.response?.data?.message || t('notifications.roleAddedError', {ns: "common"})
                        );
                    } finally {
                        setSubmitting(false);
                    }
                }}
        >
            {({errors, touched}) => (
                <Form>
                    <Stack spacing="8px">
                        <Field name="name">
                            {({field}: { field: any }) => (
                                <FormControl isInvalid={!!errors.name && touched.name}>
                                    <FormLabel>{t('shared.roleName')}</FormLabel>
                                    <Input {...field} placeholder={t('shared.roleName')} {...inputProps} />
                                    <FormErrorMessage>{errors.name}</FormErrorMessage>
                                </FormControl>
                            )}
                        </Field>
                    </Stack>
                </Form>
            )}
        </Formik>
    );
};
export default AddRoleForm;