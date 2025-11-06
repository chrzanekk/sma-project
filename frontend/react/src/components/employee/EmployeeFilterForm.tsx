import * as Yup from 'yup';
import {useTranslation} from "react-i18next";
import {Form, Formik, FormikHelpers} from "formik";
import {Button, Flex} from "@chakra-ui/react";
import {CustomInputFilterField} from "@/components/shared/CustomFormFields.tsx";
import React from "react";

interface FilterValues {
    firstNameContains?: string;
    lastNameContains?: string;
    positionContains?: string;
    hourRateStartsWith?: string;
    hourRateEndsWith?: string;
    companyContains?: string;
}

const validationSchema = Yup.object({
    firstNameContains: Yup.string(),
    lastNameContains: Yup.string(),
    positionContains: Yup.string(),
    hourRateStartsWith: Yup.string(),
    hourRateEndsWith: Yup.string(),
    companyContains: Yup.string(),
})

interface Props {
    onSubmit: (values: FilterValues) => void;
}

const EmployeeFilterForm: React.FC<Props> = ({onSubmit}) => {
    const {t} = useTranslation(['employees', 'common','positions','companies']);
    return (
        <Formik<FilterValues>
            initialValues={{
                firstNameContains: '',
                lastNameContains: '',
                positionContains: '',
                hourRateStartsWith: '',
                hourRateEndsWith: '',
                companyContains: '',
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
                            <CustomInputFilterField name="firstNameContains" placeholder={t('employees:firstName')}/>
                            <CustomInputFilterField name="lastNameContains" placeholder={t('employees:lastName')}/>
                            <CustomInputFilterField name="positionContains" placeholder={t('positions:position')}/>
                            <CustomInputFilterField name="hourRateStartsWith" placeholder={t('employees:hourRateStartsWith')}/>
                            <CustomInputFilterField name="hourRateEndsWith" placeholder={t('employees:hourRateEndsWith')}/>

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
export default EmployeeFilterForm;