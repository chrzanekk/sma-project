import {
    BaseScaffoldingLogPositionFormValues,
    ScaffoldingLogPositionBaseDTO
} from "@/types/scaffolding-log-position-types.ts";
import React from "react";
import {useTranslation} from "react-i18next";
import {getSelectedCompany} from "@/utils/company-utils.ts";

interface AddScaffoldingLogPositionFormProps {
    onSuccess: (data: BaseScaffoldingLogPositionFormValues) => void;
    parentPosition?: ScaffoldingLogPositionBaseDTO;
}

const AddScaffoldingLogPositionForm: React.FC<AddScaffoldingLogPositionFormProps> = ({onSuccess, parentPosition}) => {
    const {t} = useTranslation(['common','scaffoldingLogPositions','errors']);
    const currentCompany = getSelectedCompany();
    const initialValues: BaseScaffoldingLogPositionFormValues = {
        id: undefined,

        // Pola tekstowe i daty
        scaffoldingNumber: "",
        assemblyLocation: "",
        assemblyDate: "",
        dismantlingDate: "",
        dismantlingNotificationDate: "",
        scaffoldingFullDimension: "",
        fullWorkingTime: "",
        // Enumy i Obiekty (DTO)
        // Używamy null jako wartości początkowej "pustej"
        // Rzutowanie 'as any' jest konieczne, jeśli interfejs nie dopuszcza null
        scaffoldingType: null as any,
        scaffoldingFullDimensionUnit: null as any,
        technicalProtocolStatus: null as any, // Lub np. TechnicalProtocolStatus.IN_PROGRESS

        parentPosition: null as any,
        scaffoldingLog: null as any,

        contractor: null as any,
        contractorContact: null as any,
        scaffoldingUser: null as any,
        scaffoldingUserContact: null as any,

        // Tablice
        childPositions: [],
        dimensions: [],
        workingTimes: []
    }
}

export default AddScaffoldingLogPositionForm;