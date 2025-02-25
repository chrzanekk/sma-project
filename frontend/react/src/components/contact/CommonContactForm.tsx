// ReusableContactForm.tsx
import React from 'react';
import {Form, Formik, FormikHelpers} from 'formik';
import * as Yup from 'yup';
import {Button, Grid, GridItem, Stack} from '@chakra-ui/react';
import {CustomInputField, CustomTextAreaField} from '@/components/shared/CustomFormFields';
import {useTranslation} from "react-i18next";

export interface ContactFormValues {
    firstName: string;
    lastName: string;
    phoneNumber: string;
    email: string;
    additionalInfo: string;
}

interface ReusableContactFormProps {
    initialValues: ContactFormValues;
    validationSchema: Yup.Schema<ContactFormValues>;
    onSubmit: (values: ContactFormValues, formikHelpers: FormikHelpers<ContactFormValues>) => Promise<void>;
    readOnly?: boolean;
}

const CommonContactForm: React.FC<ReusableContactFormProps> = ({
                                                                   initialValues,
                                                                   validationSchema,
                                                                   onSubmit,
                                                                   readOnly = false
                                                               }) => {
    const {t} = useTranslation(['common', 'contractors']);
    return (
        <Formik<ContactFormValues>
            initialValues={initialValues}
            validationSchema={validationSchema}
            onSubmit={onSubmit}
            enableReinitialize
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
                        <Grid templateColumns="repeat(6, 1fr)" gap={4}>
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
                        </Grid>
                    </Stack>
                </Form>
            )}
        </Formik>
    );
};

export default CommonContactForm;
