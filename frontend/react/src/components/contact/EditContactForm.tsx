import {useTranslation} from "react-i18next";
import React, {useEffect, useState} from "react";
import {errorNotification, successNotification} from "@/notifications/notifications.ts";
import {formatMessage} from "@/notifications/FormatMessage.tsx";
import {ContactDTO, EditContactDTO, EditContactFormValues} from "@/types/contact-types.ts";
import {getContactById, updateContact} from "@/services/contact-service.ts";
import CommonContactForm, {ContactFormValues} from "@/components/contact/CommonContactForm.tsx";
import {getContactValidationSchema} from "@/validation/contactValidationSchema.ts";


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

    const validationSchema = getContactValidationSchema(t);

    const handleSubmit = async (values: ContactFormValues) => {
        try {
            const mappedContact: EditContactDTO = {
                ...values
            }
            await updateContact(mappedContact);
            successNotification(
                t('success', {ns: "common"}),
                formatMessage('notifications.editContactSuccess', {
                    firstName: mappedContact.firstName,
                    lastName: mappedContact.lastName
                }, 'contacts')
            );
            onSuccess();
        } catch (err: any) {
            console.error(err);
            errorNotification(
                t('common:error'),
                err.response?.data?.message || t('contractors:notifications.editContractorError')
            );
        }
    };

    return (
        <>{!isLoading && (
            <CommonContactForm initialValues={initialValues}
                               validationSchema={validationSchema}
                               onSubmit={handleSubmit}
            />
        )}
        </>
    )
}

export default EditContactForm;