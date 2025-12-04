import * as Yup from 'yup';
import React from "react";
import {useTranslation} from "react-i18next";
import {Form, Formik, FormikHelpers} from "formik";
import {Button, Flex} from "@chakra-ui/react";
import {CustomInputFilterField} from "@/components/shared/CustomFormFields.tsx";

interface FilterValues {
    symbolContains?: string;
    descriptionContains?: string;
}

const validationSchema = Yup.object({
   symbolContains: Yup.string(),
    descriptionContains: Yup.string()
})

interface FilterProps {
    onSubmit: (values: FilterValues) => void;
}

const UnitFilterForm: React.FC<FilterProps> = ({onSubmit}) => {
    const {t} = useTranslation(['units', 'common']);
    return (
        <Formik<FilterValues>
            initialValues={{
                symbolContains:'',
                descriptionContains:''
            }}
            validationSchema={validationSchema}
            onSubmit={(values,{setSubmitting}: FormikHelpers<FilterValues>) => {
                setSubmitting(false);
                onSubmit(values);
            }}>
            {({handleSubmit, resetForm})=> {
                return (
                    <Form onSubmit={handleSubmit}>
                        <Flex
                            gap={1}
                            px={1}
                            py={1}
                            justifyContent={"center"}
                            flexWrap={"wrap"}
                        >
                            <CustomInputFilterField name="symbolContains" placeholder={t('units:symbol')}/>
                            <CustomInputFilterField name="descriptionContains"
                                                    placeholder={t('units:description')}/>
                        {/*    TODO: add filter by UnitType using enum and on backend */}
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

export default UnitFilterForm;