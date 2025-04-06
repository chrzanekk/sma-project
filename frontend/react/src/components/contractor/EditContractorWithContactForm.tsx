import {useTranslation} from "react-i18next";
import React, {useEffect, useRef, useState} from "react";
import {ContractorFormValues, ContractorUpdateDTO, FetchableContractorDTO} from "@/types/contractor-types.ts";
import {BaseContactDTOForContractor, BaseContactFormValues} from "@/types/contact-types.ts";
import {getContactsByContractorIdPaged, getContractorById, updateContractor} from "@/services/contractor-service.ts";
import {Country, getCountryOptions} from "@/types/country-type.ts";
import {errorNotification, successNotification} from "@/notifications/notifications.ts";
import {formatMessage} from "@/notifications/FormatMessage.tsx";
import {getContractorValidationSchema} from "@/validation/contractorValidationSchema.ts";
import CommonContractorForm from "@/components/contractor/CommonContractorForm.tsx";
import ContactFormWithSearch from "@/components/contact/ContactFormWithSearch.tsx";
import {Box, Flex, Heading, Spinner, Steps, Table, Text} from "@chakra-ui/react";
import {Button} from "@/components/ui/button.tsx";
import {FormikProps} from "formik";
import {StepsNextTrigger, StepsPrevTrigger,} from "@/components/ui/steps";
import {useThemeColors} from "@/theme/theme-colors.ts";
import ContractorSummary from "@/components/contractor/ContractorSummary.tsx";
import {getSelectedCompany} from "@/utils/company-utils.ts";

interface EditContractorWithContactFormStepsProps {
    onSuccess: () => void;
    contractorId: number;
}

interface ContactState {
    contacts: BaseContactFormValues[];
    page: number;
    hasMore: boolean;
    loading: boolean;
    addedContacts: BaseContactFormValues[];
    deletedContacts: BaseContactFormValues[];
}

