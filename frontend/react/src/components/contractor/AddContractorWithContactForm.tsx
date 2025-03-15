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
import {Box, Flex, Heading, Steps, Text} from "@chakra-ui/react";
import {Button} from "@/components/ui/button.tsx";
import {FormikProps} from "formik";
import {themeColors} from "@/theme/theme-colors.ts";

interface AddContractorWithContactFormProps {
    onSuccess: () => void;
}

const AddContractorWithContactForm: React.FC<AddContractorWithContactFormProps> = ({onSuccess}) => {
    const {t} = useTranslation(["common", "contractors", "errors", "contacts"]);
    const countryOptions = getCountryOptions(t);

    const [contractorData, setContractorData] = useState<ContractorFormValues | null>(null);
    const [contactData, setContactData] = useState<BaseContactFormValues | null>(null);

    const [currentStep, setCurrentStep] = useState(0);
    const [isContractorValid, setIsContractorValid] = useState(false);


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
            console.log(payload);
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
                <Steps.Item key={0} index={0} color={themeColors.fontColor()}>
                    <Steps.Indicator/>
                    <Steps.Title>{<Text ml={2} color={themeColors.fontColor()}>{t("contractors:add")}</Text>}</Steps.Title>
                    <Steps.Separator/>
                </Steps.Item>
                <Steps.Item key={1} index={1} color={themeColors.fontColor()}>
                    <Steps.Trigger>
                    <Steps.Indicator/>
                    <Steps.Title>{<Text ml={2} color={themeColors.fontColor()}>{t("contacts:addOptional")}</Text>} </Steps.Title>
                    </Steps.Trigger>
                    <Steps.Separator/>
                </Steps.Item>
                <Steps.Item key={2} index={2} color={themeColors.fontColor()}>
                    <Steps.Indicator/>
                    <Steps.Title>{<Text ml={2} color={themeColors.fontColor()}>{t("common:summary")}</Text>} </Steps.Title>
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
                <ContactFormWithSearch onSuccess={handleContactSubmit}
                                       hideSubmit={true}
                                       innerRef={contactFormRef}/>
            </Steps.Content>

            <Steps.Content key={2} index={2}>
                <Flex direction="column" align="center" justify="center" textAlign="center" mb={4}>
                    <Heading size="md" color={themeColors.fontColor()}>{t("common:summary")}</Heading>
                    <Box mt={2}>
                        {/*//todo add header for Contractor*/}
                        <p>
                            <strong>{t("contractors:name")}: </strong>
                            {contractorData ? contractorData.name : t("common:empty")}
                        </p>
                        {/*//todo add header for Contact*/}
                        <p>
                            <strong>{t("contacts:firstName")}: </strong>
                            {contactData ? contactData.firstName : t("common:empty")}
                        </p>
                        <p>
                            <strong>{t("contacts:lastName")}: </strong>
                            {contactData ? contactData.lastName : t("common:empty")}
                        </p>
                    </Box>
                </Flex>
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