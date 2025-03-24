import {useTranslation} from "react-i18next";
import React, {useRef, useState} from "react";
import {ContractorDTO, ContractorFormValues} from "@/types/contractor-types.ts";
import {BaseContactFormValues} from "@/types/contact-types.ts";
import {addContractor} from "@/services/contractor-service.ts";
import {Country, getCountryOptions} from "@/types/country-type.ts";
import {getContractorValidationSchema} from "@/validation/contractorValidationSchema.ts";
import {formatMessage} from "@/notifications/FormatMessage.tsx";
import {errorNotification, successNotification} from "@/notifications/notifications.ts";
import CommonContractorForm from "@/components/contractor/CommonContractorForm.tsx";
import ContactFormWithSearch from "@/components/contact/ContactFormWithSearch.tsx";
import {Box, Flex, Heading, Steps, Table, Text} from "@chakra-ui/react";
import {Button} from "@/components/ui/button.tsx";
import {FormikProps} from "formik";
import {useThemeColors} from "@/theme/theme-colors.ts";
import ContractorSummary from "@/components/contractor/ContractorSummary.tsx";

interface AddContractorWithContactFormProps {
    onSuccess: () => void;
}

const AddContractorWithContactForm: React.FC<AddContractorWithContactFormProps> = ({onSuccess}) => {
    const {t} = useTranslation(["common", "contractors", "errors", "contacts"]);
    const countryOptions = getCountryOptions(t);

    const [contractorData, setContractorData] = useState<ContractorFormValues | null>(null);

    const [currentStep, setCurrentStep] = useState(0);
    const [isContractorValid, setIsContractorValid] = useState(false);
    const [contacts, setContacts] = useState<BaseContactFormValues[]>([]);
    const themeColors = useThemeColors();


    const contractorInitialValues: ContractorFormValues = {
        name: "",
        taxNumber: "",
        street: "",
        buildingNo: "",
        apartmentNo: "",
        postalCode: "",
        city: "",
        country: countryOptions[0].value,
        customer: undefined,
        supplier: undefined,
        scaffoldingUser: undefined,
    };

    const contractorValidationSchema = getContractorValidationSchema(t, countryOptions);

    const contractorFormRef = useRef<FormikProps<ContractorFormValues>>(null);
    const contactFormRef = useRef<FormikProps<BaseContactFormValues>>(null);

    const handleNext = async () => {
        if (currentStep === 0) {
            if (contractorFormRef.current) {
                await contractorFormRef.current.submitForm();
            }
        } else if (currentStep === 1) {
            if (contactFormRef.current) {
                await contactFormRef.current.submitForm();
            }
        }
    };

    const handlePrev = () => {
        setCurrentStep((prev) => prev - 1);
    };

    const handleContractorSubmit = async (values: ContractorFormValues) => {
        setContractorData(values);
        setCurrentStep(1);
    };

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

    const handleRemoveContact = (index: number) => {
        setContacts((prev) => prev.filter((_, i) => i !== index));
    };

    const handleFinalSubmit = async () => {
        if (!contractorData) return;
        const payload: ContractorDTO = {
            ...contractorData,
            country: Country.fromCode(contractorData.country),
            customer: contractorData.customer ?? false,
            supplier: contractorData.supplier ?? false,
            scaffoldingUser: contractorData.scaffoldingUser ?? false,
            contacts: contacts
        };
        try {
            await addContractor(payload);
            successNotification(
                t("success", {ns: "common"}),
                formatMessage("notifications.addContractorSuccess", {name: contractorData.name}, "contractors")
            );
            onSuccess();
        } catch (err: any) {
            console.error(err);
            errorNotification(
                t("error", {ns: "common"}),
                err.response?.data?.message || t("contractors:notifications.addContractorError")
            );
        }
    };

    return (
        <Steps.Root defaultStep={0} count={3} variant={"solid"} colorPalette={'green'} size={"sm"}>
            <Steps.List>
                <Steps.Item key={0} index={0} color={themeColors.fontColor}>
                    <Steps.Indicator/>
                    <Steps.Title>{<Text ml={2}
                                        color={themeColors.fontColor}>{t("contractors:add")}</Text>}
                    </Steps.Title>
                    <Steps.Separator/>
                </Steps.Item>
                <Steps.Item key={1} index={1} color={themeColors.fontColor}>
                    <Steps.Indicator/>
                    <Steps.Title>{<Text ml={2}
                                        color={themeColors.fontColor}>{t("contacts:addOptional")}</Text>}
                    </Steps.Title>
                    <Steps.Separator/>
                </Steps.Item>
                <Steps.Item key={2} index={2} color={themeColors.fontColor}>
                    <Steps.Indicator/>
                    <Steps.Title>{<Text ml={2}
                                        color={themeColors.fontColor}>{t("common:summary")}</Text>} </Steps.Title>
                </Steps.Item>
            </Steps.List>

            <Steps.Content key={0} index={0}>
                <CommonContractorForm
                    initialValues={contractorInitialValues}
                    validationSchema={contractorValidationSchema}
                    onSubmit={handleContractorSubmit}
                    hideSubmit={true}
                    innerRef={contractorFormRef}
                    onValidityChange={(isValid) => setIsContractorValid(isValid)}
                />
            </Steps.Content>


            <Steps.Content key={1} index={1}>
                <Heading size="md" mb={2} color={themeColors.fontColor}>
                    {t("contacts:addOptional")}
                </Heading>
                <ContactFormWithSearch onSuccess={handleAddContact}
                                       hideSubmit={true}
                                       innerRef={contactFormRef}/>
                {/* Przycisk "Dodaj kontakt" */}
                <Button
                    mt={2}
                    size="xs"
                    colorPalette="blue"
                    onClick={async () => {
                        if (contactFormRef.current) {
                            await contactFormRef.current.submitForm(); // wywołuje handleContactSubmit
                        }
                    }}
                >
                    {t("contacts:add", "Dodaj kontakt")}
                </Button>

                {/* Lista kontaktów (opcjonalnie z przyciskiem usuń) */}
                <Box mt={4}>
                    <Heading size="sm" mb={2} color={themeColors.fontColor}>
                        {t("contacts:list", "Lista kontaktów")}
                    </Heading>

                    <Table.ScrollArea borderWidth="1px" rounded="sm" height="200px">
                        <Table.Root size="sm" stickyHeader showColumnBorder interactive color={themeColors.fontColor}>
                            <Table.Header>
                                <Table.Row bg={themeColors.bgColorPrimary}>
                                    <Table.ColumnHeader textAlign="center" color={themeColors.fontColor}>
                                        {t("contacts:firstName")}
                                    </Table.ColumnHeader>
                                    <Table.ColumnHeader textAlign="center" color={themeColors.fontColor}>
                                        {t("contacts:lastName")}
                                    </Table.ColumnHeader>
                                    <Table.ColumnHeader textAlign="center" color={themeColors.fontColor}>
                                        {t("contacts:phoneNumber")}
                                    </Table.ColumnHeader>
                                    <Table.ColumnHeader textAlign="center" color={themeColors.fontColor}>
                                        {t("common:actions", "Akcje")}
                                    </Table.ColumnHeader>
                                </Table.Row>
                            </Table.Header>

                            <Table.Body>
                                {contacts.length > 0 ? (
                                    contacts.map((contact, idx) => (
                                        <Table.Row
                                            key={idx}
                                            bg={themeColors.bgColorSecondary}
                                            _hover={{
                                                textDecoration: "none",
                                                bg: themeColors.highlightBgColor,
                                                color: themeColors.fontColorHover,
                                            }}
                                        >
                                            <Table.Cell textAlign="center">{contact.firstName}</Table.Cell>
                                            <Table.Cell textAlign="center">{contact.lastName}</Table.Cell>
                                            <Table.Cell textAlign="center">{contact.phoneNumber}</Table.Cell>
                                            <Table.Cell textAlign="center">
                                                <Button
                                                    variant="ghost"
                                                    colorScheme="red"
                                                    size="2xs"
                                                    onClick={() => handleRemoveContact(idx)}
                                                >
                                                    {t("common:delete", "Usuń")}
                                                </Button>
                                            </Table.Cell>
                                        </Table.Row>
                                    ))
                                ) : (
                                    <Box textAlign="center" py={2}>
                                        {t("contacts:noContacts", "Brak kontaktów")}
                                    </Box>
                                )}
                            </Table.Body>
                        </Table.Root>
                    </Table.ScrollArea>
                </Box>
            </Steps.Content>


            <Steps.Content key={2} index={2}>
                <Box direction="column" textAlign="center" mb={2}>
                    <Heading size="md" color={themeColors.fontColor}>{t("common:summary")}</Heading>
                    <ContractorSummary contractorData={contractorData!!} contacts={contacts}/>
                </Box>
                <Flex justify="center" gap={4}>
                    <Button onClick={handleFinalSubmit} colorPalette={"green"}>
                        {t("common:save")}
                    </Button>
                </Flex>
            </Steps.Content>

            <Flex direction="row" align="center" justify="center" textAlign="center" mb={4} gap={6}>
                {currentStep > 0 && (
                    <Steps.PrevTrigger asChild>
                        <Button variant="solid" colorPalette={"green"} size="sm" onClick={handlePrev}>
                            {t("common:previous")}
                        </Button>
                    </Steps.PrevTrigger>
                )}
                {currentStep < 2 && (
                    <Steps.NextTrigger asChild>
                        <Button
                            variant="solid"
                            colorPalette={"green"}
                            size="sm"
                            onClick={handleNext}
                            disabled={currentStep === 0 && !isContractorValid}>
                            {t("common:next")}
                        </Button>
                    </Steps.NextTrigger>
                )}
            </Flex>
        </Steps.Root>
    )
};


export default AddContractorWithContactForm;