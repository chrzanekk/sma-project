import {useTranslation} from "react-i18next";
import {themeColors} from "@/theme/theme-colors.ts";
import {Field, Form, Formik} from "formik";
import * as Yup from "yup";
import {UserDTO, UserFormDTO} from "@/types/user-types.ts";
import {getUserById, updateUser} from "@/services/user-service.ts";
import {errorNotification, successNotification} from "@/notifications/notifications.ts";
import {Button, FormControl, FormErrorMessage, FormLabel, Input, Stack} from "@chakra-ui/react";
import {Select} from "chakra-react-select";
import React, {useEffect, useState} from "react";

interface EditUserFormProps {
    onSuccess: () => void;
    userId: number;
}

const EditUserForm: React.FC<EditUserFormProps> = ({onSuccess, userId}) => {
    const {t} = useTranslation(['auth', 'common'])
    const [initialValues, setInitialValues] = useState<UserFormDTO | null>(null);

    useEffect(() => {
        const fetchUser = async () => {
            try {
                const user: UserDTO = await getUserById(userId);
                setInitialValues({
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
        return <div>Loading...</div>;
    }

    const inputProps = {
        size: "sm",
        bg: themeColors.bgColorLight(),
        borderRadius: "md"
    }

    const roleOptions = [
        {value: "ROLE_USER", label: "USER"},
        {value: "ROLE_ADMIN", label: "ADMIN"}
    ];

    const booleanOptions = [
        {value: true, label: t("yes", {ns: "common"})},
        {value: false, label: t("no", {ns: "common"})}
    ];

    return (
        <Formik
            initialValues={initialValues}
            validationSchema={Yup.object({
                login: Yup.string()
                    .min(5, t('verification.minLength', {field: t('shared.login'), count: 5}))
                    .max(15, t('verification.maxLength', {field: t('shared.login'), count: 15}))
                    .required(t('verification.required', {field: t('shared.login')})),
                email: Yup.string()
                    .email(t('verification.invalidEmail'))
                    .required(t('verification.required', {field: t('shared.email')})),
                password: Yup.string()
                    .min(4, t('verification.minLength', {field: t('shared.password'), count: 4}))
                    .max(10, t('verification.maxLength', {field: t('shared.password'), count: 10}))
                    .required(t('verification.required', {field: t('shared.password')})),
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
                roles: Yup.array().of(Yup.string())
                    .min(1, t('updateProfile.roleMissing')),
                // locked: Yup.string()
                //     .oneOf(["true", "false"], t("verification.invalidValue"))
                //     .required(t("verification.required", {field: t("shared.locked")})),
                // enabled: Yup.string()
                //     .oneOf(["true", "false"], t("verification.invalidValue"))
                //     .required(t("verification.required", {field: t("shared.enabled")})),
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
                        t('notifications.userAddedSuccess', {login: user.login, ns: "common"})
                    );
                    onSuccess();
                } catch (err: any) {
                    console.error(err);
                    errorNotification(
                        t('error', {ns: "common"}),
                        err.response?.data?.message || t('notifications.userAddedError', {ns: "common"})
                    );
                } finally {
                    setSubmitting(false)
                }
            }}
        >
            {({errors, touched, isValid, isSubmitting, setFieldValue, setFieldTouched, values}) => (
                <Form>
                    <Stack spacing="8px">
                        <Field name="login">
                            {({field}: { field: any }) => (
                                <FormControl isInvalid={!!errors.login && touched.login}>
                                    <FormLabel>{t('shared.login')}</FormLabel>
                                    <Input {...field} placeholder={t('shared.login')} {...inputProps} />
                                    <FormErrorMessage>{errors.login}</FormErrorMessage>
                                </FormControl>
                            )}
                        </Field>
                        <Field name="email">
                            {({field}: { field: any }) => (
                                <FormControl isInvalid={!!errors.email && touched.email}>
                                    <FormLabel>{t('shared.email')}</FormLabel>
                                    <Input {...field} placeholder={t('shared.email')} {...inputProps} />
                                    <FormErrorMessage>{errors.email}</FormErrorMessage>
                                </FormControl>
                            )}
                        </Field>
                        <Field name="password">
                            {({field}: { field: any }) => (
                                <FormControl isInvalid={!!errors.password && touched.password}>
                                    <FormLabel>{t('shared.password')}</FormLabel>
                                    <Input {...field} type="password"
                                           placeholder={t('shared.password')} {...inputProps} />
                                    <FormErrorMessage>{errors.password}</FormErrorMessage>
                                </FormControl>
                            )}
                        </Field>
                        <Field name="firstName">
                            {({field}: { field: any }) => (
                                <FormControl isInvalid={!!errors.firstName && touched.firstName}>
                                    <FormLabel>{t('shared.firstName')}</FormLabel>
                                    <Input {...field} placeholder={t('shared.firstName')} {...inputProps} />
                                    <FormErrorMessage>{errors.firstName}</FormErrorMessage>
                                </FormControl>
                            )}
                        </Field>
                        <Field name="lastName">
                            {({field}: { field: any }) => (
                                <FormControl isInvalid={!!errors.lastName && touched.lastName}>
                                    <FormLabel>{t('shared.lastName')}</FormLabel>
                                    <Input {...field} placeholder={t('shared.lastName')} {...inputProps} />
                                    <FormErrorMessage>{errors.lastName}</FormErrorMessage>
                                </FormControl>
                            )}
                        </Field>
                        <Field name="position">
                            {({field}: { field: any }) => (
                                <FormControl isInvalid={!!errors.position && touched.position}>
                                    <FormLabel>{t('shared.position')}</FormLabel>
                                    <Input {...field} placeholder={t('shared.position')} {...inputProps} />
                                    <FormErrorMessage>{errors.position}</FormErrorMessage>
                                </FormControl>
                            )}
                        </Field>
                        <FormControl isInvalid={!!errors.roles && touched.roles}>
                            <FormLabel>{t("shared.userRoles")}</FormLabel>
                            <Select
                                isMulti
                                options={roleOptions}
                                placeholder={t("shared.userRoles")}
                                closeMenuOnSelect={false}
                                onChange={(selectedOptions) => {
                                    const roles = selectedOptions.map((option) => option.value);
                                    setFieldValue("roles", roles || []).catch(() => {
                                    });
                                    setFieldTouched("roles", true, false).catch(() => {
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
                            <FormErrorMessage>{errors.roles}</FormErrorMessage>
                        </FormControl>
                        <FormControl isInvalid={!!errors.locked && touched.locked}>
                            <FormLabel>{t("shared.locked")}</FormLabel>
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
                            <FormErrorMessage>{errors.locked}</FormErrorMessage>
                        </FormControl>
                        <FormControl isInvalid={!!errors.enabled && touched.enabled}>
                            <FormLabel>{t("shared.enabled")}</FormLabel>
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
                            <FormErrorMessage>{errors.enabled}</FormErrorMessage>
                        </FormControl>
                        <Button isDisabled={!isValid || isSubmitting} type="submit" colorScheme="green">
                            {t('shared.addUser')}
                        </Button>
                    </Stack>
                </Form>
            )}
        </Formik>
    );
}

export default EditUserForm;