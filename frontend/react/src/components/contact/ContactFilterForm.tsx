import * as Yup from 'yup';
import {useTranslation} from "react-i18next";
import {Form, Formik, FormikHelpers} from "formik";
import {Button, Flex} from "@chakra-ui/react";
import React from "react";
import {CustomInputFilterField} from "@/components/shared/CustomFormFields.tsx";


interface FilterValues {
    firstNameStartsWith?: string;
    lastNameStartsWith?: string;
    emailStartsWith?: string;
    phoneStartsWith?: string;
    contractorNameStartsWith?: string;
}

const validationSchema = Yup.object({
    firstNameStartsWith: Yup.string(),
    lastNameStartsWith: Yup.string(),
    emailStartsWith: Yup.string(),
    phoneStartsWith: Yup.string(),
    contractorNameStartsWith: Yup.string()
});

interface Props {
    onSubmit: (values: FilterValues) => void;
}

const ContactFilterForm: React.FC<Props> = ({onSubmit}) => {
    const {t} = useTranslation(['common', 'contacts']);

    return (
        <Formik<FilterValues>
            initialValues={
                {
                    firstNameStartsWith: '',
                    lastNameStartsWith: '',
                    emailStartsWith: '',
                    phoneStartsWith: '',
                    contractorNameStartsWith: ''
                }}
            validationSchema={validationSchema}
            onSubmit={(values, {setSubmitting}: FormikHelpers<FilterValues>) => {
                setSubmitting(false);
                onSubmit(values)
            }}>
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
                            <CustomInputFilterField name="firstNameStartsWith" placeholder={t('contacts:firstName')}/>
                            <CustomInputFilterField name="lastNameStartsWith" placeholder={t('contacts:lastName')}/>
                            <CustomInputFilterField name="emailStartsWith" placeholder={t('contacts:email')}/>
                            <CustomInputFilterField name="phoneStartsWith" placeholder={t('contacts:phoneNumber')}/>
                            <CustomInputFilterField name="contractorNameStartsWith"
                                                    placeholder={t('contacts:contractor')}/>
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

export default ContactFilterForm;