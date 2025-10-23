import {BasePositionFormValues} from "@/types/contact-types.ts";
import React, {useState} from "react";
import {getFreeContactsByFilter} from "@/services/contact-service.ts";
import {useTranslation} from "react-i18next";
import {Box, Button} from "@chakra-ui/react";
import CommonContactForm from "@/components/contact/CommonContactForm.tsx";
import {getContactValidationSchema} from "@/validation/contactValidationSchema.ts";
import ContactSearch from "@/components/contact/ContactSearch.tsx";

interface ContactFormWithSearchProps {
    onSuccess: (values: BasePositionFormValues) => void;
    hideSubmit?: boolean;
    innerRef?: React.Ref<any>;
}

const ContactFormWithSearch: React.FC<ContactFormWithSearchProps> = ({onSuccess, hideSubmit, innerRef}) => {
    const {t} = useTranslation(["common", "contacts", "errors"]);
    const [selectedContact, setSelectedContact] = useState<BasePositionFormValues | null>(null);
    const validationSchema = getContactValidationSchema(t);

    const defaultValues: BasePositionFormValues = {
        firstName: "",
        lastName: "",
        phoneNumber: "",
        email: "",
        additionalInfo: "",
    };

    const handleSelectContact = (contact: BasePositionFormValues) => {
        setSelectedContact(contact);
    };

    const handleResetContact = () => {
        setSelectedContact(null);
    };

    const handleAddContact = async (values: BasePositionFormValues) => {
        onSuccess(values);
    };

    return (
        <Box>
            <Box mb={2}>
                <ContactSearch
                    searchFn={async (q: string) => {
                        const res = await getFreeContactsByFilter({lastNameStartsWith: q});
                        return res.contacts;
                    }}
                    onSelect={handleSelectContact}
                    minChars={2}
                    debounceMs={300}
                    autoSearch={true} // ustaw na true, gdy wymagane wyszukiwanie w trakcie wpisywania
                    size="sm"
                />
            </Box>

            {selectedContact && (
                <Box mb={4}>
                    <Button onClick={handleResetContact} colorPalette="red" size={"2xs"}>
                        {t("common:resetSelected")}
                    </Button>
                </Box>
            )}

            {/* Formularz kontaktu – zarówno dla nowego kontaktu, jak i edycji wybranego */}
            <CommonContactForm
                initialValues={selectedContact || defaultValues}
                validationSchema={validationSchema}
                onSubmit={async (values) => {
                    // Wywołanie callbacka po walidacji i zatwierdzeniu formularza
                    await handleAddContact(values);
                }}
                disabled={selectedContact !== null} // Jeżeli kontakt został wybrany, można uniemożliwić edycję
                hideSubmit={true}
                innerRef={innerRef}
            />

            {/* Przycisk do ręcznego zatwierdzenia formularza kontaktu */}
            {!hideSubmit && (
                <Button
                    mt={1}
                    colorScheme="blue"
                    size={"2xs"}
                    onClick={async () => {
                        if (innerRef && typeof innerRef !== "function" && innerRef.current) {
                            await innerRef.current.submitForm();
                        }
                    }}
                >
                    {t("contacts:add", "Dodaj kontakt")}
                </Button>
            )}
        </Box>
    );
};

export default ContactFormWithSearch;
