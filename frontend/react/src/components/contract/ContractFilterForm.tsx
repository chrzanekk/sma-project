import * as Yup from 'yup';
import {useTranslation} from "react-i18next";
import {Form, Formik, FormikHelpers} from "formik";
import {Button, Flex} from "@chakra-ui/react";
import React from "react";
import {CustomInputField, CustomInputFilterField} from "@/components/shared/CustomFormFields.tsx";
import {useThemeColors} from "@/theme/theme-colors.ts";


interface FilterValues {
    numberStartsWith?: string;
    descriptionStartsWith?: string;
    valueStartsWith?: string;
    valueEndsWith?: string;
    contractorNameStartsWith?: string;
    constructionSiteStartsWith?: string;
    startDateStartWith?: string;
    startDateEndWith?: string;
    endDateStartWith?: string;
    endDateEndWith?: string;
    signUpDateStartWith?: string;
    signUpDateEndWith?: string;
}

const validationSchema = Yup.object({
    numberStartsWith: Yup.string(),
    descriptionStartsWith: Yup.string(),
    valueStartsWith: Yup.string(),
    valueEndsWith: Yup.string(),
    contractorNameStartsWith: Yup.string(),
    constructionSiteStartsWith: Yup.string(),
    startDateStartWith: Yup.date(),
    startDateEndWith: Yup.date(),
    endDateStartWith: Yup.date(),
    endDateEndWith: Yup.date(),
    signUpDateStartWith: Yup.date(),
    signUpDateEndWith: Yup.date(),
});

interface Props {
    onSubmit: (values: FilterValues) => void;
}

const ContractFilterForm: React.FC<Props> = ({onSubmit}) => {
    const {t} = useTranslation(['common', 'contracts']);
    const themeColors = useThemeColors();

    return (
        <Formik<FilterValues>
            initialValues={
                {
                    numberStartsWith: '',
                    descriptionStartsWith: '',
                    valueStartsWith: '',
                    valueEndsWith: '',
                    contractorNameStartsWith: '',
                    constructionSiteStartsWith: '',
                    startDateStartWith: '',
                    startDateEndWith: '',
                    endDateStartWith: '',
                    endDateEndWith: '',
                    signUpDateStartWith: '',
                    signUpDateEndWith: '',
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
                            <CustomInputFilterField name="numberStartsWith"
                                                    placeholder={t('contracts:number')}
                            />
                            <CustomInputFilterField name="descriptionStartsWith"
                                                    placeholder={t('contracts:description')}
                            />
                            <CustomInputFilterField name="valueStartsWith"
                                                    placeholder={t('contracts:valueFrom')}
                            />
                            <CustomInputFilterField name="valueEndsWith"
                                                    placeholder={t('contracts:valueTo')}
                            />
                            <CustomInputFilterField name="contractorNameStartsWith"
                                                    placeholder={t('contracts:contractor')}
                            />
                            <CustomInputFilterField name="constructionSiteNameStartsWith"
                                                    placeholder={t('contracts:constructionSite')}
                            />
                        </Flex>
                        <Flex
                            gap={1}
                            px={1}
                            py={1}
                            justifyContent={"center"}
                            flexWrap={"wrap"}
                        >
                            <CustomInputField name="startDateStartWith"
                                              placeholder={t('contracts:startDateFrom')}
                                              label={t('contracts:startDateFrom')}
                                              type="date"
                                              width={"150px"}
                                              fontSize={"2xs"}
                                              inputBGColor={themeColors.bgColorSecondary}
                            />
                            <CustomInputField name="startDateEndWith"
                                              placeholder={t('contracts:startDateTo')}
                                              type="date"
                                              label={t('contracts:startDateTo')}
                                              width={"150px"}
                                              fontSize={"2xs"}
                                              inputBGColor={themeColors.bgColorSecondary}
                            />
                            <CustomInputField name="endDateStartWith"
                                              placeholder={t('contracts:endDateFrom')}
                                              label={t('contracts:endDateFrom')}
                                              type="date"
                                              width={"150px"}
                                              fontSize={"2xs"}
                                              inputBGColor={themeColors.bgColorSecondary}
                            />
                            <CustomInputField name="endDateEndWith"
                                              placeholder={t('contracts:endDateTo')}
                                              type="date"
                                              label={t('contracts:endDateTo')}
                                              width={"150px"}
                                              fontSize={"2xs"}
                                              inputBGColor={themeColors.bgColorSecondary}
                            />
                            <CustomInputField name="signupDateStartWith"
                                              placeholder={t('contracts:signupDateFrom')}
                                              label={t('contracts:signupDateFrom')}
                                              type="date"
                                              width={"150px"}
                                              fontSize={"2xs"}
                                              inputBGColor={themeColors.bgColorSecondary}
                            />
                            <CustomInputField name="signupDateEndWith"
                                              placeholder={t('contracts:signupDateTo')}
                                              type="date"
                                              label={t('contracts:signupDateTo')}
                                              width={"150px"}
                                              fontSize={"2xs"}
                                              inputBGColor={themeColors.bgColorSecondary}
                            />
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

export default ContractFilterForm;