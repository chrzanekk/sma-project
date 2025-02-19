import {useTranslation} from "react-i18next";
import React, {useEffect, useState} from "react";
import {Form, Formik} from "formik";
import * as Yup from "yup";
import {errorNotification, successNotification} from "@/notifications/notifications.ts";
import {formatMessage} from "@/notifications/FormatMessage.tsx";
import {Button, Grid, GridItem, Stack} from "@chakra-ui/react";
import {CustomInputField, CustomTextAreaField} from "@/components/shared/CustomFormFields.tsx";
import {ContactDTO, EditContactDTO, EditContactFormValues} from "@/types/contact-types.ts";
import {getContactById, updateContact} from "@/services/contact-service.ts";


interface EditContactFormProps {
    onSuccess: () => void;
    contactId: number;
}

const EditContactForm: React.FC<EditContactFormProps> = ({onSuccess, contactId}) => {
    const {t} = useTranslation(['common', 'contractors', 'errors'])
    const defaultValues: EditContactFormValues = {
        id: 0,
        firstName: '',
        lastName: '',
        phoneNumber: '',
        email: '',
        additionalInfo: ''
    }

    const [initialValues, setInitialValues] = useState<EditContactFormValues>(defaultValues);
    const [isLoading, setIsLoading] = useState<boolean>(true);


    useEffect(() => {
        const fetchContact = async () => {
            setIsLoading(true);
            try {
                const contact: ContactDTO = await getContactById(contactId);
                setInitialValues({
                    id: contact.id,
                    firstName: contact.firstName,
                    lastName: contact.lastName,
                    phoneNumber: contact.phoneNumber,
                    email: contact.email,
                    additionalInfo: contact.additionalInfo
                })
            } catch (err) {
                console.error("Error fetching contact: ", err);
            } finally {
                setIsLoading(false);
            }
        };
        fetchContact().catch();
    }, [contactId]);

    return (
        <Formik<EditContactFormValues>
            initialValues={initialValues}
            enableReinitialize={true}
            validationSchema={Yup.object({
                firstName: Yup.string()
                    .min(2, t('errors:verification.minLength', {field: t('contacts:firstName'), count: 2}))
                    .max(20, t('errors:verification.maxLength', {field: t('contacts:firstName'), count: 20}))
                    .required(t('errors:verification.required', {field: t('contacts:firstName')})),
                lastName: Yup.string()
                    .min(2, t('errors:verification.minLength', {field: t('contacts:lastName'), count: 2}))
                    .max(20, t('errors:verification.maxLength', {field: t('contacts:lastName'), count: 20}))
                    .required(t('errors:verification.required', {field: t('contacts:lastName')})),
                phoneNumber: Yup.string()
                    .min(5, t('errors:verification.minLength', {field: t('contacts:phoneNumber'), count: 5}))
                    .max(20, t('errors:verification.maxLength', {field: t('contacts:phoneNumber'), count: 20}))
                    .required(t('errors:verification.required', {field: t('contacts:phoneNumber')})),
                email: Yup.string()
                    .min(5, t('errors:verification.minLength', {field: t('contacts:email'), count: 5}))
                    .max(20, t('errors:verification.maxLength', {field: t('contacts:email'), count: 20}))
                    .required(t('errors:verification.required', {field: t('contacts:email')})),
                additionalInfo: Yup.string()
                    .min(5, t('errors:verification.minLength', {field: t('contacts:additionalInfo'), count: 5}))
                    .max(200, t('errors:verification.maxLength', {field: t('contacts:additionalInfo'), count: 200}))
                    .required(t('errors:verification.required', {field: t('contacts:additionalInfo')})),
            })}
            onSubmit={async (formValues, {setSubmitting}) => {
                setSubmitting(true);
                try {
                    const mappedContact: EditContactDTO = {
                        ...formValues
                    }
                    await updateContact(mappedContact);
                    successNotification(
                        t('success', {ns: "common"}),
                        formatMessage('notifications.editContractorSuccess', {
                            firstName: mappedContact.firstName,
                            lastName: mappedContact.lastName
                        }, 'contractors')
                    );
                    onSuccess();
                } catch (err: any) {
                    console.error(err);
                    errorNotification(
                        t('common:error'),
                        err.response?.data?.message || t('contractors:notifications.editContractorError')
                    );
                } finally {
                    setSubmitting(false);
                }
            }}
        >
            {({isValid, isSubmitting, dirty}) =>
                (
                    <Form>
                        <Stack gap={4}>
                            {/* Wiersz 1: Name (4/6) i Tax Number (2/6) */}
                            <Grid templateColumns="repeat(2, 3fr)" gap={4}>
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

                            {/* Wiersz 2: Emial + Phone number */}
                            <Grid templateColumns="repeat(2, 3fr)" gap={4}>
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

                            {/* Wiersz 3: Additional Info */}
                            <Grid templateColumns="repeat(1, 6fr)" gap={4}>
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
                                        disabled={!isValid || isSubmitting || !dirty || isLoading}
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
                )
            }
        </Formik>
    )
}

export default EditContactForm;