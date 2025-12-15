import {
    BaseScaffoldingLogPositionFormValues,
    ScaffoldingLogPositionBaseDTO,
    ScaffoldingLogPositionDTO
} from "@/types/scaffolding-log-position-types.ts";
import React from "react";
import {useTranslation} from "react-i18next";
import {getSelectedCompany} from "@/utils/company-utils.ts";
import {getScaffoldingTypeOptions} from "@/utils/scaffolding-type-util.ts";
import {getTechnicalProtocolStatusOptions} from "@/utils/technical-protocol-status-util.ts";
import {getScaffoldingLogPositionValidationSchema} from "@/validation/scaffoldingLogPositionValidationSchema.ts";
import {addScaffoldingLogPosition} from "@/services/scaffolding-log-position-service.ts";
import {errorNotification, successNotification} from "@/notifications/notifications.ts";
import {formatMessage} from "@/notifications/FormatMessage.tsx";
import {Box} from "@chakra-ui/react";
import {useThemeColors} from "@/theme/theme-colors.ts";
import CommonScaffoldingLogPositionForm from "@/components/scaffolding/position/CommonScaffoldingLogPositionForm.tsx";
import {ScaffoldingType} from "@/enums/scaffolding-type-types-enum.ts";
import {TechnicalProtocolStatus} from "@/enums/technical-protocol-status-types-enum.ts";

interface AddScaffoldingLogPositionFormProps {
    onSuccess: (data: BaseScaffoldingLogPositionFormValues) => void;
    parentPosition?: ScaffoldingLogPositionBaseDTO;
}

const AddScaffoldingLogPositionForm: React.FC<AddScaffoldingLogPositionFormProps> = ({onSuccess, parentPosition}) => {
    const {t} = useTranslation(['common', 'scaffoldingLogPositions', 'errors']);
    const currentCompany = getSelectedCompany();
    const themeColors = useThemeColors();

    const scaffoldingTypeOptions = React.useMemo(() => getScaffoldingTypeOptions(t), [t]);
    const technicalProtocolStatusOptions = React.useMemo(() => getTechnicalProtocolStatusOptions(t), [t]);

    const initialValues: BaseScaffoldingLogPositionFormValues = {
        id: undefined,
        scaffoldingNumber: "",
        assemblyLocation: "",
        assemblyDate: "",
        dismantlingDate: "",
        dismantlingNotificationDate: "",
        scaffoldingType: ScaffoldingType.BASIC,
        scaffoldingFullDimensionUnit: null as any,
        technicalProtocolStatus: TechnicalProtocolStatus.TO_BE_CREATED,
        scaffoldingFullDimension: "",
        fullWorkingTime: "",

        parentPosition: parentPosition || null as any,
        scaffoldingLog: null as any,

        contractor: undefined,
        contractorContact: undefined,
        scaffoldingUser: undefined,
        scaffoldingUserContact: undefined,
        childPositions: [],
        dimensions: [],
        workingTimes: []
    }

    const validationSchema = getScaffoldingLogPositionValidationSchema(t, scaffoldingTypeOptions, technicalProtocolStatusOptions);

    const handleSubmit = async (values: BaseScaffoldingLogPositionFormValues) => {
        try {
            const mappedScaffoldingLogPosition: ScaffoldingLogPositionDTO = {
                ...values,
                contractor: values.contractor!,
                contractorContact: values.contractorContact!,
                scaffoldingUser: values.scaffoldingUser!,
                scaffoldingUserContact: values.scaffoldingUserContact!,
                company: currentCompany!
            }
            const response = await addScaffoldingLogPosition(mappedScaffoldingLogPosition);
            successNotification(
                t('common:success'),
                formatMessage('notifications.addScaffoldingLogPositionSuccess', {
                    scaffoldingNumber: values.scaffoldingNumber
                }, 'scaffoldingLogPositions')
            )
            const mappedResponse: BaseScaffoldingLogPositionFormValues = {
                ...response
            }
            onSuccess?.(mappedResponse);
        } catch (error: any) {
            errorNotification(
                t('common:error'),
                error.response?.data?.message || t('scaffoldingLogPositions:notifications.addScaffoldingLogPositionError')
            )
        }
    };

    return (
        <Box ml={2} mr={2}>
            <CommonScaffoldingLogPositionForm initialValues={initialValues}
                                              validationSchema={validationSchema}
                                              onSubmit={handleSubmit}/>
        </Box>
    )
}

export default AddScaffoldingLogPositionForm;