const EditContractorWithContactFormSteps: React.FC<EditContractorWithContactFormStepsProps> = ({
                                                                                                   onSuccess,
                                                                                                   contractorId
                                                                                               }) => {
    const {t} = useTranslation(["common", "contractors", "errors", "contacts"]);
    const themeColors = useThemeColors();
    const countryOptions = getCountryOptions(t);
    const currentCompany = getSelectedCompany();

    // Stany dla danych kontrahenta i listy kontaktów
    const [contractorData, setContractorData] = useState<ContractorFormValues | null>(null);
    const [contactState, setContactState] = useState<ContactState>({
        contacts: [],
        page: 0,
        hasMore: true,
        loading: false,
        addedContacts: [],
        deletedContacts: [],
    });
    const [isLoading, setIsLoading] = useState<boolean>(true);
    const [currentStep, setCurrentStep] = useState<number>(0);
    const [isContractorValid, setIsContractorValid] = useState<boolean>(false);

    // Refs formularzy
    const contractorFormRef = useRef<FormikProps<ContractorFormValues>>(null);
    const contactFormRef = useRef<FormikProps<BaseContactFormValues>>(null);
    const scrollContainerRef = useRef<HTMLDivElement>(null);

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
                await fetchContactsPage(0);
            } catch (err) {
                console.error("Błąd pobierania kontrahenta: ", err);
            } finally {
                setIsLoading(false);
            }
        };
        fetchContractor().catch();
    }, [contractorId]);

    const fetchContactsPage = async (page: number) => {
        try {
            setContactState((prev) => ({...prev, loading: true}));
            const response = await getContactsByContractorIdPaged(contractorId, page, 5);
            setContactState((prev) => ({
                ...prev,
                contacts: [...prev.contacts, ...response.items],
                page: page + 1,
                hasMore: page + 1 < response.totalPages,
                loading: false,
            }));
        } catch (error) {
            console.error("Błąd ładowania kontaktów:", error);
            setContactState((prev) => ({...prev, loading: false}));
        }
    };

    const onScroll = () => {
        const container = scrollContainerRef.current;
        if (container && contactState.hasMore && !contactState.loading) {
            const {scrollTop, scrollHeight, clientHeight} = container;
            if (scrollHeight - scrollTop <= clientHeight + 20) {
                fetchContactsPage(contactState.page).catch();
            }
        }
    };

    const handleAddContact = (contact: BaseContactFormValues) => {
        const exists = contactState.contacts.some(
            (c) =>
                c.firstName === contact.firstName &&
                c.lastName === contact.lastName &&
                c.phoneNumber === contact.phoneNumber
        );
        if (!exists) {
            setContactState((prev) => ({
                ...prev,
                contacts: [...prev.contacts, contact],
                addedContacts: [...prev.addedContacts, contact]
            }));
        }
    };

    const handleRemoveContact = (index: number) => {
        setContactState((prev) => {
            const contactToRemove = prev.contacts[index];
            return {
                ...prev,
                contacts: prev.contacts.filter((_, i) => i !== index),
                deletedContacts: [...prev.deletedContacts, contactToRemove],
            };
        });
    };

    const validationSchema = getContractorValidationSchema(t, countryOptions);

    const handleNext = async () => {
        if (currentStep === 0) {
            if (contractorFormRef.current) {
                await contractorFormRef.current.submitForm();
            }
        } else if (currentStep === 1) {
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

    // Finalne zatwierdzenie – wysłanie zmodyfikowanych danych kontrahenta
    const handleFinalSubmit = async () => {
        if (!contractorData) return;

        const addedContacts: BaseContactDTOForContractor[] = contactState.addedContacts.map((contact) => ({
            ...contact,
            company: currentCompany!,
        }))

        const deletedContacts: BaseContactDTOForContractor[] = contactState.deletedContacts.map((contact) => ({
            ...contact,
            company: currentCompany!,
        }))

        const payload: ContractorUpdateDTO = {
            ...contractorData,
            country: Country.fromCode(contractorData.country),
            customer: contractorData.customer ?? false,
            supplier: contractorData.supplier ?? false,
            scaffoldingUser: contractorData.scaffoldingUser ?? false,
            addedContacts: addedContacts,
            deletedContacts: deletedContacts,
            company: currentCompany!
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
                    color={themeColors.fontColor}>
                    <Steps.Indicator/>
                    <Steps.Title>{<Text
                        color={themeColors.fontColor}>{t("contractors:edit")}</Text>}</Steps.Title>
                    <Steps.Separator/>
                </Steps.Item>
                <Steps.Item
                    key={1}
                    index={1}
                    color={themeColors.fontColor}>
                    <Steps.Indicator/>
                    <Steps.Title>{<Text
                        color={themeColors.fontColor}>{t("contacts:manage")}</Text>}</Steps.Title>
                    <Steps.Separator/>
                </Steps.Item>
                <Steps.Item
                    key={2}
                    index={2}
                    color={themeColors.fontColor}>
                    <Steps.Indicator/>
                    <Steps.Title>{<Text
                        color={themeColors.fontColor}>{t("common:summary")}</Text>}</Steps.Title>
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
                             color={themeColors.fontColor}>{t("contacts:manage")}</Heading>
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
                    {t("contacts:add")}
                </Button>
                {/* Lista kontaktów z możliwością usunięcia */}
                <Box mt={2}>
                    <Text textStyle="lg" fontWeight="bold" color={themeColors.fontColor}>
                        {t("contacts:contactList")}
                    </Text>
                    <Table.ScrollArea borderWidth={"1px"} rounded={"sm"} height={"150px"}
                                      onScroll={onScroll} ref={scrollContainerRef}>
                        <Table.Root size={"sm"}
                                    stickyHeader
                                    showColumnBorder
                                    interactive
                                    color={themeColors.fontColor}
                        >
                            <Table.Header>
                                <Table.Row bg={themeColors.bgColorPrimary}>
                                    <Table.ColumnHeader color={themeColors.fontColor}
                                                        textAlign={"center"}>{t("contacts:firstName")}</Table.ColumnHeader>
                                    <Table.ColumnHeader color={themeColors.fontColor}
                                                        textAlign={"center"}>{t("contacts:lastName")}</Table.ColumnHeader>
                                    <Table.ColumnHeader color={themeColors.fontColor}
                                                        textAlign={"center"}>{t("contacts:phoneNumber")}</Table.ColumnHeader>
                                    <Table.ColumnHeader color={themeColors.fontColor}
                                                        textAlign={"center"}>{t("common:actions", "Akcje")}</Table.ColumnHeader>
                                </Table.Row>
                            </Table.Header>

                            <Table.Body>
                                {contactState.contacts.map((contact, idx) => (
                                    <Table.Row key={idx} bg={themeColors.bgColorSecondary}>
                                        <Table.Cell textAlign="center">{contact.firstName}</Table.Cell>
                                        <Table.Cell textAlign="center">{contact.lastName}</Table.Cell>
                                        <Table.Cell textAlign="center">{contact.phoneNumber}</Table.Cell>
                                        <Table.Cell textAlign="center">
                                            <Button variant="ghost" colorScheme="red" size="2xs"
                                                    onClick={() => handleRemoveContact(idx)}>
                                                {t("common:delete")}
                                            </Button>
                                        </Table.Cell>
                                    </Table.Row>
                                ))}
                                {contactState.loading && (
                                    <Table.Row>
                                        <Table.Cell colSpan={4} textAlign="center">
                                            <Spinner size="sm" mt={2}/>
                                        </Table.Cell>
                                    </Table.Row>
                                )}
                            </Table.Body>
                        </Table.Root>
                    </Table.ScrollArea>
                </Box>
            </Steps.Content>

            {/* Krok 2 – podsumowanie */}
            <Steps.Content key={2} index={2}>
                <Box direction="column" textAlign="center" mb={2}>
                    <Heading size="md" color={themeColors.fontColor}>
                        {t("common:summary")}
                    </Heading>
                    <ContractorSummary
                        contractorData={contractorData}
                        addedContacts={contactState.addedContacts}
                        deletedContacts={contactState.deletedContacts}
                    />
                </Box>
                <Flex justify="center" gap={4}>
                    <Button onClick={handleFinalSubmit} colorPalette={"green"}>
                        {t("common:save")}
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
