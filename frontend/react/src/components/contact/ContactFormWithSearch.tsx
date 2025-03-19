import {BaseContactFormValues} from "@/types/contact-types.ts";
import React, {useState} from "react";
import {getContactsByFilter} from "@/services/contact-service.ts";
import {useTranslation} from "react-i18next";
import {Box, Button, Table} from "@chakra-ui/react";
import CommonContactForm from "@/components/contact/CommonContactForm.tsx";
import {getContactValidationSchema} from "@/validation/contactValidationSchema.ts";
import {themeColors} from "@/theme/theme-colors.ts";
import {CustomInputSearchField} from "@/components/shared/CustomFormFields.tsx";

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
            const result = await getContactsByFilter({lastNameStartsWith: searchTerm});
            setSearchResults(result.contacts);
        } catch (err) {
            console.error("Błąd wyszukiwania kontaktu", err);
        }
    };

    const handleSelectContact = (contact: BaseContactFormValues) => {
        setSelectedContact(contact);
        setSearchResults([]);
    };

    const handleResetContact = () => {
        setSelectedContact(null);
    };

    // Dodanie przycisku "Dodaj kontakt" – uruchamia submit formularza kontaktu
    const handleAddContact = async (values: BaseContactFormValues) => {
        // Możesz dodatkowo dodać walidację lub inne akcje przed dodaniem
        onSuccess(values);
    };

    return (
        <Box>
            <Box mb={2}>
                <CustomInputSearchField
                    searchTerm={searchTerm}
                    setSearchTerm={setSearchTerm}
                    handleSearch={handleSearch}
                    placeholder={t("contacts:searchPlaceholder", "Wyszukaj kontakt (nazwisko)")}
                />
                {searchResults.length > 0 && (
                    <Table.ScrollArea borderWidth={"1px"} rounded={"sm"} height={"150px"}>
                        <Table.Root size={"sm"}
                                    stickyHeader
                                    showColumnBorder
                                    interactive
                                    color={themeColors.fontColor()}
                        >
                            <Table.Header>
                                <Table.Row bg={themeColors.bgColorPrimary()} >
                                    <Table.ColumnHeader
                                        color={themeColors.fontColor()}>{t("contacts:firstName")}</Table.ColumnHeader>
                                    <Table.ColumnHeader color={themeColors.fontColor()}
                                                        textAlign={"center"}>{t("contacts:lastName")}</Table.ColumnHeader>
                                    <Table.ColumnHeader color={themeColors.fontColor()}
                                                        textAlign={"end"}>{t("contacts:phoneNumber")}</Table.ColumnHeader>
                                </Table.Row>
                            </Table.Header>

                            <Table.Body>
                                {searchResults.map((contact, idx) => (
                                    <Table.Row key={idx}
                                               onClick={() => handleSelectContact(contact)}
                                               style={{cursor: "pointer"}}
                                               bg={themeColors.bgColorSecondary()}
                                               _hover={{
                                                   textDecoration: 'none',
                                                   bg: themeColors.highlightBgColor(),
                                                   color: themeColors.fontColorHover()
                                               }}
                                    >
                                        <Table.Cell>{contact.firstName}</Table.Cell>
                                        <Table.Cell textAlign={"center"}>{contact.lastName}</Table.Cell>
                                        <Table.Cell textAlign={"end"}>{contact.phoneNumber}</Table.Cell>
                                    </Table.Row>
                                ))}
                            </Table.Body>
                        </Table.Root>
                    </Table.ScrollArea>
                )}
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
