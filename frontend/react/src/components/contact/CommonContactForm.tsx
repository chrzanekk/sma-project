// ReusableContactForm.tsx
import React from 'react';
import {Form, Formik, FormikHelpers, FormikProps} from 'formik';
import * as Yup from 'yup';
import {Button, Grid, GridItem, Stack} from '@chakra-ui/react';
import {CustomInputField, CustomTextAreaField} from '@/components/shared/CustomFormFields';
import {useTranslation} from "react-i18next";
import {BaseContactFormValues} from "@/types/contact-types.ts";

interface CommonContactFormProps {
    initialValues: BaseContactFormValues;
    validationSchema: Yup.Schema<BaseContactFormValues>;
    onSubmit: (values: BaseContactFormValues, formikHelpers: FormikHelpers<BaseContactFormValues>) => Promise<void>;
    readOnly?: boolean;
    hideSubmit?: boolean;
    innerRef?: React.Ref<FormikProps<BaseContactFormValues>>;
}

const CommonContactForm: React.FC<CommonContactFormProps> = (({
                                                                  initialValues,
                                                                  validationSchema,
                                                                  onSubmit,
                                                                  readOnly = false,
                                                                  hideSubmit = false,
                                                                  innerRef,
                                                              }) => {
        const {t} = useTranslation(['common', 'contractors']);
        return (
            <Formik<BaseContactFormValues>
                initialValues={initialValues}
                validationSchema={validationSchema}
                onSubmit={onSubmit}
                enableReinitialize
                innerRef={innerRef}
            >
                {({isValid, isSubmitting, dirty}) => (
                    <Form>
                        <Stack gap={4}>
                            {/* Wiersz 1: FirstName 3/6 + LastName 3/6*/}
                            <Grid templateColumns="repeat(6, 1fr)" gap={4}>
                                <GridItem colSpan={3}>
                                    <CustomInputField
                                        name="firstName"
                                        label={t('contacts:firstName')}
                                        placeholder={t('contacts:firstName')}
                                        readOnly={readOnly}
                                    />
                                </GridItem>
                                <GridItem colSpan={3}>
                                    <CustomInputField
                                        name="lastName"
                                        label={t('contacts:lastName')}
                                        placeholder={t('contacts:lastName')}
                                        readOnly={readOnly}
                                    />
                                </GridItem>
                            </Grid>

                            {/* Wiersz 2: Emial 3/6 + Phone number 3/6 */}
                            <Grid templateColumns="repeat(6, 1fr)" gap={4}>
                                <GridItem colSpan={3}>
                                    <CustomInputField
                                        name="phoneNumber"
                                        label={t('contacts:phoneNumber')}
                                        placeholder={t('contacts:phoneNumber')}
                                        readOnly={readOnly}
                                    />
                                </GridItem>
                                <GridItem colSpan={3}>
                                    <CustomInputField
                                        name="email"
                                        label={t('contacts:email')}
                                        placeholder={t('contacts:email')}
                                        readOnly={readOnly}
                                    />
                                </GridItem>
                            </Grid>

                            {/* Wiersz 3: Additional Info 6/6 */}
                            <Grid templateColumns="repeat(6, 1fr)" gap={4}>
                                <GridItem colSpan={6}>
                                    <CustomTextAreaField
                                        name="additionalInfo"
                                        label={t('contacts:additionalInfo')}
                                        placeholder={t('contacts:additionalInfo')}
                                        readOnly={readOnly}
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
        );
    }
);

export default CommonContactForm;
