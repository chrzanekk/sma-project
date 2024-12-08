import {Form, Formik} from 'formik';
import * as Yup from 'yup';
import {Button, Stack} from "@chakra-ui/react";
import {errorNotification, successNotification} from "@/notifications/notifications.ts";
import {MySelect, MyTextInput} from "@/components/form/CustomFields";
import {addUser} from "@/services/user-service.ts";
import {UserDTO} from "@/types/user-types.ts";
import {useTranslation} from "react-i18next";
import React from "react";

interface AddUserFormProps {
    onSuccess: () => void; // Funkcja, która zostanie wywołana po pomyślnym dodaniu użytkownika
}

const AddUserForm: React.FC<AddUserFormProps> = ({onSuccess}) => {
    const {t} = useTranslation(['auth', 'common']);
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
                roles: []
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
                locked: Yup.string().required(t("verification.required", {field: t("shared.locked")})),
                enabled: Yup.string().required(t("verification.required", {field: t("shared.enabled")})),
            })}
            onSubmit={async (newUser: UserDTO, {setSubmitting}) => {
                setSubmitting(true);
                try {
                    await addUser(newUser);
                    successNotification(
                        t('success', {ns: "common"}),
                        t('notifications.userAddedSuccess', {login: newUser.login, ns: "common"})
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
            {({isValid, isSubmitting}) => (
                <Form>
                    <Stack spacing="24px">
                        <MyTextInput
                            label={t('shared.login')}
                            name="login"
                            type="text"
                            placeholder={t('shared.login')}
                        />
                        <MyTextInput
                            label={t('shared.email')}
                            name="email"
                            type="email"
                            placeholder="example@example.com"
                        />
                        <MyTextInput
                            label={t('shared.password')}
                            name="password"
                            type="password"
                            placeholder={t('shared.password')}
                        />
                        <MyTextInput
                            label={t('shared.firstName')}
                            name="firstName"
                            type="text"
                            placeholder={t('shared.firstName')}
                        />
                        <MyTextInput
                            label={t('shared.lastName')}
                            name="lastName"
                            type="text"
                            placeholder={t('shared.lastName')}
                        /><MyTextInput
                        label={t('shared.position')}
                        name="position"
                        type="text"
                        placeholder={t('shared.position')}
                    />
                        <MySelect
                            label={t("shared.userRoles")}
                            name="roles"
                            multiple
                        >
                            <option value="ROLE_USER">USER</option>
                            <option value="ROLE_ADMIN">ADMIN</option>
                        </MySelect>

                        {/* Pole wyboru dla locked */}
                        <MySelect
                            label={t("shared.locked")}
                            name="locked"
                        >
                            <option value="true">{t("yes", {ns: "common"})}</option>
                            <option value="false">{t("no", {ns: "common"})}</option>
                        </MySelect>

                        {/* Pole wyboru dla enabled */}
                        <MySelect
                            label={t("shared.enabled")}
                            name="enabled"
                        >
                            <option value="true">{t("yes", {ns: "common"})}</option>
                            <option value="false">{t("no", {ns: "common"})}</option>
                        </MySelect>

                        <Button isDisabled={!isValid || isSubmitting} type="submit">
                            {t('register.submit')}
                        </Button>
                    </Stack>
                </Form>
            )}
        </Formik>
    );
};

export default AddUserForm;
