import {useTranslation} from "react-i18next";
import {getSelectedCompany} from "@/utils/company-utils.ts";
import {errorNotification, successNotification} from "@/notifications/notifications.ts";
import {formatMessage} from "@/notifications/FormatMessage.tsx";
import React from "react";
import {BaseUnitFormValues, UnitDTO} from "@/types/unit-types.ts";
import {UnitType, UnitTypeValue} from "@/types/enums/unit-types-enum.ts";
import {getUnitValidationSchema} from "@/validation/unitValidationSchema.ts";
import CommonUnitForm from "@/components/unit/CommonUnitForm.tsx";
import {addUnit} from "@/services/unit-service.ts";

interface AddUnitFormProps {
    onSuccess: (data: BaseUnitFormValues) => void;
}

const AddUnitForm: React.FC<AddUnitFormProps> = ({onSuccess}) => {
    const {t} = useTranslation(['common', 'units', 'errors']);
    const currentCompany = getSelectedCompany();

    const initialValues: BaseUnitFormValues = {
        symbol: '',
        description: '',
        unitType: '' as UnitTypeValue,
    };

    const validationSchema = getUnitValidationSchema(t);

    const handleSubmit = async (values: BaseUnitFormValues) => {
        try {
            const mappedUnit: UnitDTO = {
                ...values,
                company: currentCompany!,
                unitType: values.unitType as UnitType,
            }
            const response = await addUnit(mappedUnit);
            successNotification(
                t('success', {ns: "common"}),
                formatMessage('notifications.addUnitSuccess', {
                    name: values.symbol
                }, 'units')
            );
            const mappedResponse: BaseUnitFormValues = {
                ...response
            }
            onSuccess(mappedResponse);
        } catch (err: any) {
            errorNotification(
                t('error', {ns: "common"}),
                err.response?.data?.message || t('units:notifications.addUnitError'));
        }
    };
    return (
        <CommonUnitForm
            initialValues={initialValues}
            validationSchema={validationSchema}
            onSubmit={handleSubmit}/>
    )
}
export default AddUnitForm;