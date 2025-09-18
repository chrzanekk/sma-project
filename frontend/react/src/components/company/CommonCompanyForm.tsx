import {CompanyFormValues} from "@/types/company-types.ts";
import {Form, Formik, FormikHelpers, FormikProps} from "formik";
import React from 'react';
import {useTranslation} from "react-i18next";
import {Button, Grid, GridItem, Stack} from "@chakra-ui/react";
import {CustomInputField, CustomTextAreaField} from "@/components/shared/CustomFormFields.tsx";
import * as Yup from 'yup';

interface CommonCompanyFormProps {
    initialValues: CompanyFormValues;
    validationSchema: Yup.Schema<CompanyFormValues>;
    onSubmit: (values: CompanyFormValues, formikHelpers: FormikHelpers<CompanyFormValues>) => Promise<void>;
    disabled?: boolean;
    hideSubmit?: boolean;
    innerRef?: React.Ref<FormikProps<CompanyFormValues>>;
}

const CommonCompanyForm: React.FC<CommonCompanyFormProps> = (({
                                                                  initialValues,
                                                                  validationSchema,
                                                                  onSubmit,
                                                                  disabled = false,
                                                                  hideSubmit = false,
                                                                  innerRef
                                                              }) => {
    const {t} = useTranslation(['common', 'companies']);
    return (
        <Formik<CompanyFormValues>
            initialValues={initialValues}
            validationSchema={validationSchema}
            onSubmit={onSubmit}
            enableReinitialize
            innerRef={innerRef}
        >
            {({isValid, isSubmitting, dirty}) => (
                <Form>
                    <Stack gap={4}>
                        {/* Wiersz 1: Name 3/6 + TaxNumber 3/6*/}
                        <Grid templateColumns="repeat(6, 1fr)" gap={4}>
                            <GridItem colSpan={3}>
                                <CustomInputField
                                    name="name"
                                    label={t('companies:name')}
                                    placeholder={t('companies:name')}
                                    disabled={disabled}
                                />
                            </GridItem>
                            <GridItem colSpan={3}>
                                <CustomInputField
                                    name="taxNumber"
                                    label={t('companies:taxNumber')}
                                    placeholder={t('companies:taxNumber')}
                                    disabled={true}
                                />
                            </GridItem>
                        </Grid>

                        {/* Wiersz 2: Emial 3/6 + Phone number 3/6 */}
                        <Grid templateColumns="repeat(6, 1fr)" gap={4}>
                            <GridItem colSpan={3}>
                                <CustomInputField
                                    name="phoneNumber"
                                    label={t('companies:phoneNumber')}
                                    placeholder={t('companies:phoneNumber')}
                                    disabled={true}
                                />
                            </GridItem>
                            <GridItem colSpan={3}>
                                <CustomInputField
                                    name="email"
                                    label={t('companies:email')}
                                    placeholder={t('companies:email')}
                                    disabled={true}
                                />
                            </GridItem>
                        </Grid>

                        {/* Wiersz 3: Additional Info 6/6 */}
                        <Grid templateColumns="repeat(6, 1fr)" gap={4}>
                            <GridItem colSpan={6}>
                                <CustomTextAreaField
                                    name="additionalInfo"
                                    label={t('companies:additionalInfo')}
                                    placeholder={t('companies:additionalInfo')}
                                    disabled={disabled}
                                />
                            </GridItem>
                        </Grid>
                        {/* Wiersz 4: Button wyśrodkowany */}
                        {!hideSubmit && (<Grid templateColumns="repeat(6, 1fr)" gap={4}>
                            <GridItem colSpan={6} textAlign="center">
                                <Button
                                    disabled={!isValid || isSubmitting || !dirty}
                                    type="submit"
                                    colorPalette="green"
                                    width="400px"
                                >
                                    {t('save', {ns: "common"})}
                                </Button>
                            </GridItem>
                        </Grid>)}
                    </Stack>
                </Form>
            )}
        </Formik>
    )
})

export default CommonCompanyForm;