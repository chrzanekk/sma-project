import {useTranslation} from "react-i18next";
import {themeColors} from "@/theme/theme-colors.ts";
import {Field, Form, Formik} from "formik";
import * as Yup from "yup";
import {UserDTO, UserFormDTO} from "@/types/user-types.ts";
import {getUserById, updateUser} from "@/services/user-service.ts";
import {errorNotification, successNotification} from "@/notifications/notifications.ts";
import {Button, Input, InputProps, Stack, Text} from "@chakra-ui/react";
import Select from "react-select";
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
            {({errors, touched, isValid, isSubmitting, setFieldValue, setFieldTouched, values}) => (
                <Form>
                    <Stack gap="12px">
                        <Field name="firstName">
                            {({field}: any) => (
                                <div>
                                    <Text fontSize="sm" fontWeight="bold" mb="1">
                                        {t('shared.firstName')}
                                    </Text>
                                    <Input
                                        {...field}
                                        placeholder={t('shared.firstName')}
                                        {...inputProps}
                                    />
                                    {touched.firstName && errors.firstName && (
                                        <Text color="red.500" fontSize="xs" mt="1">
                                            {errors.firstName}
                                        </Text>
                                    )}
                                </div>
                            )}
                        </Field>

                        {/* Last Name Field */}
                        <Field name="lastName">
                            {({field}: any) => (
                                <div>
                                    <Text fontSize="sm" fontWeight="bold" mb="1">
                                        {t('shared.lastName')}
                                    </Text>
                                    <Input
                                        {...field}
                                        placeholder={t('shared.lastName')}
                                        {...inputProps}
                                    />
                                    {touched.lastName && errors.lastName && (
                                        <Text color="red.500" fontSize="xs" mt="1">
                                            {errors.lastName}
                                        </Text>
                                    )}
                                </div>
                            )}
                        </Field>

                        {/* Position Field */}
                        <Field name="position">
                            {({field}: any) => (
                                <div>
                                    <Text fontSize="sm" fontWeight="bold" mb="1">
                                        {t('shared.position')}
                                    </Text>
                                    <Input
                                        {...field}
                                        placeholder={t('shared.position')}
                                        {...inputProps}
                                    />
                                    {touched.position && errors.position && (
                                        <Text color="red.500" fontSize="xs" mt="1">
                                            {errors.position}
                                        </Text>
                                    )}
                                </div>
                            )}
                        </Field>

                        {/* Locked Field */}
                        <Field name="locked">
                            {() => (
                                <div>
                                    <Text fontSize="sm" fontWeight="bold" mb="1">
                                        {t('shared.locked')}
                                    </Text>
                                    <Select
                                        options={booleanOptions}
                                        placeholder={t("shared.locked")}
                                        value={booleanOptions.find((option) => option.value === values.locked)}
                                        onChange={(selectedOption) => {
                                            setFieldValue("locked", selectedOption?.value).catch();
                                            setFieldTouched("locked", true, false).catch();
                                        }}
                                        styles={{
                                            control: (provided) => ({
                                                ...provided,
                                                backgroundColor: themeColors.bgColorLight(),
                                                borderColor: themeColors.borderColor(),
                                                borderRadius: "md",
                                                boxShadow: "none",
                                            })
                                        }}
                                    />
                                    {touched.locked && errors.locked && (
                                        <Text color="red.500" fontSize="xs" mt="1">
                                            {errors.locked}
                                        </Text>
                                    )}
                                </div>
                            )}
                        </Field>

                        {/* Enabled Field */}
                        <Field name="enabled">
                            {() => (
                                <div>
                                    <Text fontSize="sm" fontWeight="bold" mb="1">
                                        {t('shared.enabled')}
                                    </Text>
                                    <Select
                                        options={booleanOptions}
                                        placeholder={t("shared.enabled")}
                                        value={booleanOptions.find((option) => option.value === values.enabled)}
                                        onChange={(selectedOption) => {
                                            setFieldValue("enabled", selectedOption?.value).catch();
                                            setFieldTouched("enabled", true, false).catch();
                                        }}
                                        styles={{
                                            control: (provided) => ({
                                                ...provided,
                                                backgroundColor: themeColors.bgColorLight(),
                                                borderColor: themeColors.borderColor(),
                                                borderRadius: "md",
                                                boxShadow: "none",
                                            })
                                        }}
                                    />
                                    {touched.enabled && errors.enabled && (
                                        <Text color="red.500" fontSize="xs" mt="1">
                                            {errors.enabled}
                                        </Text>
                                    )}
                                </div>
                            )}
                        </Field>

                        <Button disabled={!isValid || isSubmitting} type="submit" colorPalette="green">
                            {t('save', {ns: "common"})}
                        </Button>
                    </Stack>
                </Form>
            )}
        </Formik>
    );
}

export default EditUserDataForm;