import * as Yup from 'yup';
import {useTranslation} from "react-i18next";
import {Form, Formik, FormikHelpers} from "formik";
import {Button, Flex} from "@chakra-ui/react";
import React from "react";
import {CustomInputFilterField, CustomSelectField, getBooleanOptions} from "@/components/shared/FormConfig.tsx";


interface FilterValues {
    nameStartsWith?: string;
    taxNumberStartsWith?: string;
    streetStartsWith?: string;
    buildingNoStartsWith?: string;
    apartmentNoStartsWith?: string;
    postalCodeStartsWith?: string;
    cityStartsWith?: string;
    customer?: boolean;
    supplier?: boolean;
    scaffoldingUser?: boolean;
}

const validationSchema = Yup.object({
    nameStartsWith: Yup.string(),
    taxNumberStartsWith: Yup.string(),
    streetStartsWith: Yup.string(),
    buildingNoStartsWith: Yup.string(),
    apartmentNoStartsWith: Yup.string(),
    postalCodeStartsWith: Yup.string(),
    cityStartsWith: Yup.string(),
    customer: Yup.boolean(),
    supplier: Yup.boolean(),
    scaffoldingUser: Yup.boolean(),
});

interface Props {
    onSubmit: (values: FilterValues) => void;
}

const ContractorFilterForm: React.FC<Props> = ({onSubmit}) => {
    const {t} = useTranslation(['common', 'contractors']);
    const booleanOptions = getBooleanOptions(t);

    return (
        <Formik<FilterValues>
            initialValues={
                {
                    nameStartsWith: '',
                    taxNumberStartsWith: '',
                    streetStartsWith: '',
                    buildingNoStartsWith: '',
                    apartmentNoStartsWith: '',
                    postalCodeStartsWith: '',
                    cityStartsWith: '',
                    customer: undefined,
                    supplier: undefined,
                    scaffoldingUser: undefined
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
                        >
                            <CustomInputFilterField name="nameStartsWith" placeholder={t('contractors:name')}/>
                            <CustomInputFilterField name="taxNumberStartsWith"
                                                    placeholder={t('contractors:taxNumber')}/>
                            <CustomInputFilterField name="streetStartsWith" placeholder={t('contractors:street')}/>
                            <CustomInputFilterField name="buildingNoStartsWith"
                                                    placeholder={t('contractors:buildingNo')}/>
                            <CustomInputFilterField name="apartmentNoStartsWith"
                                                    placeholder={t('contractors:apartmentNo')}/>
                            <CustomInputFilterField name="postalCodeStartsWith"
                                                    placeholder={t('contractors:postalCode')}/>
                            <CustomInputFilterField name="cityStartsWith" placeholder={t('contractors:city')}/>
                            <CustomSelectField name={"customer"} placeholder={t("contractors:customer")}
                                               options={booleanOptions}/>
                            <CustomSelectField name={"supplier"} placeholder={t("contractors:supplier")}
                                               options={booleanOptions}/>
                            <CustomSelectField name={"scaffoldingUser"} placeholder={t("contractors:scaffoldingUser")}
                                               options={booleanOptions}/>
                        </Flex>
                        <Flex gap={1} justifyContent={"center"}>
                            <Button type="submit" colorPalette="blue"
                                    size={"2xs"}>
                                {t('search')}
                                {/*{t('search', {ns: "common"})}*/}
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
                                {/*{t('clearFilters', {ns: "common"})}*/}
                            </Button>
                        </Flex>

                    </Form>
                );
            }}
        </Formik>
    )
}

export default ContractorFilterForm;