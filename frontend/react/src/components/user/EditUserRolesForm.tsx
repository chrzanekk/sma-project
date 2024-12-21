import {useTranslation} from "react-i18next";
import {themeColors} from "@/theme/theme-colors.ts";
import {Form, Formik} from "formik";
import * as Yup from "yup";
import {AdminEditRoleUpdateRequest, UserDTO} from "@/types/user-types.ts";
import {getUserById} from "@/services/user-service.ts";
import {errorNotification, successNotification} from "@/notifications/notifications.ts";
import {Button, FormControl, FormErrorMessage, FormLabel, Stack} from "@chakra-ui/react";
import {Select} from "chakra-react-select";
import React, {useEffect, useState} from "react";
import {updateUserRoles} from "@/services/account-service.ts";

interface EditUserFormProps {
    onSuccess: () => void;
    userId: number;
    login: string;
}

const EditUserForm: React.FC<EditUserFormProps> = ({onSuccess, userId, login}) => {
    const {t} = useTranslation(['auth', 'common'])
    const [initialValues, setInitialValues] = useState<AdminEditRoleUpdateRequest | null>(null);
    const allRoles = ["ROLE_USER", "ROLE_ADMIN"]; // Wszystkie możliwe role

    useEffect(() => {
        const fetchUser = async () => {
            try {
                const user: UserDTO = await getUserById(userId);
                setInitialValues({
                    userId: user.id,
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

    const roleOptions = allRoles.map(role => ({
        value: role,
        label: role.replace("ROLE_", "")
    }));

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
                        t('notifications.userRolesEditedSuccess', {login: login, ns: "common"})
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
            {({errors, touched, isValid, isSubmitting, setFieldValue, values}) => {
                const selectedRoles = values.roles || [];
                const remainingRoles = roleOptions.filter(option => !selectedRoles.includes(option.value));
                const rolesAreUnchanged =
                    JSON.stringify(selectedRoles.sort()) === JSON.stringify(initialValues.roles.sort());
                return (
                    <Form>
                        <Stack spacing="8px">
                            <FormControl isInvalid={!!errors.roles && touched.roles}>
                                <FormLabel>{t('shared.chooseRoles')}</FormLabel>
                                <Select
                                    isMulti
                                    options={roleOptions}
                                    value={roleOptions.filter(option => selectedRoles.includes(option.value))}
                                    placeholder={t("shared.chooseRoles")}
                                    closeMenuOnSelect={remainingRoles.length === 1}
                                    onChange={(selectedOptions) => {
                                        const roles = selectedOptions.map(option => option.value);
                                        setFieldValue("roles", roles).catch();
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
                            <Button isDisabled={!isValid || isSubmitting || rolesAreUnchanged} type="submit" colorScheme="green">
                                {t('shared.changerRoles')}
                            </Button>
                        </Stack>
                    </Form>
                );
            }}
        </Formik>
    );
}

export default EditUserForm;