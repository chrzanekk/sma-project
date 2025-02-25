import {useTranslation} from "react-i18next";
import React, {useState} from "react";
import {ContractorDTO, ContractorFormValues} from "@/types/contractor-types.ts";
import {BaseContactFormValues} from "@/types/contact-types.ts";
import {addContractor} from "@/services/contractor-service.ts";
import {
    StepsCompletedContent,
    StepsContent,
    StepsItem,
    StepsList,
    StepsNextTrigger,
    StepsPrevTrigger,
    StepsRoot,
} from "@/components/ui/steps"
import {Country, getCountryOptions} from "@/types/country-type.ts";
import {getContractorValidationSchema} from "@/validation/contractorValidationSchema.ts";
import {formatMessage} from "@/notifications/FormatMessage.tsx";
import {errorNotification, successNotification} from "@/notifications/notifications.ts";
import CommonContractorForm from "@/components/contractor/CommonContractorForm.tsx";
import ContactFormWithSearch from "@/components/contact/ContactFormWithSearch.tsx";
import {Box, Group, Heading} from "@chakra-ui/react";
import {Button} from "@/components/ui/button.tsx";


interface AddContractorWithContactFormProps {
    onSuccess: () => void;
}

const AddContractorWithContactForm: React.FC<AddContractorWithContactFormProps> = ({onSuccess}) => {
    const {t} = useTranslation(["common", "contractors", "errors", "contacts"]);
    const countryOptions = getCountryOptions(t);

    const [contractorData, setContractorData] = useState<ContractorFormValues | null>(null);
    const [contactData, setContactData] = useState<BaseContactFormValues | null>(null);

    const [currentStep, setCurrentStep] = useState(0);

    const contractorInitialValues: ContractorFormValues = {
        name: "",
        taxNumber: "",
        street: "",
        buildingNo: "",
        apartmentNo: "",
        postalCode: "",
        city: "",
        country: countryOptions[0].value,
        customer: false,
        supplier: false,
        scaffoldingUser: false,
    };

    const contractorValidationSchema = getContractorValidationSchema(t, countryOptions);

    const handleContractorSubmit = async (values: ContractorFormValues) => {
        setContractorData(values);
        setCurrentStep(1);
    };

    const handleContactSubmit = (values: BaseContactFormValues) => {
        setContactData(values);
        setCurrentStep(2);
    };

    const handleFinalSubmit = async () => {
        if (!contractorData) return;
        const payload: ContractorDTO = {
            ...contractorData,
            country: Country.fromCode(contractorData.country),
            contacts: contactData ? [contactData] : [],
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
        <StepsRoot defaultStep={0} count={3}>
            <StepsList>
                <StepsItem index={0} title={t("contractors:contractorData", "Dane kontrahenta")}/>
                <StepsItem index={1} title={t("contacts:contactData", "Kontakt")}/>
                <StepsItem index={2} title={t("common:summary", "Podsumowanie")}/>
            </StepsList>

            <StepsContent index={0}>
                <CommonContractorForm
                    initialValues={contractorInitialValues}
                    validationSchema={contractorValidationSchema}
                    onSubmit={handleContractorSubmit}
                />
            </StepsContent>

            <StepsContent index={1}>
                <ContactFormWithSearch onSuccess={handleContactSubmit}/>
            </StepsContent>

            <StepsContent index={2}>
                <Box mb={4}>
                    <Heading size="md">{t("common:summary", "Podsumowanie")}</Heading>
                    <Box mt={2}>
                        <p>
                            <strong>{t("contractors:name", "Nazwa kontrahenta")}: </strong>
                            {contractorData ? contractorData.name : t("common:empty", "Brak danych")}
                        </p>
                        <p>
                            <strong>{t("contacts:firstName", "Imię kontaktu")}: </strong>
                            {contactData ? contactData.firstName : t("common:empty", "Brak")}
                        </p>
                        <p>
                            <strong>{t("contacts:lastName", "Nazwisko kontaktu")}: </strong>
                            {contactData ? contactData.lastName : t("common:empty", "Brak")}
                        </p>
                    </Box>
                </Box>
                <Button onClick={handleFinalSubmit} colorScheme="green">
                    {t("common:save", "Zapisz")}
                </Button>
            </StepsContent>

            <StepsCompletedContent>{t("common:allStepsCompleted", "Wszystkie kroki zakończone!")}</StepsCompletedContent>

            <Group mt={4}>
                <StepsPrevTrigger asChild>
                    <Button variant="outline" size="sm" onClick={() => setCurrentStep(currentStep - 1)}>
                        {t("common:previous", "Poprzedni")}
                    </Button>
                </StepsPrevTrigger>
                <StepsNextTrigger asChild>
                    <Button variant="outline" size="sm" onClick={() => setCurrentStep(currentStep + 1)}>
                        {t("common:next", "Następny")}
                    </Button>
                </StepsNextTrigger>
            </Group>
        </StepsRoot>
    )
};


export default AddContractorWithContactForm;