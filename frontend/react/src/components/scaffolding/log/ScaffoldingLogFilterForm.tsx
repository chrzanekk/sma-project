import * as Yup from 'yup';
import {useTranslation} from "react-i18next";
import {Form, Formik, FormikHelpers} from "formik";
import {Button, Flex} from "@chakra-ui/react";
import {CustomInputFilterField} from "@/components/shared/CustomFormFields.tsx";
import React from "react";

interface FilterValues {
    nameContains?: string;
    additionalInfoContains?: string;
    constructionSiteNameContains?: string;
    contractorNameContains?: string;
}

const validationSchema = Yup.object({
    nameContains: Yup.string(),
    additionalInfoContains: Yup.string(),
    constructionSiteNameContains: Yup.string(),
    contractorNameContains: Yup.string(),
});

interface FilterProps {
    onSubmit: (values: FilterValues) => void;
}

const ScaffoldingLogFilterForm: React.FC<FilterProps> = ({onSubmit}) => {
    const {t} = useTranslation(['common', 'scaffoldingLogs']);
    return (
        <Formik<FilterValues>
            initialValues={{
                nameContains: '',
                additionalInfoContains: '',
                constructionSiteNameContains: '',
                contractorNameContains: '',
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
                            flexWrap={"wrap"}
                        >
                            <CustomInputFilterField name="nameContains" placeholder={t('scaffoldingLogs:name')}/>
                            <CustomInputFilterField name="additionalInfoContains"
                                                    placeholder={t('scaffoldingLogs:additionalInfo')}/>
                            <CustomInputFilterField name="constructionSiteNameContains"
                                                    placeholder={t('scaffoldingLogs:constructionSiteName')}/>
                            <CustomInputFilterField name="contractorNameContains"
                                                    placeholder={t('scaffoldingLogs:contractorName')}/>

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
                                {t('common:clearFilters')}
                            </Button>
                        </Flex>
                    </Form>
                )
            }}
        </Formik>
    )
}

export default ScaffoldingLogFilterForm;