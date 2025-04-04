import {CompanyBaseDTO, CompanyFormValues} from "@/types/company-type.ts";
import {useTranslation} from "react-i18next";
import React from "react";
import {addCompany} from "@/services/company-service.ts";
import {errorNotification, successNotification} from "@/notifications/notifications.ts";
import {formatMessage} from "@/notifications/FormatMessage.tsx";
import CommonCompanyForm from "@/components/company/CommonCompanyForm.tsx";
import {getCompanyValidationSchema} from "@/validation/companyValidationSchema.ts";


interface AddCompanyFormProps {
    onSuccess: (data: CompanyFormValues) => void;
}

const AddCompanyForm: React.FC<AddCompanyFormProps> = ({onSuccess}) => {
    const {t} = useTranslation(['common', 'companies', 'errors']);
    const initialValues: CompanyFormValues = {
        name: '',
        additionalInfo: ''
    };

    const validationSchema = getCompanyValidationSchema(t);

    const handleSubmit = async (values: CompanyFormValues) => {
        try {
            const mappedCompany: CompanyBaseDTO = {
                ...values
            }
            const response = await addCompany(mappedCompany);
            successNotification(t('common:success'),
                formatMessage('companies:notifications.addCompanySuccess', {name: values.name}));
            const mappedResponse: CompanyFormValues = {
                ...response
            }
            onSuccess(mappedResponse);
        } catch (err: any) {
            console.error(err);
            errorNotification(t('common:error'), err.response?.data?.message || t('companies:notifications.addCompanyError'))
        }
    }
    return (
        <CommonCompanyForm initialValues={initialValues}
                           validationSchema={validationSchema}
                           onSubmit={handleSubmit}/>
    )
}
export default AddCompanyForm;