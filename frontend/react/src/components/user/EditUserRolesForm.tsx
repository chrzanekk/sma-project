import {useTranslation} from "react-i18next";
import {Form, Formik} from "formik";
import * as Yup from "yup";
import {AdminEditRoleUpdateRequest, UserDTO} from "@/types/user-types.ts";
import {getUserById} from "@/services/user-service.ts";
import {errorNotification, successNotification} from "@/notifications/notifications.ts";
import {Button, Stack} from "@chakra-ui/react";
import React, {useEffect, useState} from "react";
import {updateUserRoles} from "@/services/account-service.ts";
import useRoles from "@/hooks/UseRoles.tsx";
import {formatMessage} from "@/notifications/FormatMessage.tsx";
import {CustomSelectField} from "@/components/shared/CustomFormFields.tsx";
import {themeVars} from "@/theme/theme-colors.ts";

interface EditUserRolesFormProps {
    onSuccess: () => void;
    userId: number;
    login: string;
}

const EditUserRolesForm: React.FC<EditUserRolesFormProps> = ({onSuccess, userId, login}) => {
    const {t} = useTranslation(['auth', 'common'])
    const [initialValues, setInitialValues] = useState<AdminEditRoleUpdateRequest | null>(null);
    const {roles: roleOptions, isLoading, error} = useRoles();

    useEffect(() => {
        const fetchUser = async () => {
            try {
                const user: UserDTO = await getUserById(userId);
                setInitialValues({
                    userId: user.id,
                    roles: user.roles ? Array.from(user.roles).map(role => role.name) : [],
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

    if (isLoading) return <div>{t('processing', {ns: "common"})}</div>;
    if (error) return <div>{t('error', {ns: "common"})}: {error}</div>;
    if (!initialValues) return null;

    return (
        <Formik
            enableReinitialize
            initialValues={initialValues}
            validationSchema={Yup.object({
                roles: Yup.array().of(Yup.string())
                    .min(1, t('updateProfile.roleMissing')),
            })}
            onSubmit={async (userRoles: AdminEditRoleUpdateRequest, {setSubmitting}) => {
                setSubmitting(true);
                try {
                    await updateUserRoles(userRoles);
                    successNotification(
                        t('success', {ns: "common"}),
                        formatMessage('notifications.userRolesEditedSuccess', {login: login, ns: "common"})
                    );
                    onSuccess();
                } catch (err: any) {
                    console.error(err);
                    errorNotification(
                        t('error', {ns: "common"}),
                        err.response?.data?.message || t('notifications.userRolesEditedError', {
                            login: login,
                            ns: "common"
                        })
                    );
                } finally {
                    setSubmitting(false)
                }
            }}
        >
            {({isValid, isSubmitting}) => {

                return (
                    <Form>
                        <Stack gap="8px">
                            <CustomSelectField name={"roles"} options={roleOptions} label={t('shared.chooseRoles')}
                                               placeholder={t('shared.chooseRoles')} isMulti={true}
                                               bgColor={themeVars.bgColorSecondary}
                            />
                            <Button disabled={!isValid || isSubmitting} type="submit"
                                    colorPalette="green">
                                {t('shared.changerRoles')}
                            </Button>
                        </Stack>
                    </Form>
                );
            }}
        </Formik>
    );
}

export default EditUserRolesForm;