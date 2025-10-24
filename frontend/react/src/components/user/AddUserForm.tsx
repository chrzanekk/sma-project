import {Form, Formik} from 'formik';
import * as Yup from 'yup';
import {Box, Button, Stack, Text} from "@chakra-ui/react";
import {errorNotification, successNotification} from "@/notifications/notifications.ts";
import {addUser} from "@/services/user-service.ts";
import {UserFormDTO} from "@/types/user-types.ts";
import {useTranslation} from "react-i18next";
import React, {useMemo} from "react";
import {themeVars} from "@/theme/theme-colors.ts";
import {formatMessage} from "@/notifications/FormatMessage.tsx";
import useRoles from "@/hooks/UseRoles.tsx";
import {CustomInputField, CustomSelectField} from "@/components/shared/CustomFormFields.tsx";
import {getBooleanOptions} from "@/components/shared/formOptions.ts";
import {getSelectedCompanyId} from "@/utils/company-utils.ts";
import {makePositionSearchAdapter} from "@/search/position-search-adapter.ts";
import {PositionBaseDTO} from "@/types/position-types.ts";
import PositionSearchWithSelect from "@/components/position/PositionSearchWithSelect.tsx";

interface AddUserFormProps {
    onSuccess: () => void;
}

const AddUserForm: React.FC<AddUserFormProps> = ({onSuccess}) => {
    const {t} = useTranslation(['auth', 'common']);
    const booleanOptions = getBooleanOptions(t);
    const {roles: roleOptions, isLoading, error} = useRoles();
    const companyId: number = getSelectedCompanyId()!;

    const positionSearchFn = useMemo(
        () => makePositionSearchAdapter({
            fixed: {companyId},
            defaults: {page: 0, size: 10, sort: "name,asc"},
        }),
        [companyId]
    );

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
                position: undefined as PositionBaseDTO | undefined,
                locked: true,
                enabled: false,
                roles: [] as string[],
                companies: []
            }}
            validationSchema={Yup.object({
                login: Yup.string()
                    .min(5, t('verification.minLength', {field: t('shared.login'), count: 5}))
                    .max(20, t('verification.maxLength', {field: t('shared.login'), count: 20}))
                    .required(t('verification.required', {field: t('shared.login')})),
                email: Yup.string()
                    .email(t('verification.invalidEmail'))
                    .required(t('verification.required', {field: t('shared.email')})),
                password: Yup.string()
                    .min(4, t('verification.minLength', {field: t('shared.password'), count: 4}))
                    .max(50, t('verification.maxLength', {field: t('shared.password'), count: 50}))
                    .required(t('verification.required', {field: t('shared.password')})),
                firstName: Yup.string()
                    .min(2, t('verification.minLength', {field: t('shared.firstName'), count: 2}))
                    .max(30, t('verification.maxLength', {field: t('shared.firstName'), count: 30}))
                    .required(t('verification.required', {field: t('shared.firstName')})),
                lastName: Yup.string()
                    .min(2, t('verification.minLength', {field: t('shared.lastName'), count: 2}))
                    .max(30, t('verification.maxLength', {field: t('shared.lastName'), count: 30}))
                    .required(t('verification.required', {field: t('shared.lastName')})),
                roles: Yup.array().of(Yup.string())
                    .min(1, t('updateProfile.roleMissing')),
                locked: Yup.boolean()
                    .required(t("verification.required", {field: t("shared.locked")})),
                enabled: Yup.boolean()
                    .required(t("verification.required", {field: t("shared.enabled")})),
                position: Yup.object().nullable(),
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
            {({touched, errors, isValid, isSubmitting, setFieldValue, setFieldTouched, dirty, values}) => (
                <Form>
                    <Stack gap="8px">
                        {/* Login Field */}
                        <CustomInputField name={"login"}
                                          label={t('shared.login')}
                                          placeholder={t('shared.login')}/>

                        {/* Email Field */}
                        <CustomInputField name={"email"}
                                          label={t('shared.email')}
                                          placeholder={t('shared.email')}/>
                        {/* Password Field */}
                        <CustomInputField name={"password"}
                                          label={t('shared.password')}
                                          placeholder={t('shared.password')}
                                          type={"password"}/>

                        {/* First Name Field */}
                        <CustomInputField name={"firstName"}
                                          label={t('shared.firstName')}
                                          placeholder={t('shared.firstName')}/>

                        {/* Last Name Field */}
                        <CustomInputField
                            name={"lastName"}
                            label={t('shared.lastName')}
                            placeholder={t('shared.lastName')}/>

                        {/* Position Field */}

                        <div>
                            <Text fontSize="sm"
                                  fontWeight="bold"
                                  mb="1"
                                  textAlign={"center"}
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
                                size={"md"}
                                width={"100%"}
                            />
                            {touched.position && errors.position && (
                                <Text color="red.500" fontSize="xs" mt="1">
                                    {errors.position as string}
                                </Text>
                            )}
                        </div>

                        {/* Roles Select Field */}
                        <Box>
                            <Text fontSize="sm" fontWeight="bold" mb="1" textAlign={"center"}>
                                {t('shared.chooseRoles')}
                            </Text>
                            <CustomSelectField name={"roles"}
                                               isMulti={true}
                                               placeholder={t("shared.chooseRoles")}
                                               options={roleOptions}
                                               bgColor={themeVars.bgColorSecondary}/>
                        </Box>
                        {/* Locked Field */}
                        <CustomSelectField name={"locked"}
                                           label={t('shared.locked')}
                                           placeholder={t('shared.locked')}
                                           options={booleanOptions}/>
                        {/* Enabled Field */}
                        <CustomSelectField name={"enabled"}
                                           label={t('shared.enabled')}
                                           placeholder={t('shared.enabled')}
                                           options={booleanOptions}/>
                        <Button disabled={!isValid || isSubmitting || !dirty} type="submit" colorPalette="green">
                            {t('shared.addUser')}
                        </Button>
                    </Stack>
                </Form>
            )}
        </Formik>
    );
};

export default AddUserForm;
