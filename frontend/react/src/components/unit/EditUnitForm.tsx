import {useTranslation} from "react-i18next";
import React, {useEffect, useState} from "react";
import {errorNotification, successNotification} from "@/notifications/notifications.ts";
import {formatMessage} from "@/notifications/FormatMessage.tsx";
import {getSelectedCompany} from "@/utils/company-utils.ts";
import {BaseUnitFormValues, FetchableUnitDTO, UnitDTO} from "@/types/unit-types.ts";
import {UnitType, UnitTypeValue} from "@/enums/unit-types-enum.ts";
import {getUnitById, updateUnit} from "@/services/unit-service.ts";
import {getUnitValidationSchema} from "@/validation/unitValidationSchema.ts";
import CommonUnitForm from "@/components/unit/CommonUnitForm.tsx";


interface EditUnitFormProps {
    onSuccess: () => void;
    unitId: number;
}

const EditUnitForm: React.FC<EditUnitFormProps> = ({onSuccess, unitId}) => {
    const defaultValues: BaseUnitFormValues = {
        id: 0,
        symbol: '',
        description: '',
        unitType: '' as UnitTypeValue
    }
    const {t} = useTranslation(['common', 'positions', 'errors'])
    const [initialValues, setInitialValues] = useState<BaseUnitFormValues>(defaultValues);
    const [isLoading, setIsLoading] = useState<boolean>(true);
    const currentCompany = getSelectedCompany();

    useEffect(() => {
        const fetchUnit = async () => {
            setIsLoading(true);
            try {
                const unit: FetchableUnitDTO = await getUnitById(unitId);
                setInitialValues({
                    id: unit.id,
                    symbol: unit.symbol,
                    description: unit.description,
                    unitType: unit.unitType,
                })
            } catch (err) {
                console.error("Error fetching unit: ", err);
            } finally {
                setIsLoading(false);
            }
        };
        fetchUnit().catch();
    }, [unitId, onSuccess]);

    const validationSchema = getUnitValidationSchema(t);

    const handleSubmit = async (values: BaseUnitFormValues) => {
        try {
            const mappedUnit: UnitDTO = {
                ...values,
                unitType: values.unitType as UnitType,
                company: currentCompany!
            }
            await updateUnit(values.id!, mappedUnit);
            successNotification(
                t('success', {ns: "common"}),
                formatMessage('notifications.editUnitSuccess', {
                    symbol: mappedUnit.symbol,
                }, 'units')
            );
            onSuccess();
        } catch (err: any) {
            console.error(err);
            errorNotification(
                t('common:error'),
                err.response?.data?.message || t('units:notifications.editUnitError')
            );
        }
    };

    return (
        <>{!isLoading && (
            <CommonUnitForm initialValues={initialValues}
                            validationSchema={validationSchema}
                            onSubmit={handleSubmit}
            />
        )}
        </>
    )
}

export default EditUnitForm;