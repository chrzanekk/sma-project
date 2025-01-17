import {useTranslation} from "react-i18next";
import {Field, Form, Formik, FormikHelpers} from "formik";
import {Button, Flex, Input} from "@chakra-ui/react";
import React from "react";
import * as Yup from 'yup';
import {themeColors} from "@/theme/theme-colors.ts";

interface FilterValues {
    name?: string;
}

const validationSchema = Yup.object({
    name: Yup.string().required()
});

interface Props {
    onSubmit: (values: FilterValues) => void;
}

const RoleFilterForm: React.FC<Props> = ({onSubmit}) => {
    const {t} = useTranslation('auth');
    return (
        <Formik
            initialValues={{name: ''}}
            validationSchema={validationSchema}
            onSubmit={(values, {setSubmitting}: FormikHelpers<FilterValues>) => {
                setSubmitting(false);
                onSubmit(values);
            }}
        >
            {({handleSubmit, resetForm}) => {
                return (
                    <Form onSubmit={handleSubmit}>
                        <Flex gap={2} mb={1} justifyContent={"center"}>
                            <Field
                                name="name"
                                as={Input}
                                placeholder={t('shared.roleName')}
                                size="sm"
                                bg={themeColors.bgColorLight()}
                                borderRadius={"md"}
                                width="150px"
                            />
                        </Flex>
                        <Flex gap={2} justifyContent={"center"}>
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
}

export default RoleFilterForm;