import * as Yup from 'yup';
import {useTranslation} from "react-i18next";
import {Form, Formik, FormikHelpers} from "formik";
import {Button, Flex} from "@chakra-ui/react";
import React from "react";
import {CustomInputFilterField, CustomSelectField} from "@/components/shared/CustomFormFields.tsx";
import {getCountryOptions} from "@/types/country-type.ts";
import {useThemeColorsHex} from "@/theme/theme-colors.ts";


interface FilterValues {
    nameStartsWith?: string;
    addressStartsWith?: string;
    shortNameStartsWith?: string;
    codeStartsWith?: string;
    country?: string;
    contractorNameStartsWith?: string;
}

const validationSchema = Yup.object({
    nameStartsWith: Yup.string(),
    shortNameStartsWith: Yup.string(),
    codeStartsWith: Yup.string(),
    country: Yup.string(),
    contractorNameStartsWith: Yup.string()
});

interface Props {
    onSubmit: (values: FilterValues) => void;
}

const ConstructionSiteFilterForm: React.FC<Props> = ({onSubmit}) => {
    const {t} = useTranslation(['common', 'constructionSites']);
    const countryOptions = getCountryOptions(t);
    const themeColorsHex = useThemeColorsHex();

    return (
        <Formik<FilterValues>
            initialValues={
                {
                    nameStartsWith: '',
                    addressStartsWith: '',
                    shortNameStartsWith: '',
                    codeStartsWith: '',
                    country: '',
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
                            <CustomInputFilterField name="nameStartsWith" placeholder={t('constructionSites:name')}/>
                            <CustomInputFilterField name="addressStartsWith" placeholder={t('constructionSites:address')}/>
                            <CustomInputFilterField name="shortNameStartsWith" placeholder={t('constructionSites:shortName')}/>
                            <CustomInputFilterField name="codeStartsWith" placeholder={t('constructionSites:code')}/>
                            <CustomSelectField
                                name="country"
                                placeholder={t('constructionSites:country')}
                                options={countryOptions}
                                bgColor={themeColorsHex.bgColorSecondary}
                            />
                            <CustomInputFilterField name="contractorNameStartsWith"
                                                    placeholder={t('constructionSites:contractor')}/>
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

export default ConstructionSiteFilterForm;