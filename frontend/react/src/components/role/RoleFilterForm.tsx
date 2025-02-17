import {useTranslation} from "react-i18next";
import {Form, Formik, FormikHelpers} from "formik";
import {Button, Flex} from "@chakra-ui/react";
import React from "react";
import * as Yup from 'yup';
import {CustomInputFilterField} from "@/components/shared/CustomFormFields.tsx";

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
                            <CustomInputFilterField name="name" placeholder={t('shared.roleName')}/>
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