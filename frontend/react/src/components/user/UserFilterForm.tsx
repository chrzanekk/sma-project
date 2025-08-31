import React from 'react';
import {Form, Formik, FormikHelpers} from 'formik';
import * as Yup from 'yup';
import {Button, Flex} from '@chakra-ui/react';
import {useTranslation} from "react-i18next";
import useRoles from "@/hooks/UseRoles.tsx";
import {CustomInputFilterField, CustomSelectField} from "@/components/shared/CustomFormFields.tsx";
import {getBooleanOptions} from "@/components/shared/formOptions.ts";
import {themeVars} from "@/theme/theme-colors.ts";

interface FilterValues {
    emailStartsWith?: string;
    loginStartsWith?: string;
    firstNameStartsWith?: string;
    lastNameStartsWith?: string;
    positionStartsWith?: string;
    isLocked?: boolean;
    isEnabled?: boolean;
    roles?: string[];
}

const validationSchema = Yup.object({
    emailStartsWith: Yup.string(),
    loginStartsWith: Yup.string(),
    firstNameStartsWith: Yup.string(),
    lastNameStartsWith: Yup.string(),
    positionStartsWith: Yup.string(),
    isLocked: Yup.boolean(),
    isEnabled: Yup.boolean(),
    roles: Yup.array().of(Yup.string())
});

interface Props {
    onSubmit: (values: FilterValues) => void;
}

const UserFilterForm: React.FC<Props> = ({onSubmit}) => {
    const {t} = useTranslation('auth');
    const booleanOptions = getBooleanOptions(t);
    const {roles: roleOptions, isLoading, error} = useRoles();

    if (isLoading) return <div>{t('processing', {ns: "common"})}</div>;
    if (error) return <div>{t('error', {ns: "common"})}: {error}</div>;

    return (
        <Formik<FilterValues>
            initialValues={{
                emailStartsWith: '',
                loginStartsWith: '',
                firstNameStartsWith: '',
                lastNameStartsWith: '',
                positionStartsWith: '',
                isLocked: undefined,
                isEnabled: undefined,
                roles: []
            }}
            validationSchema={validationSchema}
            onSubmit={(values, {setSubmitting}: FormikHelpers<FilterValues>) => {
                setSubmitting(false);
                onSubmit(values);
            }}
        >
            {({handleSubmit, resetForm}) => {
                return (
                    <Form onSubmit={handleSubmit}>
                        <Flex
                            gap={1}
                            px={1}
                            py={1}
                            justifyContent={"center"}
                            flexWrap={"wrap"}
                        >
                            <CustomInputFilterField name="emailStartsWith" placeholder={t('shared.email')}/>
                            <CustomInputFilterField name="loginStartsWith" placeholder={t('shared.login')}/>
                            <CustomInputFilterField name="firstNameStartsWith" placeholder={t('shared.firstName')}/>
                            <CustomInputFilterField name="lastNameStartsWith" placeholder={t('shared.lastName')}/>
                            <CustomInputFilterField name="positionStartsWith" placeholder={t('shared.position')}/>
                            <CustomSelectField name={"locked"}
                                               placeholder={t("shared.locked")}
                                               options={booleanOptions}
                                               bgColor={themeVars.bgColorSecondary}/>
                            <CustomSelectField name={"enabled"}
                                               placeholder={t("shared.enabled")}
                                               options={booleanOptions}
                                               bgColor={themeVars.bgColorSecondary}/>
                            <CustomSelectField name={"roles"}
                                               isMulti={true}
                                               placeholder={t("shared.chooseRoles")}
                                               options={roleOptions}
                                               bgColor={themeVars.bgColorSecondary}/> </Flex>
                        <Flex gap={1} justifyContent={"center"}>
                            <Button type="submit" colorPalette="blue"
                                    size={"2xs"}>
                                {t('search', {ns: "common"})}
                            </Button>
                            <Button
                                type="button"
                                colorPalette="orange"
                                size={"2xs"}
                                onClick={() => {
                                    resetForm();
                                    onSubmit({});
                                }}
                            >
                                {t('clearFilters', {ns: "common"})}
                            </Button>
                        </Flex>

                    </Form>
                );
            }}
        </Formik>
    );
};

export default UserFilterForm;
