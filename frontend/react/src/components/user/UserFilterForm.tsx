import React from 'react';
import {Field, Form, Formik} from 'formik';
import * as Yup from 'yup';
import {Button, Flex, Input, Select} from '@chakra-ui/react';
import {themeColors} from "@/theme/theme-colors.ts";
import {useTranslation} from "react-i18next";

interface FilterValues {
    emailStartsWith?: string;
    loginStartsWith?: string;
    firstNameStartsWith?: string;
    lastNameStartsWith?: string;
    positionStartsWith?: string;
    isLocked?: boolean;
    isEnabled?: boolean;
}

const validationSchema = Yup.object({
    emailStartsWith: Yup.string(),
    loginStartsWith: Yup.string(),
    firstNameStartsWith: Yup.string(),
    lastNameStartsWith: Yup.string(),
    positionStartsWith: Yup.string(),
    isLocked: Yup.boolean(),
    isEnabled: Yup.boolean(),
});

interface Props {
    onSubmit: (values: FilterValues) => void;
}

const UserFilterForm: React.FC<Props> = ({onSubmit}) => {
    const {t} = useTranslation('auth')
    return (
        <Formik
            initialValues={{
                emailStartsWith: '',
                loginStartsWith: '',
                firstNameStartsWith: '',
                lastNameStartsWith: '',
                positionStartsWith: '',
                isLocked: undefined,
                isEnabled: undefined,
            }}
            validationSchema={validationSchema}
            onSubmit={onSubmit}
        >
            {({handleSubmit, resetForm}) => (
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
                        <Field name="isLocked" as={Select} placeholder={t('shared.locked')}
                               size="sm"
                               bg={themeColors.bgColorLight()}
                               borderRadius={"md"}
                               width="150px">
                            <option value="true">{t('yes', {ns: "common"})}</option>
                            <option value="false">{t('no', {ns: "common"})}</option>
                        </Field>
                        <Field name="isEnabled" as={Select} placeholder={t('shared.enabled')}
                               size="sm"
                               bg={themeColors.bgColorLight()}
                               borderRadius={"md"}
                               width="150px">
                            <option value="true">{t('yes', {ns: "common"})}</option>
                            <option value="false">{t('no', {ns: "common"})}</option>
                        </Field>
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
            )}
        </Formik>
    );
};

export default UserFilterForm;
