import {BaseContactFormValues} from "@/types/contact-types.ts";
import React, {useState} from "react";
import {getContactsByFilter} from "@/services/contact-service.ts";
import {useTranslation} from "react-i18next";
import {Box, Button, Input, List} from "@chakra-ui/react";
import CommonContactForm from "@/components/contact/CommonContactForm.tsx";
import {getContactValidationSchema} from "@/validation/contactValidationSchema.ts";
import {themeColorsHex} from "@/theme/theme-colors.ts";

interface ContactFormWithSearchProps {
    onSuccess: (values: BaseContactFormValues) => void;
    hideSubmit?: boolean;
    innerRef?: React.Ref<any>;
}

const ContactFormWithSearch: React.FC<ContactFormWithSearchProps> = ({onSuccess, hideSubmit, innerRef}) => {
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
            console.log(searchTerm)
            const result = await getContactsByFilter({lastNameStartsWith: searchTerm});
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
                    _placeholder={{color: themeColorsHex.fontColor()}}
                    value={searchTerm}
                    onChange={(e) => setSearchTerm(e.target.value)}
                />
                <Button mt={2} onClick={handleSearch} colorPalette={"orange"}>
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
                    <Button onClick={handleResetContact} colorPalette="red">
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
                disabled={selectedContact !== null}
                hideSubmit={hideSubmit}
                innerRef={innerRef}
            />
        </Box>

    )
}

export default ContactFormWithSearch;