import {BaseContractFormValues, ContractDTO} from "@/types/contract-types.ts";
import {useTranslation} from "react-i18next";
import {getSelectedCompany} from "@/utils/company-utils.ts";
import {getContractValidationSchema} from "@/validation/contractValidationSchema.ts";
import {getCurrencyOptions} from "@/types/currency-types.ts";
import {addContract} from "@/services/contract-service.ts";
import {errorNotification, successNotification} from "@/notifications/notifications.ts";
import {formatMessage} from "@/notifications/FormatMessage.tsx";
import CommonContractForm from "@/components/contract/CommonContractForm.tsx";
import React from "react";
import {Box, Heading} from "@chakra-ui/react";
import {useThemeColors} from "@/theme/theme-colors.ts";

interface AddContractFormProps {
    onSuccess?: (data: BaseContractFormValues) => void;
}

const AddContractForm: React.FunctionComponent<AddContractFormProps> = ({onSuccess}) => {
    const {t} = useTranslation(['common', 'contracts', 'errors']);
    const currentCompany = getSelectedCompany();
    const currencyOptions = getCurrencyOptions();
    const themeColors = useThemeColors();

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

    const handleSubmit = async (values: BaseContractFormValues) => {
        try {
            const mappedContract: ContractDTO = {
                ...values,
                contractor: values.contractor!,
                constructionSite: values.constructionSite!,
                contact: values.contact!,
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
        <Box ml={2} mr={2}>
            <Heading size={"xl"} color={themeColors.fontColor}>
                {t("contracts:details")}
            </Heading>
            <CommonContractForm initialValues={initialValues}
                                validationSchema={validationSchema}
                                onSubmit={handleSubmit}
            />
        </Box>
    )
}

export default AddContractForm;