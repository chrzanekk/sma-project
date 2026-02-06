import React, {useEffect, useState} from "react";
import {useTranslation} from "react-i18next";
import {Box, Center, Spinner} from "@chakra-ui/react";
import {
    BaseScaffoldingLogPositionFormValues,
    ScaffoldingLogPositionDTO
} from "@/types/scaffolding-log-position-types.ts";
import {
    getScaffoldingLogPositionById,
    updateScaffoldingLogPosition
} from "@/services/scaffolding-log-position-service.ts";
import {errorNotification, successNotification} from "@/notifications/notifications.ts";
import {formatMessage} from "@/notifications/FormatMessage.tsx";
import {getSelectedCompany} from "@/utils/company-utils.ts";
import CommonScaffoldingLogPositionForm from "@/components/scaffolding/position/CommonScaffoldingLogPositionForm.tsx";
import {getScaffoldingLogPositionValidationSchema} from "@/validation/scaffoldingLogPositionValidationSchema.ts";
import {getScaffoldingTypeOptions} from "@/utils/scaffolding-type-util.ts";
import {getTechnicalProtocolStatusOptions} from "@/utils/technical-protocol-status-util.ts";

interface EditScaffoldingLogPositionFormProps {
    positionId: number;
    onSuccess: (data: BaseScaffoldingLogPositionFormValues) => void;
    onClose?: () => void;
}

const EditScaffoldingLogPositionForm: React.FC<EditScaffoldingLogPositionFormProps> = ({
                                                                                           positionId,
                                                                                           onSuccess,
                                                                                           onClose
                                                                                       }) => {
    const {t} = useTranslation(['common', 'scaffoldingLogPositions', 'errors']);
    const [initialValues, setInitialValues] = useState<BaseScaffoldingLogPositionFormValues | null>(null);
    const [isLoading, setIsLoading] = useState(true);
    const currentCompany = getSelectedCompany();

    const scaffoldingTypeOptions = React.useMemo(() => getScaffoldingTypeOptions(t), [t]);
    const technicalProtocolStatusOptions = React.useMemo(() => getTechnicalProtocolStatusOptions(t), [t]);
    const validationSchema = getScaffoldingLogPositionValidationSchema(t, scaffoldingTypeOptions, technicalProtocolStatusOptions);

    useEffect(() => {
        const loadData = async () => {
            try {
                setIsLoading(true);
                const data = await getScaffoldingLogPositionById(positionId);

                // Mapowanie danych z API na format formularza
                // Inputy typu date wymagają formatu YYYY-MM-DD
                const mapDate = (dateStr?: string) => dateStr ? dateStr.substring(0, 10) : "";

                const formValues: BaseScaffoldingLogPositionFormValues = {
                    ...data,
                    // Upewniamy się, że daty są w dobrym formacie dla input[type="date"]
                    assemblyDate: mapDate(data.assemblyDate),
                    dismantlingDate: mapDate(data.dismantlingDate),
                    dismantlingNotificationDate: mapDate(data.dismantlingNotificationDate),
                    // Upewniamy się, że tablice istnieją
                    dimensions: data.dimensions || [],
                    workingTimes: data.workingTimes || [],
                    childPositions: data.childPositions || []
                };

                setInitialValues(formValues);
            } catch (error) {
                errorNotification(t('common:error'), t('scaffoldingLogPositions:notifications.fetchError'));
                onClose?.();
            } finally {
                setIsLoading(false);
            }
        };

        if (positionId) {
            void loadData();
        }
    }, [positionId, t, onClose]);

    const handleSubmit = async (values: BaseScaffoldingLogPositionFormValues) => {
        try {
            const cleanedDimensions = values.dimensions.filter(dim =>
                dim.length || dim.width || dim.height
            );

            const cleanedWorkingTimes = values.workingTimes.filter(wt =>
                wt.numberOfWorkers || wt.numberOfHours
            );

            const mappedScaffoldingLogPosition: ScaffoldingLogPositionDTO = {
                ...values,
                contractor: values.contractor!,
                contractorContact: values.contractorContact!,
                scaffoldingUser: values.scaffoldingUser!,
                scaffoldingUserContact: values.scaffoldingUserContact!,
                dimensions: cleanedDimensions,
                workingTimes: cleanedWorkingTimes,
                company: currentCompany!
            };

            const response = await updateScaffoldingLogPosition(mappedScaffoldingLogPosition);

            successNotification(
                t('common:success'),
                formatMessage('notifications.updateScaffoldingLogPositionSuccess', {
                    scaffoldingNumber: values.scaffoldingNumber
                }, 'scaffoldingLogPositions')
            );

            onSuccess(response as unknown as BaseScaffoldingLogPositionFormValues);
        } catch (error: any) {
            errorNotification(
                t('common:error'),
                error.response?.data?.message || t('scaffoldingLogPositions:notifications.updateScaffoldingLogPositionError')
            );
        }
    };

    if (isLoading || !initialValues) {
        return (
            <Center p={10}>
                <Spinner size="xl" />
            </Center>
        );
    }

    return (
        <Box ml={2} mr={2}>
            <CommonScaffoldingLogPositionForm
                initialValues={initialValues}
                validationSchema={validationSchema}
                onSubmit={handleSubmit}
            />
        </Box>
    );
};

export default EditScaffoldingLogPositionForm;
