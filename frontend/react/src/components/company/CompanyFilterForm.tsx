import * as Yup from 'yup';
import {useTranslation} from "react-i18next";
import {Form, Formik, FormikHelpers} from "formik";
import React from "react";
import {Button, Flex} from "@chakra-ui/react";
import {CustomInputFilterField} from "@/components/shared/CustomFormFields.tsx";

interface FilterValues {
    nameStartsWith?: string
}

const validationSchema = Yup.object({
    nameStartsWith: Yup.string()
})

interface Props {
    onSubmit: (values: FilterValues) => void;
}

const CompanyFilterForm: React.FC<Props> = ({onSubmit}) => {
    const {t} = useTranslation(['common', 'companies']);

    return (
        <Formik<FilterValues>
            initialValues={
                {
                    nameStartsWith: ''
                }}
            validationSchema={validationSchema}
            onSubmit={(values, {setSubmitting}: FormikHelpers<FilterValues>) => {
                setSubmitting(false);
                onSubmit(values);
            }}>
            {({handleSubmit, resetForm}) => {
                return (
                    <Form onSubmit={handleSubmit}>
                        <Flex
                            gap={1}
                            px={1}
                            py={1}
                            justifyContent={"center"}
                            flexWrap={"wrap"}>
                            <CustomInputFilterField name={"nameStartsWith"} placeholder={t('companies:name')}/>
                        </Flex>

                        <Flex gap={1} justifyContent={"center"}>
                            <Button type="submit" colorPalette="blue"
                                    size={"2xs"}>
                                {t('search')}
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
                                {t('clearFilters')}
                            </Button>
                        </Flex>
                    </Form>
                );
            }}
        </Formik>
    )
}

export default CompanyFilterForm;