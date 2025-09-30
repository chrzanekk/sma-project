import {BaseContractFormValues, ContractDTO} from "@/types/contract-types.ts";
import {useTranslation} from "react-i18next";
import {getSelectedCompany, getSelectedCompanyId} from "@/utils/company-utils.ts";
import {getContractValidationSchema} from "@/validation/contractValidationSchema.ts";
import {getCurrencyOptions} from "@/types/currency-types.ts";
import {addContract} from "@/services/contract-service.ts";
import {errorNotification, successNotification} from "@/notifications/notifications.ts";
import {formatMessage} from "@/notifications/FormatMessage.tsx";
import CommonContractForm from "@/components/contract/CommonContractForm.tsx";
import React, {useMemo, useRef, useState} from "react";
import {Box, Grid, GridItem, Heading, Separator} from "@chakra-ui/react";
import {useThemeColors} from "@/theme/theme-colors.ts";
import ContractorPicker from "@/components/contractor/ContractorPicker.tsx";
import {FormikProps} from "formik";
import {ContractorBaseDTO} from "@/types/contractor-types.ts";
import {ConstructionSiteBaseDTO} from "@/types/constrution-site-types.ts";
import ConstructionSitePicker from "@/components/constructionsite/ConstructionSitePicker.tsx";
import {getConstructionSiteByFilter} from "@/services/construction-site-service.ts";
import {makeContractorSearchAdapter} from "@/search/contractor-search-adapter.ts";
import {makeContactSearchAdapter} from "@/search/contact-search-adapter.ts";
import ContactPicker from "@/components/contact/ContactPicker.tsx";
import {ContactBaseDTO} from "@/types/contact-types.ts";

interface AddContractFormProps {
    onSuccess?: (data: BaseContractFormValues) => void;
}

const AddContractForm: React.FunctionComponent<AddContractFormProps> = ({onSuccess}) => {
    const {t} = useTranslation(['common', 'contracts', 'errors']);
    const currentCompany = getSelectedCompany();
    const currencyOptions = getCurrencyOptions();
    const themeColors = useThemeColors();

    const formikRef = useRef<FormikProps<BaseContractFormValues>>(null);

    const [selectedContractor, setSelectedContractor] = useState<ContractorBaseDTO | null>(null);
    const [selectedSite, setSelectedSite] = useState<ConstructionSiteBaseDTO | null>(null);
    const [selectedContact, setSelectedContact] = useState<ContactBaseDTO | null>(null);

    const initialValues: BaseContractFormValues = {
        number: '',
        description: '',
        value: '',
        currency: '',
        startDate: '',
        endDate: '',
        signupDate: '',
        realEndDate: '',
        constructionSite: undefined,
        contractor: undefined,
        contact: undefined,
    }

    const validationSchema = getContractValidationSchema(t, currencyOptions);

    const companyId: number = getSelectedCompanyId()!;

    const contractorSearchFn = useMemo(
        () =>
            makeContractorSearchAdapter({
                fixed: {companyId},
                defaults: {page: 0, size: 10, sort: "id,asc"},
            }),
        [companyId]
    );

    const contactSearchFn = useMemo(
        () =>
            makeContactSearchAdapter({
                fixed: {companyId, contractorId: selectedContractor?.id},
                defaults: {page: 0, size: 10, sort: "id,asc"},
            }),
        [companyId, selectedContractor?.id]
    );

    const handleSubmit = async (values: BaseContractFormValues) => {
        try {
            const mappedContract: ContractDTO = {
                ...values,
                contractor: selectedContractor!,
                constructionSite: selectedSite!,
                contact: selectedContact!,
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
            onSuccess?.(mappedResponse);
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
                <GridItem colSpan={2}>
                    <Box ml={2} mr={2}>
                        <Heading size={"xl"} color={themeColors.fontColor}>
                            {t("contracts:details")}
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
                            <Box>
                                <ContractorPicker
                                    formikRef={formikRef}
                                    selected={selectedContractor}
                                    onSelectChange={setSelectedContractor}
                                    searchFn={contractorSearchFn}
                                />

                                {selectedContractor && (
                                    <Box mt={4}>
                                        <ContactPicker
                                        formikRef={formikRef}
                                        selected={selectedContact}
                                        onSelectChange={setSelectedContact}
                                        searchFn={contactSearchFn}
                                    /></Box>
                                    )}
                            </Box>
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