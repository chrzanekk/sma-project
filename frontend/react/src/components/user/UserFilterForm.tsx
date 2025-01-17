import React from 'react';
import {Field, Form, Formik, FormikHelpers} from 'formik';
import * as Yup from 'yup';
import {Button, Flex, Input} from '@chakra-ui/react';
import {themeColors, themeColorsHex} from "@/theme/theme-colors.ts";
import {useTranslation} from "react-i18next";
import Select, {StylesConfig} from "react-select";
import useRoles from "@/hooks/UseRoles.tsx";

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
    const {roles: roleOptions, isLoading, error} = useRoles();

    if (isLoading) return <div>{t('processing', {ns: "common"})}</div>;
    if (error) return <div>{t('error', {ns: "common"})}: {error}</div>;

    const booleanOptions = [
        {value: undefined, label: t("empty", {ns: "common"})},
        {value: true, label: t("yes", {ns: "common"})},
        {value: false, label: t("no", {ns: "common"})}
    ];

    const selectStyles: StylesConfig<any, boolean> = {
        control: (provided) => ({
            ...provided,
            backgroundColor: themeColorsHex.bgColorLight(),
            color: themeColorsHex.fontColor(),
            borderRadius: "5px",
            boxShadow: "none",
            minHeight: "2rem",
            height: "36px",
            fontSize: "0.875rem",
            minWidth: "150px",
        }),
        placeholder: (provided: any) => ({
            ...provided,
            color: themeColorsHex.fontColor(),
        }),
        singleValue: (provided) => ({
            ...provided,
            color: themeColorsHex.fontColor()
        }),
        menuList: (provided) => ({
            ...provided,
            padding: 0,
        }),
        option: (provided, state) => ({
            ...provided,
            backgroundColor: state.isSelected ? themeColorsHex.popoverBgColor() : themeColorsHex.bgColorLight(),
            color: state.isSelected ? themeColorsHex.fontColor() : themeColorsHex.fontColor(),
            cursor: "pointer",
            "&:hover": {
                backgroundColor: themeColorsHex.popoverBgColor(),
                color: themeColorsHex.popoverBgColor(),
            },
        }),
    };

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
                        <Flex
                            gap={1}
                            px={1}
                            py={1}
                            justifyContent={"center"}
                        >
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
                            <Select
                                options={booleanOptions}
                                placeholder={t("shared.locked")}
                                value={booleanOptions.find((option) => option.value === values.isLocked)}
                                onChange={(selectedOption) => {
                                    setFieldValue("isLocked", selectedOption?.value).catch(() => {
                                    });
                                }}
                                styles={selectStyles}
                            />
                            <Select
                                options={booleanOptions}
                                placeholder={t("shared.enabled")}
                                value={booleanOptions.find((option) => option.value === values.isEnabled)}
                                onChange={(selectedOption) => {
                                    setFieldValue("isEnabled", selectedOption?.value).catch(() => {
                                    });
                                }}
                                styles={selectStyles}
                            />
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
                                styles={selectStyles}
                            />
                        </Flex>
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
