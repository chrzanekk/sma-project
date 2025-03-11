import { useTranslation } from "react-i18next";
import React, { useEffect, useRef, useState } from "react";
import { ContractorDTO, ContractorFormValues, FetchableContractorDTO } from "@/types/contractor-types.ts";
import { BaseContactFormValues } from "@/types/contact-types.ts";
import { getContractorById, updateContractor } from "@/services/contractor-service.ts";
import { Country, getCountryOptions } from "@/types/country-type.ts";
import { errorNotification, successNotification } from "@/notifications/notifications.ts";
import { formatMessage } from "@/notifications/FormatMessage.tsx";
import { getContractorValidationSchema } from "@/validation/contractorValidationSchema.ts";
import CommonContractorForm from "@/components/contractor/CommonContractorForm.tsx";
import ContactFormWithSearch from "@/components/contact/ContactFormWithSearch.tsx";
import { Box, Flex, Heading, Text } from "@chakra-ui/react";
import { Button } from "@/components/ui/button.tsx";
import { FormikProps } from "formik";
import {
    StepsRoot,
    StepsList,
    StepsItem,
    StepsContent,
    StepsNextTrigger,
    StepsPrevTrigger,
} from "@/components/ui/steps";
import { themeColors } from "@/theme/theme-colors.ts";

interface EditContractorWithContactFormStepsProps {
    onSuccess: () => void;
    contractorId: number;
}

