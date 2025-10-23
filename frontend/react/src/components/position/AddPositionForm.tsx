import {BasePositionFormValues, PositionDTO} from "@/types/position-types.ts";
import {useTranslation} from "react-i18next";
import {getSelectedCompany} from "@/utils/company-utils.ts";
import {getPositionValidationSchema} from "@/validation/positionValidationSchema.ts";
import {addPosition} from "@/services/position-service.ts";
import {errorNotification, successNotification} from "@/notifications/notifications.ts";
import {formatMessage} from "@/notifications/FormatMessage.tsx";
import CommonPositionForm from "@/components/position/CommonPositionForm.tsx";
import React from "react";

interface AddPositionFormProps {
    onSuccess: (data: BasePositionFormValues) => void;
}

const AddPositionForm: React.FC<AddPositionFormProps> = ({onSuccess}) => {
    const {t} = useTranslation(['common', 'positions', 'errors']);
    const currentCompany = getSelectedCompany();

    const initialValues: BasePositionFormValues = {
        name: '',
        description: '',
    };

    const validationSchema = getPositionValidationSchema(t);

    const handleSubmit = async (values: BasePositionFormValues) => {
        try {
            const mappedPosition: PositionDTO = {
                ...values,
                company: currentCompany!,
            }
            const response = await addPosition(mappedPosition);
            successNotification(
                t('success', {ns: "common"}),
                formatMessage('notifications.addPositionSuccess', {
                    name: values.name
                }, 'positions')
            );
            const mappedResponse: BasePositionFormValues = {
                ...response
            }
            onSuccess(mappedResponse);
        } catch (err: any) {
            errorNotification(
                t('error', {ns: "common"}),
                err.response?.data?.message || t('positions:notifications.addPositionError'));
        }
    };
    return (
        <CommonPositionForm
            initialValues={initialValues}
            validationSchema={validationSchema}
            onSubmit={handleSubmit}/>
    )
}
export default AddPositionForm;