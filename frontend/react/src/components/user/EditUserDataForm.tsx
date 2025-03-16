import {useTranslation} from "react-i18next";
import {Form, Formik} from "formik";
import * as Yup from "yup";
import {UserDTO, UserFormDTO} from "@/types/user-types.ts";
import {getUserById, updateUser} from "@/services/user-service.ts";
import {errorNotification, successNotification} from "@/notifications/notifications.ts";
import {Button, Stack} from "@chakra-ui/react";
import React, {useEffect, useState} from "react";
import {formatMessage} from "@/notifications/FormatMessage.tsx";
import {CustomInputField, CustomSelectField} from "@/components/shared/CustomFormFields.tsx";
import {getBooleanOptions} from "@/components/shared/formOptions.ts";


interface EditUserDataFormProps {
    onSuccess: () => void;
    userId: number;
}

const EditUserDataForm: React.FC<EditUserDataFormProps> = ({onSuccess, userId}) => {
    const {t} = useTranslation(['auth', 'common'])
    const defaultValues: UserFormDTO = {
        id: 0,
        login: '',
        email: '',
        password: '',
        firstName: '',
        lastName: '',
        position: '',
        locked: false,
        enabled: true,
        roles: []
    };
    const [initialValues, setInitialValues] = useState<UserFormDTO>(defaultValues);
    const [isLoading, setIsLoading] = useState<boolean>(true);

    useEffect(() => {
        const fetchUser = async () => {
            setIsLoading(true);
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
            } finally {
                setIsLoading(false);
            }
        };
        fetchUser().catch();
    }, [userId]);


    return (
        <Formik
            initialValues={initialValues}
            enableReinitialize={true}
            validationSchema={Yup.object({
                firstName: Yup.string()
                    .min(2, t('verification.minLength', {field: t('shared.firstName'), count: 2}))
                    .max(30, t('verification.maxLength', {field: t('shared.firstName'), count: 30}))
                    .required(t('verification.required', {field: t('shared.firstName')})),
                lastName: Yup.string()
                    .min(2, t('verification.minLength', {field: t('shared.lastName'), count: 2}))
                    .max(30, t('verification.maxLength', {field: t('shared.lastName'), count: 30}))
                    .required(t('verification.required', {field: t('shared.lastName')})),
                position: Yup.string()
                    .min(2, t('verification.minLength', {field: t('shared.position'), count: 2}))
                    .max(50, t('verification.maxLength', {field: t('shared.position'), count: 50})),
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
            {({isValid, isSubmitting, dirty}) => {
                const booleanOptions = getBooleanOptions(t);
                const width = "450px";
                return (
                    <Form>
                        <Stack gap="8px">
                            <CustomInputField name={"firstName"} label={t('shared.firstName')}
                                              placeholder={t('shared.firstName')}
                                              width={width}/>
                            <CustomInputField name={"lastName"} label={t('shared.lastName')}
                                              placeholder={t('shared.lastName')}
                                              width={width}/>
                            <CustomInputField name={"position"} label={t('shared.position')}
                                              placeholder={t('shared.position')}
                                              width={width}/>
                            <CustomSelectField name={"locked"} label={t('shared.locked')}
                                               placeholder={t('shared.locked')} options={booleanOptions}
                                               width={width}/>
                            <CustomSelectField name={"enabled"} label={t('shared.enabled')}
                                               placeholder={t('shared.enabled')} options={booleanOptions}
                                               width={width}/>
                            <Button disabled={!isValid || isSubmitting || !dirty || isLoading} type="submit"
                                    colorPalette="green" width={width}>
                                {t('save', {ns: "common"})}
                            </Button>
                        </Stack>
                    </Form>
                );
            }}
        </Formik>
    );
}

export default EditUserDataForm;