import {Field, Form, Formik} from 'formik';
import * as Yup from 'yup';
import {Button, Input,InputProps, Stack} from "@chakra-ui/react";
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
        bg: themeColors.bgColorLight(),
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
            {({errors, touched, handleChange, isValid, isSubmitting, setFieldValue, setFieldTouched, values}) => (
                <Form>
                    <Stack gap="8px">
                        <Field
                            label={t('shared.login')}
                            invalid={!!errors.login && touched.login}
                            errorText={errors.login}>
                            <Input
                                type={"text"}
                                name={"login"}
                                onChange={handleChange}
                                placeholder={t('shared.login')}
                                {...inputProps}/>
                        </Field>
                        <Field
                            label={t('shared.email')}
                            invalid={!!errors.email && touched.email}
                            errorText={errors.email}>
                            <Input
                                type={"email"}
                                name={"email"}
                                onChange={handleChange}
                                placeholder={t('shared.email')}
                                {...inputProps} />
                        </Field>
                        <Field
                            label={t('shared.password')}
                            invalid={!!errors.password && touched.password}
                            errorText={errors.password}>
                            <Input
                                type={"password"}
                                name={"password"}
                                onChange={handleChange}
                                placeholder={t('shared.password')}
                                {...inputProps} />
                        </Field>
                        <Field
                            label={t('shared.firstName')}
                            invalid={!!errors.firstName && touched.firstName}
                            errorText={errors.firstName}>
                            <Input
                                type={"text"}
                                name={"firstName"}
                                onChange={handleChange}
                                placeholder={t('shared.firstName')}
                                {...inputProps} />
                        </Field>
                        <Field
                            label={t('shared.lastName')}
                            invalid={!!errors.lastName && touched.lastName}
                            errorText={errors.lastName}>
                            <Input
                                type={"text"}
                                name={"lastName"}
                                onChange={handleChange}
                                placeholder={t('shared.lastName')}
                                {...inputProps} />
                        </Field>
                        <Field
                            label={t('shared.position')}
                            invalid={!!errors.position && touched.position}
                            errorText={errors.position}>
                            <Input
                                type={"text"}
                                name={"position"}
                                onChange={handleChange}
                                placeholder={t('shared.position')}
                                {...inputProps} />
                        </Field>
                        <Field
                            label={t('shared.userRoles')}
                            invalid={!!errors.roles && touched.roles}
                            errorText={errors.roles}>
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
                        </Field>
                        <Field
                            label={t('shared.locked')}
                            invalid={!!errors.locked && touched.locked}
                            errorText={errors.locked}>
                            <Select
                                options={booleanOptions}
                                placeholder={t("shared.locked")}
                                value={booleanOptions.find((option) => option.value === String(values.locked))}
                                onChange={(selectedOption) => {
                                    setFieldValue("locked", selectedOption?.value).catch(() => {
                                    });
                                    setFieldTouched("locked", true, false).catch(() => {
                                    });
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
                        </Field>
                        <Field
                            label={t('shared.enabled')}
                            invalid={!!errors.enabled && touched.enabled}
                            errorText={errors.enabled}>
                            <Select
                                options={booleanOptions}
                                placeholder={t("shared.enabled")}
                                value={booleanOptions.find((option) => option.value === String(values.enabled))}
                                onChange={(selectedOption) => {
                                    setFieldValue("enabled", selectedOption?.value).catch(() => {
                                    });
                                    setFieldTouched("enabled", true, false).catch(() => {
                                    });
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
                        </Field>
                        <Button disabled={!isValid || isSubmitting} type="submit" colorScheme="green">
                            {t('shared.addUser')}
                        </Button>
                    </Stack>
                </Form>
            )}
        </Formik>
    );
};

export default AddUserForm;
