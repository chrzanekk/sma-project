import {useTranslation} from "react-i18next";
import {CustomInputField, CustomTextAreaField} from "@/components/shared/CustomFormFields.tsx";
import {Form, Formik} from "formik";
import * as Yup from 'yup';
import {errorNotification, successNotification} from "@/notifications/notifications.ts";
import {formatMessage} from "@/notifications/FormatMessage.tsx";
import React from "react";
import {Button, Grid, GridItem, Stack} from "@chakra-ui/react";
import {addContact} from "@/services/contact-service.ts";
import {AddContactDTO, AddContactFormValues} from "@/types/contact-types.ts";


interface AddContactFormProps {
    onSuccess: () => void;
}

const AddContactForm: React.FC<AddContactFormProps> = ({onSuccess}) => {
    const {t} = useTranslation(['common', 'contacts', 'errors']);

    return (
        <Formik<AddContactFormValues>
            initialValues={{
                firstName: '',
                lastName: '',
                phoneNumber: '',
                email: '',
                additionalInfo: ''
            }}
            validationSchema={Yup.object({
                firstName: Yup.string()
                    .min(2, t('errors:verification.minLength', {field: t('contacts:firstName'), count: 2}))
                    .max(20, t('errors:verification.maxLength', {field: t('contacts:firstName'), count: 20}))
                    .required(t('errors:verification.required', {field: t('contacts:firstName')})),
                lastName: Yup.string()
                    .min(2, t('errors:verification.minLength', {field: t('contacts:lastName'), count: 2}))
                    .max(30, t('errors:verification.maxLength', {field: t('contacts:lastName'), count: 30}))
                    .required(t('errors:verification.required', {field: t('contacts:lastName')})),
                phoneNumber: Yup.string()
                    .min(5, t('errors:verification.minLength', {field: t('contacts:phoneNumber'), count: 5}))
                    .max(25, t('errors:verification.maxLength', {field: t('contacts:phoneNumber'), count: 25}))
                    .required(t('errors:verification.required', {field: t('contacts:phoneNumber')})),
                email: Yup.string()
                    .min(5, t('errors:verification.minLength', {field: t('contacts:email'), count: 5}))
                    .max(75, t('errors:verification.maxLength', {field: t('contacts:email'), count: 75}))
                    .required(t('errors:verification.required', {field: t('contacts:email')})),
                additionalInfo: Yup.string()
                    .min(5, t('errors:verification.minLength', {field: t('contacts:additionalInfo'), count: 5}))
                    .max(200, t('errors:verification.maxLength', {field: t('contacts:additionalInfo'), count: 200}))
                    .required(t('errors:verification.required', {field: t('contacts:additionalInfo')})),

            })}
            onSubmit={async (formValues, {setSubmitting}) => {
                setSubmitting(true);
                try {
                    const mappedContact: AddContactDTO = {
                        ...formValues
                    }
                    await addContact(mappedContact);
                    successNotification(
                        t('success', {ns: "common"}),
                        formatMessage('notifications.addContactSuccess', {
                            firstName: formValues.firstName,
                            lastName: formValues.lastName
                        }, 'contacts')
                    );
                    onSuccess();
                } catch (err: any) {
                    console.error(err);
                    errorNotification(
                        t('error', {ns: "common"}),
                        err.response?.data?.message || t('contractors:notifications.addContactError')
                    );
                } finally {
                    setSubmitting(false);
                }
            }}
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
                                />
                            </GridItem>
                            <GridItem colSpan={3}>
                                <CustomInputField
                                    name="lastName"
                                    label={t('contacts:lastName')}
                                    placeholder={t('contacts:lastName')}
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
                                />
                            </GridItem>
                            <GridItem colSpan={3}>
                                <CustomInputField
                                    name="email"
                                    label={t('contacts:email')}
                                    placeholder={t('contacts:email')}
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

export default AddContactForm;