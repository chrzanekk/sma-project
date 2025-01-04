import {useTranslation} from "react-i18next";
import {themeColors} from "@/theme/theme-colors.ts";
import {Field, Form, Formik} from "formik";
import * as Yup from "yup";
import {UserDTO, UserFormDTO} from "@/types/user-types.ts";
import {getUserById, updateUser} from "@/services/user-service.ts";
import {errorNotification, successNotification} from "@/notifications/notifications.ts";
import {Button, Input, InputProps, Stack} from "@chakra-ui/react";
import {Select} from "chakra-react-select";
import React, {useEffect, useState} from "react";
import {formatMessage} from "@/notifications/FormatMessage.tsx";

interface EditUserDataFormProps {
    onSuccess: () => void;
    userId: number;
}

const EditUserDataForm: React.FC<EditUserDataFormProps> = ({onSuccess, userId}) => {
    const {t} = useTranslation(['auth', 'common'])
    const [initialValues, setInitialValues] = useState<UserFormDTO | null>(null);

    useEffect(() => {
        const fetchUser = async () => {
            try {
                const user: UserDTO = await getUserById(userId);
                setInitialValues({
                    id: user.id,
                    login: user.login,
                    email: user.email,
                    password: '', // Password is not pre-filled for security reasons
                    firstName: user.firstName,
                    lastName: user.lastName,
                    position: user.position,
                    locked: user.locked,
                    enabled: user.enabled,
                    roles: user.roles ? Array.from(user.roles).map(role => role.name) : []
                });
            } catch (err) {
                console.error('Error fetching user:', err);
            }
        };

        fetchUser().catch();
    }, [userId]);

    if (!initialValues) {
        return <div>{t('processing', {ns: "common"})}</div>;
    }

    const inputProps: InputProps = {
        size: "sm",
        bg: themeColors.bgColorLight(),
        borderRadius: "md"
    }

    const booleanOptions = [
        {value: true, label: t("yes", {ns: "common"})},
        {value: false, label: t("no", {ns: "common"})}
    ];

    return (
        <Formik
            initialValues={initialValues}
            validationSchema={Yup.object({
                firstName: Yup.string()
                    .min(2, t('verification.minLength', {field: t('shared.firstName'), count: 2}))
                    .max(10, t('verification.maxLength', {field: t('shared.firstName'), count: 10}))
                    .required(t('verification.required', {field: t('shared.firstName')})),
                lastName: Yup.string()
                    .min(2, t('verification.minLength', {field: t('shared.lastName'), count: 2}))
                    .max(10, t('verification.maxLength', {field: t('shared.lastName'), count: 10}))
                    .required(t('verification.required', {field: t('shared.lastName')})),
                position: Yup.string()
                    .min(2, t('verification.minLength', {field: t('shared.position'), count: 2}))
                    .max(50, t('verification.maxLength', {field: t('shared.position'), count: 50}))
                    .required(t('verification.required', {field: t('shared.position')})),
                locked: Yup.boolean()
                    .required(t("verification.required", {field: t("shared.locked")})),
                enabled: Yup.boolean()
                    .required(t("verification.required", {field: t("shared.enabled")})),
            })}
            onSubmit={async (user: UserFormDTO, {setSubmitting}) => {
                setSubmitting(true);
                try {
                    await updateUser(user);
                    successNotification(
                        t('success', {ns: "common"}),
                        formatMessage('notifications.userEditedSuccess', {login: user.login})
                    );
                    onSuccess();
                } catch (err: any) {
                    console.error(err);
                    errorNotification(
                        t('error', {ns: "common"}),
                        err.response?.data?.message || t('notifications.userEditedError', {ns: "common"})
                    );
                } finally {
                    setSubmitting(false)
                }
            }}
        >
            {({errors, touched, handleChange, isValid, isSubmitting, setFieldValue, setFieldTouched, values}) => (
                <Form>
                    <Stack gap="8px">
                        <Field
                            label={t('shared.firstName')}
                            invalid={!!errors.firstName && touched.firstName}
                            errorText={errors.firstName}>
                            {({field}: { field: any }) => (
                                <Input
                                    {...field}
                                    placeholder={t('shared.firstName')}
                                    onChange={handleChange}
                                    {...inputProps} />
                            )}
                        </Field>
                        <Field
                            label={t('shared.lastName')}
                            invalid={!!errors.lastName && touched.lastName}
                            errorText={errors.lastName}>
                            {({field}: { field: any }) => (
                                <Input {...field}
                                       placeholder={t('shared.lastName')}
                                       onChange={handleChange}
                                       {...inputProps} />
                            )}
                        </Field>
                        <Field
                            label={t('shared.position')}
                            invalid={!!errors.position && touched.position}
                            errorText={errors.position}>
                            {({field}: { field: any }) => (
                                <Input {...field}
                                       placeholder={t('shared.position')}
                                       onChange={handleChange}
                                       {...inputProps} />
                            )}
                        </Field>
                        <Field
                            label={t('shared.locked')}
                            invalid={!!errors.locked && touched.locked}
                            errorText={errors.locked}>
                            <Select
                                options={booleanOptions}
                                placeholder={t("shared.locked")}
                                value={booleanOptions.find((option) => option.value === values.locked)}
                                onChange={(selectedOption) => {
                                    setFieldValue("locked", selectedOption?.value).catch(() => {
                                    });
                                    setFieldTouched("locked", true, false).catch(() => {
                                    });
                                }}
                                chakraStyles={{
                                    control: (provided) => ({
                                        ...provided,
                                        backgroundColor: themeColors.bgColorLight(),
                                        borderColor: themeColors.borderColor(),
                                        borderRadius: "md",
                                        boxShadow: "none",
                                    })
                                }}
                            />
                        </Field>
                        <Field
                            label={t('shared.enabled')}
                            invalid={!!errors.enabled && touched.enabled}
                            errorText={errors.enabled}>
                            <Select
                                options={booleanOptions}
                                placeholder={t("shared.enabled")}
                                value={booleanOptions.find((option) => option.value === values.enabled)}
                                onChange={(selectedOption) => {
                                    setFieldValue("enabled", selectedOption?.value).catch(() => {
                                    });
                                    setFieldTouched("enabled", true, false).catch(() => {
                                    });
                                }}
                                chakraStyles={{
                                    control: (provided) => ({
                                        ...provided,
                                        backgroundColor: themeColors.bgColorLight(),
                                        borderColor: themeColors.borderColor(),
                                        borderRadius: "md",
                                        boxShadow: "none",
                                    })
                                }}
                            />
                        </Field>
                        <Button disabled={!isValid || isSubmitting} type="submit" colorScheme="green">
                            {t('save', {ns: "common"})}
                        </Button>
                    </Stack>
                </Form>
            )}
        </Formik>
    );
}

export default EditUserDataForm;