import {
    BaseScaffoldingLogFormValues,
    FetchableScaffoldingLogDTO,
    ScaffoldingLogDTO
} from "@/types/scaffolding-log-types.ts";
import {useTranslation} from "react-i18next";
import {getSelectedCompany} from "@/utils/company-utils.ts";
import {getScaffoldingLogValidationSchema} from "@/validation/scaffoldingLogValidationSchema.ts";
import {getScaffoldingLogById, updateScaffoldingLog} from "@/services/scaffolding-log-service.ts";
import {errorNotification, successNotification} from "@/notifications/notifications.ts";
import {formatMessage} from "@/notifications/FormatMessage.tsx";
import CommonScaffoldingLogForm from "@/components/scaffolding/log/CommonScaffoldingLogForm.tsx";
import React, {useEffect, useState} from "react";

interface EditScaffoldingLogFormProps {
    onSuccess: (data: BaseScaffoldingLogFormValues) => void;
    logId: number
}

const AddScaffoldingLogForm: React.FC<EditScaffoldingLogFormProps> = ({onSuccess, logId}) => {

    const defaultValues: BaseScaffoldingLogFormValues = {
        id: 0,
        name: '',
        additionalInfo: '',
        contractor: undefined,
        constructionSite: undefined
    };
    const [initialValues, setInitialValues] = useState<BaseScaffoldingLogFormValues>(defaultValues);
    const [isLoading, setIsLoading] = useState<boolean>(true);
    const {t} = useTranslation(['common', 'scaffoldingLogs', 'errors']);
    const currentCompany = getSelectedCompany();
    const validationSchema = getScaffoldingLogValidationSchema(t);

    useEffect(() => {
        const fetchLog = async () => {
            setIsLoading(true);
            try {
                const log: FetchableScaffoldingLogDTO = await getScaffoldingLogById(logId);
                setInitialValues({
                    id: log.id,
                    name: log.name,
                    additionalInfo: log.additionalInfo,
                    contractor: log.contractor,
                    constructionSite: log.constructionSite
                })
            } catch (err) {
                console.log(err);
            } finally {
                setIsLoading(false);
            }
        };
        fetchLog().catch();
    }, [logId]);

    const handleSubmit = async (values: BaseScaffoldingLogFormValues) => {
        try {
            const mappedScaffoldingLog: ScaffoldingLogDTO = {
                ...values,
                company: currentCompany!,
                constructionSite: values.constructionSite!,
                contractor: values.contractor!,
            }
            const response = await updateScaffoldingLog(mappedScaffoldingLog);
            successNotification(
                t('success', {ns: "common"}),
                formatMessage('notifications.editScaffoldingLogSuccess', {
                    name: values.name,
                }, 'scaffoldingLogs')
            );
            const mappedResponse: BaseScaffoldingLogFormValues = {
                ...response
            }
            onSuccess(mappedResponse);
        } catch (error: any) {
            errorNotification(
                t('error', {ns: "common"}),
                error.response?.data?.message || t('scaffoldingLogs:notifications:editScaffoldingLogError')
            )
        }
    };
    return (
        <>{!isLoading && (
            <CommonScaffoldingLogForm initialValues={initialValues}
                                      validationSchema={validationSchema}
                                      onSubmit={handleSubmit}/>)}
        </>

    );
}
export default AddScaffoldingLogForm;