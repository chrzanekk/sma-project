import React from 'react';
import {Field, Form, Formik, FormikHelpers} from 'formik';
import * as Yup from 'yup';
import {Button, Flex, Input, Select as ChakraSelect} from '@chakra-ui/react';
import {themeColors} from "@/theme/theme-colors.ts";
import {useTranslation} from "react-i18next";
import {Select} from "chakra-react-select";

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
    const allRoles = ["ROLE_USER", "ROLE_ADMIN"];

    const roleOptions = allRoles.map(role => ({
        value: role,
        label: role.replace("ROLE_", "")
    }));

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
            {({handleSubmit, resetForm, setFieldValue, values}) => {
                const remainingRoles = roleOptions.filter(
                    (option) => !values.roles?.includes(option.value)
                );

                return (
                    <Form onSubmit={handleSubmit}>
                        <Flex gap={2} mb={1} justifyContent={"center"}>
                            <Field name="emailStartsWith" as={Input} placeholder={t('shared.email')}
                                   size="sm"
                                   bg={themeColors.bgColorLight()}
                                   borderRadius={"md"}
                                   width="150px"/>
                            <Field name="loginStartsWith" as={Input} placeholder={t('shared.login')}
                                   size="sm"
                                   bg={themeColors.bgColorLight()}
                                   borderRadius={"md"}
                                   width="150px"/>
                            <Field name="firstNameStartsWith" as={Input} placeholder={t('shared.firstName')}
                                   size="sm"
                                   bg={themeColors.bgColorLight()}
                                   borderRadius={"md"}
                                   width="150px"/>
                            <Field name="lastNameStartsWith" as={Input} placeholder={t('shared.lastName')}
                                   size="sm"
                                   bg={themeColors.bgColorLight()}
                                   borderRadius={"md"}
                                   width="150px"/>
                            <Field name="positionStartsWith" as={Input} placeholder={t('shared.position')}
                                   size="sm"
                                   bg={themeColors.bgColorLight()}
                                   borderRadius={"md"}
                                   width="150px"/>
                            <Field name="isLocked" as={ChakraSelect} placeholder={t('shared.locked')}
                                   size="sm"
                                   bg={themeColors.bgColorLight()}
                                   borderRadius={"md"}
                                   width="150px">
                                <option value="true">{t('yes', {ns: "common"})}</option>
                                <option value="false">{t('no', {ns: "common"})}</option>
                            </Field>
                            <Field name="isEnabled" as={ChakraSelect} placeholder={t('shared.enabled')}
                                   size="sm"
                                   bg={themeColors.bgColorLight()}
                                   borderRadius={"md"}
                                   width="150px">
                                <option value="true">{t('yes', {ns: "common"})}</option>
                                <option value="false">{t('no', {ns: "common"})}</option>
                            </Field>
                            <Select
                                isMulti
                                options={roleOptions}
                                value={roleOptions.filter(option => values.roles?.includes(option.value))}
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
                                        minHeight: "2rem", // Wysokość odpowiadająca `size="sm"`
                                        height: "2rem",    // Wysokość odpowiadająca innym polom
                                    }),
                                    valueContainer: (provided) => ({
                                        ...provided,
                                        padding: "0 0.5rem", // Dopasowanie paddingu
                                    }),
                                    indicatorsContainer: (provided) => ({
                                        ...provided,
                                        height: "2rem", // Dopasowanie wysokości wskaźników
                                    }),
                                }}
                            />
                        </Flex>
                        <Flex gap={2} justifyContent={"center"}>
                            <Button type="submit" colorScheme="blue"
                                    size={"xs"}>
                                {t('search', {ns: "common"})}
                            </Button>
                            <Button
                                type="button"
                                colorScheme="orange"
                                size="xs"
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
