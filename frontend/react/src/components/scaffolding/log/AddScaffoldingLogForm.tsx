import {BaseScaffoldingLogFormValues, ScaffoldingLogDTO} from "@/types/scaffolding-log-types.ts";
import {useTranslation} from "react-i18next";
import {getSelectedCompany} from "@/utils/company-utils.ts";
import {getScaffoldingLogValidationSchema} from "@/validation/scaffoldingLogValidationSchema.ts";
import {addScaffoldingLog} from "@/services/scaffolding-log-service.ts";
import {errorNotification, successNotification} from "@/notifications/notifications.ts";
import {formatMessage} from "@/notifications/FormatMessage.tsx";
import CommonScaffoldingLogForm from "@/components/scaffolding/log/CommonScaffoldingLogForm.tsx";
import React from "react";

interface AddScaffoldingLogFormProps {
    onSuccess: (data: BaseScaffoldingLogFormValues) => void;
}

const AddScaffoldingLogForm: React.FC<AddScaffoldingLogFormProps> = ({onSuccess}) => {
    const {t} = useTranslation(['common', 'scaffoldingLogs', 'errors']);
    const currentCompany = getSelectedCompany();
    const initialValues: BaseScaffoldingLogFormValues = {
        name: '',
        additionalInfo: '',
        contractor: undefined,
        constructionSite: undefined
    };

    const validationSchema = getScaffoldingLogValidationSchema(t);

    const handleSubmit = async (values: BaseScaffoldingLogFormValues) => {
        try {
            const mappedScaffoldingLog: ScaffoldingLogDTO = {
                ...values,
                company: currentCompany!,
                constructionSite: values.constructionSite!,
                contractor: values.contractor!,
            }
            const response = await addScaffoldingLog(mappedScaffoldingLog);
            successNotification(
                t('success', {ns: "common"}),
                formatMessage('notifications.addScaffoldingLogSuccess', {
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
                error.response?.data?.message || t('scaffoldingLogs:notifications:addScaffoldingLogError')
            )
        }
    };
    return (
        <CommonScaffoldingLogForm initialValues={initialValues}
                                  validationSchema={validationSchema}
                                  onSubmit={handleSubmit}/>
    );
}
export default AddScaffoldingLogForm;