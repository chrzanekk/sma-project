import {useTranslation} from "react-i18next";
import {Form, Formik} from "formik";
import * as Yup from "yup";
import {UserDTO, UserFormDTO} from "@/types/user-types.ts";
import {getUserById, updateUser} from "@/services/user-service.ts";
import {errorNotification, successNotification} from "@/notifications/notifications.ts";
import {Box, Button, Stack, Text} from "@chakra-ui/react";
import React, {useEffect, useMemo, useState} from "react";
import {formatMessage} from "@/notifications/FormatMessage.tsx";
import {CustomInputField, CustomSelectField} from "@/components/shared/CustomFormFields.tsx";
import {getBooleanOptions} from "@/components/shared/formOptions.ts";
import useUser from "@/hooks/UseUser.tsx";
import {makePositionSearchAdapter} from "@/search/position-search-adapter.ts";
import PositionSearchWithSelect from "@/components/position/PositionSearchWithSelect.tsx";
import {PositionBaseDTO} from "@/types/position-types.ts";
import {getSelectedCompanyId} from "@/utils/company-utils.ts";
import {useThemeColors} from "@/theme/theme-colors.ts";


interface EditUserDataFormProps {
    onSuccess: () => void;
    userId: number;
}

const EditUserDataForm: React.FC<EditUserDataFormProps> = ({onSuccess, userId}) => {
    const {t} = useTranslation(['auth', 'common'])
    const {user: currentUser, updateUser: updateLocalUser} = useUser();
    const companyId: number = getSelectedCompanyId()!;
    const themeColors = useThemeColors();

    const defaultValues: UserFormDTO = {
        id: 0,
        login: '',
        email: '',
        password: '',
        firstName: '',
        lastName: '',
        position: undefined,
        locked: false,
        enabled: true,
        roles: [],
        companies: []
    };
    const [initialValues, setInitialValues] = useState<UserFormDTO>(defaultValues);
    const [isLoading, setIsLoading] = useState<boolean>(true);

    const positionSearchFn = useMemo(
        () => makePositionSearchAdapter({
            fixed: {companyId},
            defaults: {page: 0, size: 10, sort: "name,asc"},
        }),
        [companyId]
    );

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
                    position: user.position || undefined,
                    locked: user.locked,
                    enabled: user.enabled,
                    roles: user.roles ? Array.from(user.roles).map(role => role.name) : [],
                    companies: user.companies
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
                position: Yup.object()
                    .nullable(),
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

                    if (currentUser && currentUser.id === userId) {
                        updateLocalUser({
                            firstName: user.firstName,
                            lastName: user.lastName,
                            email: user.email,
                            position: user.position,
                            // Możesz uzupełnić inne pola, które uległy zmianie
                        });
                    }

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
            {({isValid, isSubmitting, dirty, values, setFieldValue, setFieldTouched, touched, errors}) => {
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

                            <Box>
                                <Text fontSize="sm"
                                      fontWeight="bold"
                                      mb="1"
                                      textAlign={"center"}
                                      color={themeColors.fontColor}

                                >
                                    {t('shared.position')}
                                </Text>
                                <PositionSearchWithSelect
                                    value={values.position}
                                    onSelect={(position: PositionBaseDTO | null) => {
                                        setFieldValue("position", position).catch();
                                        setFieldTouched("position", true, false).catch();
                                    }}
                                    searchFn={positionSearchFn}
                                    placeholder={t('shared.position')}
                                    width={width} // "450px"
                                    size="md"
                                />
                                {touched.position && errors.position && (
                                    <Text color="red.500" fontSize="xs" mt="1">
                                        {errors.position as string}
                                    </Text>
                                )}
                            </Box>
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