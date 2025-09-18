import {useTranslation} from "react-i18next";
import React, {useEffect, useState} from "react";
import {errorNotification, successNotification} from "@/notifications/notifications.ts";
import {formatMessage} from "@/notifications/FormatMessage.tsx";
import {CompanyBaseDTO, CompanyFormValues, FetchableCompanyDTO} from "@/types/company-types.ts";
import {getCompanyById, updateCompany} from "@/services/company-service.ts";
import {getCompanyValidationSchema} from "@/validation/companyValidationSchema.ts";
import CommonCompanyForm from "@/components/company/CommonCompanyForm.tsx";


interface EditCompanyFormProps {
    onSuccess: () => void;
    companyId: number;
}

const EditCompanyForm: React.FC<EditCompanyFormProps> = ({onSuccess, companyId}) => {
    const defaultValues: CompanyFormValues = {
        id: 0,
        name: '',
        additionalInfo: ''
    }
    const {t} = useTranslation(['common', 'companies', 'errors'])
    const [initialValues, setInitialValues] = useState<CompanyFormValues>(defaultValues);
    const [isLoading, setIsLoading] = useState<boolean>(true);

    useEffect(() => {
        const fetchCompany = async () => {
            setIsLoading(true);
            try {
                const company: FetchableCompanyDTO = await getCompanyById(companyId);
                setInitialValues({
                    id: company.id,
                    name: company.name,
                    additionalInfo: company.additionalInfo
                })
            } catch (err) {
                console.error("Error fetching company: ", err);
            } finally {
                setIsLoading(false);
            }
        };
        fetchCompany().catch();
    }, [companyId]);

    const validationSchema = getCompanyValidationSchema(t);

    const handleSubmit = async (values: CompanyFormValues) => {
        try {
            const mappedContact: CompanyBaseDTO = {
                ...values
            }
            await updateCompany(mappedContact);
            successNotification(
                t('common:success'),
                formatMessage('companies:notifications.editCompanySuccess', {
                    name: mappedContact.name
                })
            );
            onSuccess();
        } catch (err: any) {
            console.error(err);
            errorNotification(
                t('common:error'),
                err.response?.data?.message || t('companies:notifications.editCompanyError')
            );
        }
    };

    return (
        <>{!isLoading && (
            <CommonCompanyForm initialValues={initialValues}
                               validationSchema={validationSchema}
                               onSubmit={handleSubmit}
            />
        )}
        </>
    )
}

export default EditCompanyForm;