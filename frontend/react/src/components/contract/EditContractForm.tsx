import {BaseContractFormValues, ContractDTO, FetchableContractDTO} from "@/types/contract-types.ts";
import {useTranslation} from "react-i18next";
import {getSelectedCompany} from "@/utils/company-utils.ts";
import {getContractValidationSchema} from "@/validation/contractValidationSchema.ts";
import {getCurrencyOptions} from "@/types/currency-types.ts";
import {getContractById, updateContract} from "@/services/contract-service.ts";
import {errorNotification, successNotification} from "@/notifications/notifications.ts";
import {formatMessage} from "@/notifications/FormatMessage.tsx";
import CommonContractForm from "@/components/contract/CommonContractForm.tsx";
import React, {useEffect, useState} from "react";

interface EditContractFormProps {
    onSuccess?: (data: BaseContractFormValues) => void;
    contractId: number;
}

const EditContractForm: React.FunctionComponent<EditContractFormProps> = ({onSuccess, contractId}) => {
    const {t} = useTranslation(['common', 'contracts', 'errors']);
    const currentCompany = getSelectedCompany();
    const currencyOptions = getCurrencyOptions();

    const defaultValues: BaseContractFormValues = {
        number: '',
        description: '',
        value: '',
        currency: '',
        startDate: '',
        endDate: '',
        signupDate: '',
        realEndDate: '',
    }

    const [initialValues, setInitialValues] = useState<BaseContractFormValues>(defaultValues);
    const [isLoading, setIsLoading] = useState<boolean>(true);

    useEffect(() => {
        const fetchContract = async () => {
            setIsLoading(true);
            try {
                const contract: FetchableContractDTO = await getContractById(contractId)
                setInitialValues({
                    id: contract.id,
                    number: contract.number,
                    description: contract.description,
                    value: contract.value,
                    currency: contract.currency,
                    startDate: contract.startDate,
                    endDate: contract.endDate,
                    signupDate: contract.signupDate,
                    realEndDate: contract.realEndDate,
                    contractor: contract.contractor,
                    constructionSite: contract.constructionSite,
                })
            } catch (err) {
                console.log(err);
            } finally {
                setIsLoading(false);
            }
        };
        fetchContract().catch();
    }, [contractId]);

    const validationSchema = getContractValidationSchema(t, currencyOptions);

    const handleSubmit = async (values: BaseContractFormValues) => {
        try {
            const mappedContract: ContractDTO = {
                ...values,
                company: currentCompany!,
            }
            const response = await updateContract(mappedContract);
            successNotification(
                t('success', {ns: "common"}),
                formatMessage('notifications.editContractSuccess', {
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
                error.response?.data?.message || t('contracts:notifications.editContractError')
            );
        }
    };

    return (
        <>{!isLoading && (
            <CommonContractForm initialValues={initialValues}
                                validationSchema={validationSchema}
                                onSubmit={handleSubmit}/>)}</>

    )
}

export default EditContractForm;