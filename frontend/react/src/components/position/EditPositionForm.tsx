import {useTranslation} from "react-i18next";
import React, {useEffect, useState} from "react";
import {errorNotification, successNotification} from "@/notifications/notifications.ts";
import {formatMessage} from "@/notifications/FormatMessage.tsx";
import {getSelectedCompany} from "@/utils/company-utils.ts";
import {BasePositionFormValues, FetchablePositionDTO, PositionDTO} from "@/types/position-types.ts";
import {getPositionById, updatePosition} from "@/services/position-service.ts";
import {getPositionValidationSchema} from "@/validation/positionValidationSchema.ts";
import CommonPositionForm from "@/components/position/CommonPositionForm.tsx";


interface EditPositionFormProps {
    onSuccess: () => void;
    positionId: number;
}

const EditPositionForm: React.FC<EditPositionFormProps> = ({onSuccess, positionId}) => {
    const defaultValues: BasePositionFormValues = {
        id: 0,
        name: '',
        description: '',
    }
    const {t} = useTranslation(['common', 'positions', 'errors'])
    const [initialValues, setInitialValues] = useState<BasePositionFormValues>(defaultValues);
    const [isLoading, setIsLoading] = useState<boolean>(true);
    const currentCompany = getSelectedCompany();

    useEffect(() => {
        const fetchPosition = async () => {
            setIsLoading(true);
            try {
                const position: FetchablePositionDTO = await getPositionById(positionId);
                setInitialValues({
                    id: position.id,
                    name: position.name,
                    description: position.description,
                })
            } catch (err) {
                console.error("Error fetching position: ", err);
            } finally {
                setIsLoading(false);
            }
        };
        fetchPosition().catch();
    }, [positionId]);

    const validationSchema = getPositionValidationSchema(t);

    const handleSubmit = async (values: BasePositionFormValues) => {
        try {
            const mappedPosition: PositionDTO = {
                ...values,
                company: currentCompany!
            }
            await updatePosition(mappedPosition);
            successNotification(
                t('success', {ns: "common"}),
                formatMessage('notifications.editPositionSuccess', {
                    name: mappedPosition.name,
                }, 'positions')
            );
            onSuccess();
        } catch (err: any) {
            console.error(err);
            errorNotification(
                t('common:error'),
                err.response?.data?.message || t('positions:notifications.editPositionError')
            );
        }
    };

    return (
        <>{!isLoading && (
            <CommonPositionForm initialValues={initialValues}
                                validationSchema={validationSchema}
                                onSubmit={handleSubmit}
            />
        )}
        </>
    )
}

export default EditPositionForm;