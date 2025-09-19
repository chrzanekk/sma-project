import {BaseContractFormValues, ContractDTO} from "@/types/contract-types.ts";
import {useTranslation} from "react-i18next";
import {getSelectedCompany} from "@/utils/company-utils.ts";
import {getContractValidationSchema} from "@/validation/contractValidationSchema.ts";
import {getCurrencyOptions} from "@/types/currency-types.ts";
import {addContract} from "@/services/contract-service.ts";
import {errorNotification, successNotification} from "@/notifications/notifications.ts";
import {formatMessage} from "@/notifications/FormatMessage.tsx";
import CommonContractForm from "@/components/contract/CommonContractForm.tsx";
import React, {useRef, useState} from "react";
import {Box, Grid, GridItem, Heading, Separator} from "@chakra-ui/react";
import {useThemeColors} from "@/theme/theme-colors.ts";
import ContractorPicker from "@/components/contractor/ContractorPicker.tsx";
import {getContractorsByFilter} from "@/services/contractor-service.ts";
import {FormikProps} from "formik";
import {ContractorBaseDTO} from "@/types/contractor-types.ts";
import {ConstructionSiteBaseDTO} from "@/types/constrution-site-types.ts";
import ConstructionSitePicker from "@/components/constructionsite/ConstructionSitePicker.tsx";
import {getConstructionSiteByFilter} from "@/services/construction-site-service.ts";

interface AddContractFormProps {
    onSuccess?: (data: BaseContractFormValues) => void;
}

const AddContractForm: React.FunctionComponent<AddContractFormProps> = ({onSuccess}) => {
    const {t} = useTranslation(['common', 'contracts', 'errors']);
    const currentCompany = getSelectedCompany();
    const currencyOptions = getCurrencyOptions();
    const themeColors = useThemeColors();

    // ref do Formika
    const formikRef = useRef<FormikProps<BaseContractFormValues>>(null);

    // local state do pokazania wyboru
    const [selectedContractor, setSelectedContractor] = useState<ContractorBaseDTO | null>(null);
    const [selectedSite, setSelectedSite] = useState<ConstructionSiteBaseDTO | null>(null);

    const initialValues: BaseContractFormValues = {
        number: '',
        description: '',
        value: '',
        currency: '',
        startDate: '',
        endDate: '',
        signupDate: '',
        realEndDate: '',
        constructionSite: null,
        contractor: null
    }

    const validationSchema = getContractValidationSchema(t, currencyOptions);

    const handleSubmit = async (values: BaseContractFormValues) => {
        try {
            const mappedContract: ContractDTO = {
                ...values,
                company: currentCompany!
            }
            const response = await addContract(mappedContract);
            successNotification(
                t('success', {ns: "common"}),
                formatMessage('notifications.addContractSuccess', {
                    number: values.number
                }, 'contracts')
            )
            const mappedResponse: BaseContractFormValues = {
                ...response,
            }
            onSuccess(mappedResponse);
        } catch (error: any) {
            console.error(error);
            errorNotification(
                t('error', {ns: "common"}),
                error.response?.data?.message || t('contracts:notifications.addContractError')
            );
        }
    };

    return (
        <Box>
            <Grid templateColumns="repeat(6, 1fr)">
                <GridItem colSpan={6} rowSpan={1}>
                    <Heading size={"2xl"} color={themeColors.fontColor}>
                        {t("common:add")}
                    </Heading>
                </GridItem>

                <GridItem colSpan={2}>
                    <Box ml={2} mr={2}>
                        <Heading size={"xl"} color={themeColors.fontColor}>
                            {t("contracts:add")}
                        </Heading>
                        <CommonContractForm initialValues={initialValues}
                                            validationSchema={validationSchema}
                                            onSubmit={handleSubmit}/>
                    </Box>
                </GridItem>

                <GridItem colSpan={2}>
                    <Separator orientation={"vertical"} height={"100%"}>
                        <Box ml={2} mr={2}>
                            <Heading size={"xl"} color={themeColors.fontColor}>
                                {t("contractors:contractor")}
                            </Heading>
                            <ContractorPicker
                                formikRef={formikRef}
                                selected={selectedContractor}
                                onSelectChange={setSelectedContractor}
                                searchFn={async (q) => {
                                    const {contractors} = await getContractorsByFilter({nameStartsWith: q});
                                    return contractors;
                                }}
                            />
                        </Box>
                    </Separator>
                </GridItem>

                <GridItem colSpan={2}>
                    <Separator orientation={"vertical"} height={"100%"}>
                        <Box ml={2} mr={2}>
                            <Heading size={"xl"} color={themeColors.fontColor}>
                                {t("constructionSites:constructionSite")}
                            </Heading>
                            <ConstructionSitePicker
                                formikRef={formikRef}
                                selected={selectedSite}
                                onSelectChange={setSelectedSite}
                                searchFn={async (q) => {
                                    const {constructionSites} = await getConstructionSiteByFilter({nameStartsWith: q});
                                    return constructionSites;
                                }}
                            />
                        </Box>
                    </Separator>
                </GridItem>
            </Grid>
        </Box>


    )
}

export default AddContractForm;