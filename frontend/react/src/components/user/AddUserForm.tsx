import {Field, Form, Formik} from 'formik';
import * as Yup from 'yup';
import {Button, Input, InputProps, Stack, Text} from "@chakra-ui/react";
import {errorNotification, successNotification} from "@/notifications/notifications.ts";
import {addUser} from "@/services/user-service.ts";
import {UserFormDTO} from "@/types/user-types.ts";
import {useTranslation} from "react-i18next";
import React from "react";
import {themeColors} from "@/theme/theme-colors.ts";
import Select from 'react-select';
import {formatMessage} from "@/notifications/FormatMessage.tsx";
import useRoles from "@/hooks/UseRoles.tsx";

interface AddUserFormProps {
    onSuccess: () => void;
}

const AddUserForm: React.FC<AddUserFormProps> = ({onSuccess}) => {
    const {t} = useTranslation(['auth', 'common']);
    const {roles: roleOptions, isLoading, error} = useRoles();


    const inputProps: InputProps = {
        size: "sm",
        bg: themeColors.bgColorSecondary(),
        borderRadius: "md"
    };

    const booleanOptions = [
        {value: "true", label: t("yes", {ns: "common"})},
        {value: "false", label: t("no", {ns: "common"})}
    ];

    if (isLoading) return <div>{t('processing', {ns: "common"})}</div>;
    if (error) return <div>{t('error', {ns: "common"})}: {error}</div>;

    return (
        <Formik
            initialValues={{
                login: '',
                email: '',
                password: '',
                firstName: '',
                lastName: '',
                position: '',
                locked: true,
                enabled: false,
                roles: [] as string[]
            }}
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
                locked: Yup.string()
                    .oneOf(["true", "false"], t("verification.invalidValue"))
                    .required(t("verification.required", {field: t("shared.locked")})),
                enabled: Yup.string()
                    .oneOf(["true", "false"], t("verification.invalidValue"))
                    .required(t("verification.required", {field: t("shared.enabled")})),
            })}
            onSubmit={async (newUser: UserFormDTO, {setSubmitting}) => {
                setSubmitting(true);
                try {
                    await addUser(newUser);
                    successNotification(
                        t('success', {ns: "common"}),
                        formatMessage('notifications.userAddedSuccess', {login: newUser.login})
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
            {({touched, errors,isValid, isSubmitting, setFieldValue, setFieldTouched, values, dirty}) => (
                <Form>
                    <Stack gap="8px">
                        {/* Login Field */}
                        <Field name="login">
                            {({field}: any) => (
                                <div>
                                    <Text fontSize="sm" fontWeight="bold" mb="1">
                                        {t('shared.login')}
                                    </Text>
                                    <Input {...field} placeholder={t('shared.login')} {...inputProps}/>
                                       {touched.login && errors.login && (
                                           <Text color="red.500" fontSize="xs" mt="1">
                                               {errors.login}
                                           </Text>
                                       )}
                                </div>
                            )}
                        </Field>

                        {/* Email Field */}
                        <Field name="email">
                            {({field}: any) => (
                                <div>
                                    <Text fontSize="sm" fontWeight="bold" mb="1">
                                        {t('shared.email')}
                                    </Text>
                                    <Input {...field} type="email" placeholder={t('shared.email')} {...inputProps}/>
                                       {touched.email && errors.email && (
                                           <Text color="red.500" fontSize="xs" mt="1">
                                               {errors.email}
                                           </Text>
                                       )}
                                </div>
                            )}
                        </Field>

                        {/* Password Field */}
                        <Field name="password">
                            {({field}: any) => (
                                <div>
                                    <Text fontSize="sm" fontWeight="bold" mb="1">
                                        {t('shared.password')}
                                    </Text>
                                    <Input {...field} type="password" placeholder={t('shared.password')} {...inputProps}/>
                                       {touched.password && errors.password && (
                                           <Text color="red.500" fontSize="xs" mt="1">
                                               {errors.password}
                                           </Text>
                                       )}
                                </div>
                            )}
                        </Field>

                        {/* First Name Field */}
                        <Field name="firstName">
                            {({field}: any) => (
                                <div>
                                    <Text fontSize="sm" fontWeight="bold" mb="1">
                                        {t('shared.firstName')}
                                    </Text>
                                    <Input {...field} placeholder={t('shared.firstName')} {...inputProps}/>
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
                                    <Input {...field} placeholder={t('shared.lastName')} {...inputProps}/>
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
                                    <Input {...field} placeholder={t('shared.position')} {...inputProps}/>
                                       {touched.position && errors.position && (
                                           <Text color="red.500" fontSize="xs" mt="1">
                                               {errors.position}
                                           </Text>
                                       )}
                                </div>
                            )}
                        </Field>

                        {/* Roles Select Field */}
                        <Field name="roles">
                            {() => (
                                <div>
                                    <Text fontSize="sm" fontWeight="bold" mb="1">
                                        {t('shared.chooseRoles')}
                                    </Text>
                                    <Select
                                        isMulti
                                        options={roleOptions}
                                        placeholder={t("shared.chooseRoles")}
                                        closeMenuOnSelect={false}
                                        onChange={(selectedOptions) => {
                                            const roles = selectedOptions.map((option) => option.value);
                                            setFieldValue("roles", roles || []).catch();
                                            setFieldTouched("roles", true, false).catch();
                                        }}
                                        styles={{
                                            control: (provided) => ({
                                                ...provided,
                                                backgroundColor: themeColors.bgColorSecondary(),
                                                borderColor: themeColors.borderColor(),
                                                borderRadius: "md",
                                                boxShadow: "none",
                                            })
                                        }}
                                    />
                                    {touched.roles && errors.roles && (
                                        <Text color="red.500" fontSize="xs" mt="1">
                                            {errors.roles}
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
                                        value={booleanOptions.find((option) => option.value === String(values.locked))}
                                        onChange={(selectedOption) => {
                                            setFieldValue("locked", selectedOption?.value).catch();
                                            setFieldTouched("locked", true, false).catch();
                                        }}
                                        styles={{
                                            control: (provided) => ({
                                                ...provided,
                                                backgroundColor: themeColors.bgColorSecondary(),
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
                                        value={booleanOptions.find((option) => option.value === String(values.enabled))}
                                        onChange={(selectedOption) => {
                                            setFieldValue("enabled", selectedOption?.value).catch();
                                            setFieldTouched("enabled", true, false).catch();
                                        }}
                                        styles={{
                                            control: (provided) => ({
                                                ...provided,
                                                backgroundColor: themeColors.bgColorSecondary(),
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
                        <Button disabled={!isValid || isSubmitting ||!dirty} type="submit" colorPalette="green">
                            {t('shared.addUser')}
                        </Button>
                    </Stack>
                </Form>
            )}
        </Formik>
    );
};

export default AddUserForm;