const EditContractorWithContactFormSteps: React.FC<EditContractorWithContactFormStepsProps> = ({ onSuccess, contractorId }) => {
    const { t } = useTranslation(["common", "contractors", "errors", "contacts"]);
    const countryOptions = getCountryOptions(t);

    // Stany dla danych kontrahenta i listy kontaktów
    const [contractorData, setContractorData] = useState<ContractorFormValues | null>(null);
    const [contacts, setContacts] = useState<BaseContactFormValues[]>([]);
    const [isLoading, setIsLoading] = useState<boolean>(true);
    const [currentStep, setCurrentStep] = useState<number>(0);
    const [isContractorValid, setIsContractorValid] = useState<boolean>(false);

    // Refs formularzy
    const contractorFormRef = useRef<FormikProps<ContractorFormValues>>(null);
    const contactFormRef = useRef<FormikProps<BaseContactFormValues>>(null);

    // Pobranie danych kontrahenta przy montowaniu
    useEffect(() => {
        const fetchContractor = async () => {
            setIsLoading(true);
            try {
                const contractor: FetchableContractorDTO = await getContractorById(contractorId);
                const initial: ContractorFormValues = {
                    id: contractor.id,
                    name: contractor.name,
                    taxNumber: contractor.taxNumber,
                    street: contractor.street,
                    buildingNo: contractor.buildingNo,
                    apartmentNo: contractor.apartmentNo,
                    postalCode: contractor.postalCode,
                    city: contractor.city,
                    country: contractor.country.code,
                    customer: contractor.customer,
                    supplier: contractor.supplier,
                    scaffoldingUser: contractor.scaffoldingUser,
                };
                setContractorData(initial);
                setContacts(contractor.contacts || []);
            } catch (err) {
                console.error("Błąd pobierania kontrahenta: ", err);
            } finally {
                setIsLoading(false);
            }
        };
        fetchContractor().catch();
    }, [contractorId]);

    const validationSchema = getContractorValidationSchema(t, countryOptions);

    // Obsługa kolejnych kroków
    const handleNext = async () => {
        if (currentStep === 0) {
            // Zatwierdzenie formularza kontrahenta
            if (contractorFormRef.current) {
                await contractorFormRef.current.submitForm();
            }
        } else if (currentStep === 1) {
            // Krok kontaktów – dodatkowe zatwierdzenie nie jest wymagane, ale można dodać walidację jeśli potrzeba.
            if (contactFormRef.current) {
                await contactFormRef.current.submitForm();
            }
            setCurrentStep((prev) => prev + 1);
        }
    };

    const handlePrev = () => {
        setCurrentStep((prev) => prev - 1);
    };

    const handleContractorSubmit = async (values: ContractorFormValues) => {
        setContractorData(values);
        setCurrentStep(1);
    };

    // Dodanie kontaktu – wykorzystujemy ContactFormWithSearch
    const handleAddContact = (contact: BaseContactFormValues) => {
        const alreadyExists = contacts.some(
            (c) =>
                c.firstName === contact.firstName &&
                c.lastName === contact.lastName &&
                c.phoneNumber === contact.phoneNumber
        );
        if (!alreadyExists) {
            setContacts((prev) => [...prev, contact]);
        }
    };

    // Usunięcie kontaktu z listy
    const handleRemoveContact = (index: number) => {
        setContacts((prev) => prev.filter((_, i) => i !== index));
    };

    // Finalne zatwierdzenie – wysłanie zmodyfikowanych danych kontrahenta
    const handleFinalSubmit = async () => {
        if (!contractorData) return;
        const payload: ContractorDTO = {
            ...contractorData,
            country: Country.fromCode(contractorData.country),
            contacts: contacts,
        };
        try {
            await updateContractor(payload);
            successNotification(
                t("success", { ns: "common" }),
                formatMessage("notifications.editContractorSuccess", { name: contractorData.name }, "contractors")
            );
            onSuccess();
        } catch (err: any) {
            console.error(err);
            errorNotification(
                t("common:error"),
                err.response?.data?.message || t("contractors:notifications.editContractorError")
            );
        }
    };

    if (isLoading || !contractorData) return <div>Loading...</div>;

    return (
        <StepsRoot defaultStep={0} count={3}>
            <StepsList>
                <StepsItem
                    index={0}
                    title={<Text color={themeColors.fontColor()}>{t("contractors:edit", "Edycja kontrahenta")}</Text>}
                    color={themeColors.fontColor()}
                />
                <StepsItem
                    index={1}
                    title={<Text color={themeColors.fontColor()}>{t("contacts:manage", "Edycja kontaktów")}</Text>}
                    color={themeColors.fontColor()}
                />
                <StepsItem
                    index={2}
                    title={<Text color={themeColors.fontColor()}>{t("common:summary", "Podsumowanie")}</Text>}
                    color={themeColors.fontColor()}
                />
            </StepsList>

            {/* Krok 0 – formularz kontrahenta */}
            <StepsContent index={0}>
                <CommonContractorForm
                    initialValues={contractorData}
                    validationSchema={validationSchema}
                    onSubmit={handleContractorSubmit}
                    hideSubmit={true}
                    innerRef={contractorFormRef}
                    onValidityChange={(isValid) => setIsContractorValid(isValid)}
                />
            </StepsContent>

            {/* Krok 1 – edycja kontaktów */}
            <StepsContent index={1}>
                <Box mb={4}>
                    <Heading size="md">{t("contacts:manage", "Edycja kontaktów")}</Heading>
                </Box>
                {/* Formularz wyszukiwania/dodawania kontaktu – przekazujemy innerRef */}
                <ContactFormWithSearch
                    onSuccess={handleAddContact}
                    hideSubmit
                    innerRef={contactFormRef}
                />
                {/* Przycisk do zatwierdzenia formularza kontaktu */}
                <Button
                    mt={2}
                    colorScheme="blue"
                    onClick={async () => {
                        if (contactFormRef.current) {
                            await contactFormRef.current.submitForm();
                        }
                    }}
                >
                    {t("contacts:add", "Dodaj kontakt")}
                </Button>
                {/* Lista kontaktów z możliwością usunięcia */}
                <Box mt={4}>
                    <Heading size="sm" mb={2}>
                        {t("contacts:list", "Lista kontaktów")}
                    </Heading>
                    <Box
                        maxH="200px"
                        overflowY="auto"
                        border="1px solid #ccc"
                        borderRadius="md"
                        p={2}
                    >
                        {/* Nagłówek listy */}
                        <Box display="grid" gridTemplateColumns="repeat(4, 1fr)" fontWeight="bold" mb={2}>
                            <Box>{t("contacts:firstName", "Imię")}</Box>
                            <Box>{t("contacts:lastName", "Nazwisko")}</Box>
                            <Box>{t("contacts:phoneNumber", "Telefon")}</Box>
                            <Box>{t("common:actions", "Akcje")}</Box>
                        </Box>
                        {/* Wiersze */}
                        {contacts.length > 0 ? (
                            contacts.map((contact, index) => (
                                <Box
                                    key={index}
                                    display="grid"
                                    gridTemplateColumns="repeat(4, 1fr)"
                                    alignItems="center"
                                    py={1}
                                    borderBottom="1px solid #e2e8f0"
                                >
                                    <Box>{contact.firstName}</Box>
                                    <Box>{contact.lastName}</Box>
                                    <Box>{contact.phoneNumber}</Box>
                                    <Box>
                                        <Button
                                            variant="ghost"
                                            colorScheme="red"
                                            size="xs"
                                            onClick={() => handleRemoveContact(index)}
                                        >
                                            {t("common:delete", "Usuń")}
                                        </Button>
                                    </Box>
                                </Box>
                            ))
                        ) : (
                            <Box textAlign="center" py={2}>
                                {t("contacts:noContacts", "Brak kontaktów")}
                            </Box>
                        )}
                    </Box>
                </Box>
            </StepsContent>


            {/* Krok 2 – podsumowanie */}
            <StepsContent index={2}>
                <Flex direction="column" align="center" justify="center" textAlign="center" mb={4}>
                    <Heading size="md" color={themeColors.fontColor()}>
                        {t("common:summary", "Podsumowanie")}
                    </Heading>
                    <Box mt={2}>
                        <Text>
                            <strong>{t("contractors:name", "Nazwa kontrahenta")}: </strong>
                            {contractorData.name || t("common:empty", "Brak danych")}
                        </Text>
                        <Text>
                            <strong>{t("contractors:taxNumber", "NIP")}: </strong>
                            {contractorData.taxNumber || t("common:empty", "Brak danych")}
                        </Text>
                        <Box mt={4}>
                            <Heading size="sm" mb={2}>
                                {t("contacts:list", "Lista kontaktów")}
                            </Heading>
                            <Box
                                maxH="150px"
                                overflowY="auto"
                                border="1px solid #ccc"
                                borderRadius="md"
                                p={2}
                            >
                                <Box display="grid" gridTemplateColumns="repeat(4, 1fr)" fontWeight="bold" mb={2}>
                                    <Box>{t("contacts:firstName", "Imię")}</Box>
                                    <Box>{t("contacts:lastName", "Nazwisko")}</Box>
                                    <Box>{t("contacts:phoneNumber", "Telefon")}</Box>
                                    <Box>{t("common:actions", "Akcje")}</Box>
                                </Box>
                                {contacts.length > 0 ? (
                                    contacts.map((contact, index) => (
                                        <Box
                                            key={index}
                                            display="grid"
                                            gridTemplateColumns="repeat(4, 1fr)"
                                            alignItems="center"
                                            py={1}
                                            borderBottom="1px solid #e2e8f0"
                                        >
                                            <Box>{contact.firstName}</Box>
                                            <Box>{contact.lastName}</Box>
                                            <Box>{contact.phoneNumber}</Box>
                                            <Box>
                                                <Button
                                                    variant="ghost"
                                                    colorScheme="red"
                                                    size="xs"
                                                    onClick={() => handleRemoveContact(index)}
                                                >
                                                    {t("common:delete", "Usuń")}
                                                </Button>
                                            </Box>
                                        </Box>
                                    ))
                                ) : (
                                    <Box textAlign="center" py={2}>
                                        {t("contacts:noContacts", "Brak kontaktów")}
                                    </Box>
                                )}
                            </Box>
                        </Box>
                    </Box>
                </Flex>
                <Flex justify="center" gap={4}>
                    <Button onClick={handleFinalSubmit} colorPalette={"green"}>
                        {t("common:save", "Zapisz")}
                    </Button>
                </Flex>
            </StepsContent>

            <Flex direction="row" align="center" justify="center" textAlign="center" mb={4} gap={6}>
                {currentStep > 0 && (
                    <StepsPrevTrigger asChild>
                        <Button variant="solid" colorPalette={"green"} size="sm" onClick={handlePrev}>
                            {t("common:previous", "Poprzedni")}
                        </Button>
                    </StepsPrevTrigger>
                )}
                {currentStep < 2 && (
                    <StepsNextTrigger asChild>
                        <Button
                            variant="solid"
                            colorPalette={"green"}
                            size="sm"
                            onClick={handleNext}
                            disabled={currentStep === 0 && !isContractorValid}
                        >
                            {t("common:next", "Następny")}
                        </Button>
                    </StepsNextTrigger>
                )}
            </Flex>
        </StepsRoot>
    );
};

export default EditContractorWithContactFormSteps;
