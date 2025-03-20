import {useTranslation} from "react-i18next";
import React, {useEffect, useRef, useState} from "react";
import {ContractorDTO, ContractorFormValues, FetchableContractorDTO} from "@/types/contractor-types.ts";
import {BaseContactFormValues} from "@/types/contact-types.ts";
import {getContractorById, updateContractor} from "@/services/contractor-service.ts";
import {Country, getCountryOptions} from "@/types/country-type.ts";
import {errorNotification, successNotification} from "@/notifications/notifications.ts";
import {formatMessage} from "@/notifications/FormatMessage.tsx";
import {getContractorValidationSchema} from "@/validation/contractorValidationSchema.ts";
import CommonContractorForm from "@/components/contractor/CommonContractorForm.tsx";
import ContactFormWithSearch from "@/components/contact/ContactFormWithSearch.tsx";
import {Box, Flex, Heading, Steps, Table, Text} from "@chakra-ui/react";
import {Button} from "@/components/ui/button.tsx";
import {FormikProps} from "formik";
import {StepsNextTrigger, StepsPrevTrigger,} from "@/components/ui/steps";
import {themeColors} from "@/theme/theme-colors.ts";
import ContractorSummary from "@/components/contractor/ContractorSummary.tsx";

interface EditContractorWithContactFormStepsProps {
    onSuccess: () => void;
    contractorId: number;
}

