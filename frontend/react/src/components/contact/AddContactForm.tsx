import {useTranslation} from "react-i18next";
import {errorNotification, successNotification} from "@/notifications/notifications.ts";
import {formatMessage} from "@/notifications/FormatMessage.tsx";
import React from "react";
import {addContact} from "@/services/contact-service.ts";
import {BaseContactFormValues, ContactDTO} from "@/types/contact-types.ts";
import CommonContactForm from "@/components/contact/CommonContactForm.tsx";
import {getContactValidationSchema} from "@/validation/contactValidationSchema.ts";


interface AddContactFormProps {
    onSuccess: (data: BaseContactFormValues) => void;
}

const AddContactForm: React.FC<AddContactFormProps> = ({onSuccess}) => {
    const {t} = useTranslation(['common', 'contacts', 'errors']);
    const initialValues: BaseContactFormValues = {
        firstName: '',
        lastName: '',
        phoneNumber: '',
        email: '',
        additionalInfo: '',
    };

    const validationSchema = getContactValidationSchema(t);

    const handleSubmit = async (values: BaseContactFormValues) => {
        try {
            const mappedContact: ContactDTO = {
                ...values
            }
            const response = await addContact(mappedContact);
            successNotification(
                t('success', {ns: "common"}),
                formatMessage('notifications.addContactSuccess', {
                    firstName: values.firstName,
                    lastName: values.lastName
                }, 'contacts')
            );
            const mappedResponse: BaseContactFormValues = {
                ...response
            }
            onSuccess(mappedResponse);
        } catch (err: any) {
            console.error(err);
            errorNotification(
                t('error', {ns: "common"}),
                err.response?.data?.message || t('contacts:notifications.addContactError')
            );
        }
    };
    return (
        <CommonContactForm initialValues={initialValues}
                           validationSchema={validationSchema}
                           onSubmit={handleSubmit}/>
    );
}
export default AddContactForm;