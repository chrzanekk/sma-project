import {themeColors} from "@/theme/theme-colors.ts";
import {useTranslation} from "react-i18next";
import {Field, Form, Formik} from "formik";
import * as Yup from 'yup';
import React from "react";
import {addNewRole} from "@/services/role-service.ts";
import {errorNotification, successNotification} from "@/notifications/notifications.ts";
import {formatMessage} from "@/notifications/FormatMessage.tsx";
import {Button, Input, InputProps, Stack} from "@chakra-ui/react";
import {RoleDTO} from "@/types/role-types.ts";

interface AddRoleFormProps {
    onSuccess: () => void;
}

const AddRoleForm: React.FC<AddRoleFormProps> = ({onSuccess}) => {

    const {t} = useTranslation('auth');

    const inputProps: InputProps = {
        size: "sm",
        bg: themeColors.bgColorSecondary(),
        borderRadius: "md"
    };

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
                            formatMessage('notifications.addRoleSuccess', {ns: "common", role: newRole.name})
                        );
                        onSuccess();
                    } catch (err: any) {
                        console.error(err);
                        errorNotification(
                            t('error', {ns: "common"}),
                            err.response?.data?.message || t('notifications.roleAddedError', {ns: "errors"})
                        );
                    } finally {
                        setSubmitting(false);
                    }
                }}
        >
            {({errors, touched, values, handleChange, isValid, isSubmitting, dirty}) => (
                <Form>
                    <Stack gap="8px">
                        <Field
                            label={t('shared.roleName')}
                            invalid={!!errors.name && touched.name}
                            errorText={errors.name}
                            name={"name"}
                        >
                            {({field}: any) => (
                                <Input
                                    {...field}
                                    type="text"
                                    name="name"
                                    value={values.name}
                                    onChange={handleChange}
                                    placeholder={t('shared.roleName')}
                                    {...inputProps}
                                />
                            )}
                        </Field>
                        <Button disabled={!isValid || isSubmitting ||!dirty} type="submit" colorPalette="green">
                            {t('shared.addRole')}
                        </Button>
                    </Stack>
                </Form>
            )}
        </Formik>
    );
};
export default AddRoleForm;