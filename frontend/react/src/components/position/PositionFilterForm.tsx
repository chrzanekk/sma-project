import * as Yup from 'yup';
import {useTranslation} from "react-i18next";
import {Form, Formik, FormikHelpers} from "formik";
import React from "react";
import {Button, Flex} from "@chakra-ui/react";
import {CustomInputFilterField} from "@/components/shared/CustomFormFields.tsx";

interface FilterValues {
    nameContains?: string;
    descriptionContains?: string;
}

const validationSchema = Yup.object({
    nameContains: Yup.string(),
    descriptionContains: Yup.string(),
})

interface Props {
    onSubmit: (values: FilterValues) => void;
}

const PositionFilterForm: React.FC<Props> = ({onSubmit}) => {
    const {t} = useTranslation(['positions', 'common']);
    return (
        <Formik<FilterValues>
            initialValues={
                {
                    nameContains: '',
                    descriptionContains: ''
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
                            <CustomInputFilterField name="nameContains" placeholder={t('positions:name')}/>
                            <CustomInputFilterField name="descriptionContains"
                                                    placeholder={t('positions:description')}/>
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

export default PositionFilterForm;