import {BaseContactFormValues} from "@/types/contact-types.ts";
import React, {useState} from "react";
import {getContactsByFilter} from "@/services/contact-service.ts";
import {useTranslation} from "react-i18next";
import {Box, Button, Input, List} from "@chakra-ui/react";
import CommonContactForm from "@/components/contact/CommonContactForm.tsx";
import {getContactValidationSchema} from "@/validation/contactValidationSchema.ts";

interface ContactFormWithSearchProps {
    onSuccess: (values: BaseContactFormValues) => void;
}

const ContactFormWithSearch: React.FC<ContactFormWithSearchProps> = ({onSuccess}) => {
    const {t} = useTranslation(["common", "contacts", "errors"]);
    const [searchTerm, setSearchTerm] = useState("");
    const [searchResults, setSearchResults] = useState<BaseContactFormValues[]>([]);
    const [selectedContact, setSelectedContact] = useState<BaseContactFormValues | null>(null);
    const validationSchema = getContactValidationSchema(t);

    const defaultValues: BaseContactFormValues = {
        firstName: "",
        lastName: "",
        phoneNumber: "",
        email: "",
        additionalInfo: "",
    };

    const handleSearch = async () => {
        try {
            const result = await getContactsByFilter({lastName: searchTerm});
            setSearchResults(result.contacts)
        } catch (err) {
            console.error('Bład wyszukiwania kontaktu', err);
        }
    };
    const handleSelectContact = (contact: BaseContactFormValues) => {
        setSelectedContact(contact);
        setSearchResults([]);
    }

    const handleResetContact = () => {
        setSelectedContact(null);
    }

    return (
        <Box>
            <Box mb={4}>
                <Input
                    placeholder={"Wyszukaj kontakt (nazwisko)"}
                    value={searchTerm}
                    onChange={(e) => setSearchTerm(e.target.value)}
                />
                <Button mt={2} onClick={handleSearch}>
                    {t('common:search')}
                </Button>
                {searchResults.length > 0 && (
                    <List.Root mt={2} border="1px solid gray" borderRadius="md" p={2}>
                        {searchResults.map((contact, idx) => (
                            <List.Item
                                key={idx}
                                p={2}
                                cursor="pointer"
                                onClick={() => handleSelectContact(contact)}
                                _hover={{bg: "gray.100"}}
                            >
                                {contact.firstName} {contact.lastName}
                            </List.Item>
                        ))}
                    </List.Root>
                )}
            </Box>

            {selectedContact && (
                <Box mb={4}>
                    <Button onClick={handleResetContact} colorScheme="red">
                        {t("contacts:resetSelected", "Resetuj wybrany kontakt")}
                    </Button>
                </Box>
            )}

            <CommonContactForm
                initialValues={selectedContact || defaultValues}
                validationSchema={validationSchema}
                onSubmit={async (values) => {
                    onSuccess(values);
                }}
                readOnly={selectedContact !== null}
            />
        </Box>

    )
}

export default ContactFormWithSearch;