const EditContractorWithContactFormSteps: React.FC<EditContractorWithContactFormStepsProps> = ({
                                                                                                   onSuccess,
                                                                                                   contractorId
                                                                                               }) => {
    const {t} = useTranslation(["common", "contractors", "errors", "contacts"]);
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
            customer: contractorData.customer ?? false,
            supplier: contractorData.supplier ?? false,
            scaffoldingUser: contractorData.scaffoldingUser ?? false,
            contacts: contacts,
        };
        try {
            await updateContractor(payload);
            successNotification(
                t("success", {ns: "common"}),
                formatMessage("notifications.editContractorSuccess", {name: contractorData.name}, "contractors")
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
        <Steps.Root defaultStep={0} count={3} variant={"solid"} colorPalette={'green'} size={"sm"}>
            <Steps.List>
                <Steps.Item
                    key={0}
                    index={0}
                    color={themeColors.fontColor()}>
                    <Steps.Indicator/>
                    <Steps.Title>{<Text
                        color={themeColors.fontColor()}>{t("contractors:edit", "Edycja kontrahenta")}</Text>}</Steps.Title>
                    <Steps.Separator/>
                </Steps.Item>
                <Steps.Item
                    key={1}
                    index={1}
                    color={themeColors.fontColor()}>
                    <Steps.Indicator/>
                    <Steps.Title>{<Text
                        color={themeColors.fontColor()}>{t("contacts:manage", "Edycja kontaktów")}</Text>}</Steps.Title>
                    <Steps.Separator/>
                </Steps.Item>
                <Steps.Item
                    key={2}
                    index={2}
                    color={themeColors.fontColor()}>
                    <Steps.Indicator/>
                    <Steps.Title>{<Text
                        color={themeColors.fontColor()}>{t("common:summary", "Podsumowanie")}</Text>}</Steps.Title>
                    <Steps.Separator/>
                </Steps.Item>
            </Steps.List>

            {/* Krok 0 – formularz kontrahenta */}
            <Steps.Content key={0} index={0}>
                <CommonContractorForm
                    initialValues={contractorData}
                    validationSchema={validationSchema}
                    onSubmit={handleContractorSubmit}
                    hideSubmit={true}
                    innerRef={contractorFormRef}
                    onValidityChange={(isValid) => setIsContractorValid(isValid)}
                />
            </Steps.Content>

            {/* Krok 1 – edycja kontaktów */}
            <Steps.Content key={1} index={1}>
                <Box mb={2}>
                    <Heading size="md"
                             color={themeColors.fontColor()}>{t("contacts:manage", "Edycja kontaktów")}</Heading>
                </Box>
                {/* Formularz wyszukiwania/dodawania kontaktu – przekazujemy innerRef */}
                <ContactFormWithSearch
                    onSuccess={handleAddContact}
                    hideSubmit
                    innerRef={contactFormRef}
                />
                {/* Przycisk do zatwierdzenia formularza kontaktu */}
                <Button
                    mt={1}
                    size={"2xs"}
                    colorPalette="blue"
                    onClick={async () => {
                        if (contactFormRef.current) {
                            await contactFormRef.current.submitForm();
                        }
                    }}
                >
                    {t("contacts:add", "Dodaj kontakt")}
                </Button>
                {/* Lista kontaktów z możliwością usunięcia */}
                <Box mt={2}>
                    <Heading size="sm" mb={2}>
                        {t("contacts:list", "Lista kontaktów")}
                    </Heading>

                    <Table.ScrollArea borderWidth={"1px"} rounded={"sm"} height={"150px"}>
                        <Table.Root size={"sm"}
                                    stickyHeader
                                    showColumnBorder
                                    interactive
                                    color={themeColors.fontColor()}
                        >
                            <Table.Header>
                                <Table.Row bg={themeColors.bgColorPrimary()}>
                                    <Table.ColumnHeader color={themeColors.fontColor()}
                                                        textAlign={"center"}>{t("contacts:firstName")}</Table.ColumnHeader>
                                    <Table.ColumnHeader color={themeColors.fontColor()}
                                                        textAlign={"center"}>{t("contacts:lastName")}</Table.ColumnHeader>
                                    <Table.ColumnHeader color={themeColors.fontColor()}
                                                        textAlign={"center"}>{t("contacts:phoneNumber")}</Table.ColumnHeader>
                                    <Table.ColumnHeader color={themeColors.fontColor()}
                                                        textAlign={"center"}>{t("common:actions", "Akcje")}</Table.ColumnHeader>
                                </Table.Row>
                            </Table.Header>

                            <Table.Body>
                                {contacts.length > 0 ? (contacts.map((contact, idx) => (
                                    <Table.Row key={idx}
                                               style={{cursor: "pointer"}}
                                               bg={themeColors.bgColorSecondary()}
                                               _hover={{
                                                   textDecoration: 'none',
                                                   bg: themeColors.highlightBgColor(),
                                                   color: themeColors.fontColorHover()
                                               }}
                                    >
                                        <Table.Cell textAlign={"center"}>{contact.firstName}</Table.Cell>
                                        <Table.Cell textAlign={"center"}>{contact.lastName}</Table.Cell>
                                        <Table.Cell textAlign={"center"}>{contact.phoneNumber}</Table.Cell>
                                        <Table.Cell textAlign={"center"}>
                                            <Box>
                                                <Button
                                                    variant="ghost"
                                                    colorScheme="red"
                                                    size="2xs"
                                                    onClick={() => handleRemoveContact(idx)}
                                                >
                                                    {t("common:delete", "Usuń")}
                                                </Button>
                                            </Box></Table.Cell>
                                    </Table.Row>
                                ))) : (
                                    <Box textAlign="center" py={2}>
                                        {t("contacts:noContacts", "Brak kontaktów")}
                                    </Box>
                                )}
                            </Table.Body>
                        </Table.Root>
                    </Table.ScrollArea>
                </Box>
            </Steps.Content>

            {/* Krok 2 – podsumowanie */}
            <Steps.Content key={2} index={2}>
                <Box direction="column" textAlign="center" mb={2}>
                    <Heading size="md" color={themeColors.fontColor()}>
                        {t("common:summary", "Podsumowanie")}
                    </Heading>
                    <ContractorSummary contractorData={contractorData} contacts={contacts} t={t}
                                       themeColors={themeColors}/>
                </Box>
                <Flex justify="center" gap={4}>
                    <Button onClick={handleFinalSubmit} colorPalette={"green"}>
                        {t("common:save", "Zapisz")}
                    </Button>
                </Flex>
            </Steps.Content>

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
        </Steps.Root>
    );
};

export default EditContractorWithContactFormSteps;
